const Comparator = require('../Comparator');

function CacheConfig(comp, mergeSim) {
  this.comparator = comp ? comp : new Comparator();
  this.mergeSimilarity = mergeSim || 0.95;
  this.maxSize = 1000;
  this.subConfig = null;
  this.initiatlizeDefault = () => {
    this.subConfig = new CacheConfig(this.comparator, 0.98);
    this.subConfig.subConfig = new CacheConfig(this.comparator, 0.99);
    this.subConfig.subConfig.subConfig = new CacheConfig(this.comparator, 1.0);
  };
  this.setComparator = (comp) => {
    this.comparator = comp;
    if (this.subConfig) {
      this.subConfig.setComparator(comp);
    }
  };
}
module.exports = CacheConfig;
