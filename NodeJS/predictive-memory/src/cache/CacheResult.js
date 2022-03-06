function CacheResult(sim, elems) {
  this.similarity = sim || 0.0;
  this.elements = elems || [];
  this.secondary = null;
  this.subResults = [];

  this.winnerLevel = 0;
  this.winner = null;

  this.addSimilarElement = (s, element) => {
    let added = false;
    if (s > this.similarity) {
      this.similarity = s;
      this.elements = [];
    }
    if (s === this.similarity) {
      this.elements.push(element);
      added = true;
    }
    return added;
  };

  this.addSubResults = (key, minSimilarity, level, maxDepth) => {
    for (let i = 0; i < this.elements.length; i += 1) {
      const subResult = this.elements[i].subCache.lookup(key, minSimilarity, (level + 1), maxDepth);
      if (subResult.elements.length > 0) {
        this.subResults.push(subResult);
      }
    }
  };

  this.determineWinner = (result, level) => {
    if (this.winner === null
      || (result.similarity > this.winner.similarity && level >= this.winnerLevel)
    ) {
      this.winnerLevel = level;
      this.winner = result;
    }
    for (let i = 0; i < result.subResults.length; i += 1) {
      this.determineWinner(result.subResults[i], (level + 1));
    }
  };

  this.determineWinnerSecondary = (result, level) => {
    if (level >= this.winnerLevel && result !== this.winner
      && (this.winner.secondary === null || (result.similarity > this.winner.secondary.similarity))
    ) {
      this.winner.secondary = result;
    }
    for (let i = 0; i < result.subResults.length; i += 1) {
      this.determineWinnerSecondary(result.subResults[i], (level + 1));
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
