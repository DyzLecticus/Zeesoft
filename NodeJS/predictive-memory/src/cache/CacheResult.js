function CacheResult(sim, pCount, elems) {
  this.similarity = sim || 0.0;
  this.parentCount = pCount || 0;
  this.elements = elems || [];
  this.secondary = null;
  this.subResults = [];

  this.winnerLevel = 0;
  this.winner = null;

  this.addSimilarElement = (s, pc, element) => {
    let added = false;
    if (s > this.similarity) {
      this.similarity = s;
      this.parentCount = pc;
      this.elements = [];
    }
    if (s === this.similarity) {
      this.elements.push(element);
      added = true;
    }
    return added;
  };

  this.addSubResults = (key, level, pc, options) => {
    for (let i = 0; i < this.elements.length; i += 1) {
      const npc = pc + this.elements[i].count;
      const subResult = this.elements[i].subCache.lookup(key, (level + 1), npc, options);
      if (subResult.elements.length > 0) {
        this.subResults.push(subResult);
      }
    }
  };

  this.determineWinner = (result, level) => {
    if (this.winner === null
      || (result.similarity > this.winner.similarity && level >= this.winnerLevel)
      || (result.similarity === this.winner.similarity && level > this.winnerLevel)
    ) {
      this.winnerLevel = level;
      this.winner = result;
    }
    for (let i = 0; i < result.subResults.length; i += 1) {
      this.determineWinner(result.subResults[i], (level + 1));
    }
  };

  this.determineWinnerSecondary = (result, level) => {
    if (result.similarity < this.winner.similarity
      && (this.winner.secondary === null
          || result.similarity >= this.winner.secondary.similarity)
    ) {
      this.winner.secondary = result;
    } else if (result.secondary && result.secondary.similarity < this.winner.similarity
      && (this.winner.secondary === null
        || result.secondary.similarity >= this.winner.secondary.similarity)
    ) {
      this.winner.secondary = result.secondary;
    }
    for (let i = 0; i < result.subResults.length; i += 1) {
      this.determineWinnerSecondary(result.subResults[i], (level + 1));
    }
    if (result.secondary) {
      for (let i = 0; i < result.secondary.subResults.length; i += 1) {
        this.determineWinnerSecondary(result.secondary.subResults[i], (level + 1));
      }
    }
  };

  this.summarize = () => {
    this.determineWinner(this, 0);
    if (this.winner.secondary == null) {
      this.determineWinnerSecondary(this, 0);
    }
  };
}
module.exports = CacheResult;
