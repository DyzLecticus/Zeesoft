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
    expect(analysis1.average).toBe(81.99769486272876);
    expect(analysis1.stdDev).toBe(51.77647380498931);
    expect(analysis1.min).toBe(0);
    expect(analysis1.max).toBe(133.37166115783367);

    expect(analysis2.distances.length).toBe(9);
    expect(analysis2.average).toBe(90.52532061693164);
    expect(analysis2.stdDev).toBe(68.76999431512229);
    expect(analysis2.min).toBe(0);
    expect(analysis2.max).toBe(150.11995203836165);
  });

  test('Compares two maps correctly', () => {
    const analyzer = new MapAnalyzer();
    const analysis = analyzer.analyze(createMap1(), createMap2());
    expect(analysis.distances.length).toBe(12);
    expect(analysis.average).toBe(148.45440106768072);
    expect(analysis.stdDev).toBe(15.755200133273245);
    expect(analysis.min).toBe(125.23178510266473);
    expect(analysis.max).toBe(164.93635136015348);
  });
});
