const ClassifierConfig = require('./ClassifierConfig');
const SymbolMap = require('./SymbolMap');
const Cache = require('../cache/Cache');
const MathUtil = require('../MathUtil');
const SymbolUtil = require('./SymbolUtil');

const CLASSIFIER_INDEX_MOD = 100;

function Classifier(config) {
  const that = this;
  this.config = config || new ClassifierConfig();

  this.clss = [];

  this.map = new SymbolMap(this.config.characters);
  this.cache = new Cache(this.config.cacheConfig);

  this.recordInput = false;
  this.recordedInput = [];

  this.setRecordInput = (record) => {
    this.recordInput = record;
  };

  this.sequentialize = (str) => SymbolUtil.sequentialize(
    str,
    that.config.sequenceMaxLength,
    that.config.sequenceStepSize,
    that.config.characters,
  );

  this.getKey = (symbol) => ({ symNumArray: symbol.toNumArray() });

  this.getOrAddClass = (cls) => {
    let r = that.clss.indexOf(cls);
    if (r < 0) {
      r = that.clss.length;
      that.clss.push(cls);
    }
    return r;
  };

  this.put = (str, cls) => {
    if (that.recordInput) {
      that.recordedInput.push({ str, cls });
    }
    const r = [];
    const clsIndex = (that.getOrAddClass(cls) + CLASSIFIER_INDEX_MOD);
    const sequences = that.sequentialize(str);
    sequences.forEach((sequence) => {
      const symbol = that.map.put(sequence);
      that.cache.process(that.getKey(symbol), { clsIndex });
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
        confidence: 0,
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
    const simCache = {};
    sequences.forEach((sequence) => {
      const symbol = that.map.createSymbol(sequence);
      const cacheResult = that.cache.query(that.getKey(symbol), that.config.cacheQueryOptions);
      const results = cacheResult.getDeepestElements(2);
      results.forEach((result) => {
        const classification = that.clss[(result.element.value.clsIndex - CLASSIFIER_INDEX_MOD)];
        const id = MathUtil.stringify(result.element.key.symNumArray);
        const resultSymbol = that.map.getById(id);
        const count = result.element.count + result.parentCount;
        r.push({
          sequence,
          str: resultSymbol.str,
          similarity: result.similarity,
          count,
          classification,
        });
        let sim = -1;
        const simKey = `${sequence}_${resultSymbol.str}`;
        if (simCache[simKey] === undefined) {
          sim = that.config.comparator.calculateValueSimilarity(sequence, resultSymbol.str);
          simCache[simKey] = sim;
        } else {
          sim = simCache[simKey];
        }
        const cl = that.getOrAddClassification(classifications, classification);
        cl.similarity += (sim * count);
        totalCount += count;
      });
    });
    return { results: r, totalCount };
  };

  this.classify = (str) => {
    let classifications = [];
    const sequences = that.sequentialize(str);
    const { results, totalCount } = that.classifySequences(sequences, classifications);
    classifications.forEach((classification) => {
      const c = classification;
      c.confidence = c.similarity / totalCount;
    });
    classifications = classifications.sort((a, b) => b.confidence - a.confidence);
    return { results, classifications };
  };
}
module.exports = Classifier;
