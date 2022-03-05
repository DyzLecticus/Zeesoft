const Comparator = require('../../src/Comparator');

describe('Comparator', () => {
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

  test('Calculates the similarity between two values correctly', () => {
    const comparator = new Comparator();
    expect(comparator.calculateValueSimilarity(1, 1)).toBe(1);
    expect(comparator.calculateValueSimilarity(-1, -1)).toBe(1);
    expect(comparator.calculateValueSimilarity(0, 1)).toBe(0);
    expect(comparator.calculateValueSimilarity(0, -1)).toBe(0.6666666666666667);
    expect(comparator.calculateValueSimilarity(22, 25)).toBe(0.9361702127659575);
    expect(comparator.calculateValueSimilarity(-3, 4)).toBe(0.46153846153846156);
    expect(comparator.calculateValueSimilarity(4, -3)).toBe(0.46153846153846156);
    expect(comparator.calculateValueSimilarity(1, 'Pizza')).toBe(0);
    expect(comparator.calculateValueSimilarity('Pizza', 1)).toBe(0);
    expect(comparator.calculateValueSimilarity('Pizza', '')).toBe(0);
    expect(comparator.calculateValueSimilarity('Pizza', 'Pizza')).toBe(1);
    expect(comparator.calculateValueSimilarity(null, comparator)).toBe(0);
    expect(comparator.calculateValueSimilarity(comparator, comparator)).toBe(1);
  });

  test('Calculates the similarity between two objects correctly', () => {
    const comparator = new Comparator();
    const a = {
      a: 1,
      b: 1,
    };
    const b = {
      a: 1,
      b: 1,
    };
    expect(comparator.calculateObjectSimilarity(a, b)).toBe(1);
    a.a = 2;
    expect(comparator.calculateObjectSimilarity(a, b)).toBe(0.8333333333333334);
    a.b = 2;
    expect(comparator.calculateObjectSimilarity(a, b)).toBe(0.6666666666666667);
    a.x = 1;
    expect(comparator.calculateObjectSimilarity(a, b)).toBe(0.4444444444444445);
    b.y = 1;
    expect(comparator.calculateObjectSimilarity(a, b)).toBe(0.33333333333333337);
    expect(comparator.calculateObjectSimilarity({}, {})).toBe(1);
  });

  test('Calculates the similarity between two object arrays correctly', () => {
    const comparator = new Comparator();
    const a = [
      { a: 1, b: 1 },
      { a: 2, b: 2 },
    ];
    const b = [
      { a: 1, b: 1 },
      { a: 2, b: 2 },
    ];
    expect(comparator.calculateObjectArraySimilarity(a, b)).toBe(1);
    a[0].a = 2;
    expect(comparator.calculateObjectArraySimilarity(a, b)).toBe(0.9166666666666667);
    a[0].b = 2;
    expect(comparator.calculateObjectArraySimilarity(a, b)).toBe(0.8333333333333334);
    a[2] = { a: 3, b: 3 };
    expect(comparator.calculateObjectArraySimilarity(a, b)).toBe(0.5555555555555556);
    expect(comparator.calculateObjectArraySimilarity([], [])).toBe(1);
  });
});
