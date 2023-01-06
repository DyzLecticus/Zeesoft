const SymbolConstants = require('./SymbolConstants');
const Symbol = require('./Symbol');

function SymbolMap(characters) {
  const that = this;
  this.characters = characters || SymbolConstants.CHARACTERS;

  this.elements = {};

  this.generateNumArray = (str) => {
    const counts = [];
    const indexes = [];
    const transitions = [];
    for (let c = 0; c < that.characters.length; c += 1) {
      let count = 0;
      const char = that.characters.substring(c, c + 1);
      let transition = 0;
      for (let i = 0; i < str.length; i += 1) {
        const symChar = str.substring(i, i + 1);
        if (symChar === char) {
          count += 1;
          if (i < str.length - 1) {
            const nextSymChar = str.substring(i + 1, i + 2);
            transition = (that.characters.indexOf(nextSymChar) + 1);
          }
        }
      }
      counts.push(count);
      indexes.push((str.indexOf(char) + 1));
      transitions.push(transition);
    }
    return [...counts, ...indexes, ...transitions];
  };

  this.createSymbol = (str, meta) => new Symbol(str, that.generateNumArray(str), meta);

  this.get = (str) => {
    const symbol = that.createSymbol(str);
    return that.elements[symbol.toString()];
  };

  this.put = (str, meta) => {
    const symbol = that.createSymbol(str, meta);
    that.elements[symbol.toString()] = symbol;
    return symbol;
  };

  this.getNearest = (str, max) => {
    const m = max || 1;
    const symbol = that.createSymbol(str);
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
