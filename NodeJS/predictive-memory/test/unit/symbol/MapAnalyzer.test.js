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
    expect(analysis1.average).toBe(128.28175388210283);
    expect(analysis1.stdDev).toBe(85.78992401399499);
    expect(analysis1.min).toBe(0);
    expect(analysis1.max).toBe(204.10046545757803);

    expect(analysis2.distances.length).toBe(9);
    expect(analysis2.average).toBe(130.089045774999);
    expect(analysis2.stdDev).toBe(98.73927886367709);
    expect(analysis2.min).toBe(0);
    expect(analysis2.max).toBe(219.4219679065886);
  });

  test('Compares two maps correctly', () => {
    const analyzer = new MapAnalyzer();
    const analysis = analyzer.analyze(createMap1(), createMap2());
    expect(analysis.distances.length).toBe(12);
    expect(analysis.average).toBe(214.32664764286594);
    expect(analysis.stdDev).toBe(21.29886322627996);
    expect(analysis.min).toBe(185.5693940282179);
    expect(analysis.max).toBe(252.43018836898253);
  });
});
