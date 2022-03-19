const PredictorConfig = require('./PredictorConfig');
const ObjectPrediction = require('./ObjectPrediction');
const History = require('./History');
const Cache = require('../cache/Cache');

function Predictor(config) {
  this.config = config || new PredictorConfig();
  this.absoluteHistory = new History(this.config.maxHistorySize);
  this.relativeHistory = new History(this.config.maxHistorySize);
  this.cache = new Cache(this.config.cacheConfig);

  this.predictions = new History(this.config.maxHistorySize);
  this.predict = false;

  this.getCacheHistory = () => (
    this.config.transformer ? this.relativeHistory : this.absoluteHistory
  );

  this.add = (hist) => {
    this.absoluteHistory.add(hist);
    if (this.config.transformer && this.absoluteHistory.elements.length > 1) {
      this.relativeHistory.add(this.config.transformer.calculateTransformation(
        this.absoluteHistory.get([1]),
        this.absoluteHistory.get([0]),
      ));
    }
    if (this.getCacheHistory().elements.length > 1) {
      const key = this.getCacheHistory().get(this.config.cacheIndexes);
      const value = this.getCacheHistory().get([0])[0];
      this.cache.process(key, value);
      this.addPrediction();
    }
  };

  this.addPrediction = () => {
    if (this.predict) {
      const result = this.cache.query(this.getCacheHistory().get(this.config.cacheIndexes, -1));
      const pred = new ObjectPrediction(result);
      pred.generatePrediction();
      this.predictions.add(pred);
    }
  };
}
module.exports = Predictor;
