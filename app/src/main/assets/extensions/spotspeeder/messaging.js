/*
 * Listen for messages sent from the message delegate, and pass them to playback.js.
 */

browser.runtime.connectNative("tunepacer").onMessage.addListener((message) => {
    if (message.type === "SET_PLAYBACK_RATE") {
        window.dispatchEvent(
            new CustomEvent("spotspeeder:set-playback-rate", {
                detail: message.playbackRate
            })
        );
    }
});
