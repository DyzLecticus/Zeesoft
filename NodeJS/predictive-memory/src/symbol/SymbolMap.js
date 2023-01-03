const Symbol = require('./Symbol');

function SymbolMap() {
  const that = this;
  this.elements = {};

  this.get = (str) => {
    const symbol = new Symbol(str);
    return that.elements[symbol.toString()];
  };

  this.put = (str) => {
    const symbol = new Symbol(str);
    that.elements[symbol.toString()] = symbol;
    return symbol;
  };

  this.getNearest = (str, max) => {
    const m = max || 1;
    const symbol = new Symbol(str);
    let r = Object.keys(that.elements).map(
      (key) => ({
        symbol: that.elements[key],
        dist: symbol.calculateDistance(that.elements[key]),
      }),
    ).sort(
      (a, b) => a.dist < b.dist,
    );
    if (r.length > m) {
      r = r.slice(r.length - m);
    }
    return r;
  };
}
module.exports = SymbolMap;
