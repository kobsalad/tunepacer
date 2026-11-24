/*
 * Generic helper functions
 */

export function waitUntil(expressionFn) {
  return new Promise((resolve) => {
    const check = () => {
      const result = expressionFn();
      if (result !== undefined) {
        resolve(result);
        return;
      }
      setTimeout(check, 100);
    };
    check();
  });
}
