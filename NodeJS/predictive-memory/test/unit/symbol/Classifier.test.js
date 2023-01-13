const ClassifierConfig = require('../../../src/symbol/ClassifierConfig');
const Classifier = require('../../../src/symbol/Classifier');

describe('Classifier', () => {
  test('Constructs itself correctly', () => {
    const config = new ClassifierConfig('ABC');
    const classifier = new Classifier(config);
    expect(classifier.config).toBe(config);
    expect(classifier.config.characters).toBe('ABC');
  });

  test('Classifies strings correctly', () => {
    const classifier = new Classifier();

    expect(classifier.classify('Pizza!')[0].results.length).toBe(0);

    classifier.put('This is some english language text.', 'EN');
    classifier.put('What language do you speak?', 'EN');
    classifier.put('Do you speak english?', 'EN');
    classifier.put('I can understand english and dutch.', 'EN');

    classifier.put('Dit is wat nederlandse tekst.', 'NL');
    classifier.put('Welke taal spreek jij?', 'NL');
    classifier.put('Spreek je nederlands?', 'NL');
    classifier.put('Ik begrijp nederlands en engels.', 'NL');

    let result = classifier.classify('Do you understand the language I speak?');
    expect(result[0].classification).toBe('EN');
    expect(result[0].confidence).toBe(0.425);

    result = classifier.classify('Begrijp je de taal die ik spreek?');
    expect(result[0].classification).toBe('NL');
    expect(result[0].confidence).toBe(0.35294117647058826);

    result = classifier.classify('What language do you speak?');
    expect(result[0].classification).toBe('EN');
    expect(result[0].confidence).toBe(1);

    result = classifier.classify('Pizza!');
    expect(result[0].classification).toBe('NL');
    expect(result[0].confidence).toBe(0.08695652173913043);
  });
});
