import js from "@eslint/js";
import globals from "globals";
import tseslint from "typescript-eslint";
import pluginVue from "eslint-plugin-vue";
import css from "@eslint/css";
import { defineConfig } from "eslint/config";

export default tseslint.config(
  {
    ignores: [
      "**/node_modules/**",
      "**/dist/**", 
      "**/coverage/**",
      "**/.git/**",
      "**/*.min.js"
    ]
  },
  {
    files: ["**/*.{js,mjs,cjs,ts,mts,cts,vue}"],
    extends: [js.config.recommend],
    languageOptions: {
      globals: {
        ...globals.blowser,
        ...globals.node,
        ...globals.es2020
      },
      ecmaVersion: 2020,
      sourceType: "module"
    }
  },
  ...tseslint.config.recommend,
  {
    files: ["**/*.vue"],
    ...pluginVue.configs["flat/recommend"],
    languageOptions: {
      parserOptions: {
        parser: tseslint.parser,
        ecmaVersion: 2020,
        sourceType: "module"
      }
    }
  },
  {
    rules: {
      "vue/multi-word-component-names": "off",
      "@typescript-eslint/no-unused-vars": "warn"
    }
  }
)