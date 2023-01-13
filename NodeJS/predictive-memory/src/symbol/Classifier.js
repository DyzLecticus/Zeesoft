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
    const sequences = SymbolUtil.sequentialize(str, that.config.sequenceMaxLength);
    sequences.forEach((sequence) => {
      const symbol = that.map.put(sequence, { cls });
      that.cache.process(that.getKey(symbol), {});
      r.push(symbol);
    });
    return r;
  };

  this.getOrAddClassification = (classifications, classification) => {
    const clss = classifications.filter((cl) => cl.classification === classification);
    let cl = null;
    if (clss.length === 0) {
      cl = {
        classification,
        similarity: 0,
      };
      classifications.push(cl);
    } else {
      [cl] = clss;
    }
    return cl;
  }

  this.classifySequences = (sequences, classifications) => {
    sequences.forEach((sequence) => {
      const symbol = that.map.createSymbol(sequence);
      const cacheResult = that.cache.query(that.getKey(symbol), that.config.cacheQueryOptions);
      const results = cacheResult.getDeepestElements(2);
      if (results.length > 0) {
        const id = MathUtil.stringify(results[0].element.key.symNumArray);
        const resultSymbol = that.map.getById(id);
        const classification = resultSymbol.meta.cls;
        const cl = that.getOrAddClassification(classifications, classification);
        cl.similarity
          += that.config.comparator.calculateValueSimilarity(sequence, resultSymbol.str);
      }
    });
  };

  this.classify = (str) => {
    let classifications = [];
    const sequences = SymbolUtil.sequentialize(str, that.config.sequenceMaxLength);
    that.classifySequences(sequences, classifications);
    classifications = classifications.sort((a, b) => b.similarity - a.similarity);
    console.log(classifications)
    let classification = '';
    let confidence = '';
    if (classifications.length > 0) {
      classification = classifications[0].classification;
      confidence = classifications[0].similarity / sequences.length;
    }
    return { classification, confidence };
  };
}
module.exports = Classifier;
