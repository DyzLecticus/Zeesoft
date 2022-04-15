const AutoCorrectingPredictorConfig = require('../../../src/predict/AutoCorrectingPredictorConfig');

describe('AutoCorrectingPredictorConfig', () => {
  test('Constructs itself correctly', () => {
    let acpc = new AutoCorrectingPredictorConfig();
    expect(acpc.maxHistorySize).toBe(129);
    expect(acpc.cacheIndexes.length).toBe(15);
    expect(acpc.correctionCacheIndexes.length).toBe(15);
    acpc = new AutoCorrectingPredictorConfig(33, 6);
    expect(acpc.maxHistorySize).toBe(33);
    expect(acpc.correctionCacheIndexes.length).toBe(10);
    const mockComparator = {};
    acpc.setComparator(mockComparator);
    expect(acpc.comparator).toBe(mockComparator);
    expect(acpc.cacheConfig.comparator).toBe(mockComparator);
    expect(acpc.correctionCacheConfig.comparator).toBe(mockComparator);
  });
});
