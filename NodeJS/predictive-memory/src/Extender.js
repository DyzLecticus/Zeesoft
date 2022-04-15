function Extender() {
  this.copyInstanceProperties = (instance, target, prefix) => {
    const pfx = prefix || '';
    const keys = Object.keys(instance);
    const tgt = target;
    for (let i = 0; i < keys.length; i += 1) {
      const tgtKey = pfx ? `${pfx}${this.upperCaseFirst(keys[i])}` : keys[i];
      tgt[tgtKey] = instance[keys[i]];
    }
  };
  this.upperCaseFirst = (str) => (str.length > 0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : '');
}
module.exports = new Extender();
