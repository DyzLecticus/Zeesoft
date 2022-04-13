const CacheConfig = require('../../../src/cache/CacheConfig');

describe('CacheConfig', () => {
  test('Initializes the default configuration correctly', () => {
    const config = new CacheConfig();
    const comp = {};
    config.comparator = comp;
    config.initiatlizeDefault();
    expect(config.mergeSimilarity).toBe(0.925);
    expect(config.subConfig.mergeSimilarity).toBe(0.96);
    expect(config.subConfig.subConfig.mergeSimilarity).toBe(1.0);
    expect(config.subConfig.subConfig.comparator).toBe(comp);
  });

  test('Sets the comparator recursively', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const comp = {};
    config.setComparator(comp);
    expect(config.subConfig.subConfig.comparator).toBe(comp);
  });

  test('Returns correct query options', () => {
    const config = new CacheConfig();
    let options = config.getQueryOptions();
    expect(options.minSimilarity).toBe(0);
    expect(options.maxDepth).toBe(0);
    expect(options.maxWidth).toBe(2);
    options = config.getQueryOptions(0.9, 1, 3);
    expect(options.minSimilarity).toBe(0.9);
    expect(options.maxDepth).toBe(1);
    expect(options.maxWidth).toBe(3);
  });
});
