/* eslint-disable no-console */
const fs = require('fs');
const path = require('path');

function Packager(srcDir, prefix, outputTo) {
  const that = this;
  this.srcDir = srcDir || './src/';
  this.prefix = prefix || 'Pm';
  this.outputTo = outputTo || './predictive-memory.js';

  this.enter = '\n';

  that.files = [];

  that.globalCodeLines = ['/* eslint-disable no-use-before-define */'];
  that.functions = [];

  this.listFiles = (dir) => {
    const files = fs.readdirSync(dir);
    files.forEach((file) => {
      if (fs.statSync(`${dir}/${file}`).isDirectory()) {
        that.listFiles(`${dir}/${file}`);
      } else if (file.indexOf('.js') > 0) {
        that.files.push(path.join(dir, '/', file));
      }
    });
  };

  this.parseExport = (line) => {
    const exp = line.split('=')[1].trim();
    if (exp.indexOf('new ') === 0) {
      const funcName = exp.split(' ')[1].split('(')[0];
      const f = that.functions.filter((func) => func.name === funcName)[0];
      f.code = f.code.replace(`function ${funcName}`, `function _${funcName}`);
      f.initCode = `const ${that.prefix}${funcName} = ${exp.replace(funcName, `_${funcName}`)}`;
    }
  };

  this.parseCode = (lines) => {
    let funcName = '';
    let funcCode = '';
    let inFunc = false;
    let pLine = '';
    lines.forEach((line) => {
      if (line.indexOf('function ') === 0) {
        // eslint-disable-next-line prefer-destructuring
        funcName = line.split(' ')[1].split('(')[0];
        inFunc = true;
      } else if (inFunc && pLine === '}') {
        inFunc = false;
        funcCode = `// eslint-disable-next-line no-unused-vars, no-underscore-dangle${that.enter}${funcCode}`;
        that.functions.push({
          name: funcName,
          code: funcCode,
          initCode: '',
        });
        funcCode = '';
      }
      if (inFunc) {
        if (funcCode.length > 0) {
          funcCode += that.enter;
        }
        funcCode += line;
      } else if (line.indexOf('module.exports ') === 0) {
        that.parseExport(line);
      } else if (line.trim().length > 0 && line.indexOf('= require(') < 0) {
        that.globalCodeLines.push(line);
      }
      pLine = line;
    });
  };

  this.readFiles = () => {
    that.files.forEach((file) => {
      let code = fs.readFileSync(file);
      code = code.toString();
      if (code.indexOf('\r') > 0) {
        that.enter = '\r\n';
        code = code.replace(/\r/g, '');
      }
      const lines = code.split('\n');
      that.parseCode(lines);
    });
  };

  this.getPrefixedCode = (code, funcName) => {
    let r = code;
    r = r.replaceAll(`function ${funcName}`, `function ${that.prefix}${funcName}`);
    r = r.replaceAll(`new ${funcName}`, `new ${that.prefix}${funcName}`);
    r = r.replaceAll(`function _${funcName}`, `function _${that.prefix}${funcName}`);
    r = r.replaceAll(`new _${funcName}`, `new _${that.prefix}${funcName}`);
    r = r.replaceAll(` ${funcName}.`, ` ${that.prefix}${funcName}.`);
    r = r.replaceAll(`(${funcName}.`, `(${that.prefix}${funcName}.`);
    r = r.replaceAll(`[${funcName}.`, `[${that.prefix}${funcName}.`);
    return r;
  };

  this.applyPrefix = () => {
    const funcNames = that.functions.map((func) => func.name).sort().reverse();
    funcNames.forEach((funcName) => {
      that.functions.forEach((func) => {
        const f = func;
        f.code = that.getPrefixedCode(func.code, funcName);
        f.initCode = that.getPrefixedCode(func.initCode, funcName);
      });
    });
  };

  this.generateCode = () => {
    let code = '';
    that.globalCodeLines.forEach((line) => {
      if (code.length > 0) {
        code += that.enter;
      }
      code += line;
    });
    that.functions.forEach((func) => {
      if (code.length > 0) {
        code += that.enter;
      }
      code += that.enter;
      code += func.code;
      if (func.initCode) {
        code += `${that.enter}${func.initCode}`;
      }
    });
    code += that.enter;
    return code;
  };

  this.writeCode = (code) => {
    fs.writeFileSync(that.outputTo, code);
  };

  this.package = () => {
    that.listFiles(that.srcDir);
    that.readFiles();
    if (that.prefix) {
      that.applyPrefix();
    }
    const code = that.generateCode();
    that.writeCode(code);
    return that.files;
  };
}

const packager = new Packager();
console.log(`Packing ${packager.srcDir} into ${packager.outputTo} ...`);
const packaged = packager.package();
console.log(packaged);
