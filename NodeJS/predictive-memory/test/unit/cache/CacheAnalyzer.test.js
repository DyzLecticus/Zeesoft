const CacheConfig = require('../../../src/cache/CacheConfig');
const Cache = require('../../../src/cache/Cache');
const CacheAnalyzer = require('../../../src/cache/CacheAnalyzer');

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

const k3 = [
  { a: 2, b: 2, c: 4 },
  { a: 3, b: 2, c: 4 },
];
const v3 = { a: 4, b: 2, c: 8 };

describe('CacheAnalyzer', () => {
  test('Analyzes caches correctly', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const cache = new Cache(config);
    cache.process(k1, v1);
    cache.process(k2, v2);
    cache.process(k3, v3);
    const analyzer = new CacheAnalyzer();
    const analysis = analyzer.analyze(cache);
    const expected = {
      level1: {
        elements: 2,
        mergeSimilarity: 0.925,
        sizeAverage: 1,
        sizeMax: 1,
        sizeMin: 1,
        sizeStdDev: 0,
      },
      level2: {
        elements: 2,
        mergeSimilarity: 0.96,
        sizeAverage: 1.5,
        sizeMax: 2,
        sizeMin: 1,
        sizeStdDev: 0.7071067811865476,
      },
      level3: {
        elements: 3,
        mergeSimilarity: 1,
        sizeAverage: 1,
        sizeMax: 1,
        sizeMin: 1,
        sizeStdDev: 0,
      },
    };
    expect(analysis).toStrictEqual(expected);
  });
});
