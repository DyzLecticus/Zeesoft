const MathUtil = require('../MathUtil');

function PredictorAnalyzer() {
  this.getAnalysisOptions = (max, type) => ({
    max: max || 0,
    type: type || 'weightedPredictedValues',
  });

  this.getAccuracies = (results, comparator) => {
    const accs = [];
    for (let i = 0; i < results.length; i += 1) {
      const p = results[i].predicted;
      const a = results[i].actual;
      accs.push(comparator.calculateValueSimilarity(p, a));
    }
    return accs;
  };

  this.getAccuracy = (results, comparator) => MathUtil.getAverage(
    this.getAccuracies(results, comparator),
  );

  this.getAccuracyStdDev = (results, comparator) => MathUtil.getStandardDeviation(
    this.getAccuracies(results, comparator),
  );

  this.getAccuracyTrend = (results, comparator) => {
    const subResults = [];
    const m = Math.round(results.length / 10);
    for (let i = 0; i < m; i += 1) {
      subResults.push(results[i]);
    }
    return this.getAccuracy(subResults, comparator);
  };

  this.getValueStdDev = (results) => {
    const diffs = [];
    for (let i = 0; i < results.length; i += 1) {
      const p = results[i].predicted;
      const a = results[i].actual;
      let diff = p - a;
      if (diff < 0) {
        diff *= -1;
      }
      diffs.push(diff);
    }
    return MathUtil.getStandardDeviation(diffs);
  };

  /**
   * @param {Predictor} predictor The predictor to analyze
   * @param {Array} keys
   * @param {Object} options An optional object that specifies type and max results
   * @returns An object containing the results of the analysis
   */
  this.analyze = (predictor, keys, options) => {
    const r = {};
    const opts = options || this.getAnalysisOptions();
    for (let k = 0; k < keys.length; k += 1) {
      const key = keys[k];
      const results = predictor.getResults(key, opts.max, opts.type);
      r[key] = {
        accuracy: this.getAccuracy(results, predictor.config.comparator),
        accuracyStdDev: this.getAccuracyStdDev(results, predictor.config.comparator),
        accuracyTrend: this.getAccuracyTrend(results, predictor.config.comparator),
        valueStdDev: this.getValueStdDev(results),
      };
    }
    return r;
  };
}
module.exports = PredictorAnalyzer;
