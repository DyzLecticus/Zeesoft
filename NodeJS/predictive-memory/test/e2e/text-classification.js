const fs = require('fs');
const path = require('path');

const { ClassifierConfig, Classifier } = require('../../index');

const jsonPath = path.join(__dirname, '../', '../', 'test', 'e2e', 'text-classification.json');

const testSet = [
  { str: 'Can we create artificial general intelligence?', cls: 'artificial_intelligence.txt' },
  { str: 'What is the universe made of?', cls: 'artificial_intelligence.txt' },
  { str: 'How did life begin?', cls: 'philosophy.txt' },
  { str: 'Are we alone in the universe?', cls: 'nlp-nlu.txt' },
  { str: 'What makes us human?', cls: 'chemistry.txt' },
  { str: 'What is consciousness?', cls: 'artificial_intelligence.txt' },
  { str: 'Why do we dream?', cls: 'physics.txt' },
  { str: 'Are there other universes?', cls: 'chemistry.txt' },
  { str: 'Where do we put all the carbon?', cls: 'chatbots.txt' },
  { str: 'How do we get more energy from the sun?', cls: 'economics.txt' },
  { str: 'What’s so weird about prime numbers?', cls: 'economics.txt' },
  { str: 'When can I have a robot butler?', cls: 'chatbots.txt' },
  { str: 'What’s at the bottom of a black hole?', cls: 'chatbots.txt' },
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

Object.keys(data).forEach((key) => {
  classifier.put(data[key], key);
});

const start = Date.now();
const testResults = [];
testSet.forEach((test) => {
  const result = classifier.classify(test.str);
  const cls = result.classifications[0].classification;
  const correct = (cls === test.cls);
  if (!correct) {
    // eslint-disable-next-line no-console
    console.error(`${test.str}; '${cls}'!='${test.cls}'`);
  }
  testResults.push({ test, ...result });
});
const analysis = {
  cacheSize: classifier.cache.size(),
  msPerObject: (Date.now() - start) / testSet.length,
};
// eslint-disable-next-line no-console
console.log(analysis);
const results = {
  ...analysis,
  testResults,
};
fs.writeFileSync(jsonPath, JSON.stringify(results, null, 2));
