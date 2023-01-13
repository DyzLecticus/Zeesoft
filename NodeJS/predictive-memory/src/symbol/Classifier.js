const ClassifierConfig = require('./ClassifierConfig');
const SymbolMap = require('./SymbolMap');
const Cache = require('../cache/Cache');
const MathUtil = require('../MathUtil');
const SymbolUtil = require('./SymbolUtil');

function Classifier(config) {
  const that = this;
  this.config = config || new ClassifierConfig();

  this.map = new SymbolMap(this.config.characters);
  this.cache = new Cache(this.config.cacheConfig);

  this.getKey = (symbol) => ({ symNumArray: symbol.numArray });

  this.put = (str, cls) => {
    const r = [];
    const sequences = SymbolUtil.sequentialize(str);
    sequences.forEach((sequence) => {
      const symbol = that.map.put(sequence, { cls });
      that.cache.process(that.getKey(symbol), {});
      r.push(symbol);
    });
    return r;
  };

  this.classify = (str) => {
    const r = [];
    const sequences = SymbolUtil.sequentialize(str);
    sequences.forEach((sequence) => {
      const symbol = that.map.createSymbol(sequence);
      const cacheResult = that.cache.query(that.getKey(symbol), that.config.cacheQueryOptions);
      const results = cacheResult.getDeepestElements(2);
      let classification = '';
      let confidence = 0;
      if (results.length > 0) {
        const id = MathUtil.stringify(results[0].element.key.symNumArray);
        const resultSymbol = that.map.getById(id);
        classification = resultSymbol.meta.cls;
        confidence = that.config.comparator.calculateValueSimilarity(sequence, resultSymbol.str);
      }
      r.push({ results, classification, confidence });
    });
    return r;
  };
}
module.exports = Classifier;
