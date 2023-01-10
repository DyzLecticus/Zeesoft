const SymbolConstants = require('./SymbolConstants');

function SymbolUtil() {
  this.trim = (str) => {
    let r = str.trim().replace(/ {4}/g, ' ');
    r = r.replace(/ {3}/g, ' ');
    r = r.replace(/ {2}/g, ' ');
    return r;
  };

  this.replaceCharacters = (str, characters, replacements) => {
    let r = '';
    for (let i = 0; i < str.length; i += 1) {
      const c = str.substring(i, i + 1);
      const idx = characters.indexOf(c);
      if (idx >= 0) {
        r += replacements.substring(idx, idx + 1);
      } else {
        r += c;
      }
    }
    return r;
  };

  this.format = (str) => {
    const characters = SymbolConstants.ALPHABET_EXTENDED
      + SymbolConstants.CAPITALS_EXTENDED;
    const replacements = SymbolConstants.ALPHABET_REPLACEMENTS
      + SymbolConstants.CAPITALS_REPLACEMENTS;
    const r = this.replaceCharacters(str, characters, replacements);
    return this.trim(r);
  };

  this.getCountTransitionReverse = (char, str, characters) => {
    let count = 0;
    let transition = -1;
    let reverse = -1;
    for (let i = 0; i < str.length; i += 1) {
      const symChar = str.substring(i, i + 1);
      if (symChar === char) {
        count += 1;
        if (transition === -1 && i < str.length - 1) {
          const nextSymChar = str.substring(i + 1, i + 2);
          transition = (characters.indexOf(nextSymChar) + 1);
        }
        if (i > 0) {
          const prevSymChar = str.substring(i - 1, i);
          reverse = (characters.indexOf(prevSymChar) + 1);
        }
      }
    }
    transition = (transition === -1) ? 0 : transition;
    reverse = (reverse === -1) ? 0 : reverse;
    return { count, transition, reverse };
  };

  this.generateNumArray = (str, characters) => {
    const indexes = [];
    const counts = [];
    const transitions = [];
    const reversed = [];
    for (let c = 0; c < characters.length; c += 1) {
      const char = characters.substring(c, c + 1);
      const { count, transition, reverse } = this.getCountTransitionReverse(char, str, characters);
      indexes.push((str.indexOf(char) + 1));
      counts.push(count);
      transitions.push(transition);
      reversed.push(reverse);
    }
    return [str.length, ...indexes, ...counts, ...transitions, ...reversed];
  };
}
module.exports = new SymbolUtil();
