const SymbolConstants = require('./SymbolConstants');
const Comparator = require('../Comparator');
const CacheConfig = require('../cache/CacheConfig');

function ClassifierConfig(characters) {
  this.characters = characters || SymbolConstants.CLASSIFIER_CHARACTERS;
  this.sequenceMaxLength = 4;
  this.comparator = new Comparator();

  this.cacheConfig = new CacheConfig();
  // TODO: determine optimal default cache config for symbols
  this.cacheConfig.initiatlizeDefault();
  this.cacheQueryOptions = this.cacheConfig.getQueryOptions();

  this.setComparator = (com) => {
    this.comparator = com;
    this.cacheConfig.setComparator(com);
  };

  this.setComparator(this.comparator);
}
module.exports = ClassifierConfig;
