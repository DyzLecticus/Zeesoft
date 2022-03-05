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
  this.calculateValueSimilarity = (a, b) => {
    let perc = 0.0;
    if (typeof (a) === 'number' && typeof (b) === 'number') {
      perc = this.calculateNumberSimilarity(a, b);
    } else if (a === b) {
      perc = 1.0;
    }
    return perc;
  };
  this.calculateObjectSimilarity = (a, b) => {
    let perc = 1.0;
    const abKeys = Object.keys(a).filter((key) => key in b);
    const nbKeys = Object.keys(a).filter((key) => !(key in b));
    const naKeys = Object.keys(b).filter((key) => !(key in a));
    const total = abKeys.length + nbKeys.length + naKeys.length;
    if (total > 0) {
      perc = 0;
      for (let i = 0; i < abKeys.length; i += 1) {
        perc += this.calculateValueSimilarity(a[abKeys[i]], b[abKeys[i]]);
      }
      perc /= total;
    }
    return perc;
  };
  this.calculateObjectArraySimilarity = (a, b) => {
    let perc = 1.0;
    const max = this.max(a.length, b.length);
    if (max > 0) {
      perc = 0;
      for (let i = 0; i < max; i += 1) {
        if (a.length > i && b.length > i) {
          perc += this.calculateObjectSimilarity(a[i], b[i]);
        }
      }
      perc /= max;
    }
    return perc;
  };
  this.calculateSimilarity = (a, b) => {
    let perc = 0.0;
    if (Array.isArray(a) && Array.isArray(b)) {
      perc = this.calculateObjectArraySimilarity(a, b);
    } else {
      perc = this.calculateObjectSimilarity(a, b);
    }
    return perc;
  };
}
module.exports = Comparator;
