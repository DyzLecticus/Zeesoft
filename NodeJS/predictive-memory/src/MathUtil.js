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

  this.stringify = (numArray) => {
    let r = `${numArray.length}`;
    numArray.forEach((num, index) => {
      if (num !== 0) {
        r += `,${index}=${num}`;
      }
    });
    return r;
  };

  this.parse = (str) => {
    let r = [];
    const elems = str.split(',');
    elems.forEach((elem, index) => {
      if (index === 0) {
        r = Array(parseInt(elem, 10)).fill(0);
      } else {
        const iv = elem.split('=');
        r[parseInt(iv[0], 10)] = parseInt(iv[1], 10);
      }
    });
    return r;
  };
}
module.exports = new MathUtil();
