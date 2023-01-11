const SymbolMap = require('./SymbolMap');
const SymbolConstants = require('./SymbolConstants');
const Comparator = require('../Comparator');

function SymbolClassifier(characters, comparator) {
  const that = this;
  this.map = new SymbolMap(characters || SymbolConstants.CHARACTERS);
  this.comparator = comparator || new Comparator();

  this.put = (str, cls) => {
    that.map.put(str, { cls });
  };

  this.classify = (str) => {
    const results = that.map.getNearest(str);
    let classification = '';
    let confidence = '';
    if (results.length > 0) {
      classification = results[0].symbol.meta.cls;
      confidence = that.comparator.calculateValueSimilarity(str, results[0].symbol.str);
    }
    return { results, classification, confidence };
  };
}
module.exports = SymbolClassifier;
