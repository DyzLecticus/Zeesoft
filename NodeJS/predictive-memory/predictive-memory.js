/* eslint-disable no-use-before-define */
const CLASSIFIER_INDEX_MOD = 100;
const TRANSFORMER_MINIMUM_VANUE = 0.0000000001;

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmCacheElement(k, v) {
  this.key = k;
  this.value = v;
  this.count = 0;
  this.subCache = null;
  this.copy = () => {
    const r = new PmCacheElement(this.key, this.value);
    r.count = this.count;
    if (this.subCache != null) {
      r.subCache = this.subCache.copy();
    }
    return r;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmCache(config) {
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
   * @returns The updated or added CacheElement
   */
  this.process = (key, value) => {
    let elem = this.get(key, value, this.config.mergeSimilarity);
    if (elem === null) {
      elem = new PmCacheElement(key, value);
      if (this.config.subConfig) {
        elem.subCache = new PmCache(this.config.subConfig);
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
   * @param {Object} options An object with query options (See PmCacheConfig.getQueryOptions)
   * @returns {Object} A CacheResult
   */
  this.query = (key, options) => {
    const res = new PmCacheResult();
    const opts = options || this.config.getQueryOptions();
    this.lookup(res, key, 0, 0, opts);
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
    const r = new PmCache(this.config);
    this.elements.forEach((elem, index) => {
      r.elements[index] = elem.copy();
    });
    return r;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmCacheAnalyzer() {
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
        cr.sizeAverage = PmMathUtil.getAverage(cr.sizes);
        cr.sizeStdDev = PmMathUtil.getStandardDeviation(cr.sizes);
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

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmCacheConfig(comp, mergeSim) {
  this.comparator = comp || new PmComparator();
  this.mergeSimilarity = mergeSim || 0.925;
  this.maxSize = 1000;
  this.subConfig = null;

  this.initiatlizeDefault = () => {
    this.subConfig = new PmCacheConfig(this.comparator, 0.96);
    this.subConfig.subConfig = new PmCacheConfig(this.comparator, 1);
  };

  this.setComparator = (com) => {
    this.comparator = com;
    if (this.subConfig) {
      this.subConfig.setComparator(com);
    }
  };

  this.getQueryOptions = (minSimilarity, maxDepth, maxWidth) => ({
    minSimilarity: minSimilarity || 0,
    maxDepth: maxDepth || 0,
    maxWidth: maxWidth || 2,
  });
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmLevelElement(sim, pCount, elem) {
  this.similarity = sim;
  this.parentCount = pCount;
  this.element = elem;
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmCacheResult() {
  this.levelElements = [];

  this.addLevelElement = (lvl, sim, pCount, elem) => {
    let added = false;
    if (lvl >= this.levelElements.length) {
      this.levelElements[lvl] = [];
    }
    for (let i = 0; i < this.levelElements[lvl].length; i += 1) {
      const levelElem = this.levelElements[lvl][i];
      if (sim >= levelElem.similarity) {
        this.levelElements[lvl].splice(i, 0, new PmLevelElement(sim, pCount, elem));
        added = true;
        break;
      }
    }
    if (!added) {
      this.levelElements[lvl].push(new PmLevelElement(sim, pCount, elem));
    }
  };

  this.getLevelElements = (lvl, num) => {
    const n = num || 0;
    const r = [];
    if (this.levelElements[lvl]) {
      for (let i = 0; i < this.levelElements[lvl].length; i += 1) {
        r.push(this.levelElements[lvl][i]);
        if (num > 0 && r.length === n) {
          break;
        }
      }
    }
    return r;
  };

  this.getDeepestElements = (num) => this.getLevelElements(this.levelElements.length - 1, num);
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmComparator() {
  this.calculateNumberSimilarity = (a, b) => {
    let perc = 0.0;
    let ac = a;
    let bc = b;
    const min = Math.min(a, b);
    if (min < 0) {
      ac += (min * -2.0);
      bc += (min * -2.0);
    }
    if (ac > bc) {
      perc = (ac - bc) / ((ac + bc) / 2.0);
    } else {
      perc = (bc - ac) / ((ac + bc) / 2.0);
    }
    perc = 1.0 - (perc / 2.0);
    return perc;
  };

  this.calculateLevenshteinDistance = (a, b) => {
    const c = a.length + 1;
    const d = b.length + 1;
    const r = Array(c);
    for (let i = 0; i < c; i += 1) r[i] = Array(d);
    for (let i = 0; i < c; i += 1) r[i][0] = i;
    for (let j = 0; j < d; j += 1) r[0][j] = j;
    for (let i = 1; i < c; i += 1) {
      for (let j = 1; j < d; j += 1) {
        const s = (a[i - 1] === b[j - 1] ? 0 : 1);
        r[i][j] = Math.min(r[i - 1][j] + 1, r[i][j - 1] + 1, r[i - 1][j - 1] + s);
      }
    }
    return r[a.length][b.length];
  };

  this.calculateStringSimilarity = (a, b) => {
    let perc = 0.0;
    if (a.length > 0 && b.length > 0) {
      const dist = this.calculateLevenshteinDistance(a, b);
      const len = Math.max(a.length, b.length);
      perc = (len - dist) / len;
    }
    return perc;
  };

  this.calculateArraySimilarity = (a, b, valueCompareFunction) => {
    let perc = 1.0;
    const max = Math.max(a.length, b.length);
    if (max > 0) {
      perc = 0;
      for (let i = 0; i < max; i += 1) {
        if (a.length > i && b.length > i) {
          perc += valueCompareFunction(a[i], b[i]);
        }
      }
      perc /= max;
    }
    return perc;
  };

  // eslint-disable-next-line max-len
  this.calculateArrayValueSimilarity = (a, b) => this.calculateArraySimilarity(a, b, this.calculateValueSimilarity);

  this.calculateValueSimilarity = (a, b) => {
    let perc = 0.0;
    if (a === b) {
      perc = 1.0;
    } else if (typeof (a) === 'number' && typeof (b) === 'number') {
      perc = this.calculateNumberSimilarity(a, b);
    } else if (typeof (a) === 'string' && typeof (b) === 'string') {
      perc = this.calculateStringSimilarity(a, b);
    } else if (Array.isArray(a) && Array.isArray(b)) {
      perc = this.calculateArrayValueSimilarity(a, b);
    }
    return perc;
  };

  this.calculateObjectSimilarity = (a, b) => {
    let perc = 1.0;
    const abKeys = Object.keys(a).filter((key) => key in b);
    const nbKeys = Object.keys(a).filter((key) => !(key in b));
    const naKeys = Object.keys(b).filter((key) => !(key in a));
    const total = abKeys.length + nbKeys.length + naKeys.length;
    if (total > 0) {
      perc = 0;
      abKeys.forEach((key) => { perc += this.calculateValueSimilarity(a[key], b[key]); });
      perc /= total;
    }
    return perc;
  };

  // eslint-disable-next-line max-len
  this.calculateObjectArraySimilarity = (a, b) => this.calculateArraySimilarity(a, b, this.calculateObjectSimilarity);

  this.calculateSimilarity = (a, b) => {
    let perc = 0.0;
    if (Array.isArray(a) && Array.isArray(b)) {
      perc = this.calculateObjectArraySimilarity(a, b);
    } else {
      perc = this.calculateObjectSimilarity(a, b);
    }
    return perc;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function _PmMathUtil() {
  this.getAverage = (numArray) => {
    let avg = 0.0;
    if (numArray.length > 0) {
      numArray.forEach((val) => { avg += val; });
      avg /= numArray.length;
    }
    return avg;
  };

  this.getStandardDeviation = (numArray) => {
    let dev = 0.0;
    if (numArray.length > 1) {
      let sum = 0.0;
      numArray.forEach((val) => { sum += val; });
      const avg = sum / numArray.length;
      numArray.forEach((val) => { dev += (val - avg) ** 2; });
      dev = Math.sqrt(dev / (numArray.length - 1));
    }
    return dev;
  };

  this.stringify = (numArray) => {
    let r = `${numArray.length}`;
    numArray.forEach((num, index) => {
      if (num !== 0) {
        r += `,${index}=${num}`;
      }
    });
    return r;
  };

  this.parse = (str) => {
    let r = [];
    const elems = str.split(',');
    elems.forEach((elem, index) => {
      if (index === 0) {
        r = Array(parseInt(elem, 10)).fill(0);
      } else {
        const iv = elem.split('=');
        r[parseInt(iv[0], 10)] = parseInt(iv[1], 10);
      }
    });
    return r;
  };
}
const PmMathUtil = new _PmMathUtil();

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmHistory(max) {
  this.elements = [];
  this.maxSize = max || 1000;

  this.applyMaxSize = () => {
    if (this.elements.length > this.maxSize) {
      this.elements = this.elements.slice(this.elements.length - this.maxSize);
    }
  };

  this.add = (elem) => {
    this.elements.push(elem);
    this.applyMaxSize();
  };

  this.get = (indexes, trans) => {
    const r = [];
    for (let i = 0; i < indexes.length; i += 1) {
      let idx = indexes[i];
      if (trans < 0) {
        idx -= (trans * -1);
      } else if (trans > 0) {
        idx += trans;
      }
      const ri = (this.elements.length - 1) - idx;
      if (ri >= 0) {
        r.push(this.elements[ri]);
      } else {
        break;
      }
    }
    return r;
  };

  this.copy = () => {
    const r = new PmHistory(this.maxSize);
    this.elements.forEach((elem, index) => {
      r.elements[index] = elem;
    });
    return r;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmIndexGenerator(depth) {
  this.depth = depth || 8;

  this.addIndex = (idxs, idx) => {
    const existing = idxs.filter((ix) => ix === idx);
    if (existing.length === 0) {
      idxs.push(idx);
    }
  };

  this.addDefault = (idxs) => {
    for (let i = 0; i < this.depth; i += 1) {
      this.addIndex(idxs, (i + 1));
    }
  };

  this.getFibonacci = () => {
    const idxs = [1];
    let idx = 2;
    for (let i = 0; i < (this.depth - 1); i += 1) {
      this.addIndex(idxs, idx);
      idx += idxs[idxs.length - 2];
    }
    return idxs;
  };

  this.addFibonacci = (idxs) => {
    this.getFibonacci().forEach((idx) => { this.addIndex(idxs, idx); });
  };

  this.addPower = (idxs) => {
    let idx = 1;
    for (let i = 0; i < this.depth; i += 1) {
      this.addIndex(idxs, idx);
      idx += idx;
    }
  };

  this.generate = () => {
    const idxs = [];
    this.addDefault(idxs);
    this.addFibonacci(idxs);
    this.addPower(idxs);
    idxs.sort((a, b) => a - b);
    return idxs;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmKeyPrediction(val) {
  this.value = val;
  this.totalSimilarity = 0.0;
  this.totalCount = 0;
  this.weight = 0.0;
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmObjectPrediction(result) {
  this.cacheResult = result;
  this.keyPredictions = {};
  this.predictedValues = {};
  this.weightedPredictedValues = {};

  this.generateKeyPredictions = (elems) => {
    elems.forEach((elem) => {
      Object.keys(elem.element.value).forEach((key) => {
        const value = elem.element.value[key];
        if (!this.keyPredictions[key]) {
          this.keyPredictions[key] = [];
        }
        const kps = this.keyPredictions[key].filter((kp) => kp.value === value);
        let kp = kps.length ? kps[0] : null;
        if (!kp) {
          kp = new PmKeyPrediction(value);
          this.keyPredictions[key].push(kp);
        }
        kp.totalSimilarity += (elem.similarity);
        kp.totalCount += (elem.parentCount + elem.element.count);
      });
    });
  };

  this.calculateKeyPredictionWeights = () => {
    Object.keys(this.keyPredictions).forEach((key) => {
      let total = 0.0;
      this.keyPredictions[key].forEach((kp) => {
        total += (kp.totalCount * kp.totalSimilarity);
      });
      this.keyPredictions[key].forEach((kp) => {
        const p = kp;
        p.weight = (kp.totalCount * kp.totalSimilarity) / total;
      });
      this.keyPredictions[key] = this.keyPredictions[key].sort((a, b) => (b.weight - a.weight));
    });
  };

  this.calculatePredictedValues = () => {
    Object.keys(this.keyPredictions).forEach((key) => {
      let val = 0.0;
      let isNum = true;
      this.keyPredictions[key].forEach((kp, i) => {
        if (i === 0) {
          this.predictedValues[key] = kp.value;
        }
        if (typeof (kp.value) === 'number') {
          val += kp.weight * kp.value;
        } else {
          isNum = false;
        }
      });
      this.weightedPredictedValues[key] = isNum ? val : this.predictedValues[key];
    });
  };

  this.generatePrediction = (num) => {
    this.generateKeyPredictions(this.cacheResult.getDeepestElements(num));
    this.calculateKeyPredictionWeights();
    this.calculatePredictedValues();
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmPredictor(config) {
  this.config = config || new PmPredictorConfig();
  this.history = new PmHistory(this.config.maxHistorySize);

  this.learn = true;
  this.cache = new PmCache(this.config.cacheConfig);

  this.predict = false;
  this.predictions = new PmHistory(this.config.maxPredictionHistorySize);

  this.setLearn = (l) => {
    this.learn = l;
  };

  this.setPredict = (p) => {
    this.predict = p;
    if (!p) {
      this.predictions.elements = [];
    }
  };

  this.addPrediction = () => {
    let r = null;
    if (this.predict) {
      const key = this.history.get(this.config.cacheIndexes, -1);
      const result = this.cache.query(key, this.config.cacheQueryOptions);
      r = new PmObjectPrediction(result);
      r.generatePrediction();
      this.predictions.add(r);
    }
    return r;
  };

  /**
   * @param {Object} hist An object that has one or more properties (See Comparator)
   * @returns An ObjectPrediction (with predictedValues and weightedPredictedValues) or null
   */
  this.process = (hist) => {
    let r = null;
    this.history.add(hist);
    if (this.history.elements.length > 1) {
      if (this.learn) {
        const key = this.history.get(this.config.cacheIndexes);
        const value = this.history.get([0])[0];
        this.cache.process(key, value);
      }
      r = this.addPrediction();
    }
    return r;
  };

  /**
   * @param {String} key The object property key
   * @param {Number} max The optional maximum number of results
   * @param {String} type The optional type; 'predictedValues' | 'weightedPredictedValues'
   * @returns An array of objects containing predicted and actual values for the specified key
   */
  this.getResults = (key, max, type) => {
    const r = [];
    let m = max;
    if (!m || m >= (this.predictions.elements.length - 1)) {
      m = (this.predictions.elements.length - 1);
    }
    const typeName = type || 'predictedValues';
    for (let i = 1; i <= m; i += 1) {
      const pred = this.predictions.get([i])[0];
      const predicted = pred[typeName][key];
      const hist = this.history.get([i - 1])[0];
      const actual = hist[key];
      if (predicted !== undefined && actual !== undefined) {
        r.push({ predicted, actual });
      }
    }
    return r;
  };

  this.copy = () => {
    const r = new PmPredictor(this.config);
    r.history = this.history.copy();
    r.learn = this.learn;
    r.cache = this.cache.copy();
    r.predict = this.predict;
    r.predictions = this.predictions.copy();
    return r;
  };

  /**
   * @param {Number} steps The number of future steps
   * @param {String} type The optional type; 'predictedValues' | 'weightedPredictedValues'
   * @returns An array of ObjectPredictions
   */
  this.generatePredictions = (steps, type) => {
    const r = [];
    let pred = this.predictions.get([0])[0];
    if (pred) {
      r.push(pred);
      const pathPredictor = this.copy();
      pathPredictor.setLearn(false);
      const typeName = type || 'predictedValues';
      for (let s = 1; s < steps; s += 1) {
        pred = pathPredictor.process(pred[typeName]);
        r.push(pred);
      }
    }
    return r;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmPredictorAnalyzer() {
  this.getAnalysisOptions = (max, type) => ({
    max: max || 0,
    type: type || 'predictedValues',
  });

  this.getAccuracies = (results, comparator) => results.map(
    (result) => comparator.calculateValueSimilarity(result.predicted, result.actual),
  );

  this.getAccuracy = (results, comparator) => PmMathUtil.getAverage(
    this.getAccuracies(results, comparator),
  );

  this.getAccuracyStdDev = (results, comparator) => PmMathUtil.getStandardDeviation(
    this.getAccuracies(results, comparator),
  );

  this.getAccuracyTrend = (results, comparator) => {
    const m = Math.round(results.length / 10);
    return this.getAccuracy(results.slice(0, m), comparator);
  };

  this.getValueStdDev = (results) => {
    const diffs = results.map((result) => {
      let diff = result.predicted - result.actual;
      if (diff < 0) {
        diff *= -1;
      }
      return diff;
    });
    return PmMathUtil.getStandardDeviation(diffs);
  };

  /**
   * @param {Predictor} predictor The predictor to analyze
   * @param {Array} keys A list of value keys to analyze
   * @param {Object} options An optional object that specifies type and max results
   * @returns An object containing the results of the analysis
   */
  this.analyze = (predictor, keys, options) => {
    const r = {};
    const opts = options || this.getAnalysisOptions();
    keys.forEach((key) => {
      const results = predictor.getResults(key, opts.max, opts.type);
      r[key] = {
        accuracy: this.getAccuracy(results, predictor.config.comparator),
        accuracyStdDev: this.getAccuracyStdDev(results, predictor.config.comparator),
        accuracyTrend: this.getAccuracyTrend(results, predictor.config.comparator),
        valueStdDev: this.getValueStdDev(results),
      };
    });
    return r;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmPredictorConfig(size, depth) {
  this.maxHistorySize = size || 129;
  this.comparator = new PmComparator();

  this.cacheIndexes = (new PmIndexGenerator(depth)).generate();
  this.cacheConfig = new PmCacheConfig();
  this.cacheConfig.initiatlizeDefault();
  this.cacheQueryOptions = this.cacheConfig.getQueryOptions();

  this.maxPredictionHistorySize = size || 129;

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmClassifier(config) {
  const that = this;
  this.config = config || new PmClassifierConfig();

  this.clss = [];

  this.map = new PmSymbolMap(this.config.characters);
  this.cache = new PmCache(this.config.cacheConfig);

  this.recordInput = false;
  this.recordedInput = [];

  this.setRecordInput = (record) => {
    this.recordInput = record;
  };

  this.sequentialize = (str) => PmSymbolUtil.sequentialize(
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
        const id = PmMathUtil.stringify(result.element.key.symNumArray);
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

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmClassifierAnalyzer(testPercentage) {
  const that = this;

  this.testPercentage = testPercentage || 0.1;

  this.getTestSet = (classifier) => {
    const mod = Math.round(100 / (100 * that.testPercentage));
    const r = [];
    classifier.recordedInput.forEach((input, index) => {
      if (index % mod === 0) {
        r.push(input);
      }
    });
    return r;
  };

  this.analyze = (classifier, customTestSet) => {
    const start = Date.now();
    const accuracies = [];
    const conficdences = [];
    const testResults = [];
    const testSet = customTestSet || that.getTestSet(classifier);
    testSet.forEach((test) => {
      const result = classifier.classify(test.str);
      const acc = result.classifications[0].classification === test.cls ? 1 : 0;
      accuracies.push(acc);
      conficdences.push(result.classifications[0].confidence);
      testResults.push({ test, classifications: result.classifications });
    });
    const msPerString = (Date.now() - start) / testSet.length;
    return {
      statistics: {
        accuracy: PmMathUtil.getAverage(accuracies),
        confidence: PmMathUtil.getAverage(conficdences),
        confidenceStdDev: PmMathUtil.getStandardDeviation(conficdences),
        msPerString,
      },
      testResults,
    };
  };

  this.mergeStatistics = (statistics) => {
    let accuracy = 0;
    let confidence = 0;
    let confidenceStdDev = 0;
    let msPerString = 0;
    statistics.forEach((stats) => {
      accuracy += stats.accuracy;
      confidence += stats.confidence;
      confidenceStdDev += stats.confidenceStdDev;
      msPerString += stats.msPerString;
    });
    if (statistics.length > 1) {
      accuracy /= statistics.length;
      confidence /= statistics.length;
      confidenceStdDev /= statistics.length;
      msPerString /= statistics.length;
    }
    return {
      accuracy, confidence, confidenceStdDev, msPerString,
    };
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmClassifierConfig(characters) {
  this.characters = characters || PmSymbolConstants.CLASSIFIER_CHARACTERS;
  this.sequenceMaxLength = 4;
  this.sequenceStepSize = 2;
  this.comparator = new PmComparator();

  this.cacheConfig = new PmCacheConfig();
  this.cacheConfig.initiatlizeDefault();
  this.cacheConfig.mergeSimilarity = 0.825;
  this.cacheConfig.subConfig.mergeSimilarity = 0.875;
  this.cacheQueryOptions = this.cacheConfig.getQueryOptions();

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmMapAnalyzer() {
  const that = this;

  this.getDistances = (symbolsA, symbolsB) => {
    const r = [];
    symbolsA.forEach((symbolA) => {
      symbolsB.forEach((symbolB) => {
        if (symbolA !== symbolB) {
          r.push(symbolA.calculateDistance(symbolB));
        } else {
          r.push(0);
        }
      });
    });
    return r;
  };

  this.createAnalysis = (distances) => ({
    distances,
    average: PmMathUtil.getAverage(distances),
    stdDev: PmMathUtil.getStandardDeviation(distances),
    min: Math.min(...distances),
    max: Math.max(...distances),
  });

  this.analyze = (symbolMapA, symbolMapB) => {
    const symbolsA = symbolMapA.toArray();
    const symbolsB = symbolMapB ? symbolMapB.toArray() : symbolsA;
    const distances = that.getDistances(symbolsA, symbolsB);
    return that.createAnalysis(distances);
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmSymbol(str, meta, characters) {
  const that = this;
  this.str = PmSymbolUtil.format(str);
  this.meta = meta || null;

  this.length = 0;
  this.keyValues = {};

  this.toNumArray = () => {
    const r = [that.length];
    for (let i = 0; i < that.length; i += 1) {
      const v = that.keyValues[`${i}`] || 0;
      r[i] = v;
    }
    return r;
  };

  this.toString = () => PmMathUtil.stringify(that.toNumArray());

  this.calculateDistance = (other) => {
    let r = -1;
    if (that === other) {
      r = 0;
    } else if (that.length === other.length) {
      r = 0;
      const otherNumArray = other.toNumArray();
      that.toNumArray().forEach((num, index) => {
        if (num !== otherNumArray[index]) {
          const diff = (num - otherNumArray[index]);
          r += (diff * diff);
        }
      });
      r = Math.sqrt(r);
    }
    return r;
  };

  this.getIndexes = (s, chars) => {
    const idxs = {};
    for (let i = 0; i < s.length; i += 1) {
      const c = s.substring(i, i + 1);
      const idx = chars.indexOf(c);
      if (!idxs[`${idx}`]) {
        idxs[`${idx}`] = (i + 1);
      }
    }
    return idxs;
  };

  this.getTransitions = (s, chars) => {
    const r = {};
    let pc = '';
    for (let i = 0; i < s.length; i += 1) {
      const c = s.substring(i, i + 1);
      const idx = chars.indexOf(c);
      const tIdx = (chars.length + idx);
      const fIdx = ((chars.length * 2) + idx);
      if (i < (s.length - 1) && !r[`${tIdx}`]) {
        const nc = s.substring(i, 1);
        r[`${tIdx}`] = chars.indexOf(nc);
      }
      if (pc.length > 0) {
        r[`${fIdx}`] = chars.indexOf(pc);
      }
      pc = c;
    }
    return r;
  };

  this.mergeTransitions = (transitions) => {
    const r = {};
    transitions.forEach((transition) => {
      Object.keys(transition).forEach((key) => {
        let v = r[key];
        if (!v) {
          v = 0;
        }
        v += transition[key];
        r[key] = v;
      });
    });
    return r;
  };

  this.generateKeyValues = (chars) => {
    const chrs = chars || PmSymbolConstants.CLASSIFIER_CHARACTERS;
    that.length = (chrs.length * 3);
    const s = PmSymbolUtil.tokenizeFormat(that.str, chrs);
    const indexes = that.getIndexes(s, chrs);
    const transitions = [];
    const tokens = s.split(' ');
    tokens.forEach((token) => {
      transitions.push(that.getTransitions(token, chrs));
    });
    const merged = that.mergeTransitions(transitions);
    that.keyValues = { ...indexes, ...merged };
  };

  this.generateKeyValues(characters);
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmSymbolClassifier(characters, comparator) {
  const that = this;
  this.map = new PmSymbolMap(characters || PmSymbolConstants.CHARACTERS);
  this.comparator = comparator || new PmComparator();

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

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function _PmSymbolConstants() {
  this.ALPHABET = 'abcdefghijklmnopqrstuvwxyz';
  this.CAPITALS = this.ALPHABET.toUpperCase();
  this.NUMBERS = '0123456789';
  this.ALPHANUMERICS = this.ALPHABET + this.CAPITALS + this.NUMBERS;

  this.ENDERS = '.!?';
  this.SEPARATORS = ' -,/';
  this.BINDERS = '<>[]()\'"';
  this.SPECIALS = '@#$%^&*_+=|\\~';
  this.CONTROLS = '\r\n\t';

  this.NON_ALPHANUMERICS = this.ENDERS
    + this.SEPARATORS
    + this.BINDERS
    + this.SPECIALS
    + this.CONTROLS;

  this.ALPHABET_EXTENDED = 'üéâäàåçêëèïîìôöòûùÿ';
  this.CAPITALS_EXTENDED = 'ÇÄÅÉÖÜ';
  this.ALPHABET_REPLACEMENTS = 'ueaaaaceeeiiiooouuy';
  this.CAPITALS_REPLACEMENTS = 'CAAEOU';

  this.CHARACTERS = this.ALPHANUMERICS + this.NON_ALPHANUMERICS;

  this.CLASSIFIER_CHARACTERS = this.ALPHABET
    + this.NUMBERS
    + this.ENDERS
    + this.SEPARATORS
    + this.BINDERS;
}
const PmSymbolConstants = new _PmSymbolConstants();

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmSymbolMap(characters) {
  const that = this;
  this.characters = characters || PmSymbolConstants.CHARACTERS;
  this.elements = {};

  this.createSymbol = (str, meta) => new PmSymbol(str, meta, that.characters);

  this.getById = (id) => that.elements[id];

  this.get = (str) => that.getById(that.createSymbol(str).toString());

  this.put = (str, meta) => {
    const symbol = that.createSymbol(str, meta);
    that.elements[symbol.toString()] = symbol;
    return symbol;
  };

  this.toArray = () => Object.keys(that.elements).map((key) => that.elements[key]);

  this.addArray = (symbols) => {
    symbols.forEach((symbol) => { that.elements[symbol.toString()] = symbol; });
  };

  this.getDistances = (str) => {
    const symbol = that.createSymbol(str);
    return Object.keys(that.elements).map(
      (key) => ({
        symbol: that.elements[key],
        dist: symbol.calculateDistance(that.elements[key]),
      }),
    );
  };

  this.getNearest = (str) => {
    const r = that.getDistances(str).sort(
      (a, b) => a.dist - b.dist,
    );
    return r;
  };
}

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function _PmSymbolUtil() {
  this.trim = (str) => {
    let r = str.trim().replace(/ {4}/g, ' ');
    r = r.replace(/ {3}/g, ' ');
    r = r.replace(/ {2}/g, ' ');
    return r;
  };

  this.replaceCharacters = (str, characters, replacements) => {
    let r = '';
    for (let i = 0; i < str.length; i += 1) {
      const c = str.substring(i, i + 1);
      const idx = characters.indexOf(c);
      if (idx >= 0) {
        r += replacements.substring(idx, idx + 1);
      } else {
        r += c;
      }
    }
    return r;
  };

  this.replaceExtensions = (str) => {
    const characters = PmSymbolConstants.ALPHABET_EXTENDED
      + PmSymbolConstants.CAPITALS_EXTENDED;
    const replacements = PmSymbolConstants.ALPHABET_REPLACEMENTS
      + PmSymbolConstants.CAPITALS_REPLACEMENTS;
    return this.replaceCharacters(str, characters, replacements);
  };

  this.format = (str) => this.trim(this.replaceExtensions(str));

  this.spaceOutNonAllowedCharacters = (str, allowedCharacters) => {
    let r = '';
    for (let i = 0; i < str.length; i += 1) {
      const c = str.substring(i, i + 1);
      const idx = allowedCharacters.indexOf(c);
      if (idx < 0) {
        r += ' ';
      } else {
        r += c;
      }
    }
    return r;
  };

  this.spaceCharacters = (str, characters) => {
    let r = '';
    for (let i = 0; i < str.length; i += 1) {
      const c = str.substring(i, i + 1);
      const idx = characters.indexOf(c);
      if (idx >= 0) {
        r += ` ${c} `;
      } else {
        r += c;
      }
    }
    return r;
  };

  this.tokenizeFormat = (str, characters) => {
    const chars = characters || PmSymbolConstants.CLASSIFIER_CHARACTERS;
    let r = str;
    if (chars.indexOf(PmSymbolConstants.CAPITALS) < 0) {
      r = r.toLowerCase();
    }
    r = this.replaceExtensions(r);
    r = this.spaceOutNonAllowedCharacters(r, chars);
    const spaceChars = PmSymbolConstants.NON_ALPHANUMERICS.replace(' ', '');
    r = this.spaceCharacters(r, spaceChars);
    return this.trim(r);
  };

  this.tokenize = (str, characters) => this.tokenizeFormat(str, characters).split(' ');

  this.stringify = (tokens) => {
    let s = '';
    tokens.forEach((token) => {
      if (s.length > 0) {
        s += ' ';
      }
      s += token;
    });
    return s;
  };

  this.parseSentences = (str, characters) => {
    const tokens = this.tokenize(str, characters);
    const ts = [];
    let seq = [];
    tokens.forEach((token) => {
      seq.push(token);
      if (PmSymbolConstants.ENDERS.indexOf(token) >= 0) {
        ts.push(seq);
        seq = [];
      }
    });
    if (seq.length > 0) {
      ts.push(seq);
    }
    return ts.map(this.stringify);
  };

  this.sequentialize = (str, maxLength, stepSize, characters) => {
    const max = maxLength || 8;
    const step = stepSize || (max / 2);
    const tokens = this.tokenize(str, characters);
    const ts = [];
    for (let i = 0; i < tokens.length; i += step) {
      const seq = [];
      for (let j = i; j < tokens.length; j += 1) {
        seq.push(tokens[j]);
        if (seq.length === max) {
          break;
        }
      }
      if (seq.length > 1 || tokens.length === 1) {
        ts.push(seq);
      }
    }
    return ts.map(this.stringify);
  };
}
const PmSymbolUtil = new _PmSymbolUtil();

// eslint-disable-next-line no-unused-vars, no-underscore-dangle
function PmTransformer() {
  this.getMinValue = (value) => {
    let r = value;
    let reverse = false;
    if (r < 0) {
      reverse = true;
      r *= -1;
    }
    if (r < TRANSFORMER_MINIMUM_VANUE) {
      r = TRANSFORMER_MINIMUM_VANUE;
    }
    if (reverse) {
      r *= -1;
    }
    return r;
  };

  this.calculateValueTransformation = (fromVal, toVal) => {
    let perc = 1.0;
    if (typeof (fromVal) === 'number' && typeof (toVal) === 'number') {
      perc = toVal / this.getMinValue(fromVal);
    } else if (fromVal !== toVal) {
      perc = 0.0;
    }
    return perc;
  };

  this.calculateTransformation = (fromObj, toObj) => {
    const r = {};
    const fromToKeys = Object.keys(fromObj).filter((key) => key in toObj);
    fromToKeys.forEach((key) => {
      const fromVal = fromObj[key];
      const toVal = toObj[key];
      if (fromVal != null && toVal != null) {
        r[key] = this.calculateValueTransformation(fromVal, toVal);
      }
    });
    return r;
  };

  this.applyValueTransformation = (fromVal, perc) => {
    let r = fromVal;
    if (perc !== 1.0 && typeof (fromVal) === 'number') {
      r = this.getMinValue(fromVal) * perc;
    }
    return r;
  };

  this.applyTransformation = (fromObj, transformation) => {
    const r = {};
    const fromToKeys = Object.keys(fromObj).filter((key) => key in transformation);
    fromToKeys.forEach((key) => {
      const fromVal = fromObj[key];
      const perc = transformation[key];
      if (fromVal != null) {
        r[key] = this.applyValueTransformation(fromVal, perc);
      }
    });
    return r;
  };
}
