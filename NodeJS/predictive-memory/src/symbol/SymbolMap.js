const SymbolConstants = require('./SymbolConstants');
const SymbolUtil = require('./SymbolUtil');
const Symbol = require('./Symbol');

function SymbolMap(characters) {
  const that = this;
  this.characters = characters || SymbolConstants.CHARACTERS;
  this.elements = {};

  this.format = (str) => SymbolUtil.format(str);

  this.generateNumArray = (str) => SymbolUtil.generateNumArray(str, that.characters);

  this.createSymbol = (str, meta) => {
    const s = that.format(str);
    return new Symbol(s, that.generateNumArray(s), meta);
  };

  this.getById = (id) => that.elements[id];

  this.get = (str) => that.getById(that.createSymbol(str).toString());

  this.put = (str, meta) => {
    const symbol = that.createSymbol(str, meta);
    that.elements[symbol.toString()] = symbol;
    return symbol;
  };

  this.toArray = () => Object.keys(that.elements).map((key) => that.elements[key]);

  this.addArray = (symbols) => {
    symbols.forEach((symbol) => { that.elements[symbol.toString()] = symbol; });
  };

  this.getDistances = (str) => {
    const symbol = that.createSymbol(str);
    return Object.keys(that.elements).map(
      (key) => ({
        symbol: that.elements[key],
        dist: symbol.calculateDistance(that.elements[key]),
      }),
    );
  };

  this.getNearest = (str) => {
    const r = that.getDistances(str).sort(
      (a, b) => a.dist - b.dist,
    );
    return r;
  };
}
module.exports = SymbolMap;
