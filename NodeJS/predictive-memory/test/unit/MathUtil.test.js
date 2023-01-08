const MathUtil = require('../../src/MathUtil');

describe('MathUtil', () => {
  test('Calculates the average of an array of numbers correctly', () => {
    expect(MathUtil.getAverage([])).toBe(0);
    expect(MathUtil.getAverage([1, 2, 3])).toBe(2);
    expect(MathUtil.getAverage([1, 1, 2, 2])).toBe(1.5);
  });

  test('Calculates the standard deviation of an array of numbers correctly', () => {
    expect(MathUtil.getStandardDeviation([])).toBe(0);
    expect(MathUtil.getStandardDeviation([1])).toBe(0);
    expect(MathUtil.getStandardDeviation([1, 2, 3])).toBe(1);
    expect(MathUtil.getStandardDeviation([1, 1, 2, 2])).toBe(0.5773502691896257);
  });

  test('Stringifies and parses number arrays correctly', () => {
    const str = MathUtil.stringify([1, 0, 2, 0, 3, 0]);
    expect(str).toBe('6,0=1,2=2,4=3');
    expect(MathUtil.parse(str)).toStrictEqual([1, 0, 2, 0, 3, 0]);
  });
});
