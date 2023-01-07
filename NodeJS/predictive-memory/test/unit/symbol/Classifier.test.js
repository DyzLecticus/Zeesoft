const SymbolMap = require('../../../src/symbol/SymbolMap');
const MapAnalyzer = require('../../../src/symbol/MapAnalyzer');
const Classifier = require('../../../src/symbol/Classifier');

describe('MapAnalyzer', () => {
  test('Constructs itself correctly', () => {
    const map = new SymbolMap();
    const analyzer = new MapAnalyzer();
    const classifier = new Classifier(map, analyzer);
    expect(classifier.map).toBe(map);
    expect(classifier.analyzer).toBe(analyzer);
  });

  test('Classifies strings correctly', () => {
    const classifier = new Classifier();
    classifier.put('This is some english language text.', 'EN');
    classifier.put('What language do you speak?', 'EN');
    classifier.put('Do you speak english?', 'EN');
    classifier.put('I can understand english and dutch.', 'EN');

    classifier.put('Dit is wat nederlandse tekst.', 'NL');
    classifier.put('Welke taal spreek jij?', 'NL');
    classifier.put('Spreek je nederlands?', 'NL');
    classifier.put('I begrijp nederlands en engels.', 'EN');

    let result = classifier.classify('Do you understand the language I speak?');
    expect(result[0].symbol.meta.cls).toBe('EN');

    result = classifier.classify('Begrijp je de taal die ik spreek?');
    expect(result[0].symbol.meta.cls).toBe('NL');
  });
});
