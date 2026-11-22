import { defineConfig } from "vite";

export default defineConfig({
  build: {
    emptyOutDir: false,
    lib: {
      entry: "src/main.js",
      formats: ["iife"],
      name: "SpotSpeeder",
      fileName: () => "main.js",
    },
  },
});
