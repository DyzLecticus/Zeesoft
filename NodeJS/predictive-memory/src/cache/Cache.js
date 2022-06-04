const CacheResult = require('./CacheResult');

function CacheElement(k, v) {
  this.key = k;
  this.value = v;
  this.count = 0;
  this.subCache = null;
  this.copy = () => {
    const r = new CacheElement(this.key, this.value);
    r.count = this.count;
    if (this.subCache != null) {
      r.subCache = this.subCache.copy();
    }
    return r;
  };
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

  /**
   * @param {Array} key An array of similar objects (See Comparator and Transformer)
   * @param {Object} value An object to associate with the specified key
   * @returns The updated or added cache element
   */
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

  this.lookup = (res, key, lvl, pCount, options) => {
    this.elements.forEach((element) => {
      const sim = this.config.comparator.calculateSimilarity(key, element.key);
      if (sim >= options.minSimilarity) {
        res.addLevelElement(lvl, sim, pCount, element);
      }
    });
    if (this.config.subConfig
      && (options.maxDepth === 0 || lvl < options.maxDepth)
    ) {
      const elems = res.getLevelElements(lvl, options.maxWidth);
      elems.forEach((elem) => {
        elem.element.subCache.lookup(res, key, lvl + 1, pCount + elem.element.count, options);
      });
    }
  };

  /**
   * @param {String} key An array of similar objects (See Comparator and Transformer)
   * @param {Object} options An object with query options (See CacheConfig.getQueryOptions)
   * @returns
   */
  this.query = (key, options) => {
    const res = new CacheResult();
    const opts = options || this.config.getQueryOptions();
    this.lookup(res, key, 0, 0, opts, true);
    return res;
  };

  this.size = (obj) => {
    const ob = obj || {};
    const key = `${this.config.mergeSimilarity}`;
    let s = ob[key] ? ob[key] : 0;
    s += this.elements.length;
    ob[key] = s;
    if (this.config.subConfig) {
      this.elements.forEach((element) => { element.subCache.size(ob); });
    }
    return ob;
  };

  this.copy = () => {
    const r = new Cache(this.config);
    this.elements.forEach((elem, index) => {
      r.elements[index] = elem.copy();
    });
    return r;
  };
}
module.exports = Cache;
