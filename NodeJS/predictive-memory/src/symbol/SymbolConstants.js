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

  this.ALPHABET_EXTENDED = 'üéâäàåçêëèïîìôöòûùÿ';
  this.CAPITALS_EXTENDED = 'ÇÄÅÉÖÜ';
  this.ALPHABET_REPLACEMENTS = 'ueaaaaceeeiiiooouuy';
  this.CAPITALS_REPLACEMENTS = 'CAAEOU';

  this.CHARACTERS = this.ALPHANUMERICS
      + this.ENDERS
      + this.SEPARATORS
      + this.BINDERS
      + this.SPECIALS
      + this.CONTROLS;
}
module.exports = new SymbolConstants();
