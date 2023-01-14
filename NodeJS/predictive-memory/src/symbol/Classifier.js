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
  };

  this.classifySequences = (sequences, classifications) => {
    const r = [];
    let totalCount = 0;
    sequences.forEach((sequence) => {
      const symbol = that.map.createSymbol(sequence);
      const cacheResult = that.cache.query(that.getKey(symbol), that.config.cacheQueryOptions);
      const results = cacheResult.getDeepestElements(2);
      results.forEach((result) => {
        const id = MathUtil.stringify(result.element.key.symNumArray);
        const resultSymbol = that.map.getById(id);
        r.push({
          similarity: result.similarity,
          count: result.element.count,
          symbol: resultSymbol,
        });
        const sim = that.config.comparator.calculateValueSimilarity(sequence, resultSymbol.str);
        const classification = resultSymbol.meta.cls;
        const cl = that.getOrAddClassification(classifications, classification);
        cl.similarity += (sim * result.element.count);
        totalCount += result.element.count;
      });
    });
    return { results: r, totalCount };
  };

  this.classify = (str) => {
    let classifications = [];
    const sequences = SymbolUtil.sequentialize(str, that.config.sequenceMaxLength);
    const { results, totalCount } = that.classifySequences(sequences, classifications);
    classifications = classifications.sort((a, b) => b.similarity - a.similarity);
    let classification = '';
    let confidence = '';
    if (classifications.length > 0) {
      classification = classifications[0].classification;
      confidence = classifications[0].similarity / totalCount;
    }
    return { results, classification, confidence };
  };
}
module.exports = Classifier;
