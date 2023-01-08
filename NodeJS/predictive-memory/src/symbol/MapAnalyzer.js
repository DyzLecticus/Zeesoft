const MathUtil = require('../MathUtil');

function MapAnalyzer() {
  const that = this;

  this.getDistances = (symbolsA, symbolsB) => {
    const r = [];
    symbolsA.forEach((symbolA) => {
      symbolsB.forEach((symbolB) => {
        if (symbolA !== symbolB) {
          r.push(symbolA.calculateDistance(symbolB));
        } else {
          r.push(0);
        }
      });
    });
    return r;
  };

  this.createAnalysis = (distances) => ({
    distances,
    average: MathUtil.getAverage(distances),
    stdDev: MathUtil.getStandardDeviation(distances),
    min: Math.min(...distances),
    max: Math.max(...distances),
  });

  this.analyze = (symbolMapA, symbolMapB) => {
    const symbolsA = symbolMapA.toArray();
    const symbolsB = symbolMapB ? symbolMapB.toArray() : symbolsA;
    const distances = that.getDistances(symbolsA, symbolsB);
    return that.createAnalysis(distances);
  };
}
module.exports = MapAnalyzer;
