function CacheElement(k, v) {
  this.key = k || null;
  this.value = v || null;
  this.count = 0;
  this.subCache = null;
}
module.exports = CacheElement;
