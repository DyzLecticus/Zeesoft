function ObjectMap() {
  this.map = {};

  this.put = (name, obj) => {
    this.map[name] = obj;
  };

  this.get = (name) => this.map[name];

  this.remove = (name) => {
    delete this.map[name];
  };

  this.forEach = (func) => {
    const keys = Object.keys(this.map);
    for (let i = 0; i < keys.length; i += 1) {
      func(this.map[keys[i]]);
    }
  };
}
module.exports = ObjectMap;
