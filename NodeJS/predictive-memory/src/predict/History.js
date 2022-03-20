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

  this.get = (indexes, trans) => {
    const r = [];
    for (let i = 0; i < indexes.length; i += 1) {
      let idx = indexes[i];
      if (trans < 0) {
        idx -= (trans * -1);
      } else if (trans > 0) {
        idx += trans;
      }
      const ri = (this.elements.length - 1) - idx;
      if (ri >= 0) {
        r.push(this.elements[ri]);
      } else {
        break;
      }
    }
    return r;
  };
}
module.exports = History;
