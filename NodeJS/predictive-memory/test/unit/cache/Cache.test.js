const CacheConfig = require('../../../src/cache/CacheConfig');
const Cache = require('../../../src/cache/Cache');

describe('Cache', () => {
  test('Constructs itself correctly', () => {
    const config = new CacheConfig();
    config.initiatlizeDefault();
    const cache = new Cache(config);
    expect(cache.config).toBe(config);
  });
});
