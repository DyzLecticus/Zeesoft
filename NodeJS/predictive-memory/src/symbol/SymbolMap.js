const SymbolConstants = require('./SymbolConstants');
const Symbol = require('./Symbol');

function SymbolMap(characters) {
  const that = this;
  this.characters = characters || SymbolConstants.CHARACTERS;

  this.elements = {};

  this.getCountTransitionReverse = (char, str) => {
    let count = 0;
    let transition = -1;
    let reverse = -1;
    for (let i = 0; i < str.length; i += 1) {
      const symChar = str.substring(i, i + 1);
      if (symChar === char) {
        count += 1;
        if (transition === -1 && i < str.length - 1) {
          const nextSymChar = str.substring(i + 1, i + 2);
          transition = (that.characters.indexOf(nextSymChar) + 1);
        }
        if (i > 0) {
          const prevSymChar = str.substring(i - 1, i);
          reverse = (that.characters.indexOf(prevSymChar) + 1);
        }
      }
    }
    transition = (transition === -1) ? 0 : transition;
    reverse = (reverse === -1) ? 0 : reverse;
    return { count, transition, reverse };
  };

  this.generateNumArray = (str) => {
    const indexes = [];
    const counts = [];
    const transitions = [];
    const reversed = [];
    for (let c = 0; c < that.characters.length; c += 1) {
      const char = that.characters.substring(c, c + 1);
      const { count, transition, reverse } = that.getCountTransitionReverse(char, str);
      indexes.push((str.indexOf(char) + 1));
      counts.push(count);
      transitions.push(transition);
      reversed.push(reverse);
    }
    return [...indexes, ...counts, ...transitions, ...reversed];
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

  this.getAsArray = () => Object.keys(that.elements).map((key) => that.elements[key]);

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
