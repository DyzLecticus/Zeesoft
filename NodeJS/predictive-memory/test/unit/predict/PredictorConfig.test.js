const PredictorConfig = require('../../../src/predict/PredictorConfig');

describe('PredictorConfig', () => {
  test('Constructs itself correctly', () => {
    let pc = new PredictorConfig();
    expect(pc.maxHistorySize).toBe(128);
    expect(pc.cacheConfig).not.toBe(null);
    expect(pc.cacheIndexes.length).toBe(15);
    expect(pc.comparator).not.toBe(null);
    expect(pc.transformer).not.toBe(null);
    expect(pc.cacheConfig.comparator).toBe(pc.comparator);
    pc = new PredictorConfig(32, 6);
    expect(pc.maxHistorySize).toBe(32);
    expect(pc.cacheIndexes.length).toBe(10);
  });
});
