const CacheResult = require('./CacheResult');

function CacheElement(k, v) {
  this.key = k;
  this.value = v;
  this.count = 0;
  this.subCache = null;
}

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

  this.lookup = (res, key, lvl, pCount, options, lookupSubCache) => {
    for (let i = 0; i < this.elements.length; i += 1) {
      const sim = this.config.comparator.calculateSimilarity(key, this.elements[i].key);
      if (sim >= options.minSimilarity) {
        res.addLevelElement(lvl, sim, pCount, this.elements[i]);
      }
    }
    if (lookupSubCache && this.config.subConfig
      && (options.maxDepth === 0 || lvl < options.maxDepth)
    ) {
      const elems = res.getLevelElements(lvl, 2);
      for (let i = 0; i < elems.length; i += 1) {
        const elem = elems[i].element;
        const sub = i === elems.length - 1;
        elem.subCache.lookup(res, key, lvl + 1, pCount + elem.count, options, sub);
      }
    }
  };

  this.query = (key, minSimilarity, maxDepth) => {
    const res = new CacheResult();
    const options = {
      minSimilarity: minSimilarity || 0.0,
      maxDepth: maxDepth || 0,
    };
    this.lookup(res, key, 0, 0, options, true);
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
