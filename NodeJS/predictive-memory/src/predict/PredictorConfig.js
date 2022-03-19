const Comparator = require('../Comparator');
const Transformer = require('../Transformer');
const CacheConfig = require('../cache/CacheConfig');

function PredictorConfig(max) {
  this.maxHistorySize = max || 1000;
  this.comparator = new Comparator();
  this.cacheIndexes = [1, 2, 3, 4, 5, 8, 13, 16, 21, 32, 34, 55, 64];
  this.cacheConfig = new CacheConfig();
  this.transformer = new Transformer();
}
module.exports = PredictorConfig;
