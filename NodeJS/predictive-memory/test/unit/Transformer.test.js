const Transformer = require('../../src/Transformer');

describe('Transformer', () => {
  test('Returns correct minimum values', () => {
    const transformer = new Transformer();
    expect(transformer.getMinValue(0.1)).toBe(0.1);
    expect(transformer.getMinValue(0.00000000009)).toBe(0.0000000001);
    expect(transformer.getMinValue(0.0000000001)).toBe(0.0000000001);
    expect(transformer.getMinValue(-0.1)).toBe(-0.1);
    expect(transformer.getMinValue(-0.00000000009)).toBe(-0.0000000001);
  });

  test('Calculates the transformation between values correctly', () => {
    const transformer = new Transformer();
    expect(transformer.calculateValueTransformation(1, 1)).toBe(1.0);
    expect(transformer.calculateValueTransformation(1, 2)).toBe(2.0);
    expect(transformer.calculateValueTransformation(2, 3)).toBe(1.5);
    expect(transformer.calculateValueTransformation(0, 1)).toBe(10000000000);
    expect(transformer.calculateValueTransformation(1, 0)).toBe(0.0);

    expect(transformer.calculateValueTransformation(-1, -1)).toBe(1.0);
    expect(transformer.calculateValueTransformation(-1, -2)).toBe(2.0);
    expect(transformer.calculateValueTransformation(-2, -3)).toBe(1.5);
    expect(transformer.calculateValueTransformation(0, -1)).toBe(-10000000000);

    expect(transformer.calculateValueTransformation('A', 1)).toBe(0.0);
    expect(transformer.calculateValueTransformation(1, 'A')).toBe(0.0);
    expect(transformer.calculateValueTransformation('A', 'A')).toBe(1.0);
  });

  test('Calculates the transformation between objects correctly', () => {
    const transformer = new Transformer();
    const from = {
      a: 1,
      b: 2,
      c: null,
      d: 'D',
    };
    const to = {
      a: 2,
      b: null,
      c: 3,
      e: 'E',
    };
    expect(transformer.calculateTransformation(from, to)).toStrictEqual({ a: 2 });
  });

  test('Applies value transformations correctly', () => {
    const transformer = new Transformer();
    expect(transformer.applyValueTransformation(0, 1.0)).toBe(0);
    expect(transformer.applyValueTransformation('A', 1.0)).toBe('A');
    expect(transformer.applyValueTransformation(2, 1.5)).toBe(3);
    expect(transformer.applyValueTransformation(0, 10000000000)).toBe(1);
  });

  test('Applies transformations correctly', () => {
    const transformer = new Transformer();
    expect(transformer.applyTransformation({ a: 0 }, { a: 1.0 })).toStrictEqual({ a: 0 });
    expect(transformer.applyTransformation({ a: 2, b: 'B' }, { a: 1.5 })).toStrictEqual({ a: 3 });
    expect(transformer.applyTransformation(
      { a: 2, b: null },
      { a: 1.5, b: 2.5 },
    )).toStrictEqual({ a: 3 });
  });
});
