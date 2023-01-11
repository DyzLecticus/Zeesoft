const ClassifierConfig = require('./ClassifierConfig');
const SymbolMap = require('./SymbolMap');
const Cache = require('../cache/Cache');
const MathUtil = require('../MathUtil');

function Classifier(config) {
  const that = this;
  this.config = config || new ClassifierConfig();

  this.map = new SymbolMap(this.config.characters);
  this.cache = new Cache(this.config.cacheConfig);

  this.getKey = (symbol) => ({ symNumArray: symbol.numArray });

  this.put = (str, cls) => {
    const symbol = that.map.put(str, { cls });
    that.cache.process(that.getKey(symbol), {});
  };

  this.classify = (str) => {
    const symbol = that.map.createSymbol(str);
    const cacheResult = that.cache.query(that.getKey(symbol), that.config.cacheQueryOptions);
    const results = cacheResult.getDeepestElements(2);
    let classification = '';
    let confidence = 0;
    if (results.length > 0) {
      const id = MathUtil.stringify(results[0].element.key.symNumArray);
      const resultSymbol = that.map.getById(id);
      classification = resultSymbol.meta.cls;
      confidence = that.config.comparator.calculateValueSimilarity(str, resultSymbol.str);
    }
    return { results, classification, confidence };
  };
}
module.exports = Classifier;
