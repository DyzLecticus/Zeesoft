const CacheConfig = require('../../../src/cache/CacheConfig');
const Cache = require('../../../src/cache/Cache');

describe('Cache', () => {
  test('Constructs itself correctly', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const cache = new Cache(config);
    expect(cache.config).toBe(config);
  });
  test('Limits its size correctly', () => {
    const config = new CacheConfig();
    config.maxSize = 10;
    const cache = new Cache(config);
    cache.elements[123] = "Pizza";
    cache.applyMaxSize();
    expect(cache.elements.length).toBe(10);
  });
  test('Hits sub caches correctly', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const cache = new Cache(config);
    const k1 = [
      { a: 1, b: 2, c: 3 },
      { a: 2, b: 2, c: 4 },
    ];
    const v1 = { a: 3, b: 2, c: 3 };
    const elem1 = cache.hit(k1, v1);
    expect(cache.elements.length).toBe(1);
    expect(elem1.subCache).toBe(null);
    expect(elem1.count).toBe(1);
    cache.hit(k1, v1);
    expect(elem1.count).toBe(2);

    const k2 = [
      { a: 2, b: 2, c: 4 },
      { a: 3, b: 2, c: 3 },
    ];
    const v2 = { a: 4, b: 2, c: 8 };
    const elem2 = cache.hit(k2, v2);
    expect(cache.elements.length).toBe(2);
    expect(elem1.count).toBe(2);
    expect(elem2.count).toBe(1);
  });
});
