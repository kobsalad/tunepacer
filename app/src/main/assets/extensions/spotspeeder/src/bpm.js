/*
 * Handle setup of BPM analyzer and allow BPM settings to be set.
 */

import { createRealtimeBpmAnalyzer } from "realtime-bpm-analyzer";
import { setPlaybackRate, getPlaybackRate } from "./playback";

let targetBpm = NaN;
let nativeBpm = NaN;
let syncWithCadence = false;

// Set the target BPM, causing playback rate to change as soon as possible
export function setTargetBpm(newTargetBpm) {
  console.log(`setTargetBpm(${newTargetBpm})`);
  targetBpm = newTargetBpm;
  tryUpdatePlaybackRate();
}

// Set the "Sync With" setting type. When enabled, the stale nativeBpm is invalidated
export function setSyncWithCadence(enabled) {
  if (enabled && !syncWithCadence) {
    nativeBpm = NaN;
  }
  syncWithCadence = enabled;
}

let analyzer;
// Reset the history of the analyzer so no old BPM data leaks forward
export function resetAnalyzer() {
  console.log("resetAnalyzer()");
  analyzer?.reset();
}

// On song change, set playbackRate to 1, and reset analyzer and nativeBpm
export function handleSongChange() {
  resetAnalyzer();
  setPlaybackRate(1);
  nativeBpm = NaN;
}

// If possible, set the playback rate to reflect current BPM settings
function tryUpdatePlaybackRate() {
  if (syncWithCadence && !Number.isNaN(nativeBpm) && !Number.isNaN(targetBpm)) {
    setPlaybackRate(normalizePlaybackRate(targetBpm / nativeBpm));
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
    if (analyzer.state.status !== "locked") return;

    const newNativeBpm = reading.bpm / getPlaybackRate();

    if (Number.isNaN(nativeBpm) || Math.abs(newNativeBpm - nativeBpm) >= 1) {
      console.log(`Locked ${Math.round(newNativeBpm)} BPM`);
      nativeBpm = newNativeBpm;
      tryUpdatePlaybackRate();
      resetAnalyzer();
    }
  });
}
