const Classifier = require('../../../src/symbol/Classifier');
const ClassifierAnalyzer = require('../../../src/symbol/ClassifierAnalyzer');

describe('ClassifierAnalyzer', () => {
  test('Constructs itself correctly', () => {
    let analyzer = new ClassifierAnalyzer();
    expect(analyzer.testPercentage).toBe(0.1);
    analyzer = new ClassifierAnalyzer(0.2);
    expect(analyzer.testPercentage).toBe(0.2);
  });

  test('Analyzes classifier recorded input correctly', () => {
    const analyzer = new ClassifierAnalyzer(0.5);

    const classifier = new Classifier();
    classifier.setRecordInput(true);
    classifier.put('This is some english language text.', 'EN');
    classifier.put('What language do you speak?', 'EN');
    classifier.put('Do you speak english?', 'EN');
    classifier.put('I can understand english and dutch.', 'EN');

    classifier.put('Dit is wat nederlandse tekst.', 'NL');
    classifier.put('Welke taal spreek jij?', 'NL');
    classifier.put('Spreek je nederlands?', 'NL');
    classifier.put('Ik begrijp nederlands en engels.', 'NL');
    expect(classifier.recordedInput.length).toBe(8);

    let result = analyzer.analyze(classifier);
    expect(result.statistics.accuracy).toBe(1);
    expect(result.statistics.confidence).toBe(0.9736842105263158);
    expect(result.statistics.confidenceStdDev).toBe(0.05263157894736842);

    const testSet = [
      { str: 'Berijp je deze taal die ik spreek?', cls: 'NL' },
      { str: 'Do you understand the language I am speaking?', cls: 'EN' },
      { str: 'This will be classified incorrectly!', cls: 'FR' },
    ];
    result = analyzer.analyze(classifier, testSet);
    expect(result.statistics.accuracy).toBe(0.6666666666666666);
    expect(result.statistics.confidence).toBe(0.2386100808977556);
    expect(result.statistics.confidenceStdDev).toBe(0.037164569705356316);
  });

  test('Merges statistics correctly', () => {
    const analyzer = new ClassifierAnalyzer();
    const s1 = {
      accuracy: 0.75, confidence: 0.75, confidenceStdDev: 0.25, msPerString: 2,
    };
    const s2 = {
      accuracy: 0.25, confidence: 0.25, confidenceStdDev: 0.75, msPerString: 4,
    };
    let merged = analyzer.mergeStatistics([s1, s2]);
    expect(merged).toStrictEqual({
      accuracy: 0.5,
      confidence: 0.5,
      confidenceStdDev: 0.5,
      msPerString: 3,
    });
    merged = analyzer.mergeStatistics([]);
    expect(merged).toStrictEqual({
      accuracy: 0,
      confidence: 0,
      confidenceStdDev: 0,
      msPerString: 0,
    });
  });
});
