function Symbol(str, numArray, meta) {
  const that = this;
  this.str = str.trim();
  this.numArray = numArray;
  this.meta = meta || null;

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
}
module.exports = Symbol;
