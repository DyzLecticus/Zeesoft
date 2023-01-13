function SymbolConstants() {
  this.ALPHABET = 'abcdefghijklmnopqrstuvwxyz';
  this.CAPITALS = this.ALPHABET.toUpperCase();
  this.NUMBERS = '0123456789';
  this.ALPHANUMERICS = this.ALPHABET + this.CAPITALS + this.NUMBERS;

  this.ENDERS = '.!?';
  this.SEPARATORS = ' -,/';
  this.BINDERS = '<>[]()\'"';
  this.SPECIALS = '@#$%^&*_+=|\\~';
  this.CONTROLS = '\r\n\t';

  this.NON_ALPHANUMERICS = this.ENDERS
    + this.SEPARATORS
    + this.BINDERS
    + this.SPECIALS
    + this.CONTROLS;

  this.ALPHABET_EXTENDED = 'üéâäàåçêëèïîìôöòûùÿ';
  this.CAPITALS_EXTENDED = 'ÇÄÅÉÖÜ';
  this.ALPHABET_REPLACEMENTS = 'ueaaaaceeeiiiooouuy';
  this.CAPITALS_REPLACEMENTS = 'CAAEOU';

  this.CHARACTERS = this.ALPHANUMERICS + this.NON_ALPHANUMERICS;

  this.CLASSIFIER_CHARACTERS = this.ALPHABET
    + this.NUMBERS
    + this.ENDERS
    + this.SEPARATORS
    + this.BINDERS;
}
module.exports = new SymbolConstants();
