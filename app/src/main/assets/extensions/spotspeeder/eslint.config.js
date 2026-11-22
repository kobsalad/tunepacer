import js from "@eslint/js";
import globals from "globals";

export default [
  {
    files: ["src/**/*.js"],
    ...js.configs.recommended,
    rules: {
      "no-unused-vars": [
        "error",
        {
          argsIgnorePattern: "^_",
        },
      ],
    },
    languageOptions: {
      globals: {
        ...globals.browser,
        ...globals.webextensions,
      },
    },
  },
];
