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
  mock.getResults = (key, max, type) => {
    const m = max || mockResultsW.length;
    let r = mockResults.slice(0, m);
    if (type === 'predictedValues') {
      r = mockResultsW.slice(0, m);
    }
    return r;
  };
  return mock;
};

describe('PredictorAnalyzer', () => {
  test('Returns correct analysis options', () => {
    const analyzer = new PredictorAnalyzer();
    let options = analyzer.getAnalysisOptions(10, 'pizza');
    expect(options.max).toBe(10);
    expect(options.type).toBe('pizza');
    options = analyzer.getAnalysisOptions();
    expect(options.max).toBe(0);
    expect(options.type).toBe('predictedValues');
  });

  test('Analyzes predictor results correctly', () => {
    const analyzer = new PredictorAnalyzer();
    const analysis = analyzer.analyze(getMockPredictor(), ['a'], analyzer.getAnalysisOptions(0, 'predictedValues'));
    expect(analysis.a.accuracy).toBe(0.9622410734567873);
    expect(analysis.a.accuracyStdDev).toBe(0.020102689034713326);
    expect(analysis.a.accuracyTrend).toBe(1);
    expect(analysis.a.valueStdDev).toBe(0.39370039370059073);
  });

  test('Analyzes limited predictor results correctly', () => {
    const analyzer = new PredictorAnalyzer(['a'], 'predictedValues');
    const analysis = analyzer.analyze(getMockPredictor(), ['a'], analyzer.getAnalysisOptions(4, 'predictedValues'));
    expect(analysis.a.accuracy).toBe(0.975196009481389);
  });

  test('Analyzes weighted predictor results correctly', () => {
    const analyzer = new PredictorAnalyzer();
    const analysis = analyzer.analyze(getMockPredictor(), ['a']);
    expect(analysis.a.accuracy).toBe(0.9622410734567873);
    expect(analysis.a.accuracyStdDev).toBe(0.020102689034713326);
    expect(analysis.a.accuracyTrend).toBe(1);
    expect(analysis.a.valueStdDev).toBe(0.39370039370059073);
  });
});
