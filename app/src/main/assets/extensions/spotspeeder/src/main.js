/*
 * Route events and perform initial setup.
 */

import { setSyncWithCadence, setTargetBpm, setupBpmAnalyzer } from "./bpm";
import { getAudioElement, setPlaybackRate } from "./playback";
import { waitUntil } from "./util";

(async () => {
  // Listen for events passed from messaging.js
  window.addEventListener("spotspeeder:set-playback-rate", (event) => {
    setSyncWithCadence(false);
    setPlaybackRate(event.detail);
  });
  window.addEventListener("spotspeeder:set-target-bpm", (event) => {
    setSyncWithCadence(true);
    setTargetBpm(event.detail);
  });

  // Perform setup on first user interaction
  const audioElement = await waitUntil(getAudioElement);
  audioElement.addEventListener(
    "playing",
    () => {
      setupBpmAnalyzer(audioElement);
    },
    { once: true },
  );
})();
