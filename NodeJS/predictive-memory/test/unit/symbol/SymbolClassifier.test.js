const SymbolClassifier = require('../../../src/symbol/SymbolClassifier');

describe('SymbolClassifier', () => {
  test('Constructs itself correctly', () => {
    const comparator = { name: 'B' };
    const classifier = new SymbolClassifier('ABC123', comparator);
    expect(classifier.map.characters).toBe('ABC123');
    expect(classifier.comparator).toStrictEqual(comparator);
  });

  test('Classifies strings correctly', () => {
    const classifier = new SymbolClassifier();

    expect(classifier.classify('Pizza').results.length).toBe(0);

    classifier.put('This is some english language text.', 'EN');
    classifier.put('What language do you speak?', 'EN');
    classifier.put('Do you speak english?', 'EN');
    classifier.put('I can understand english and dutch.', 'EN');

    classifier.put('Dit is wat nederlandse tekst.', 'NL');
    classifier.put('Welke taal spreek jij?', 'NL');
    classifier.put('Spreek je nederlands?', 'NL');
    classifier.put('Ik begrijp nederlands en engels.', 'NL');

    let result = classifier.classify('Do you understand the language I speak?');
    expect(result.classification).toBe('EN');
    expect(result.confidence).toBe(0.358974358974359);

    result = classifier.classify('Begrijp je de taal die ik spreek?');
    expect(result.classification).toBe('NL');
    expect(result.confidence).toBe(0.3333333333333333);

    result = classifier.classify('What language do you speak?');
    expect(result.classification).toBe('EN');
    expect(result.confidence).toBe(1);

    result = classifier.classify('Pizza!');
    expect(result.classification).toBe('NL');
    expect(result.confidence).toBe(0.045454545454545456);
  });
});
