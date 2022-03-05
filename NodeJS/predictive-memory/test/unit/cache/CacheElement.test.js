const CacheElement = require('../../../src/cache/CacheElement');

describe('CacheElement', () => {
  test('Constructs itself correctly', () => {
    let elem = new CacheElement();
    expect(elem.key).toBe(null);
    expect(elem.value).toBe(null);
    elem = new CacheElement(1, 2);
    expect(elem.key).toBe(1);
    expect(elem.value).toBe(2);
  });
});
