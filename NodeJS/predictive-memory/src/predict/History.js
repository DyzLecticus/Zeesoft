function History(max) {
  this.elements = [];
  this.maxSize = max || 1000;

  this.applyMaxSize = () => {
    if (this.elements.length > this.maxSize) {
      this.elements = this.elements.slice(this.elements.length - this.maxSize);
    }
  };

  this.add = (elem) => {
    this.elements.push(elem);
    this.applyMaxSize();
  };

  this.get = (indexes) => {
    const r = [];
    for (let i = 0; i < indexes.length; i += 1) {
      const ri = (this.elements.length - 1) - indexes[i];
      if (ri >= 0) {
        r.push(this.elements[ri]);
      }
    }
    return r;
  };
}
module.exports = History;
