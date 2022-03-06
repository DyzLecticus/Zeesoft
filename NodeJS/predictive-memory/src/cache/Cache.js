const CacheElement = require('./CacheElement');
const CacheResult = require('./CacheResult');

function Cache(config) {
  this.config = config;
  this.elements = [];

  this.get = (key, value, minSimilarity) => {
    let elem = null;
    let max = 0.0;
    for (let i = 0; i < this.elements.length; i += 1) {
      const valSim = this.config.comparator.calculateSimilarity(this.elements[i].value, value);
      if (valSim >= minSimilarity) {
        const keySim = this.config.comparator.calculateSimilarity(this.elements[i].key, key);
        if (keySim >= minSimilarity && valSim + keySim > max) {
          max = valSim + keySim;
          elem = this.elements[i];
          if (max === 2.0) {
            break;
          }
        }
      }
    }
    return elem;
  };

  this.applyMaxSize = () => {
    if (this.elements.length > this.config.maxSize) {
      this.elements = this.elements.slice(this.elements.length - this.config.maxSize);
    }
  };

  this.process = (key, value) => {
    let elem = this.get(key, value, this.config.mergeSimilarity);
    if (elem === null) {
      elem = new CacheElement(key, value);
      if (this.config.mergeSimilarity < 1.0 && this.config.subConfig != null) {
        elem.subCache = new Cache(this.config.subConfig);
      }
    } else {
      this.elements = this.elements.filter((el) => el !== elem);
    }
    this.elements.push(elem);
    elem.count += 1;
    this.applyMaxSize();
    if (elem.subCache !== null) {
      elem = elem.subCache.process(key, value);
    }
    return elem;
  };

  this.lookup = (key, minSimilarity, level, maxDepth) => {
    const res = new CacheResult();
    for (let i = 0; i < this.elements.length; i += 1) {
      const sim = this.config.comparator.calculateSimilarity(key, this.elements[i].key);
      if (sim >= minSimilarity) {
        if (sim > res.similarity) {
          if (res.elements.length > 0) {
            res.secondary = new CacheResult(res.similarity, res.elements);
          }
        }
        if (!res.addSimilarElement(sim, this.elements[i])) {
          if (res.secondary === null) {
            res.secondary = new CacheResult();
          }
          res.secondary.addSimilarElement(sim, this.elements[i]);
        }
      }
    }
    if (this.config.subConfig != null && (maxDepth === 0 || level < maxDepth)) {
      res.addSubResults(key, minSimilarity, level, maxDepth);
    }
    return res;
  };

  this.query = (key, minSimilarity, maxDepth) => {
    const res = this.lookup(key, minSimilarity || 0.0, 0, maxDepth || 0);
    res.summarize();
    return res;
  };

  this.size = (obj) => {
    const ob = obj || {};
    const key = `${this.config.mergeSimilarity}`;
    let s = ob[key] ? ob[key] : 0;
    s += this.elements.length;
    ob[key] = s;
    if (config.subConfig) {
      for (let i = 0; i < this.elements.length; i += 1) {
        this.elements[i].subCache.size(ob);
      }
    }
    return ob;
  };
}
module.exports = Cache;
