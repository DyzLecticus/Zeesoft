const PredictorConfig = require('./PredictorConfig');
const ObjectPrediction = require('./ObjectPrediction');
const History = require('./History');
const Cache = require('../cache/Cache');

function Predictor(config) {
  this.config = config || new PredictorConfig();
  this.absoluteHistory = new History(this.config.maxHistorySize);
  this.relativeHistory = new History(this.config.maxHistorySize);

  this.learn = true;
  this.cache = new Cache(this.config.cacheConfig);

  this.predict = false;
  this.predictions = new History(this.config.maxHistorySize);

  this.setPredict = (p) => {
    this.predict = p;
    if (!p) {
      this.predictions.elements = [];
    }
  };

  this.getCacheHistory = () => (
    this.config.transformer ? this.relativeHistory : this.absoluteHistory
  );

  this.add = (hist) => {
    this.absoluteHistory.add(hist);
    if (this.config.transformer && this.absoluteHistory.elements.length > 1) {
      this.relativeHistory.add(this.config.transformer.calculateTransformation(
        this.absoluteHistory.get([1])[0],
        this.absoluteHistory.get([0])[0],
      ));
    }
    if (this.getCacheHistory().elements.length > 1) {
      if (this.learn) {
        const key = this.getCacheHistory().get(this.config.cacheIndexes);
        const value = this.getCacheHistory().get([0])[0];
        this.cache.process(key, value);
      }
      this.addPrediction();
    }
  };

  this.transformPrediction = (prediction) => {
    const pred = prediction;
    const from = this.absoluteHistory.get([0])[0];
    pred.predictedValues = this.config.transformer.applyTransformation(
      from,
      pred.predictedValues,
    );
    pred.weightedPredictedValues = this.config.transformer.applyTransformation(
      from,
      pred.weightedPredictedValues,
    );
  };

  this.addPrediction = () => {
    if (this.predict) {
      const key = this.getCacheHistory().get(this.config.cacheIndexes, -1);
      const result = this.cache.query(key);
      const pred = new ObjectPrediction(result);
      pred.generatePrediction();
      if (this.config.transformer) {
        this.transformPrediction(pred);
      }
      this.predictions.add(pred);
    }
  };

  this.getResults = (key, weighted, max) => {
    const r = [];
    let m = max;
    if (!m || m >= (this.predictions.elements.length - 1)) {
      m = (this.predictions.elements.length - 1);
    }
    for (let i = 1; i <= m; i += 1) {
      const pred = this.predictions.get([i])[0];
      const pObj = weighted ? pred.weightedPredictedValues : pred.predictedValues;
      const aObj = this.absoluteHistory.get([i - 1])[0];
      const predicted = pObj[key];
      const actual = aObj[key];
      if (predicted !== undefined && actual !== undefined) {
        r.push({ predicted, actual });
      }
    }
    return r;
  };
}
module.exports = Predictor;
