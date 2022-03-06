function MathUtil() {
  this.getAverage = (numArray) => {
    let avg = 0.0;
    if (numArray.length > 0) {
      for (let i = 0; i < numArray.length; i += 1) {
        avg += numArray[i];
      }
      avg /= numArray.length;
    }
    return avg;
  };

  this.getStandardDeviation = (numArray) => {
    let dev = 0.0;
    if (numArray.length > 0) {
      let sum = 0.0;
      for (let i = 0; i < numArray.length; i += 1) {
        sum += numArray[i];
      }
      const avg = sum / numArray.length;
      for (let i = 0; i < numArray.length; i += 1) {
        dev += (numArray[i] - avg) ** 2;
      }
      dev = Math.sqrt(dev / (numArray.length - 1));
    }
    return dev;
  };
}
module.exports = new MathUtil();
