const Predictor = require('../../../src/predict/Predictor');
const Planner = require('../../../src/plan/Planner');

const hists = [
  { a: 1, b: 4 },
  { a: 2, b: 5 },
  { a: 3, b: 6 },
];

const addHists = (predictor, rep) => {
  const pr = predictor;
  pr.setPredict(true);
  for (let i = 0; i < (hists.length * rep); i += 1) {
    pr.process(hists[(i % hists.length)]);
  }
};

describe('Predictor', () => {
  test('Generates plans correctly', () => {
    const predictor = new Predictor();

    const planner = new Planner();
    let plan = planner.plan(predictor, 3);
    expect(plan.length).toBe(0);

    const repeat = 12;
    addHists(predictor, repeat);
    plan = planner.plan(predictor, 3);
    expect(plan.length).toBe(3);
    plan.forEach((step, index) => {
      expect(step.weightedPredictedValues).toStrictEqual(hists[index]);
    });
  });
});
