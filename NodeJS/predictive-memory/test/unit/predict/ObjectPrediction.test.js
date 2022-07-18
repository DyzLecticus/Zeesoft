const ObjectPrediction = require('../../../src/predict/ObjectPrediction');
const CacheResult = require('../../../src/cache/CacheResult');

const getCacheResult = () => {
  const res = new CacheResult();
  res.addLevelElement(1, 0.98, 3, { count: 2, value: { a: 1, b: 2 } });
  res.addLevelElement(1, 0.96, 1, { count: 2, value: { a: 1, b: 2 } });
  res.addLevelElement(1, 0.99, 4, { count: 2, value: { a: 1, b: 3 } });
  res.addLevelElement(1, 0.97, 2, { count: 2, value: { a: 1, b: 3 } });
  return res;
};

describe('ObjectPrediction', () => {
  test('Generates key predictions correctly', () => {
    const pred = new ObjectPrediction(getCacheResult());
    pred.generateKeyPredictions(pred.cacheResult.getDeepestElements());
    expect(pred.keyPredictions.a[0].value).toBe(1);
    expect(pred.keyPredictions.a[0].totalSimilarity).toBe(3.9);
    expect(pred.keyPredictions.a[0].totalCount).toBe(18);
    expect(pred.keyPredictions.b[0].value).toBe(3);
    expect(pred.keyPredictions.b[0].totalSimilarity).toBe(1.96);
    expect(pred.keyPredictions.b[0].totalCount).toBe(10);
    expect(pred.keyPredictions.b[1].value).toBe(2);
    expect(pred.keyPredictions.b[1].totalSimilarity).toBe(1.94);
    expect(pred.keyPredictions.b[1].totalCount).toBe(8);
  });

  test('Calculates key prediction weights correctly', () => {
    const pred = new ObjectPrediction(getCacheResult());
    pred.generateKeyPredictions(pred.cacheResult.getDeepestElements());
    pred.calculateKeyPredictionWeights();
    expect(pred.keyPredictions.a[0].weight).toBe(1);
    expect(pred.keyPredictions.b[0].weight).toBe(0.5580865603644647);
    expect(pred.keyPredictions.b[1].weight).toBe(0.44191343963553525);
  });

  test('Calculates predicted values correctly', () => {
    const pred = new ObjectPrediction(getCacheResult());
    pred.generateKeyPredictions(pred.cacheResult.getDeepestElements());
    pred.calculateKeyPredictionWeights();
    pred.keyPredictions.c = [{ value: 'C', weight: 1.0 }];
    pred.calculatePredictedValues();
    expect(pred.rawPredictedValues.a).toBe(1);
    expect(pred.rawPredictedValues.b).toBe(3);
    expect(pred.rawPredictedValues.c).toBe('C');
    expect(pred.predictedValues.a).toBe(1);
    expect(pred.predictedValues.b).toBe(2.5580865603644645);
    expect(pred.predictedValues.c).toBe('C');
  });

  test('Generates predictions correctly', () => {
    const pred = new ObjectPrediction(getCacheResult());
    pred.generatePrediction();
    expect(pred.rawPredictedValues.a).toBe(1);
    expect(pred.rawPredictedValues.b).toBe(3);
    expect(pred.predictedValues.a).toBe(1);
    expect(pred.predictedValues.b).toBe(2.5580865603644645);
  });
});
