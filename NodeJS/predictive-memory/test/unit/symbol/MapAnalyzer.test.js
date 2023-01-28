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
    expect(analysis1.average).toBe(58.44395647138755);
    expect(analysis1.stdDev).toBe(42.230212906187525);
    expect(analysis1.min).toBe(0);
    expect(analysis1.max).toBe(109.78160137290766);

    expect(analysis2.distances.length).toBe(9);
    expect(analysis2.average).toBe(71.36439426973175);
    expect(analysis2.stdDev).toBe(58.107776022909064);
    expect(analysis2.min).toBe(0);
    expect(analysis2.max).toBe(132.32535660258014);
  });

  test('Compares two maps correctly', () => {
    const analyzer = new MapAnalyzer();
    const analysis = analyzer.analyze(createMap1(), createMap2());
    expect(analysis.distances.length).toBe(12);
    expect(analysis.average).toBe(121.113820844754);
    expect(analysis.stdDev).toBe(19.24556337213142);
    expect(analysis.min).toBe(101.1582918005242);
    expect(analysis.max).toBe(164.26807358704855);
  });
});
