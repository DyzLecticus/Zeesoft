function IndexGenerator(depth) {
  this.depth = depth || 8;

  this.addIndex = (idxs, idx) => {
    const existing = idxs.filter((ix) => ix === idx);
    if (existing.length === 0) {
      idxs.push(idx);
    }
  };

  this.addDefault = (idxs) => {
    for (let i = 0; i < this.depth; i += 1) {
      this.addIndex(idxs, (i + 1));
    }
  };

  this.getFibonacci = () => {
    const idxs = [1];
    let idx = 2;
    for (let i = 0; i < (this.depth - 1); i += 1) {
      this.addIndex(idxs, idx);
      idx += idxs[idxs.length - 2];
    }
    return idxs;
  };

  this.addFibonacci = (idxs) => {
    const add = this.getFibonacci();
    for (let i = 0; i < add.length; i += 1) {
      this.addIndex(idxs, add[i]);
    }
  };

  this.addPower = (idxs) => {
    let idx = 1;
    for (let i = 0; i < this.depth; i += 1) {
      this.addIndex(idxs, idx);
      idx += idx;
    }
  };

  this.generate = () => {
    const idxs = [];
    this.addDefault(idxs);
    this.addFibonacci(idxs);
    this.addPower(idxs);
    idxs.sort((a, b) => a - b);
    return idxs;
  };
}
module.exports = IndexGenerator;
