const SymbolUtil = require('../../../src/symbol/SymbolUtil');
const SymbolConstants = require('../../../src/symbol/SymbolConstants');

const MathUtil = require('../../../src/MathUtil');

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
  });

  test('Generates numArrays correctly', () => {
    const a = SymbolUtil.generateNumArray(SymbolConstants.CHARACTERS, SymbolConstants.CHARACTERS);
    expect(a.length).toBe(279);
    expect(MathUtil.stringify(a)).toBe('279,0=1,1=2,2=3,3=4,4=5,5=6,6=7,7=8,8=9,9=10,10=11,11=12,12=13,13=14,14=15,15=16,16=17,17=18,18=19,19=20,20=21,21=22,22=23,23=24,24=25,25=26,26=27,27=28,28=29,29=30,30=31,31=32,32=33,33=34,34=35,35=36,36=37,37=38,38=39,39=40,40=41,41=42,42=43,43=44,44=45,45=46,46=47,47=48,48=49,49=50,50=51,51=52,52=53,53=54,54=55,55=56,56=57,57=58,58=59,59=60,60=61,61=62,62=63,63=64,64=65,65=66,66=67,67=68,68=69,69=70,70=71,71=72,72=73,73=74,74=75,75=76,76=77,77=78,78=79,79=80,80=81,81=82,82=83,83=84,84=85,85=86,86=87,87=88,88=89,89=90,90=91,91=92,92=93,93=2,94=3,95=4,96=5,97=6,98=7,99=8,100=9,101=10,102=11,103=12,104=13,105=14,106=15,107=16,108=17,109=18,110=19,111=20,112=21,113=22,114=23,115=24,116=25,117=26,118=27,119=28,120=29,121=30,122=31,123=32,124=33,125=34,126=35,127=36,128=37,129=38,130=39,131=40,132=41,133=42,134=43,135=44,136=45,137=46,138=47,139=48,140=49,141=50,142=51,143=52,144=53,145=54,146=55,147=56,148=57,149=58,150=59,151=60,152=61,153=62,154=63,155=64,156=65,157=66,158=67,159=68,160=69,161=70,162=71,163=72,164=73,165=74,166=75,167=76,168=77,169=78,170=79,171=80,172=81,173=82,174=83,175=84,176=85,177=86,178=87,179=88,180=89,181=90,182=91,183=92,184=93,187=1,188=2,189=3,190=4,191=5,192=6,193=7,194=8,195=9,196=10,197=11,198=12,199=13,200=14,201=15,202=16,203=17,204=18,205=19,206=20,207=21,208=22,209=23,210=24,211=25,212=26,213=27,214=28,215=29,216=30,217=31,218=32,219=33,220=34,221=35,222=36,223=37,224=38,225=39,226=40,227=41,228=42,229=43,230=44,231=45,232=46,233=47,234=48,235=49,236=50,237=51,238=52,239=53,240=54,241=55,242=56,243=57,244=58,245=59,246=60,247=61,248=62,249=63,250=64,251=65,252=66,253=67,254=68,255=69,256=70,257=71,258=72,259=73,260=74,261=75,262=76,263=77,264=78,265=79,266=80,267=81,268=82,269=83,270=84,271=85,272=86,273=87,274=88,275=89,276=90,277=91,278=92');
  });
});
