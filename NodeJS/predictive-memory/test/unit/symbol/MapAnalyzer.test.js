const SymbolMap = require('../../../src/symbol/SymbolMap');
const MapAnalyzer = require('../../../src/symbol/MapAnalyzer');

const createMap1 = () => {
  const map = new SymbolMap();
  map.put('What is your name?');
  map.put('What is your full name?');
  map.put('Can you tell me your name?');
  map.put('What do people call you?');
  return map;
};

const createMap2 = () => {
  const map = new SymbolMap();
  map.put('Where can I find the monthly report?');
  map.put('What is the location of the monthly report?');
  map.put('Can you tell me where monthly report is?');
  return map;
};

describe('MapAnalyzer', () => {
  test('Analyzes a single map correctly', () => {
    const analyzer = new MapAnalyzer();
    const analysis1 = analyzer.analyze(createMap1());
    const analysis2 = analyzer.analyze(createMap2());

    expect(analysis1.distances.length).toBe(16);
    expect(analysis1.average).toBe(128.18101201407677);
    expect(analysis1.stdDev).toBe(85.75470853729789);
    expect(analysis1.min).toBe(0);
    expect(analysis1.max).toBe(204.05146409668322);

    expect(analysis2.distances.length).toBe(9);
    expect(analysis2.average).toBe(129.9983808955344);
    expect(analysis2.stdDev).toBe(98.67610442810862);
    expect(analysis2.min).toBe(0);
    expect(analysis2.max).toBe(219.33991884743642);
  });

  test('Compares two maps correctly', () => {
    const analyzer = new MapAnalyzer();
    const analysis = analyzer.analyze(createMap1(), createMap2());
    expect(analysis.distances.length).toBe(12);
    expect(analysis.average).toBe(213.46582433818492);
    expect(analysis.stdDev).toBe(21.58514235518637);
    expect(analysis.min).toBe(184.29324458590446);
    expect(analysis.max).toBe(251.70021851400924);
  });
});
