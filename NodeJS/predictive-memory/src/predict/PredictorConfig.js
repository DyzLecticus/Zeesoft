const Comparator = require('../Comparator');
const IndexGenerator = require('./IndexGenerator');
const CacheConfig = require('../cache/CacheConfig');
const Transformer = require('../Transformer');

function PredictorConfig(max) {
  this.maxHistorySize = max || 1000;
  this.comparator = new Comparator();
  this.cacheIndexes = (new IndexGenerator()).generate();
  this.cacheConfig = new CacheConfig();
  this.transformer = new Transformer();
}
module.exports = PredictorConfig;
