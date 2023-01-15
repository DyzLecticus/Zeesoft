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
    expect(a.length).toBe(373);
    expect(MathUtil.stringify(a)).toBe('373,0=93,1=1,2=2,3=3,4=4,5=5,6=6,7=7,8=8,9=9,10=10,11=11,12=12,13=13,14=14,15=15,16=16,17=17,18=18,19=19,20=20,21=21,22=22,23=23,24=24,25=25,26=26,27=27,28=28,29=29,30=30,31=31,32=32,33=33,34=34,35=35,36=36,37=37,38=38,39=39,40=40,41=41,42=42,43=43,44=44,45=45,46=46,47=47,48=48,49=49,50=50,51=51,52=52,53=53,54=54,55=55,56=56,57=57,58=58,59=59,60=60,61=61,62=62,63=63,64=64,65=65,66=66,67=67,68=68,69=69,70=70,71=71,72=72,73=73,74=74,75=75,76=76,77=77,78=78,79=79,80=80,81=81,82=82,83=83,84=84,85=85,86=86,87=87,88=88,89=89,90=90,91=91,92=92,93=93,94=1,95=1,96=1,97=1,98=1,99=1,100=1,101=1,102=1,103=1,104=1,105=1,106=1,107=1,108=1,109=1,110=1,111=1,112=1,113=1,114=1,115=1,116=1,117=1,118=1,119=1,120=1,121=1,122=1,123=1,124=1,125=1,126=1,127=1,128=1,129=1,130=1,131=1,132=1,133=1,134=1,135=1,136=1,137=1,138=1,139=1,140=1,141=1,142=1,143=1,144=1,145=1,146=1,147=1,148=1,149=1,150=1,151=1,152=1,153=1,154=1,155=1,156=1,157=1,158=1,159=1,160=1,161=1,162=1,163=1,164=1,165=1,166=1,167=1,168=1,169=1,170=1,171=1,172=1,173=1,174=1,175=1,176=1,177=1,178=1,179=1,180=1,181=1,182=1,183=1,184=1,185=1,186=1,187=2,188=3,189=4,190=5,191=6,192=7,193=8,194=9,195=10,196=11,197=12,198=13,199=14,200=15,201=16,202=17,203=18,204=19,205=20,206=21,207=22,208=23,209=24,210=25,211=26,212=27,213=28,214=29,215=30,216=31,217=32,218=33,219=34,220=35,221=36,222=37,223=38,224=39,225=40,226=41,227=42,228=43,229=44,230=45,231=46,232=47,233=48,234=49,235=50,236=51,237=52,238=53,239=54,240=55,241=56,242=57,243=58,244=59,245=60,246=61,247=62,248=63,249=64,250=65,251=66,252=67,253=68,254=69,255=70,256=71,257=72,258=73,259=74,260=75,261=76,262=77,263=78,264=79,265=80,266=81,267=82,268=83,269=84,270=85,271=86,272=87,273=88,274=89,275=90,276=91,277=92,278=93,281=1,282=2,283=3,284=4,285=5,286=6,287=7,288=8,289=9,290=10,291=11,292=12,293=13,294=14,295=15,296=16,297=17,298=18,299=19,300=20,301=21,302=22,303=23,304=24,305=25,306=26,307=27,308=28,309=29,310=30,311=31,312=32,313=33,314=34,315=35,316=36,317=37,318=38,319=39,320=40,321=41,322=42,323=43,324=44,325=45,326=46,327=47,328=48,329=49,330=50,331=51,332=52,333=53,334=54,335=55,336=56,337=57,338=58,339=59,340=60,341=61,342=62,343=63,344=64,345=65,346=66,347=67,348=68,349=69,350=70,351=71,352=72,353=73,354=74,355=75,356=76,357=77,358=78,359=79,360=80,361=81,362=82,363=83,364=84,365=85,366=86,367=87,368=88,369=89,370=90,371=91,372=92');
  });
});
