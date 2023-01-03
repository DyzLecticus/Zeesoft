const Symbol = require('../../../src/symbol/Symbol');

describe('Symbol', () => {
  test('Constructs itself correctly', () => {
    const symbol = new Symbol('Test');
    expect(symbol.str).toBe('Test');
    expect(symbol.numArray.length).toBe(279);
    expect(symbol.toString()).toBe('279,4=1,18=1,19=1,45=1,97=2,111=3,112=4,138=1,190=19,204=20,231=5');
  });

  test('Compares itself correctly', () => {
    const symbol1 = new Symbol('Test1');
    const symbol2 = new Symbol('Test2');
    expect(symbol1.toString()).toBe('279,4=1,18=1,19=1,45=1,53=1,97=2,111=3,112=4,138=1,146=5,190=19,204=20,205=54,231=5');
    expect(symbol1.equals(symbol1)).toBe(true);
    expect(symbol1.equals({})).toBe(false);
    expect(symbol1.equals(symbol2)).toBe(false);
  });

  test('Calculates distances correctly', () => {
    const symbol1 = new Symbol('Test1');
    const symbol2 = new Symbol('Test2');
    const symbol3 = new Symbol('Something else');
    expect(symbol1.calculateDistance(symbol1)).toBe(0);
    expect(symbol1.calculateDistance(symbol2)).toBe(7.280109889280518);
    expect(symbol1.calculateDistance(symbol3)).toBe(92.39047569960877);
  });
});
