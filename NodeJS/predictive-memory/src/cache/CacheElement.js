function CacheElement(k, v) {
  this.key = k ? k : null;
  this.value = v ? v: null;
  this.count = 0;
  this.subCache = null;
}
module.exports = CacheElement;
