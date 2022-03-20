const PredictorAnalyzer = require('../../../src/predict/PredictorAnalyzer');
const Comparator = require('../../../src/Comparator');

const mockResults = [
  { predicted: 10, actual: 10 },
  { predicted: 10, actual: 9 },
  { predicted: 10, actual: 10 },
  { predicted: 10, actual: 9 },
  { predicted: 10, actual: 9 },
  { predicted: 10, actual: 9 },
  { predicted: 10, actual: 11 },
  { predicted: 10, actual: 9 },
  { predicted: 10, actual: 9 },
];

const mockResultsW = [
  { predicted: 10, actual: 10 },
  { predicted: 10, actual: 9.1 },
  { predicted: 10, actual: 10.1 },
  { predicted: 10, actual: 9.1 },
  { predicted: 10, actual: 9.1 },
  { predicted: 10, actual: 9.1 },
  { predicted: 10, actual: 11.1 },
  { predicted: 10, actual: 9.1 },
  { predicted: 10, actual: 9.1 },
];

const getMockPredictor = () => {
  const mock = {
    config: {
      comparator: new Comparator(),
    },
  };
  mock.getResults = (key, weighted) => {
    let r = mockResults;
    if (weighted) {
      r = mockResultsW;
    }
    return r;
  };
  return mock;
};

describe('PredictorAnalyzer', () => {
  test('Constructs itself results correctly', () => {
    const analyzer = new PredictorAnalyzer();
    expect(analyzer.keys.length).toBe(0);
  });

  test('Analyzes predictor results correctly', () => {
    const analyzer = new PredictorAnalyzer('a', false);
    const analysis = analyzer.analyze(getMockPredictor());
    expect(analysis.a.accuracy).toBe(0.9596212754107493);
    expect(analysis.a.accuracyStdDev).toBe(0.02295130637897434);
    expect(analysis.a.accuracyTrend).toBe(1);
    expect(analysis.a.valueStdDev).toBe(0.44095855184409843);
  });

  test('Analyzes weighted predictor results correctly', () => {
    const analyzer = new PredictorAnalyzer('a');
    const analysis = analyzer.analyze(getMockPredictor());
    expect(analysis.a.accuracy).toBe(0.9622410734567873);
    expect(analysis.a.accuracyStdDev).toBe(0.020102689034713326);
    expect(analysis.a.accuracyTrend).toBe(1);
    expect(analysis.a.valueStdDev).toBe(0.39370039370059073);
  });
});
