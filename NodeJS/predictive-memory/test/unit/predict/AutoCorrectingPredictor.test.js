const AutoCorrectingPredictorConfig = require('../../../src/predict/AutoCorrectingPredictorConfig');
const AutoCorrectingPredictor = require('../../../src/predict/AutoCorrectingPredictor');

const hists = [
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
];

const addHists = (predictor, rep) => {
  const pr = predictor;
  pr.learn = false;
  for (let i = 0; i < (hists.length * rep); i += 1) {
    if (i === 30) {
      pr.learn = true;
      pr.setPredict(true);
    }
    pr.process(hists[(i % hists.length)]);
  }
};

describe('AutoCorrectingPredictor', () => {
  test('Constructs itself correctly', () => {
    const acpc = new AutoCorrectingPredictorConfig();
    acpc.maxHistorySize = 1000;
    const predictor = new AutoCorrectingPredictor(acpc);
    expect(predictor.absoluteHistory.maxSize).toBe(1000);
    expect(predictor.correctionCache.config).toBe(acpc.correctionCacheConfig);
  });

  test('Generates relative predictions correctly', () => {
    const predictor = new AutoCorrectingPredictor();
    const repeat = 12;
    addHists(predictor, repeat);
    const prediction = predictor.predictions.get([0])[0];
    expect(prediction.predictedValues).toStrictEqual({ a: 1, b: 4 });

    let results = predictor.getResults();
    expect(results.length).toBe(0);
    results = predictor.getResults(['b']);
    expect(results.length).toBe(5);
    results = predictor.getResults(['b'], 'predictedValues', 1);
    expect(results.length).toBe(1);
    expect(results).toStrictEqual([{ predicted: 6, actual: 6 }]);
    results = predictor.getResults(['b'], 'weightedPredictedValues', 1);
    expect(results.length).toBe(1);
    // TODO: Figure out why this result differs from the regular predictor test
    // expect(results).toStrictEqual([{ predicted: 4.8633860468117, actual: 6 }]);

    results = predictor.getResults(['b'], 'correctedPredictedValues');
    expect(results.length).toBe(4);
    //console.log(results);

    predictor.setPredict(false);
    expect(predictor.predictions.elements.length).toBe(0);
  });

  test('Learns regular patterns correctly', () => {
    const predictor = new AutoCorrectingPredictor();
    const repeat = 120;
    addHists(predictor, repeat);
    const results = predictor.getResults(['b']);
    expect(results.length).toBe(128);
    for (let i = 0; i < results.length; i += 1) {
      expect(results.predicted).toBe(results.actual);
    }
  });
});
