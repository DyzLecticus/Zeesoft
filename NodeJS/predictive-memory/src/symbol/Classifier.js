const MapAnalyzer = require('./MapAnalyzer');
const SymbolMap = require('./SymbolMap');

function Classifier(symbolMap, mapAnalyzer) {
  const that = this;
  this.map = symbolMap || new SymbolMap();
  this.analyzer = mapAnalyzer || new MapAnalyzer();

  this.analysis = null;

  this.put = (str, cls) => {
    that.map.put(str, { cls });
    that.analysis = null;
  };

  this.train = () => {
    that.analysis = that.analyzer.analyze(that.map);
  };

  this.classify = (str) => {
    if (that.analysis === null) {
      that.train();
    }
    const maxDistance = that.analysis.average + that.analysis.stdDev;
    const results = this.map.getNearest(str, maxDistance);
    return results;
  };
}
module.exports = Classifier;
