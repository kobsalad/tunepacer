/*
 * Handle tracking song changes, and reset the analyzer on each change.
 */

import { resetAnalyzer } from "./bpm";

let lastTrackKey = "";
// Handle metadata change
function handleMetadataChange(metadata) {
  const trackKey = `${metadata.title}|${metadata.artist}|${metadata.album}`;
  if (trackKey !== lastTrackKey) {
    // Handle song change by resetting the analyzer
    lastTrackKey = trackKey;
    console.log("Song changed:", trackKey);
    resetAnalyzer();
  }
}

// Monkey patch to detect metadata changes
const descriptor = Object.getOwnPropertyDescriptor(
  MediaSession.prototype,
  "metadata",
);
Object.defineProperty(MediaSession.prototype, "metadata", {
  ...descriptor,
  set(metadata) {
    descriptor.set.call(this, metadata);
    handleMetadataChange(metadata);
  },
});
