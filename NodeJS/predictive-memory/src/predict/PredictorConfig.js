const Comparator = require('../Comparator');
const IndexGenerator = require('./IndexGenerator');
const CacheConfig = require('../cache/CacheConfig');
const Transformer = require('../Transformer');

function PredictorConfig(size, depth) {
  this.maxHistorySize = size || 129;
  this.comparator = new Comparator();
  this.transformer = new Transformer();

  this.cacheIndexes = (new IndexGenerator(depth)).generate();
  this.cacheConfig = new CacheConfig();
  this.cacheConfig.initiatlizeDefault();

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}
module.exports = PredictorConfig;
