const ObjectMap = require('../../src/ObjectMap');

describe('ObjectMap', () => {
  test('Handles mutations correctly', () => {
    const obj1 = {};
    const obj2 = {};
    const map = new ObjectMap();
    map.put('1', obj1);
    map.put('2', obj2);
    expect(map.get('1')).toBe(obj1);
    expect(map.get('2')).toBe(obj2);
    expect(map.get('3')).toBe(undefined);
    map.remove('1');
    expect(map.get('1')).toBe(undefined);
  });

  test('Iterates over map entries correctly', () => {
    const obj1 = {
      prop: 1,
    };
    const obj2 = {
      prop: 1,
    };
    const map = new ObjectMap();
    map.put('1', obj1);
    map.put('2', obj2);
    map.forEach((obj) => { const o = obj; o.prop += 1; });
    expect(obj1.prop).toBe(2);
    expect(obj2.prop).toBe(2);
  });
});
