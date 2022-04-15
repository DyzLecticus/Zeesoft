const PredictorCacheConfig = require('../../../src/predict/PredictorCacheConfig');

describe('PredictorCacheConfig', () => {
  test('Constructs itself correctly', () => {
    let pcc = new PredictorCacheConfig();
    expect(pcc.cacheConfig).not.toBe(null);
    expect(pcc.cacheIndexes.length).toBe(15);
    expect(pcc.cacheConfig.subConfig).not.toBe(null);
    expect(pcc.cacheQueryOptions.minSimilarity).toBe(0);
    pcc = new PredictorCacheConfig(6);
    expect(pcc.cacheIndexes.length).toBe(10);
    const mockComparator = {};
    pcc.setComparator(mockComparator);
    expect(pcc.cacheConfig.comparator).toBe(mockComparator);
  });
});
