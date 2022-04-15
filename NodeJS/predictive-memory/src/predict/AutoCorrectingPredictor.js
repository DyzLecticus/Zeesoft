const AutoCorrectingPredictorConfig = require('./AutoCorrectingPredictorConfig');
const Extender = require('../Extender');
const Predictor = require('./Predictor');
const Cache = require('../cache/Cache');
const ObjectPrediction = require('./ObjectPrediction');

function AutoCorrectingPredictor(config) {
  this.config = config || new AutoCorrectingPredictorConfig();

  Extender.copyInstanceProperties(new Predictor(this.config), this);
  this.predictorProcess = this.process;

  this.correctionCache = new Cache(this.config.correctionCacheConfig);

  this.process = (hist) => {
    // If has transformer and has previous prediction
    const previousPrediction = this.predictions.get([0])[0];
    if (previousPrediction) {
      // TODO: Use weighted?
      const correction = this.config.transformer.calculateTransformation(
        previousPrediction.weightedPredictedValues,
        hist,
      );
      // Store in corrector
      const key = this.getCacheHistory().get(this.config.correctionCacheIndexes);
      this.correctionCache.process(key, correction);
    }
    const prediction = this.predictorProcess(hist);
    if (prediction) {
      const key = this.getCacheHistory().get(this.config.correctionCacheIndexes);
      const result = this.correctionCache.query(key, this.config.correctionCacheQueryOptions);
      const correctionPrediction = new ObjectPrediction(result);
      correctionPrediction.weightedPredictedValues = {};
      prediction.correctedPredictedValues = this.config.transformer.applyTransformation(
        prediction.weightedPredictedValues,
        correctionPrediction.weightedPredictedValues,
      );
    }
    return prediction;
  };
}
module.exports = AutoCorrectingPredictor;
