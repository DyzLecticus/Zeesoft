const Comparator = require('../Comparator');
const IndexGenerator = require('./IndexGenerator');
const CacheConfig = require('../cache/CacheConfig');
const Transformer = require('../Transformer');

function PredictorConfig(size, depth) {
  this.maxHistorySize = size || 1000;
  this.comparator = new Comparator();
  this.cacheIndexes = (new IndexGenerator(depth)).generate();
  this.cacheConfig = new CacheConfig();
  this.transformer = new Transformer();
}
module.exports = PredictorConfig;
