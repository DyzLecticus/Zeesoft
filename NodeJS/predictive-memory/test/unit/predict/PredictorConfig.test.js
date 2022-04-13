const PredictorConfig = require('../../../src/predict/PredictorConfig');

describe('PredictorConfig', () => {
  test('Constructs itself correctly', () => {
    let pc = new PredictorConfig();
    expect(pc.maxHistorySize).toBe(129);
    expect(pc.cacheConfig).not.toBe(null);
    expect(pc.cacheIndexes.length).toBe(15);
    expect(pc.comparator).not.toBe(null);
    expect(pc.transformer).not.toBe(null);
    expect(pc.cacheConfig.comparator).toBe(pc.comparator);
    expect(pc.cacheConfig.subConfig).not.toBe(null);
    expect(pc.cacheQueryOptions.minSimilarity).toBe(0);
    pc = new PredictorConfig(33, 6);
    expect(pc.maxHistorySize).toBe(33);
    expect(pc.cacheIndexes.length).toBe(10);
  });
});
