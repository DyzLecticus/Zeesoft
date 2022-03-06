const CacheConfig = require('../../../src/cache/CacheConfig');
const Cache = require('../../../src/cache/Cache');

const k1 = [
  { a: 1, b: 2, c: 3 },
  { a: 2, b: 2, c: 4 },
];
const v1 = { a: 3, b: 2, c: 3 };

const k2 = [
  { a: 2, b: 2, c: 4 },
  { a: 3, b: 2, c: 3 },
];
const v2 = { a: 4, b: 2, c: 8 };

const initializeTestCache = () => {
  const config = new CacheConfig();
  config.initiatlizeDefault();
  const cache = new Cache(config);
  cache.hit(k1, v1);
  cache.hit(k2, v2);
  return cache;
};

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
    cache.elements[123] = 'Pizza';
    cache.applyMaxSize();
    expect(cache.elements.length).toBe(10);
  });
  test('Hits sub caches correctly', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const cache = new Cache(config);

    const elem1 = cache.hit(k1, v1);
    expect(cache.elements.length).toBe(1);
    expect(elem1.subCache).toBe(null);
    expect(elem1.count).toBe(1);
    cache.hit(k1, v1);
    expect(elem1.count).toBe(2);

    const elem2 = cache.hit(k2, v2);
    expect(cache.elements.length).toBe(2);
    expect(elem1.count).toBe(2);
    expect(elem2.count).toBe(1);
  });
  test('Returns correct lookup results', () => {
    const cache = initializeTestCache();

    let res = cache.lookup(k1);
    expect(res.similarity).toBe(1.0);
    expect(res.elements.length).toBe(1);
    expect(res.subResults.length).toBe(1);

    const lk1 = [
      { a: 2, b: 2, c: 3 },
      { a: 3, b: 2, c: 3 },
    ];
    res = cache.lookup(lk1, 0.0, 0, 1);
    expect(res.similarity).toBe(0.9761904761904763);
    expect(res.elements.length).toBe(1);
    expect(res.subResults.length).toBe(1);
    expect(res.subResults[0].subResults.length).toBe(0);

    const k3 = [
      { a: 1, b: 2, c: 3.1 },
      { a: 2, b: 2, c: 4 },
    ];
    const v3 = { a: 3, b: 2, c: 3.1 };
    cache.hit(k3, v3);
    
    const k4 = [
      { a: 2, b: 2, c: 4.1 },
      { a: 3, b: 2, c: 3 },
    ];
    const v4 = { a: 4, b: 2, c: 8.1 };
    cache.hit(k4, v4);

    const lk2 = [
      { a: 1, b: 2, c: 3.04 },
      { a: 2, b: 2, c: 4 },
    ];

    res = cache.lookup(lk2, 0.0, 0, 1);
    expect(res.similarity).toBe(0.9988962472406182);
    expect(res.secondary.similarity).toBe(0.8645743145743146);
  });
  test('Returns the correct size(s)', () => {
    const cache = initializeTestCache();
    const size = cache.size();
    expect(size).toStrictEqual({
      0.95: 2, 0.98: 2, 0.99: 2, 1: 2,
    });
  });
});
