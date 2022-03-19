const History = require('../../../src/predict/History');

describe('History', () => {
  test('Limits its size correctly', () => {
    const hist = new History(3);
    hist.add({ a: 1 });
    hist.add({ a: 2 });
    hist.add({ a: 3 });
    expect(hist.elements.length).toBe(3);
    hist.add({ a: 4 });
    expect(hist.elements.length).toBe(3);
    expect(hist.elements[0]).toStrictEqual({ a: 2 });
  });

  test('Returns reverse indexed elements correctly', () => {
    const hist = new History();
    expect(hist.maxSize).toBe(1000);
    hist.add({ a: 1 });
    hist.add({ a: 2 });
    hist.add({ a: 3 });
    hist.add({ a: 4 });
    const indexes = [1, 2, 4];
    let elements = hist.get(indexes);
    expect(elements).toStrictEqual([{ a: 3 }, { a: 2 }]);
    elements = hist.get(indexes, -1);
    expect(elements).toStrictEqual([{ a: 4 }, { a: 3 }, { a: 1 }]);
    elements = hist.get(indexes, 1);
    expect(elements).toStrictEqual([{ a: 2 }, { a: 1 }]);
  });
});
