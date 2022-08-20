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
    pred.rawPredictedValues = this.config.transformer.applyTransformation(
      from,
      pred.rawPredictedValues,
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
   * @returns An ObjectPrediction (with predictedValues and rawPredictedValues) or null
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
   * @param {Number} max The optional maximum number of results
   * @param {String} type The optional prediction type; 'predictedValues' | 'rawPredictedValues'
   * @returns An array of objects containing predicted and actual values for the specified key
   */
  this.getResults = (key, max, type) => {
    const r = [];
    let m = max;
    if (!m || m >= (this.predictions.elements.length - 1)) {
      m = (this.predictions.elements.length - 1);
    }
    const typeName = type || 'predictedValues';
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

  this.copy = () => {
    const r = new Predictor(this.config);
    r.absoluteHistory = this.absoluteHistory.copy();
    r.relativeHistory = this.relativeHistory.copy();
    r.learn = this.learn;
    r.cache = this.cache.copy();
    r.predict = this.predict;
    r.predictions = this.predictions.copy();
    return r;
  };

  /**
   * @param {Number} steps The number of future steps
   * @param {String} type The optional prediction type; 'predictedValues' | 'rawPredictedValues'
   * @returns An array of ObjectPredictions
   */
  this.generatePredictions = (steps, type) => {
    const r = [];
    let pred = this.predictions.get([0])[0];
    if (pred) {
      r.push(pred);
      const pathPredictor = this.copy();
      pathPredictor.setLearn(false);
      const typeName = type || 'predictedValues';
      for (let s = 1; s < steps; s += 1) {
        pred = pathPredictor.process(pred[typeName]);
        r.push(pred);
      }
    }
    return r;
  };
}
module.exports = Predictor;
