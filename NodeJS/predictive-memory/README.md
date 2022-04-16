# predictive memory

_predictive memory_ generates predictions using a hierarchical transformation sequence cache.

## Why

Prediction can be implemented in low level neural networks (i.e. Numenta HTM). The goal of this module is to provide similar predictions but do it faster, while using less resources.

The long term goal is to expand and use this module in order to research cognition and perhaps consciousness.

## How

The sequence of records that it is presented with is automatically converted to transformations. A specific subset of those transformations is used to predict the next transformation, which is then converted to an absolute prediction using the latest input.

The cache that is used to record transformation sequences (keys) to specific transformations (values) uses an internal hierarchy to quickly find similar key/value pairs in large amounts of historical data. In order to get balanced predictions, the second most similar cache result is used to weigh cache results whenever possible.

## Installation

Via [npm](https://www.npmjs.com/):

```bash
npm install predictive-memory
```

## Usage

See the example implementation in test/e2e/rec-center-hourly.js.

```js
const { PredictorConfig, Predictor, PredictorAnalyzer } = require('predictive-memory');

const pc = new PredictorConfig();
const predictor = new Predictor(pc);

let prediction = null;
// Pass in any name and number of value properties
// Predictions will be generated when a minimal number of records have been processed
prediction = predictor.process({ dayOfWeek: 1, hourOfDay: 22, measuredValue: 5.5 });
prediction = predictor.process({ dayOfWeek: 1, hourOfDay: 23, measuredValue: 6.5 });
predictor.setPredict(true);
prediction = predictor.process({ dayOfWeek: 2, hourOfDay: 0, measuredValue: 1.5 });

// Optional methods to obtain predictor results (see PredictorConfig.maxPredictionHistorySize)
// const latestPrediction = predictor.predictions.get([0])[0];
// const results = predictor.getResults('measuredValue');

const analyzer = new PredictorAnalyzer(['measuredValue']);
const analysis = analyzer.analyze(predictor);

console.log(analysis);
```
