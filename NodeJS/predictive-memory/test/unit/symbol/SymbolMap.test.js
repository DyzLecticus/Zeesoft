const SymbolMap = require('../../../src/symbol/SymbolMap');

describe('SymbolMap', () => {
  test('Adds and gets elements correctly', () => {
    const map = new SymbolMap();
    expect(map.getNearest('Pizza').length).toBe(0);

    const symbol1 = map.put('Test1', 'Meta');
    map.put('Test2');
    const symbol3 = map.put('Something else');
    const symbol4 = map.put(' something different');

    expect(symbol1.toString()).toBe('373,0=5,5=2,19=3,20=4,46=1,54=5,98=1,112=1,113=1,139=1,147=1,191=19,205=20,206=54,232=5,284=46,298=5,299=19,333=20');
    expect(map.getAsArray().length).toBe(4);
    expect(symbol1.meta).toBe('Meta');

    expect(Object.keys(map.elements).length).toBe(4);
    expect(map.get('Something')).toBe(undefined);
    expect(map.get('Test1').str).toBe('Test1');

    const results = map.getNearest('Something', 2);
    expect(results[0].symbol).toBe(symbol3);
    expect(results[1].symbol).toBe(symbol4);
    expect(map.getNearest('Something').length).toBe(1);
  });
});
