const Comparator = require('../Comparator');
const Transformer = require('../Transformer');

const Extender = require('../Extender');
const PredictorCacheConfig = require('./PredictorCacheConfig');

function PredictorConfig(size, depth) {
  this.maxHistorySize = size || 129;
  this.comparator = new Comparator();
  this.transformer = new Transformer();

  Extender.copyInstanceProperties(new PredictorCacheConfig(depth), this);

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}
module.exports = PredictorConfig;
