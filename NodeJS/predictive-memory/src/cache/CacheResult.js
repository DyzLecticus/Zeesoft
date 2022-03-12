function LevelElement(sim, pCount, elem) {
  this.similarity = sim;
  this.parentCount = pCount;
  this.element = elem;
}

function CacheResult2() {
  this.levelElements = [];

  this.addLevelElement = (lvl, sim, pCount, elem) => {
    let added = false;
    if (lvl >= this.levelElements.length) {
      this.levelElements[lvl] = [];
    }
    for (let i = 0; i < this.levelElements[lvl].length; i+=1) {
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
    const r = [];
    if (this.levelElements[lvl]) {
      for (let i = 0; i < this.levelElements[lvl].length; i+=1) {
        r.push(this.levelElements[lvl][i]);
        if (r.length === num) {
          break;
        }
      }
    }
    return r;
  };

  this.getDeepestElements = (num) => {
    return this.getLevelElements(this.levelElements.length - 1, num);
  }
}
module.exports = CacheResult2;
