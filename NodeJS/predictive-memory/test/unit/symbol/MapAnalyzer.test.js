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
    expect(analysis1.average).toBe(128.2217710504569);
    expect(analysis1.stdDev).toBe(85.77755683897252);
    expect(analysis1.min).toBe(0);
    expect(analysis1.max).toBe(204.09801566894274);

    expect(analysis2.distances.length).toBe(9);
    expect(analysis2.average).toBe(130.046257736851);
    expect(analysis2.stdDev).toBe(98.7090026528512);
    expect(analysis2.min).toBe(0);
    expect(analysis2.max).toBe(219.3855054464629);
  });

  test('Compares two maps correctly', () => {
    const analyzer = new MapAnalyzer();
    const analysis = analyzer.analyze(createMap1(), createMap2());
    expect(analysis.distances.length).toBe(12);
    expect(analysis.average).toBe(213.5947034663482);
    expect(analysis.stdDev).toBe(21.55675766957668);
    expect(analysis.min).toBe(184.48848202530152);
    expect(analysis.max).toBe(251.7876089087785);
  });
});
