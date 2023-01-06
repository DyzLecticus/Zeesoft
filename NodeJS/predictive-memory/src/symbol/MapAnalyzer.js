const MathUtil = require('../MathUtil');

function MapAnalyzer() {
  const that = this;

  this.getDistances = (symbolMapA, symbolMapB) => {
    const r = [];
    const symbolsA = symbolMapA.getAsArray();
    const symbolsB = symbolMapB ? symbolMapB.getAsArray() : symbolsA;
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

  this.analyze = (symbolMapA, symbolMapB) => {
    const distances = that.getDistances(symbolMapA, symbolMapB);
    return {
      distances,
      average: MathUtil.getAverage(distances),
      stdDev: MathUtil.getStandardDeviation(distances),
      min: Math.min(...distances),
      max: Math.max(...distances),
    };
  };
}
module.exports = MapAnalyzer;
