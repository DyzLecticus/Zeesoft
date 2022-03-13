function LevelElement(sim, pCount, elem) {
  this.similarity = sim;
  this.parentCount = pCount;
  this.element = elem;
}

function KeyPrediction(val) {
  this.value = val;
  this.totalSimilarity = 0.0;
  this.totalCount = 0;
  this.weight = 0.0
}

function CacheResult2() {
  this.levelElements = [];
  this.keyPredictions = {};

  this.addLevelElement = (lvl, sim, pCount, elem) => {
    let added = false;
    if (lvl >= this.levelElements.length) {
      this.levelElements[lvl] = [];
    }
    for (let i = 0; i < this.levelElements[lvl].length; i += 1) {
      const levelElem = this.levelElements[lvl][i];
      if (sim >= levelElem.similarity) {
        this.levelElements[lvl].splice(i, 0, new LevelElement(sim, pCount, elem));
        added = true;
        break;
      }
    }
    if (!added) {
      this.levelElements[lvl].push(new LevelElement(sim, pCount, elem));
    }
  };

  this.getLevelElements = (lvl, num) => {
    const n = num || 0;
    const r = [];
    if (this.levelElements[lvl]) {
      for (let i = 0; i < this.levelElements[lvl].length; i += 1) {
        r.push(this.levelElements[lvl][i]);
        if (num > 0 && r.length === n) {
          break;
        }
      }
    }
    return r;
  };

  this.getDeepestElements = (num) => this.getLevelElements(this.levelElements.length - 1, num);

  this.generateKeyPredictions = (num) => {
    const elems = this.getDeepestElements(num);
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
        kp = this.keyPredictions[key][i]
        kp.weight = (kp.totalCount * kp.totalSimilarity) / total;
      }
    }
  };
}
module.exports = CacheResult2;
