const CacheConfig = require('../../../src/cache/CacheConfig');

describe('CacheConfig', () => {
  test('Initializes the default configuration correctly', () => {
    const config = new CacheConfig();
    const comp = {};
    config.comparator = comp;
    config.initiatlizeDefault();
    expect(config.subConfig.mergeSimilarity).toBe(0.98);
    expect(config.subConfig.subConfig.mergeSimilarity).toBe(0.99);
    expect(config.subConfig.subConfig.subConfig.mergeSimilarity).toBe(1.0);
    expect(config.subConfig.subConfig.subConfig.comparator).toBe(comp);
  });

  test('Sets the comparator recursively', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const comp = {};
    config.setComparator(comp);
    expect(config.subConfig.subConfig.subConfig.comparator).toBe(comp);
  });
});
