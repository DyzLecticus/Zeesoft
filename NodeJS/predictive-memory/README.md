# predictive memory

_predictive memory_ generates predictions using a hierarchical transformation sequence cache. 

## Installation

Via [npm](https://www.npmjs.com/):

```bash
npm install predictive-memory
```

## Usage

See the example implementation in tests/e2e/rec-center-hourly.js.

```js
const { PredictorConfig, Predictor, PredictorAnalyzer } = require('predictive-memory');

const pc = new PredictorConfig();
const predictor = new Predictor(pc);

let prediction = null;
// Pass in any name / number value properties
prediction = predictor.process({ dayOfWeek: 1, hourOfDay: 22, measuredValue: 5.5 });
prediction = predictor.process({ dayOfWeek: 1, hourOfDay: 23, measuredValue: 6.5 });
[ ... ]

const latestPrediction = predictor.predictions.get([0])[0];

const analyzer = new PredictorAnalyzer(['measuredValue']);
const analysis = analyzer.analyze(predictor);

console.log(analysis);
```
