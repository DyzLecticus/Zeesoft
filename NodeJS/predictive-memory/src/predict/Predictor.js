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
  this.predictions = new History(this.config.maxPredictionHistorySize);

  this.setLearn = (l) => {
    this.learn = l;
  };

  this.setPredict = (p) => {
    this.predict = p;
    if (!p) {
      this.predictions.elements = [];
    }
  };

  this.getCacheHistory = () => (
    this.config.transformer ? this.relativeHistory : this.absoluteHistory
  );

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
    let r = null;
    if (this.predict) {
      const key = this.getCacheHistory().get(this.config.cacheIndexes, -1);
      const result = this.cache.query(key, this.config.cacheQueryOptions);
      r = new ObjectPrediction(result);
      r.generatePrediction();
      if (this.config.transformer) {
        this.transformPrediction(r);
      }
      this.predictions.add(r);
    }
    return r;
  };

  /**
   * @param {Object} hist An object that has one or more properties (See Comparator and Transformer)
   * @returns An ObjectPrediction (with predictedValues and weightedPredictedValues) or null
   */
  this.process = (hist) => {
    let r = null;
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
      r = this.addPrediction();
    }
    return r;
  };

  /**
   * @param {String} key The object property key
   * @param {Number} max The maximum number of results
   * @param {String} type The prediction type; 'predictedValues' | 'weightedPredictedValues'
   * @returns An array of objects containing predicted and actual values for the specified key
   */
  this.getResults = (key, max, type) => {
    const r = [];
    let m = max;
    if (!m || m >= (this.predictions.elements.length - 1)) {
      m = (this.predictions.elements.length - 1);
    }
    const typeName = type || 'weightedPredictedValues';
    for (let i = 1; i <= m; i += 1) {
      const pred = this.predictions.get([i])[0];
      const predicted = pred[typeName][key];
      const hist = this.absoluteHistory.get([i - 1])[0];
      const actual = hist[key];
      if (predicted !== undefined && actual !== undefined) {
        r.push({ predicted, actual });
      }
    }
    return r;
  };
}
module.exports = Predictor;
