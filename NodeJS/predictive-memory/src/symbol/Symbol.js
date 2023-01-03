const SYMBOL_ALPHABET = 'abcdefghijklmnopqrstuvwxyz';
const SYMBOL_CAPITALS = SYMBOL_ALPHABET.toUpperCase();
const SYMBOL_NUMBERS = '0123456789';
const SYMBOL_ALPHANUMERICS = SYMBOL_ALPHABET + SYMBOL_CAPITALS + SYMBOL_NUMBERS;
const SYMBOL_ENDERS = '.!?';
const SYMBOL_SEPARATORS = ' -,/';
const SYMBOL_BINDERS = '<>[]()\'"';
const SYMBOL_SPECIALS = '@#$%^&*_+=|\\~';
const SYMBOL_CONTROLS = '\r\n\t';
const SYMBOL_CHARACTERS = SYMBOL_ALPHANUMERICS
  + SYMBOL_ENDERS
  + SYMBOL_SEPARATORS
  + SYMBOL_BINDERS
  + SYMBOL_SPECIALS
  + SYMBOL_CONTROLS;

function Symbol(str) {
  const that = this;
  this.str = str.trim();
  this.numArray = [];

  this.generateNumArray = () => {
    const counts = [];
    const indexes = [];
    const transitions = [];
    for (let c = 0; c < SYMBOL_CHARACTERS.length; c += 1) {
      let count = 0;
      const char = SYMBOL_CHARACTERS.substring(c, c + 1);
      let transition = 0;
      for (let i = 0; i < that.str.length; i += 1) {
        const symChar = that.str.substring(i, i + 1);
        if (symChar === char) {
          count += 1;
          if (i < that.str.length - 1) {
            const nextSymChar = that.str.substring(i + 1, i + 2);
            transition = (SYMBOL_CHARACTERS.indexOf(nextSymChar) + 1);
          }
        }
      }
      counts.push(count);
      indexes.push((that.str.indexOf(char) + 1));
      transitions.push(transition);
    }
    that.numArray = [...counts, ...indexes, ...transitions];
  };

  this.toString = () => {
    let r = `${that.numArray.length}`;
    that.numArray.forEach((num, index) => {
      if (num !== 0) {
        r += `,${index}=${num}`;
      }
    });
    return r;
  };

  this.equals = (other) => {
    let r = false;
    if (other.numArray) {
      r = true;
      for (let i = 0; i < that.numArray.length; i += 1) {
        if (that.numArray[i] !== other.numArray[i]) {
          r = false;
          break;
        }
      }
    }
    return r;
  };

  this.calculateDistance = (other) => {
    let r = 0;
    if (!that.equals(other)) {
      that.numArray.forEach((num, index) => {
        const diff = (num - other.numArray[index]);
        r += (diff * diff);
      });
      r = Math.sqrt(r);
    }
    return r;
  };

  this.generateNumArray();
}
module.exports = Symbol;
