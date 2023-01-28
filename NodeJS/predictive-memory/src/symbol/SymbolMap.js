const SymbolConstants = require('./SymbolConstants');
const Symbol = require('./Symbol');

function SymbolMap(characters) {
  const that = this;
  this.characters = characters || SymbolConstants.CHARACTERS;
  this.elements = {};

  this.createSymbol = (str, meta) => new Symbol(str, meta, that.characters);

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
