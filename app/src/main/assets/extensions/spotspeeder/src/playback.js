/*
 * Allow playback rate to be get and set. Handles the monkey patches
 */

let playbackRate = 1;
let audioElement;

// Set the playback rate and update the audio element
export function setPlaybackRate(newPlaybackRate) {
  console.log(`setPlaybackRate(${newPlaybackRate})`);
  playbackRate = newPlaybackRate;
  audioElement.playbackRate = playbackRate;
}

export function getPlaybackRate() {
  return playbackRate;
}

// This may be initialized late
export function getAudioElement() {
  return audioElement;
}

// Find the audio element by patching document.createElement()
const baseCreateElement = Document.prototype.createElement;
Document.prototype.createElement = function (tagName, options) {
  const element = baseCreateElement.call(this, tagName, options);
  if (tagName.toLowerCase() === "audio") {
    audioElement = element;
    audioElement.playbackRate = playbackRate;
  }
  return element;
};

// Monkey patch playbackRate setter to always set to our custom value
const basePlaybackRateDescriptor = Object.getOwnPropertyDescriptor(
  HTMLMediaElement.prototype,
  "playbackRate",
);
Object.defineProperty(HTMLAudioElement.prototype, "playbackRate", {
  ...basePlaybackRateDescriptor,
  set(_) {
    basePlaybackRateDescriptor.set.call(this, playbackRate);
  },
});
