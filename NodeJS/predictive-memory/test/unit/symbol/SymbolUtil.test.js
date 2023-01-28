const SymbolUtil = require('../../../src/symbol/SymbolUtil');
const SymbolConstants = require('../../../src/symbol/SymbolConstants');

describe('SymbolUtil', () => {
  test('Formats strings correctly', () => {
    let s = SymbolUtil.format(` ${SymbolConstants.ALPHABET_EXTENDED}`);
    expect(s).toBe(SymbolConstants.ALPHABET_REPLACEMENTS);
    s = SymbolUtil.format(SymbolConstants.CAPITALS_EXTENDED);
    expect(s).toBe(SymbolConstants.CAPITALS_REPLACEMENTS);
  });

  test('Formats strings for tokenization correctly', () => {
    let s = SymbolUtil.tokenizeFormat('What is  your @name?');
    expect(s).toBe('what is your name ?');
    const characters = SymbolConstants.CAPITALS + SymbolConstants.CLASSIFIER_CHARACTERS;
    s = SymbolUtil.tokenizeFormat('What is  your @name?', characters);
    expect(s).toBe('What is your name ?');
    s = SymbolUtil.tokenizeFormat('This thing consist of something,something else (with an example), and another thing.');
    expect(s).toBe('this thing consist of something , something else ( with an example ) , and another thing .');
  });

  test('Tokenizes strings correctly', () => {
    const tokens = SymbolUtil.tokenize('What is your  name?');
    expect(tokens.length).toBe(5);
    expect(tokens[0]).toBe('what');
    expect(tokens[4]).toBe('?');
  });

  test('Parses sentences correctly', () => {
    let sentences = SymbolUtil.parseSentences('Question?');
    expect(sentences.length).toBe(1);
    expect(sentences[0]).toBe('question ?');
    sentences = SymbolUtil.parseSentences('Sentence number one. Sentence number two! Sentence number three');
    expect(sentences.length).toBe(3);
    expect(sentences[0]).toBe('sentence number one .');
    expect(sentences[1]).toBe('sentence number two !');
    expect(sentences[2]).toBe('sentence number three');
  });

  test('Sequentializes strings correctly', () => {
    let sequences = SymbolUtil.sequentialize('This thing consist of something,something else (with an example), and another thing.');
    expect(sequences.length).toBe(5);
    expect(sequences[0]).toBe('this thing consist of something , something else');
    expect(sequences[4]).toBe('thing .');
    sequences = SymbolUtil.sequentialize('What is your  name?');
    expect(sequences.length).toBe(1);
    expect(sequences[0]).toBe('what is your name ?');
    sequences = SymbolUtil.sequentialize('This string/sentence has exactly nine tokens.');
    expect(sequences.length).toBe(2);
    expect(sequences[0]).toBe('this string / sentence has exactly nine tokens');
    expect(sequences[1]).toBe('has exactly nine tokens .');
    sequences = SymbolUtil.sequentialize('Narrow sequentialization.', 4, 1);
    expect(sequences.length).toBe(2);
    expect(sequences[0]).toBe('narrow sequentialization .');
    expect(sequences[1]).toBe('sequentialization .');
    sequences = SymbolUtil.sequentialize('Single');
    expect(sequences.length).toBe(1);
    expect(sequences[0]).toBe('single');
  });
});
