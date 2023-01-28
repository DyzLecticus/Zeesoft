const Symbol = require('../../../src/symbol/Symbol');
const SymbolConstants = require('../../../src/symbol/SymbolConstants');

describe('Symbol', () => {
  test('Constructs itself correctly', () => {
    const symbol = new Symbol('Tést', { stuff: true });
    expect(symbol.str).toBe('Test');
    expect(symbol.meta).toStrictEqual({ stuff: true });
    expect(symbol.length).toBe(153);
    expect(symbol.keyValues).toStrictEqual(
      {
        106: 19, 120: 4, 121: 18, 18: 3, 19: 1, 4: 2, 55: 0, 69: 4, 70: 19,
      },
    );
    expect(symbol.toString()).toBe('153,4=2,18=3,19=1,69=4,70=19,106=19,120=4,121=18');
  });

  test('Constructs itself correctly (Custom characters)', () => {
    const symbol = new Symbol('Tést', { stuff: true }, SymbolConstants.CHARACTERS);
    expect(symbol.str).toBe('Test');
    expect(symbol.meta).toStrictEqual({ stuff: true });
    expect(symbol.length).toBe(279);
    expect(Object.keys(symbol.keyValues).length).toBe(10);
    expect(symbol.toString()).toBe('279,4=2,18=3,19=4,45=1,111=4,138=45,190=45,204=4,205=18');
  });

  test('Calculates distances correctly', () => {
    const symbol1 = new Symbol('Test1');
    const symbol2 = new Symbol('Test2');
    const symbol3 = new Symbol('Something else');
    expect(symbol1.calculateDistance(symbol1)).toBe(0);
    expect(symbol1.calculateDistance(symbol2)).toBe(27.784887978899608);
    expect(symbol1.calculateDistance(symbol3)).toBe(60.6217782649107);
    symbol1.length = 123;
    expect(symbol1.calculateDistance(symbol2)).toBe(-1);
  });
});
