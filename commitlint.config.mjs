export default {
  extends: ['@commitlint/config-conventional'],
  rules: {
    // Subjects are the issue titles and run long. config-conventional already
    // allows 100; written out so nobody tightens it to 72.
    'header-max-length': [2, 'always', 100],
  },
};
