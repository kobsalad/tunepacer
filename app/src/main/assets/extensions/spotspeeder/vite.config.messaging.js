import { defineConfig } from "vite";

export default defineConfig({
  build: {
    emptyOutDir: false,
    lib: {
      entry: "src/messaging.js",
      formats: ["iife"],
      name: "SpotSpeederMessaging",
      fileName: () => "messaging.js",
    },
  },
});
