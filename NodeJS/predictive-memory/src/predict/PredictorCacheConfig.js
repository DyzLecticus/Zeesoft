const IndexGenerator = require('./IndexGenerator');
const CacheConfig = require('../cache/CacheConfig');

function PredictorCacheConfig(depth) {
  this.cacheIndexes = (new IndexGenerator(depth)).generate();
  this.cacheConfig = new CacheConfig();
  this.cacheConfig.initiatlizeDefault();
  this.cacheQueryOptions = this.cacheConfig.getQueryOptions();

  this.setComparator = (com) => {
    this.cacheConfig.setComparator(com);
  };
}
module.exports = PredictorCacheConfig;
