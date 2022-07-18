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
  this.rawPredictedValues = {};

  this.generateKeyPredictions = (elems) => {
    elems.forEach((elem) => {
      Object.keys(elem.element.value).forEach((key) => {
        const value = elem.element.value[key];
        if (!this.keyPredictions[key]) {
          this.keyPredictions[key] = [];
        }
        const kps = this.keyPredictions[key].filter((kp) => kp.value === value);
        let kp = kps.length ? kps[0] : null;
        if (!kp) {
          kp = new KeyPrediction(value);
          this.keyPredictions[key].push(kp);
        }
        kp.totalSimilarity += (elem.similarity);
        kp.totalCount += (elem.parentCount + elem.element.count);
      });
    });
  };

  this.calculateKeyPredictionWeights = () => {
    Object.keys(this.keyPredictions).forEach((key) => {
      let total = 0.0;
      this.keyPredictions[key].forEach((kp) => {
        total += (kp.totalCount * kp.totalSimilarity);
      });
      this.keyPredictions[key].forEach((kp) => {
        const p = kp;
        p.weight = (kp.totalCount * kp.totalSimilarity) / total;
      });
      this.keyPredictions[key] = this.keyPredictions[key].sort((a, b) => (b.weight - a.weight));
    });
  };

  this.calculatePredictedValues = () => {
    Object.keys(this.keyPredictions).forEach((key) => {
      let val = 0.0;
      let isNum = false;
      this.keyPredictions[key].forEach((kp, i) => {
        if (i === 0) {
          this.rawPredictedValues[key] = kp.value;
        }
        if (typeof (kp.value) === 'number') {
          isNum = true;
          val += kp.weight * kp.value;
        }
      });
      this.predictedValues[key] = isNum ? val : this.rawPredictedValues[key];
    });
  };

  this.generatePrediction = (num) => {
    this.generateKeyPredictions(this.cacheResult.getDeepestElements(num));
    this.calculateKeyPredictionWeights();
    this.calculatePredictedValues();
  };
}
module.exports = ObjectPrediction;
