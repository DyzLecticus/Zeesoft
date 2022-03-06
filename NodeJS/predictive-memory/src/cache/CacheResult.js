function CacheResult(sim, elems) {
  this.similarity = sim || 0.0;
  this.elements = elems || [];
  this.secondary = null;
  this.subResults = [];
  
  this.addSimilarElement = (s, element) => {
    let added = false;
    if (s > this.similarity) {
      this.similarity = s;
      this.elements = [];
    }
    if (s === this.similarity) {
      this.elements.push(element);
      added = true;
    }
    return added;
  };
  
  this.addSubResults = (key, minSimilarity, level, maxDepth) => {
    for (let i = 0; i < this.elements.length; i += 1) {
      const subResult = this.elements[i].subCache.lookup(key, minSimilarity, (level + 1), maxDepth);
      if (subResult.elements.length > 0) {
        this.subResults.push(subResult);
      }
    }
  };
}
module.exports = CacheResult;
