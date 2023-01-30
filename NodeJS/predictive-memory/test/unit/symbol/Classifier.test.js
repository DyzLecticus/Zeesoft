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

    expect(classifier.classify('Pizza!').classifications.length).toBe(0);

    classifier.put('This is some english language text.', 'EN');

    expect(classifier.recordedInput.length).toBe(0);
    classifier.setRecordInput(true);

    classifier.put('What language do you speak?', 'EN');
    classifier.put('Do you speak english?', 'EN');
    classifier.put('I can understand english and dutch.', 'EN');

    classifier.put('Dit is wat nederlandse tekst.', 'NL');
    classifier.put('Welke taal spreek jij?', 'NL');
    classifier.put('Spreek je nederlands?', 'NL');
    classifier.put('Ik begrijp nederlands en engels.', 'NL');

    expect(classifier.recordedInput.length).toBe(7);
    expect(classifier.recordedInput[6].str).toBe('Ik begrijp nederlands en engels.');
    expect(classifier.recordedInput[6].cls).toBe('NL');

    let result = classifier.classify('Do you understand the language I speak?');
    expect(result.classifications[0].classification).toBe('EN');
    expect(result.classifications[0].confidence).toBe(0.44671201814058953);

    result = classifier.classify('Begrijp je de taal die ik spreek?');
    expect(result.classifications[0].classification).toBe('NL');
    expect(result.classifications[0].confidence).toBe(0.38502331002331003);

    result = classifier.classify('This is some nederlandse tekst.');
    expect(result.classifications[0].classification).toBe('NL');
    expect(result.classifications[0].confidence).toBe(0.5673076923076923);

    result = classifier.classify('What language do you speak?');
    expect(result.classifications[0].classification).toBe('EN');
    expect(result.classifications[0].confidence).toBe(1);

    result = classifier.classify('Pizza!');
    expect(result.classifications[0].classification).toBe('NL');
    expect(result.classifications[0].confidence).toBe(0.14285714285714285);
  });
});
