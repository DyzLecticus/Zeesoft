const PredictorConfig = require('../../../src/predict/PredictorConfig');

describe('PredictorConfig', () => {
  test('Constructs itself correctly', () => {
    let pc = new PredictorConfig();
    expect(pc.maxHistorySize).toBe(1000);
    expect(pc.cacheConfig).not.toBe(null);
    expect(pc.cacheIndexes.length).toBe(15);
    pc = new PredictorConfig(3, 6);
    expect(pc.maxHistorySize).toBe(3);
    expect(pc.cacheIndexes.length).toBe(10);
  });
});
