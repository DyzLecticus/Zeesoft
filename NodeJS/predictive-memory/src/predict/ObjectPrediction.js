function KeyPrediction(val) {
  this.value = val;
  this.totalSimilarity = 0.0;
  this.totalCount = 0;
  this.weight = 0.0;
}

function ObjectPrediction(result) {
  this.cacheResult = result;
  this.keyPredictions = {};
  this.predictedValues = {};
  this.weightedPredictedValues = {};

  this.generateKeyPredictions = (elems) => {
    for (let i = 0; i < elems.length; i += 1) {
      const keys = Object.keys(elems[i].element.value);
      for (let k = 0; k < keys.length; k += 1) {
        const key = keys[k];
        const value = elems[i].element.value[key];
        if (!this.keyPredictions[key]) {
          this.keyPredictions[key] = [];
        }
        const kps = this.keyPredictions[key].filter((kp) => kp.value === value);
        let kp = kps.length ? kps[0] : null;
        if (!kp) {
          kp = new KeyPrediction(value);
          this.keyPredictions[key].push(kp);
        }
        kp.totalSimilarity += (elems[i].similarity);
        kp.totalCount += (elems[i].parentCount + elems[i].element.count);
      }
    }
  };

  this.calculateKeyPredictionWeights = () => {
    const keys = Object.keys(this.keyPredictions);
    for (let k = 0; k < keys.length; k += 1) {
      const key = keys[k];
      let total = 0.0;
      for (let i = 0; i < this.keyPredictions[key].length; i += 1) {
        const kp = this.keyPredictions[key][i];
        total += (kp.totalCount * kp.totalSimilarity);
      }
      for (let i = 0; i < this.keyPredictions[key].length; i += 1) {
        const kp = this.keyPredictions[key][i];
        kp.weight = (kp.totalCount * kp.totalSimilarity) / total;
      }
      this.keyPredictions[key] = this.keyPredictions[key].sort((a, b) => (b.weight - a.weight));
    }
  };

  this.calculatePredictedValues = () => {
    const keys = Object.keys(this.keyPredictions);
    for (let k = 0; k < keys.length; k += 1) {
      const key = keys[k];
      let val = 0.0;
      let isNum = false;
      for (let i = 0; i < this.keyPredictions[key].length; i += 1) {
        const kp = this.keyPredictions[key][i];
        if (i === 0) {
          this.predictedValues[key] = kp.value;
        }
        if (typeof (kp.value) === 'number') {
          isNum = true;
          val += kp.weight * kp.value;
        }
      }
      if (isNum) {
        this.weightedPredictedValues[key] = val;
      }
    }
  };

  this.generatePrediction = (num) => {
    this.generateKeyPredictions(this.cacheResult.getDeepestElements(num));
    this.calculateKeyPredictionWeights();
    this.calculatePredictedValues();
  };
}
module.exports = ObjectPrediction;
