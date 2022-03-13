const minValue = 0.0000000001;

function Transformer() {
  this.getMinValue = (value) => {
    let r = value;
    let reverse = false;
    if (r < 0) {
      reverse = true;
      r *= -1;
    }
    if (r < minValue) {
      r = minValue;
    }
    if (reverse) {
      r *= -1;
    }
    return r;
  };

  this.calculateValueTransformation = (fromVal, toVal) => {
    let perc = 1.0;
    if (typeof (fromVal) === 'number' && typeof (toVal) === 'number') {
      perc = toVal / this.getMinValue(fromVal);
    } else if (fromVal !== toVal) {
      perc = 0.0;
    }
    return perc;
  };

  this.calculateTransformation = (fromObj, toObj) => {
    const r = {};
    const fromToKeys = Object.keys(fromObj).filter((key) => key in toObj);
    for (let k = 0; k < fromToKeys.length; k += 1) {
      const key = fromToKeys[k];
      const fromVal = fromObj[key];
      const toVal = toObj[key];
      if (fromVal != null && toVal != null) {
        r[key] = this.calculateValueTransformation(fromVal, toVal);
      }
    }
    return r;
  };

  this.applyValueTransformation = (fromVal, perc) => {
    let r = fromVal;
    if (perc !== 1.0 && typeof (fromVal) === 'number') {
      r = this.getMinValue(fromVal) * perc;
    }
    return r;
  };

  this.applyTransformation = (fromObj, transformation) => {
    const r = {};
    const fromToKeys = Object.keys(fromObj).filter((key) => key in transformation);
    for (let k = 0; k < fromToKeys.length; k += 1) {
      const key = fromToKeys[k];
      const fromVal = fromObj[key];
      const perc = transformation[key];
      if (fromVal != null) {
        r[key] = this.applyValueTransformation(fromVal, perc);
      }
    }
    return r;
  };
}
module.exports = Transformer;
