const CacheResult = require('../../../src/cache/CacheResult');

const addLevelElements = (res) => {
  res.addLevelElement(0, 0.8, 0, {});
  res.addLevelElement(0, 0.6, 0, {});
  res.addLevelElement(0, 0.9, 0, {});
  res.addLevelElement(0, 0.7, 0, {});
  res.addLevelElement(1, 0.98, 3, {});
  res.addLevelElement(1, 0.96, 1, {});
  res.addLevelElement(1, 0.99, 4, {});
  res.addLevelElement(1, 0.97, 2, {});
}

describe('CacheResult', () => {
  test('Adds level elements correctly', () => {
    const res = new CacheResult();
    expect(res.levelElements).toStrictEqual([]);
    addLevelElements(res);
    expect(res.levelElements[0][0].similarity).toBe(0.9);
    expect(res.levelElements[0][1].similarity).toBe(0.8);
    expect(res.levelElements[0][2].similarity).toBe(0.7);
    expect(res.levelElements[0][3].similarity).toBe(0.6);
    expect(res.levelElements[1][0].similarity).toBe(0.99);
    expect(res.levelElements[1][1].similarity).toBe(0.98);
    expect(res.levelElements[1][2].similarity).toBe(0.97);
    expect(res.levelElements[1][3].similarity).toBe(0.96);
  });
  test('Returns top level elements correctly', () => {
    const res = new CacheResult();
    addLevelElements(res);
    const elems = res.getLevelElements(0, 2);
    expect(elems[0].similarity).toBe(0.9);
    expect(elems[1].similarity).toBe(0.8);
    expect(res.getLevelElements(2, 2)).toStrictEqual([]);
    expect(res.getDeepestElements(1)).toStrictEqual([res.levelElements[1][0]]);
  });
});
