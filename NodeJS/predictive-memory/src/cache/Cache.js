const CacheElement = require('./CacheElement');

function Cache(config) {
  this.config = config;
  this.elements = [];
}
module.exports = Cache;
