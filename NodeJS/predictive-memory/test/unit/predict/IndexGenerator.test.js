const IndexGenerator = require('../../../src/predict/IndexGenerator');

describe('IndexGenerator', () => {
  test('Adds indexes correctly', () => {
    const generator = new IndexGenerator();
    const indexes = [1, 2, 3];
    generator.addIndex(indexes, 3);
    expect(indexes).toStrictEqual([1, 2, 3]);
    generator.addIndex(indexes, 4);
    expect(indexes).toStrictEqual([1, 2, 3, 4]);
  });

  test('Adds default indexes correctly', () => {
    const generator = new IndexGenerator();
    generator.depth = 4;
    const indexes = [];
    generator.addDefault(indexes);
    expect(indexes).toStrictEqual([1, 2, 3, 4]);
    generator.depth = 6;
    generator.addDefault(indexes);
    expect(indexes).toStrictEqual([1, 2, 3, 4, 5, 6]);
  });

  test('Adds fibonacci indexes correctly', () => {
    const generator = new IndexGenerator(6);
    const indexes = [];
    generator.addFibonacci(indexes);
    expect(indexes).toStrictEqual([1, 2, 3, 5, 8, 13]);
  });

  test('Adds power indexes correctly', () => {
    const generator = new IndexGenerator(6);
    const indexes = [];
    generator.addPower(indexes);
    expect(indexes).toStrictEqual([1, 2, 4, 8, 16, 32]);
  });

  test('Generate indexes correctly', () => {
    const generator = new IndexGenerator();
    const indexes = generator.generate();
    expect(indexes).toStrictEqual([1, 2, 3, 4, 5, 6, 7, 8, 13, 16, 21, 32, 34, 64, 128]);
  });
});
