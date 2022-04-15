const Extender = require('../../src/Extender');

describe('Extender', () => {
  test('Copies properties correctly', () => {
    const instance = {
      prop: 'pizza',
      func() {
        return 'beer';
      },
    };
    const target = {};
    Extender.copyInstanceProperties(instance, target);
    expect(target.prop).toBe('pizza');
    expect(target.func()).toBe('beer');

    Extender.copyInstanceProperties(instance, target, 'unique');
    expect(target.uniqueProp).toBe('pizza');
    expect(target.uniqueFunc()).toBe('beer');

    expect(Extender.upperCaseFirst('')).toBe('');
  });
});
