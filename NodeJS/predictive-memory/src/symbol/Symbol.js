const MathUtil = require('../MathUtil');

function Symbol(str, numArray, meta) {
  const that = this;
  this.str = str.trim();
  this.numArray = numArray;
  this.meta = meta || null;

  this.toString = () => MathUtil.stringify(that.numArray);

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
        if (num !== other.numArray[index]) {
          const diff = (num - other.numArray[index]);
          r += (diff * diff);
        }
      });
      r = Math.sqrt(r);
    }
    return r;
  };
}
module.exports = Symbol;
