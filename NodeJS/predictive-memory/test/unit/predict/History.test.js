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

  test('Copies itself correctly', () => {
    const hist = new History();
    expect(hist.maxSize).toBe(1000);
    hist.add({ a: 1 });
    hist.add({ a: 2 });
    hist.add({ a: 3 });
    hist.add({ a: 4 });
    const copy = hist.copy();
    expect(copy.maxSize).toBe(hist.maxSize);
    expect(copy.elements.length).toBe(hist.elements.length);
    copy.elements.forEach((elem, index) => {
      expect(JSON.stringify(elem)).toStrictEqual(JSON.stringify(hist.elements[index]));
    });
  });
});
