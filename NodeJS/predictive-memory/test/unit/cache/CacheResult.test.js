const CacheResult = require('../../../src/cache/CacheResult');

describe('CacheResult', () => {
  test('Constructs itself correctly', () => {
    const res = new CacheResult(0.5, 2, [{}, {}]);
    expect(res.similarity).toBe(0.5);
    expect(res.parentCount).toBe(2);
    expect(res.elements).toStrictEqual([{}, {}]);
  });
  test('Adds similar elements correctly', () => {
    const res = new CacheResult();
    let added = res.addSimilarElement(0.1, 2, {});
    expect(added).toBe(true);
    expect(res.similarity).toBe(0.1);
    expect(res.parentCount).toBe(2);
    expect(res.elements.length).toBe(1);
    added = res.addSimilarElement(0.1, 2, {});
    expect(added).toBe(true);
    expect(res.similarity).toBe(0.1);
    expect(res.parentCount).toBe(2);
    expect(res.elements.length).toBe(2);
    added = res.addSimilarElement(0.05, 3, {});
    expect(added).toBe(false);
    expect(res.similarity).toBe(0.1);
    expect(res.parentCount).toBe(2);
    expect(res.elements.length).toBe(2);
    added = res.addSimilarElement(0.2, 5, {});
    expect(added).toBe(true);
    expect(res.similarity).toBe(0.2);
    expect(res.parentCount).toBe(5);
    expect(res.elements.length).toBe(1);
  });
  test('Adds subresults correctly', () => {
    const res = new CacheResult();
    let called = 0;
    const mockRes = { elements: [] };
    const subCache = {
      lookup() {
        called += 1;
        return mockRes;
      },
    };
    res.addSimilarElement(0.1, 2, { subCache });
    res.addSimilarElement(0.1, 2, { subCache });
    const options = {
      minSimilarity: 0.0,
      maxDepth: 0,
    };
    res.addSubResults([{}], 0, 0, options);
    expect(called).toBe(2);
    expect(res.subResults.length).toBe(0);
    mockRes.elements.push({});
    res.addSubResults([{}], 0, 0, options);
    expect(res.subResults.length).toBe(2);
  });
  test('Determines winner and winner secondary correctly', () => {
    const res = new CacheResult();
    res.subResults = [];
    res.subResults[0] = new CacheResult();
    res.subResults[0].similarity = 0.15;
    res.subResults[0].elements[0] = { mock: 1 };
    res.subResults[1] = new CacheResult();
    res.subResults[1].similarity = 0.3;
    res.subResults[1].elements[0] = { mock: 2 };
    res.subResults[2] = new CacheResult();
    res.subResults[2].similarity = 0.2;
    res.subResults[2].elements[0] = { mock: 3 };
    res.summarize();
    expect(res.winner.similarity).toBe(0.3);
    expect(res.winner.elements[0]).toStrictEqual({ mock: 2 });
    expect(res.winner.secondary.similarity).toBe(0.2);
    expect(res.winner.secondary.elements[0]).toStrictEqual({ mock: 3 });

    res.subResults[1].secondary = new CacheResult();
    res.subResults[1].secondary.similarity = 0.25;
    res.subResults[1].secondary.elements[0] = { mock: 4 };
    res.winnerLevel = 0;
    res.winner = null;
    res.summarize();
    expect(res.winner.similarity).toBe(0.3);
    expect(res.winner.elements[0]).toStrictEqual({ mock: 2 });
    expect(res.winner.secondary.similarity).toBe(0.25);
    expect(res.winner.secondary.elements[0]).toStrictEqual({ mock: 4 });
  });
});
