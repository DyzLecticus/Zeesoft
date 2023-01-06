const Symbol = require('../../../src/symbol/Symbol');

describe('Symbol', () => {
  test('Constructs itself correctly', () => {
    const symbol = new Symbol('Test', [0, 1, 2, 0, 3], { stuff: true });
    expect(symbol.str).toBe('Test');
    expect(symbol.numArray).toStrictEqual([0, 1, 2, 0, 3]);
    expect(symbol.meta).toStrictEqual({ stuff: true });
    expect(symbol.toString()).toBe('5,1=1,2=2,4=3');
  });

  test('Compares itself correctly', () => {
    const symbol1 = new Symbol('Test1', [1, 2, 3]);
    const symbol2 = new Symbol('Test2', [1, 2, 5]);
    expect(symbol1.equals(symbol1)).toBe(true);
    expect(symbol1.equals({})).toBe(false);
    expect(symbol1.equals(symbol2)).toBe(false);
  });

  test('Calculates distances correctly', () => {
    const symbol1 = new Symbol('Test1', [1, 2, 3]);
    const symbol2 = new Symbol('Test2', [1, 2, 5]);
    const symbol3 = new Symbol('Something else', [4, 5, 6]);
    expect(symbol1.calculateDistance(symbol1)).toBe(0);
    expect(symbol1.calculateDistance(symbol2)).toBe(2);
    expect(symbol1.calculateDistance(symbol3)).toBe(5.196152422706632);
  });
});
