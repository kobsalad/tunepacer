/*
 * Allows Spotify playback speed to be set. Listens to events from messaging.js. Applies
 * playbackRate to all audio and video elements.
 */

(() => {
    let playbackRate = 1;

    // This realistically will contain one audio and one video element. We will handle both for
    // safety. Only the audio element matters in the mobile web version, but the desktop web version
    // is opposite. No new elements seem to be created after these, so this shouldn't leak memory.
    let mediaElements = new Set();

    // Update the playback rate of every media element
    function setPlaybackRate(newPlaybackRate) {
        playbackRate = newPlaybackRate
        mediaElements.forEach((mediaElement) => {
            mediaElement.playbackRate = playbackRate;
        });
    }

    // Listen for events passed from messaging.js
    window.addEventListener("spotspeeder:set-playback-rate", (event) => {
        setPlaybackRate(event.detail);
    });

    // Track new media elements and initialize their playbackRate
    const baseCreateElement = Document.prototype.createElement;
    Document.prototype.createElement = function(tagName, options) {
        const mediaElement = baseCreateElement.call(this, tagName, options);
        if (tagName.toLowerCase() === "audio" || tagName.toLowerCase() === "video") {
            mediaElements.add(mediaElement);
            mediaElement.playbackRate = playbackRate;
        }
        return mediaElement;
    };

    // Monkey patch playbackRate setter to always set to our custom value
    const basePlaybackRateDescriptor = Object.getOwnPropertyDescriptor(
        HTMLMediaElement.prototype,
        "playbackRate"
    );
    Object.defineProperty(HTMLMediaElement.prototype, "playbackRate", {
        ...basePlaybackRateDescriptor,
        set(_) {
            // No matter the given value, just use the custom playbackRate
            basePlaybackRateDescriptor.set.call(this, playbackRate);

            // Track any potential media elements missed by the createElement monkey patch
            mediaElements.add(this);
        }
    });
})();
