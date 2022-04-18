function Extender() {
  this.copyInstanceProperties = (instance, target, prefix) => {
    const pfx = prefix || '';
    const keys = Object.keys(instance);
    const tgt = target;
    keys.forEach((key) => {
      const tgtKey = pfx ? `${pfx}${this.upperCaseFirst(key)}` : key;
      tgt[tgtKey] = instance[key];
    });
  };
  this.upperCaseFirst = (str) => (str.length > 0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : '');
}
module.exports = new Extender();
