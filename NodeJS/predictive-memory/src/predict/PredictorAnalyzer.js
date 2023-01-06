const MathUtil = require('../MathUtil');

function PredictorAnalyzer() {
  this.getAnalysisOptions = (max, type) => ({
    max: max || 0,
    type: type || 'predictedValues',
  });

  this.getAccuracies = (results, comparator) => results.map(
    (result) => comparator.calculateValueSimilarity(result.predicted, result.actual),
  );

  this.getAccuracy = (results, comparator) => MathUtil.getAverage(
    this.getAccuracies(results, comparator),
  );

  this.getAccuracyStdDev = (results, comparator) => MathUtil.getStandardDeviation(
    this.getAccuracies(results, comparator),
  );

  this.getAccuracyTrend = (results, comparator) => {
    const m = Math.round(results.length / 10);
    return this.getAccuracy(results.slice(0, m), comparator);
  };

  this.getValueStdDev = (results) => {
    const diffs = results.map((result) => {
      let diff = result.predicted - result.actual;
      if (diff < 0) {
        diff *= -1;
      }
      return diff;
    });
    return MathUtil.getStandardDeviation(diffs);
  };

  /**
   * @param {Predictor} predictor The predictor to analyze
   * @param {Array} keys A list of value keys to analyze
   * @param {Object} options An optional object that specifies type and max results
   * @returns An object containing the results of the analysis
   */
  this.analyze = (predictor, keys, options) => {
    const r = {};
    const opts = options || this.getAnalysisOptions();
    keys.forEach((key) => {
      const results = predictor.getResults(key, opts.max, opts.type);
      r[key] = {
        accuracy: this.getAccuracy(results, predictor.config.comparator),
        accuracyStdDev: this.getAccuracyStdDev(results, predictor.config.comparator),
        accuracyTrend: this.getAccuracyTrend(results, predictor.config.comparator),
        valueStdDev: this.getValueStdDev(results),
      };
    });
    return r;
  };
}
module.exports = PredictorAnalyzer;
