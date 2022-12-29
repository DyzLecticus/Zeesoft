const Comparator = require('../Comparator');
const IndexGenerator = require('./IndexGenerator');
const CacheConfig = require('../cache/CacheConfig');

function PredictorConfig(size, depth) {
  this.maxHistorySize = size || 129;
  this.comparator = new Comparator();

  this.cacheIndexes = (new IndexGenerator(depth)).generate();
  this.cacheConfig = new CacheConfig();
  this.cacheConfig.initiatlizeDefault();
  this.cacheQueryOptions = this.cacheConfig.getQueryOptions();

  this.maxPredictionHistorySize = size || 129;

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}
module.exports = PredictorConfig;
