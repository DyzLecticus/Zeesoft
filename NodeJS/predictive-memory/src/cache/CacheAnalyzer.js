const MathUtil = require('../MathUtil');

function CacheAnalyzer() {
  const that = this;

  this.analyzeCache = (cache, level, results) => {
    const res = results;
    const lvl = level + 1;
    const key = `level${lvl}`;
    let cr = res[key];
    if (!cr) {
      cr = {
        mergeSimilarity: cache.config.mergeSimilarity,
        elements: 0,
        sizeMin: Number.MAX_VALUE,
        sizeMax: 0,
        sizes: [],
      };
      res[key] = cr;
    }
    cr.elements += cache.elements.length;
    if (cache.config.subConfig) {
      cache.elements.forEach((element) => {
        const s = element.subCache.elements.length;
        cr.sizeMin = Math.min(cr.sizeMin, s);
        cr.sizeMax = Math.max(cr.sizeMax, s);
        cr.sizes.push(s);
        that.analyzeCache(element.subCache, level + 1, results);
      });
    }
  };

  this.analyze = (cache) => {
    const r = {};
    that.analyzeCache(cache, 0, r);
    Object.keys(r).forEach((key) => {
      const cr = r[key];
      if (cr.sizes.length) {
        cr.sizeAverage = MathUtil.getAverage(cr.sizes);
        cr.sizeStdDev = MathUtil.getStandardDeviation(cr.sizes);
      } else {
        cr.sizeMin = 1;
        cr.sizeMax = 1;
        cr.sizeAverage = 1;
        cr.sizeStdDev = 0;
      }
      delete cr.sizes;
    });
    return r;
  };
}
module.exports = CacheAnalyzer;
