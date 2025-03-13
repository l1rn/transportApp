module.exports = {
    root: true,                 
    parser: 'vue-eslint-parser', 
    parserOptions: {
      parser: '@babel/eslint-parser', 
      ecmaVersion: 2020,             
      sourceType: 'module'
    },
    env: {
      browser: true,
      node: true,
      es2021: true
    },
    extends: [
      'eslint:recommended',
      'plugin:vue/vue3-recommended'  
    ],
    rules: {
    }
  };