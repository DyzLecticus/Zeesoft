const fs = require('fs');
const path = require('path');

const {
  SymbolUtil, ClassifierConfig, Classifier, ClassifierAnalyzer,
} = require('../../index');

const jsonPath = path.join(__dirname, '../', '../', 'test', 'e2e', 'text-classification.json');

const testSet = [
  { str: 'Can we create artificial general intelligence?', cls: 'artificial_intelligence.txt' },
  { str: 'What is the universe made of?', cls: 'chemistry.txt' },
  { str: 'How did life begin?', cls: 'physics.txt' },
  { str: 'Are we alone in the universe?', cls: 'physics.txt' },
  { str: 'What makes us human?', cls: 'artificial_intelligence.txt' },
  { str: 'What is consciousness?', cls: 'artificial_intelligence.txt' },
  { str: 'Why do we dream?', cls: 'chemistry.txt' },
  { str: 'Are there other universes?', cls: 'physics.txt' },
  { str: 'Where do we put all the carbon?', cls: 'chatbots.txt' },
  { str: 'How do we get more energy from the sun?', cls: 'economics.txt' },
  { str: 'What is so weird about prime numbers?', cls: 'economics.txt' },
  { str: 'When can I have a robot butler?', cls: 'chatbots.txt' },
  { str: 'What is at the bottom of a black hole?', cls: 'economics.txt' },
];

const data = {};
const dir = './test/e2e/text';
const files = fs.readdirSync(dir);
files.forEach((file) => {
  if (file.indexOf('.txt') > 0) {
    const fullPath = path.join(dir, '/', file);
    let text = fs.readFileSync(fullPath);
    text = text.toString();
    data[file] = text;
  }
});

const config = new ClassifierConfig();
const classifier = new Classifier(config);
classifier.setRecordInput(true);

Object.keys(data).forEach((key) => {
  const sentences = SymbolUtil.parseSentences(data[key]);
  sentences.forEach((sentence) => {
    classifier.put(sentence, key);
  });
});

const analyzer = new ClassifierAnalyzer();
const analysis = analyzer.analyze(classifier, testSet);
const selfAnalysis = analyzer.analyze(classifier);
const statistics = analyzer.mergeStatistics([analysis.statistics, selfAnalysis.statistics]);
const results = {
  statistics,
  cacheSize: classifier.cache.size(),
};
// eslint-disable-next-line no-console
console.log(results);
results.analysis = analysis;
results.selfAnalysis = selfAnalysis;
fs.writeFileSync(jsonPath, JSON.stringify(results, null, 2));
