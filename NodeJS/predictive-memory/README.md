# predictive memory

_predictive memory_ generates predictions using a hierarchical sequence cache.

## Why

Prediction can be implemented in low level neural networks (i.e. Numenta HTM). The goal of this module is to provide similar predictions, but do it faster while using less resources.

The long term goal is to expand and use this module in order to research cognition and perhaps consciousness.

## How

This module provides a **Predictor** class that can _process_ historical objects. It uses a **Comparator** to compare individual objects and sequences of those objects to other previously recorded sequences. 
The cache that is used to record sequences uses an internal hierarchy to quickly find similar key/value pairs in large amounts of historical data. In order to get balanced & weighted predictions, the second (or more) most similar cache result is used to weigh cache results whenever possible.

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

const analyzer = new PredictorAnalyzer();
const analysis = analyzer.analyze(predictor, ['measuredValue']);
console.log(analysis);

const predictionsForNext24Steps = predictor.generatePredictions(24);
console.log(predictionsForNext24Steps);
```
