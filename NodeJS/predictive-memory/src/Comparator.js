function Comparator() {
  this.max = (a, b) => {
    let r = a;
    if (b > a) {
      r = b;
    }
    return r;
  };
  this.min = (a, b) => {
    let r = a;
    if (b < a) {
      r = b;
    }
    return r;
  };
  this.calculateNumberSimilarity = (a, b) => {
    let perc = 0.0;
    let ac = a;
    let bc = b;
    const min = this.min(a, b);
    if (min < 0) {
      ac += (min * -2.0);
      bc += (min * -2.0);
    }
    if (ac > bc) {
      perc = (ac - bc) / ((ac + bc) / 2.0);
    } else {
      perc = (bc - ac) / ((ac + bc) / 2.0);
    }
    perc = 1.0 - (perc / 2.0);
    return perc;
  };
}
module.exports = Comparator;
