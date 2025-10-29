module.exports = {
  root: true,
  parser: 'vue-eslint-parser',

  parserOptions: {
    parser: '@typescript-eslint/parser', 
    ecmaVersion: 2020,             
    sourceType: 'module'
  },

  env: {
    browser: true,
    node: true,
    es2022: true
  },

  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    '@vue/typescript'

  ],

  rules: {
    'vue/html-indent': 'off',
    'vue/html-closing-bracket-newline': 'off',
    'vue/first-attribute-linebreak': 'off'
  },
};
