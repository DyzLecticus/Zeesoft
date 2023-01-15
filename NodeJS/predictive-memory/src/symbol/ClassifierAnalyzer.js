const MathUtil = require('../MathUtil');

function ClassifierAnalyzer(testPercentage) {
  const that = this;

  this.testPercentage = testPercentage || 0.1;

  this.getTestSet = (classifier) => {
    const mod = Math.round(100 / (100 * that.testPercentage));
    const r = [];
    classifier.recordedInput.forEach((input, index) => {
      if (index % mod === 0) {
        r.push(input);
      }
    });
    return r;
  };

  this.analyze = (classifier, customTestSet) => {
    const start = Date.now();
    const accuracies = [];
    const conficdences = [];
    const testResults = [];
    const testSet = customTestSet || that.getTestSet(classifier);
    testSet.forEach((test) => {
      const result = classifier.classify(test.str);
      const acc = result.classifications[0].classification === test.cls ? 1 : 0;
      accuracies.push(acc);
      conficdences.push(result.classifications[0].confidence);
      testResults.push({ test, classifications: result.classifications });
    });
    const msPerString = (Date.now() - start) / testSet.length;
    return {
      statistics: {
        accuracy: MathUtil.getAverage(accuracies),
        confidence: MathUtil.getAverage(conficdences),
        confidenceStdDev: MathUtil.getStandardDeviation(conficdences),
        msPerString,
      },
      testResults,
    };
  };

  this.mergeStatistics = (statistics) => {
    let accuracy = 0;
    let confidence = 0;
    let confidenceStdDev = 0;
    let msPerString = 0;
    statistics.forEach((stats) => {
      accuracy += stats.accuracy;
      confidence += stats.confidence;
      confidenceStdDev += stats.confidenceStdDev;
      msPerString += stats.msPerString;
    });
    if (statistics.length > 1) {
      accuracy /= statistics.length;
      confidence /= statistics.length;
      confidenceStdDev /= statistics.length;
      msPerString /= statistics.length;
    }
    return {
      accuracy, confidence, confidenceStdDev, msPerString,
    };
  };
}
module.exports = ClassifierAnalyzer;
