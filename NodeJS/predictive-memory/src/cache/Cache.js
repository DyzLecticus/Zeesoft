const CacheElement = require('./CacheElement');

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
  this.hit = (key, value) => {
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
      elem = elem.subCache.hit(key, value);
    }
    return elem;
  };
}
module.exports = Cache;
