# predictive memory

_predictive memory_ generates predictions and classifications using a hierarchical sequence/object cache.

## Why

Prediction and classification can be implemented in low level neural networks (i.e. Numenta HTM). The goal of this module is to provide similar features, but do it faster while using less resources.

The long term goal is to expand and use this module in order to research cognition and perhaps consciousness.

## How

This module provides a **Predictor** class that can _process_ historical objects. It uses a **Comparator** to compare individual objects and sequences of those objects to other previously recorded sequences. 
The cache that is used to record sequences uses an internal hierarchy to quickly find similar key/value pairs in large amounts of historical data. In order to get balanced & weighted predictions, the second (or more) most similar cache result is used to weigh cache results whenever possible.

This module also provides support for text classification by providing a **Classifier** class. It uses the same type of hierarchical cache as the **Predictor**.

## Installation

Via [npm](https://www.npmjs.com/):

```bash
npm install predictive-memory
```

## Usage 

This module can be used both server and client side.
Client side implementation can be done by loading predictive-memory.js as provided the root of the module.
**Please note** that all class names in the client side implementation have been prefixed with 'Pm'.

### Prediction

#### Server side example

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

#### Client side example

See the example implementation in test/e2e/test.html.

```html
<html>
    <head>
        <script src="predictive-memory.js"></script>
        <script type="text/javascript">
            const onload = () => {
                const pc = new PmPredictorConfig();
                const predictor = new PmPredictor(pc);
                for (let i = 0; i < 200; i++) {
                    predictor.process({k: i % 5, v: i % 10});
                    if (i > 10) {
                        predictor.setPredict(true);
                    }
                }
                const analyzer = new PmPredictorAnalyzer();
                const analysis = analyzer.analyze(predictor, ['v']);
                let str = JSON.stringify(analysis, null, 2);
                str = str.replace(/ /g, '&nbsp;');
                str = str.replace(/\n/g, '<br />');
                document.getElementById('analysis').innerHTML = str;
            };
        </script>
    </head>
    <body onload="onload()">
        <div id="analysis" />
    </body>
</html>
```

### Classification

#### Server side example

See the example implementation in test/e2e/text-classification.js.

```js
const { ClasifierConfig, Classifier, ClassifierAnalyzer } = require('predictive-memory');

const config = new ClassifierConfig();
const classifier = new Classifier(config);
// Record input for analyzer
classifier.setRecordInput(true);

classifier.put('String to associate1','Class to associate1');
classifier.put('String to associate2','Class to associate2');

const result classifier.classify('String to classify');
console.log(result.classifications);

const analyzer = new ClassifierAnalyzer();
const analysis = analyzer.analyze(classifier);
console.log(analysis);
```
