const MathUtil = require('../MathUtil');
const SymbolConstants = require('./SymbolConstants');
const SymbolUtil = require('./SymbolUtil');

function Symbol(str, meta, characters) {
  const that = this;
  this.str = SymbolUtil.format(str);
  this.meta = meta || null;

  this.length = 0;
  this.keyValues = {};

  this.toNumArray = () => {
    const r = [that.length];
    for (let i = 0; i < that.length; i += 1) {
      const v = that.keyValues[`${i}`] || 0;
      r[i] = v;
    }
    return r;
  };

  this.toString = () => MathUtil.stringify(that.toNumArray());

  this.calculateDistance = (other) => {
    let r = -1;
    if (that === other) {
      r = 0;
    } else if (that.length === other.length) {
      r = 0;
      const otherNumArray = other.toNumArray();
      that.toNumArray().forEach((num, index) => {
        if (num !== otherNumArray[index]) {
          const diff = (num - otherNumArray[index]);
          r += (diff * diff);
        }
      });
      r = Math.sqrt(r);
    }
    return r;
  };

  this.getIndexes = (s, chars) => {
    const idxs = {};
    for (let i = 0; i < s.length; i += 1) {
      const c = s.substring(i, i + 1);
      const idx = chars.indexOf(c);
      if (!idxs[`${idx}`]) {
        idxs[`${idx}`] = (i + 1);
      }
    }
    return idxs;
  };

  this.getTransitions = (s, chars) => {
    const r = {};
    let pc = '';
    for (let i = 0; i < s.length; i += 1) {
      const c = s.substring(i, i + 1);
      const idx = chars.indexOf(c);
      const tIdx = (chars.length + idx);
      const fIdx = ((chars.length * 2) + idx);
      if (i < (s.length - 1) && !r[`${tIdx}`]) {
        const nc = s.substring(i, 1);
        r[`${tIdx}`] = chars.indexOf(nc);
      }
      if (pc.length > 0) {
        r[`${fIdx}`] = chars.indexOf(pc);
      }
      pc = c;
    }
    return r;
  };

  this.mergeTransitions = (transitions) => {
    const r = {};
    transitions.forEach((transition) => {
      Object.keys(transition).forEach((key) => {
        let v = r[key];
        if (!v) {
          v = 0;
        }
        v += transition[key];
        r[key] = v;
      });
    });
    return r;
  };

  this.generateKeyValues = (chars) => {
    const chrs = chars || SymbolConstants.CLASSIFIER_CHARACTERS;
    that.length = (chrs.length * 3);
    const s = SymbolUtil.tokenizeFormat(that.str, chrs);
    const indexes = that.getIndexes(s, chrs);
    const transitions = [];
    const tokens = s.split(' ');
    tokens.forEach((token) => {
      transitions.push(that.getTransitions(token, chrs));
    });
    const merged = that.mergeTransitions(transitions);
    that.keyValues = { ...indexes, ...merged };
  };

  this.generateKeyValues(characters);
}
module.exports = Symbol;
