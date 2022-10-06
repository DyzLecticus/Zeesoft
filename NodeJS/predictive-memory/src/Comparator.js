function Comparator() {
  this.calculateNumberSimilarity = (a, b) => {
    let perc = 0.0;
    let ac = a;
    let bc = b;
    const min = Math.min(a, b);
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

  this.calculateLevenshteinDistance = (a, b) => {
    const c = a.length + 1;
    const d = b.length + 1;
    const r = Array(c);
    for (let i = 0; i < c; i += 1) r[i] = Array(d);
    for (let i = 0; i < c; i += 1) r[i][0] = i;
    for (let j = 0; j < d; j += 1) r[0][j] = j;
    for (let i = 1; i < c; i += 1) {
      for (let j = 1; j < d; j += 1) {
        const s = (a[i - 1] === b[j - 1] ? 0 : 1);
        r[i][j] = Math.min(r[i - 1][j] + 1, r[i][j - 1] + 1, r[i - 1][j - 1] + s);
      }
    }
    return r[a.length][b.length];
  };

  this.calculateStringSimilarity = (a, b) => {
    let perc = 0.0;
    if (a.length > 0 && b.length > 0) {
      const dist = this.calculateLevenshteinDistance(a, b);
      const len = Math.max(a.length, b.length);
      perc = (len - dist) / len;
    }
    return perc;
  };

  this.calculateValueSimilarity = (a, b) => {
    let perc = 0.0;
    if (a === b) {
      perc = 1.0;
    } else if (typeof (a) === 'number' && typeof (b) === 'number') {
      perc = this.calculateNumberSimilarity(a, b);
    } else if (typeof (a) === 'string' && typeof (b) === 'string') {
      perc = this.calculateStringSimilarity(a, b);
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
      abKeys.forEach((key) => { perc += this.calculateValueSimilarity(a[key], b[key]); });
      perc /= total;
    }
    return perc;
  };

  this.calculateObjectArraySimilarity = (a, b) => {
    let perc = 1.0;
    const max = Math.max(a.length, b.length);
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
