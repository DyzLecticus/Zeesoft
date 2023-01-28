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

  this.replaceExtensions = (str) => {
    const characters = SymbolConstants.ALPHABET_EXTENDED
      + SymbolConstants.CAPITALS_EXTENDED;
    const replacements = SymbolConstants.ALPHABET_REPLACEMENTS
      + SymbolConstants.CAPITALS_REPLACEMENTS;
    return this.replaceCharacters(str, characters, replacements);
  };

  this.format = (str) => this.trim(this.replaceExtensions(str));

  this.spaceOutNonAllowedCharacters = (str, allowedCharacters) => {
    let r = '';
    for (let i = 0; i < str.length; i += 1) {
      const c = str.substring(i, i + 1);
      const idx = allowedCharacters.indexOf(c);
      if (idx < 0) {
        r += ' ';
      } else {
        r += c;
      }
    }
    return r;
  };

  this.spaceCharacters = (str, characters) => {
    let r = '';
    for (let i = 0; i < str.length; i += 1) {
      const c = str.substring(i, i + 1);
      const idx = characters.indexOf(c);
      if (idx >= 0) {
        r += ` ${c} `;
      } else {
        r += c;
      }
    }
    return r;
  };

  this.tokenizeFormat = (str, characters) => {
    const chars = characters || SymbolConstants.CLASSIFIER_CHARACTERS;
    let r = str;
    if (chars.indexOf(SymbolConstants.CAPITALS) < 0) {
      r = r.toLowerCase();
    }
    r = this.replaceExtensions(r);
    r = this.spaceOutNonAllowedCharacters(r, chars);
    const spaceChars = SymbolConstants.NON_ALPHANUMERICS.replace(' ', '');
    r = this.spaceCharacters(r, spaceChars);
    return this.trim(r);
  };

  this.tokenize = (str, characters) => this.tokenizeFormat(str, characters).split(' ');

  this.stringify = (tokens) => {
    let s = '';
    tokens.forEach((token) => {
      if (s.length > 0) {
        s += ' ';
      }
      s += token;
    });
    return s;
  };

  this.parseSentences = (str, characters) => {
    const tokens = this.tokenize(str, characters);
    const ts = [];
    let seq = [];
    tokens.forEach((token) => {
      seq.push(token);
      if (SymbolConstants.ENDERS.indexOf(token) >= 0) {
        ts.push(seq);
        seq = [];
      }
    });
    if (seq.length > 0) {
      ts.push(seq);
    }
    return ts.map(this.stringify);
  };

  this.sequentialize = (str, maxLength, stepSize, characters) => {
    const max = maxLength || 8;
    const step = stepSize || (max / 2);
    const tokens = this.tokenize(str, characters);
    const ts = [];
    for (let i = 0; i < tokens.length; i += step) {
      const seq = [];
      for (let j = i; j < tokens.length; j += 1) {
        seq.push(tokens[j]);
        if (seq.length === max) {
          break;
        }
      }
      if (seq.length > 1 || tokens.length === 1) {
        ts.push(seq);
      }
    }
    return ts.map(this.stringify);
  };
}
module.exports = new SymbolUtil();
