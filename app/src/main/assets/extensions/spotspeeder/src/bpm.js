/*
 * Handle setup of BPM analyzer and allow BPM settings to be set.
 */

import { createRealtimeBpmAnalyzer } from "realtime-bpm-analyzer";
import { setPlaybackRate, getPlaybackRate } from "./playback";

let targetBpm = NaN;
let measuredBpm = NaN;
let lockedPlaybackRate = 1;
let syncWithCadence = false;

// Set the target BPM, causing playback rate to change as soon as possible
export function setTargetBpm(newTargetBpm) {
  console.log(`setTargetBpm(${newTargetBpm})`);
  targetBpm = newTargetBpm;
  tryUpdatePlaybackRate();
}

// Set the "Sync With" setting type. When enabled, the stale measuredBpm is invalidated
export function setSyncWithCadence(enabled) {
  if (enabled && !syncWithCadence) {
    measuredBpm = NaN;
  }
  syncWithCadence = enabled;
}

let analyzer;
// Reset the history of the analyzer so no old BPM data leaks forward
export function resetAnalyzer() {
  console.log("resetAnalyzer()");
  analyzer?.reset();
}

// If possible, set the playback rate to reflect current BPM settings
function tryUpdatePlaybackRate() {
  if (
    syncWithCadence &&
    !Number.isNaN(measuredBpm) &&
    !Number.isNaN(targetBpm)
  ) {
    setPlaybackRate(
      normalizePlaybackRate(lockedPlaybackRate * (targetBpm / measuredBpm)),
    );
  }
}

// Use doubling and halving to scale playbackRate into a reasonable range
function normalizePlaybackRate(playbackRate) {
  return playbackRate / 2 ** Math.round(Math.log2(playbackRate));
}

// Listen to audioElement. On BPM locks, update settings and try to update playbackRate
export async function setupBpmAnalyzer(audioElement) {
  console.log("setupBpmAnalyzer()");
  const audioContext = new AudioContext();
  analyzer = await createRealtimeBpmAnalyzer(audioContext);
  const source = audioContext.createMediaElementSource(audioElement);
  source.connect(analyzer.input);
  source.connect(audioContext.destination);
  analyzer.on("reading", (reading) => {
    if (analyzer.state.status === "locked" && reading.bpm !== measuredBpm) {
      console.log(`Locked ${reading.bpm} BPM`);
      lockedPlaybackRate = getPlaybackRate();
      measuredBpm = reading.bpm;
      tryUpdatePlaybackRate();
      resetAnalyzer();
    }
  });
}
