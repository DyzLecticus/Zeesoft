function MathUtil() {
  this.getAverage = (numArray) => {
    let avg = 0.0;
    if (numArray.length > 0) {
      numArray.forEach((val) => { avg += val; });
      avg /= numArray.length;
    }
    return avg;
  };

  this.getStandardDeviation = (numArray) => {
    let dev = 0.0;
    if (numArray.length > 1) {
      let sum = 0.0;
      numArray.forEach((val) => { sum += val; });
      const avg = sum / numArray.length;
      numArray.forEach((val) => { dev += (val - avg) ** 2; });
      dev = Math.sqrt(dev / (numArray.length - 1));
    }
    return dev;
  };
}
module.exports = new MathUtil();
