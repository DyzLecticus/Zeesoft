const Comparator = require('../Comparator');

function CacheConfig(comp, mergeSim) {
  this.comparator = comp || new Comparator();
  this.mergeSimilarity = mergeSim || 0.925;
  this.maxSize = 1000;
  this.subConfig = null;

  this.initiatlizeDefault = () => {
    this.subConfig = new CacheConfig(this.comparator, 0.96);
    this.subConfig.subConfig = new CacheConfig(this.comparator, 1);
  };

  this.setComparator = (com) => {
    this.comparator = com;
    if (this.subConfig) {
      this.subConfig.setComparator(com);
    }
  };
}
module.exports = CacheConfig;
