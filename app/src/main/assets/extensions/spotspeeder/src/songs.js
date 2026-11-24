/*
 * Handle tracking song changes, and reset the analyzer on each change.
 */

import { handleSongChange } from "./bpm";

let lastTrackKey;
// Handle metadata change
function handleMetadataChange(metadata) {
  const trackKey = `${metadata?.title}\x1F${metadata?.artist}\x1F${metadata?.album}`;
  if (trackKey !== lastTrackKey) {
    // Handle song change by resetting the analyzer
    lastTrackKey = trackKey;
    console.log(
      "Song changed:",
      `${metadata?.title} - ${metadata?.artist} - ${metadata?.album}`,
    );
    handleSongChange();
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
