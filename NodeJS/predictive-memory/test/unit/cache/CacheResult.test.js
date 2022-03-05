const CacheResult = require('../../../src/cache/CacheResult');

describe('CacheResult', () => {
  test('Constructs itself correctly', () => {
    const res = new CacheResult(0.5, [{}, {}]);
    expect(res.similarity).toBe(0.5);
    expect(res.elements).toStrictEqual([{}, {}]);
  });
  test('Adds similar elements correctly', () => {
    const res = new CacheResult();
    let added = res.addSimilarElement(0.1, {});
    expect(added).toBe(true);
    expect(res.elements.length).toBe(1);
    added = res.addSimilarElement(0.1, {});
    expect(added).toBe(true);
    expect(res.elements.length).toBe(2);
    added = res.addSimilarElement(0.05, {});
    expect(added).toBe(false);
    expect(res.elements.length).toBe(2);
    added = res.addSimilarElement(0.2, {});
    expect(added).toBe(true);
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
    res.addSimilarElement(0.1, { subCache });
    res.addSimilarElement(0.1, { subCache });
    res.addSubResults([{}], 0.0, 0, 0);
    expect(called).toBe(2);
    expect(res.subResults.length).toBe(0);
    mockRes.elements.push({});
    res.addSubResults([{}], 0.0, 0, 0);
    expect(res.subResults.length).toBe(2);
  });
});
