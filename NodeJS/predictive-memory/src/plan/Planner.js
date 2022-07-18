function Planner() {
  this.plan = (predictor, steps) => {
    const r = [];
    let pred = predictor.predictions.get([0])[0];
    if (pred) {
      r.push(pred);
      const pathPredictor = predictor.copy();
      for (let s = 1; s < steps; s += 1) {
        pred = pathPredictor.process(pred.predictedValues);
        r.push(pred);
      }
    }
    return r;
  };
}
module.exports = Planner;
