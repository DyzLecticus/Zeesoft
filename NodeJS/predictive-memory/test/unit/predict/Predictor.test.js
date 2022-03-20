const PredictorConfig = require('../../../src/predict/PredictorConfig');
const Predictor = require('../../../src/predict/Predictor');

const hists = [
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
];

const addHists = (predictor) => {
  const pr = predictor;
  pr.learn = false;
  for (let i = 0; i < hists.length; i += 1) {
    if (i === 30) {
      pr.learn = true;
      pr.predict = true;
    }
    pr.add(hists[i]);
  }
};

describe('Predictor', () => {
  test('Constructs itself correctly', () => {
    const pc = new PredictorConfig();
    pc.maxHistorySize = 1000;
    const predictor = new Predictor(pc);
    expect(predictor.absoluteHistory.maxSize).toBe(1000);
    expect(predictor.relativeHistory.maxSize).toBe(1000);
    expect(predictor.predictions.maxSize).toBe(1000);
  });

  test('Generates absolute predictions correctly', () => {
    const pc = new PredictorConfig();
    pc.transformer = null;
    const predictor = new Predictor(pc);
    addHists(predictor);
    expect(predictor.absoluteHistory.elements.length).toBe(hists.length);
    expect(predictor.relativeHistory.elements.length).toBe(0);
    expect(predictor.predictions.elements.length).toBe(hists.length - 30);
    const prediction = predictor.predictions.get([0])[0];
    expect(prediction.predictedValues).toStrictEqual({ a: 1, b: 4 });
  });

  test('Generates relative predictions correctly', () => {
    const predictor = new Predictor();
    addHists(predictor);
    expect(predictor.absoluteHistory.elements.length).toBe(hists.length);
    expect(predictor.relativeHistory.elements.length).toBe(hists.length - 1);
    expect(predictor.predictions.elements.length).toBe(hists.length - 30);
    const prediction = predictor.predictions.get([0])[0];
    expect(prediction.predictedValues).toStrictEqual({ a: 1, b: 4 });
  });
});
