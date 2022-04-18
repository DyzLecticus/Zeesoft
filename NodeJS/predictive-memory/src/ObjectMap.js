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
    Object.keys(this.map).forEach((key) => { func(this.get(key)); });
  };
}
module.exports = ObjectMap;
