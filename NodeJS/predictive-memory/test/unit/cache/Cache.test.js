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
  cache.process(k1, v1);
  cache.process(k2, v2);
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

  test('Processes key/value pairs in sub caches correctly', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const cache = new Cache(config);

    const elem1 = cache.process(k1, v1);
    expect(cache.elements.length).toBe(1);
    expect(elem1.subCache).toBe(null);
    expect(elem1.count).toBe(1);
    cache.process(k1, v1);
    expect(elem1.count).toBe(2);

    const elem2 = cache.process(k2, v2);
    expect(cache.elements.length).toBe(2);
    expect(elem1.count).toBe(2);
    expect(elem2.count).toBe(1);

    const elem3 = cache.process({}, v2);
    expect(cache.elements.length).toBe(3);
    expect(elem3.count).toBe(1);
  });

  test('Returns correct query results', () => {
    const cache = initializeTestCache();

    let res = cache.query(k1);
    expect(res.levelElements.length).toBe(3);
    expect(res.levelElements[2].length).toBe(3);
    let elems = res.getDeepestElements(2);
    expect(elems[0].similarity).toBe(1.0);
    expect(elems[0].parentCount).toBe(2);
    expect(elems[1].similarity).toBe(1.0);
    expect(elems[1].parentCount).toBe(2);

    const lk1 = [
      { a: 2, b: 2, c: 3 },
      { a: 3, b: 2, c: 3 },
    ];
    res = cache.query(lk1, { minSimilarity: 0.0, maxDepth: 1 });
    elems = res.getDeepestElements(2);
    expect(elems[0].similarity).toBe(0.9761904761904763);
    expect(elems[0].parentCount).toBe(1);
    expect(elems[1].similarity).toBe(0.8873015873015874);
    expect(elems[1].parentCount).toBe(1);

    const k3 = [
      { a: 1, b: 2, c: 3.1 },
      { a: 2, b: 2, c: 4 },
    ];
    const v3 = { a: 3, b: 2, c: 3.1 };
    cache.process(k3, v3);

    const k4 = [
      { a: 2, b: 2, c: 4.1 },
      { a: 3, b: 2, c: 3 },
    ];
    const v4 = { a: 4, b: 2, c: 8.1 };
    cache.process(k4, v4);

    const lk2 = [
      { a: 1, b: 2, c: 3.04 },
      { a: 2, b: 2, c: 4 },
    ];

    res = cache.query(lk2, { minSimilarity: 0.0, maxDepth: 1 });
    elems = res.getDeepestElements(2);
    expect(elems[0].similarity).toBe(0.9988962472406182);
    expect(elems[0].parentCount).toBe(2);
    expect(elems[1].similarity).toBe(0.8645743145743146);
    expect(elems[1].parentCount).toBe(2);

    res = cache.query(lk2, { minSimilarity: 0.99, maxDepth: 1 });
    elems = res.getDeepestElements(2);
    expect(elems.length).toBe(1);
  });

  test('Returns the correct size(s)', () => {
    const cache = initializeTestCache();
    const size = cache.size();
    expect(size).toStrictEqual({
      0.925: 2, 0.96: 2, 1: 2,
    });
  });

  test('Copies itself correctly', () => {
    const cache = initializeTestCache();
    const copy = cache.copy();
    expect(copy.elements.length).toBe(cache.elements.length);
    copy.elements.forEach((elem, index) => {
      expect(JSON.stringify(elem)).toStrictEqual(JSON.stringify(cache.elements[index]));
    });
  });
});
