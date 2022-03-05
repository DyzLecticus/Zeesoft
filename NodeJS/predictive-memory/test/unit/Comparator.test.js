const Comparator = require('../../src/Comparator');

test('Determines the highest value of two numbers correctly', () => {
  const comparator = new Comparator();
  expect(comparator.max(1, 1)).toBe(1);
  expect(comparator.max(2, 1)).toBe(2);
  expect(comparator.max(1, 2)).toBe(2);
  expect(comparator.max(-2, 1)).toBe(1);
  expect(comparator.max(1, -2)).toBe(1);
});

test('Determines the lowest value of two numbers correctly', () => {
  const comparator = new Comparator();
  expect(comparator.min(1, 1)).toBe(1);
  expect(comparator.min(2, 1)).toBe(1);
  expect(comparator.min(1, 2)).toBe(1);
  expect(comparator.min(-2, 1)).toBe(-2);
  expect(comparator.min(1, -2)).toBe(-2);
});

test('Calculates the similarity between two numbers correctly', () => {
  const comparator = new Comparator();
  expect(comparator.calculateNumberSimilarity(1, 1)).toBe(1);
  expect(comparator.calculateNumberSimilarity(-1, -1)).toBe(1);
  expect(comparator.calculateNumberSimilarity(0, 1)).toBe(0);
  expect(comparator.calculateNumberSimilarity(0, -1)).toBe(0.6666666666666667);
  expect(comparator.calculateNumberSimilarity(22, 25)).toBe(0.9361702127659575);
  expect(comparator.calculateNumberSimilarity(-3, 4)).toBe(0.46153846153846156);
  expect(comparator.calculateNumberSimilarity(4, -3)).toBe(0.46153846153846156);
});
