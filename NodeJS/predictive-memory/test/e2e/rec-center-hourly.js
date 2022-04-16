/**
 * This test uses the Predictor to predict hourly energy usage.
 *
 * The data for this test was found at;
 * https://raw.githubusercontent.com/numenta/nupic/master/examples/opf/clients/hotgym/prediction/one_gym/rec-center-hourly.csv
 */
const fs = require('fs');
const path = require('path');

const { PredictorConfig, Predictor, PredictorAnalyzer } = require('../../index');

const type = 'weightedPredictedValues';
const csvPath = path.join(__dirname, '../', '../', 'test', 'e2e', 'rec-center-hourly.csv');
const jsonPath = path.join(__dirname, '../', '../', 'test', 'e2e', 'rec-center-hourly.json');

const data = fs.readFileSync(csvPath);

const pc = new PredictorConfig();
pc.maxHistorySize = 1000;
// This cache configuration is tuned for high accuracy on this data set
pc.cacheConfig.mergeSimilarity = 0.975;
pc.cacheConfig.maxSize = 500;
pc.cacheConfig.subConfig.mergeSimilarity = 0.9825;
const predictor = new Predictor(pc);

const lines = data.toString().split('\n');
let start = 0;
for (let i = 3; i < lines.length; i += 1) {
  if (lines[i].length) {
    const dtv = lines[i].split(',');
    const val = parseFloat(dtv[1]);
    const dt = dtv[0].split(' ');
    const dats = dt[0].split('/');
    const tims = dt[1].split(':');
    const d = new Date();
    d.setFullYear(parseInt(`20${dats[2]}`, 10));
    d.setMonth(parseInt(dats[0], 10) - 1);
    d.setDate(parseInt(dats[1], 10));
    d.setHours(parseInt(tims[0], 10), parseInt(tims[1], 10), 0);
    const obj = {
      wd: d.getDay() + 1,
      h: d.getHours() + 1,
      kwh: parseInt(val, 10),
    };
    predictor.process(obj);
    if (i === 3000) {
      start = Date.now();
      predictor.setPredict(true);
    }
  }
}

const analyzer = new PredictorAnalyzer(['kwh'], type);
const analysis = analyzer.analyze(predictor);
analysis.cacheSize = predictor.cache.size();
analysis.msPerObject = (Date.now() - start) / (lines.length - 3000);
// eslint-disable-next-line no-console
console.log(analysis);
analysis.results = predictor.getResults(['kwh'], type);
fs.writeFileSync(jsonPath, JSON.stringify(analysis, null, 2));
