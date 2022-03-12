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
      if (this.config.subConfig) {
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

  this.lookup = (key, level, pCount, options) => {
    const res = new CacheResult();
    for (let i = 0; i < this.elements.length; i += 1) {
      const sim = this.config.comparator.calculateSimilarity(key, this.elements[i].key);
      if (sim >= options.minSimilarity) {
        if (sim > res.similarity) {
          if (res.elements.length > 0) {
            res.secondary = new CacheResult(res.similarity, res.parentCount, res.elements);
          }
        }
        if (!res.addSimilarElement(sim, pCount, this.elements[i])) {
          if (res.secondary === null) {
            res.secondary = new CacheResult();
          }
          res.secondary.addSimilarElement(sim, pCount, this.elements[i]);
        }
      }
    }
    if (this.config.subConfig && (options.maxDepth === 0 || level < options.maxDepth)) {
      res.addSubResults(key, level, pCount, options);
      if (res.secondary) {
        res.secondary.addSubResults(key, level, pCount, options);
      }
    }
    return res;
  };

  this.query = (key, minSimilarity, maxDepth) => {
    const options = {
      minSimilarity: minSimilarity || 0.0,
      maxDepth: maxDepth || 0,
    };
    const res = this.lookup(key, 0, 0, options);
    res.summarize();
    return res;
  };

  this.size = (obj) => {
    const ob = obj || {};
    const key = `${this.config.mergeSimilarity}`;
    let s = ob[key] ? ob[key] : 0;
    s += this.elements.length;
    ob[key] = s;
    if (this.config.subConfig) {
      for (let i = 0; i < this.elements.length; i += 1) {
        this.elements[i].subCache.size(ob);
      }
    }
    return ob;
  };
}
module.exports = Cache;
