const PredictorConfig = require('../../../src/predict/PredictorConfig');

describe('PredictorConfig', () => {
  test('Constructs itself correctly', () => {
    let pc = new PredictorConfig();
    expect(pc.maxHistorySize).toBe(1000);
    expect(pc.cacheConfig).not.toBe(null);
    expect(pc.cacheIndexes).not.toBe(null);
    expect(pc.cacheIndexes).not.toBe(null);
    pc = new PredictorConfig(3);
    expect(pc.maxHistorySize).toBe(3);
  });
});
