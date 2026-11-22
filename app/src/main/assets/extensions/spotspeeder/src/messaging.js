/*
 * Listen for messages sent from the message delegate, and pass them to main.js.
 */

browser.runtime.connectNative("tunepacer").onMessage.addListener((message) => {
  if (message.type === "SET_PLAYBACK_RATE") {
    window.dispatchEvent(
      new CustomEvent("spotspeeder:set-playback-rate", {
        detail: message.playbackRate,
      }),
    );
  } else if (message.type === "SET_TARGET_BPM") {
    window.dispatchEvent(
      new CustomEvent("spotspeeder:set-target-bpm", {
        detail: message.targetBpm,
      }),
    );
  }
});
