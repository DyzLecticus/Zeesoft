const Extender = require('../Extender');
const PredictorConfig = require('./PredictorConfig');
const PredictorCacheConfig = require('./PredictorCacheConfig');

function AutoCorrectingPredictorConfig(size, depth) {
  Extender.copyInstanceProperties(new PredictorConfig(size, depth), this);
  Extender.copyInstanceProperties(new PredictorCacheConfig(depth), this, 'correction');

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
    this.correctionCacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}
module.exports = AutoCorrectingPredictorConfig;
