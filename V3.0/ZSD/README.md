Zeesoft Smart Dialogs
=====================
The Zeesoft Smart Dialogs (ZSD) is an open source library for Java application development.  

It provides support for;  
 * Context sensitive symbolic corrections; word spelling corrections  
 * Sequence context classification; sentence context classification  
 * Context sensitive sequence matching; find matching sentences  
 * Symbolic entity value translation; find variable values  
 * Dialog response generation; create a smart and scalable chatbot  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSD/releases/zsd-0.9.0.zip) to download the latest ZSD release (version 0.9.0).  
All ZSD releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSD/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSD](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/ZSD.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsd.test.TestTsvToJson
---------------------------------
This test shows how to use a *TsvToJson* instance to convert a TSV file into a JSON file.
Both formats can be used to initialize *Analyzer* instances.

**Example implementation**  
~~~~
// Create the TsvToJson instance
TsvToJson convertor = new TsvToJson();
// Convert some TSV data
JsFile json = convertor.parseTsv(new ZStringBuilder("Question\tAnswer\tContext\nWhat?\tThat!\tWTF\n"));
~~~~

Class references;  
 * [TestTsvToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestTsvToJson.java)
 * [TsvToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/TsvToJson.java)
 * [Analyzer](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/sequence/Analyzer.java)

**Test output**  
The output of this test shows the input TSV data and the converted JSON.  
~~~~
Question	Answer	Context
What is that?	It is a plant!	contextWhatQuestions
Why is that?	Because it can!	contextWhyQuestions

{
  "sequences": [
    {
      "input": "What is that?",
      "output": "It is a plant!",
      "context": "contextWhatQuestions"
    },
    {
      "input": "Why is that?",
      "output": "Because it can!",
      "context": "contextWhyQuestions"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestSequencePreprocessor
--------------------------------------------
This test shows how to use a *SequencePreprocessor* instance preprocess a sequence.

**Example implementation**  
~~~~
// Create the preprocessor
SequencePreprocessor sp = new SequencePreprocessor();
// Initialize the preprocessor
sp.initialize();
// Use the processor to process a sequence
ZStringSymbolParser sequence sp.process(new ZStringSymbolParser("Symbol sequence"));
~~~~

Class references;  
 * [TestSequencePreprocessor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequencePreprocessor.java)
 * [SequencePreprocessor](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequencePreprocessor.java)

**Test output**  
The output of this test shows some sequences and their corresponding preprocessed form.  
~~~~
<- 'I'd have done it but it wouldn't have been fun'
-> 'I would have done it but it would not have been fun.'

<- 'Ik moet 's morgensvroeg opstaan'
-> 'Ik moet smorgens vroeg opstaan.'
~~~~

nl.zeesoft.zsd.test.TestSymbolCorrector
---------------------------------------
This test shows how to use the *SymbolCorrector* to correct word spelling.

**Example implementation**  
~~~~
// Create the SymbolCorrector
SymbolCorrector corrector = new SymbolCorrector();
// Initialize the SymbolCorrector
corrector.initialize(new ZStringSymbolParser("Some text containing correctly spelled words and complete meaningfull sentences."));
// Use SymbolCorrector to correct a word
String correction = corrector.correct("contaning");
~~~~

Class references;  
 * [TestSymbolCorrector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSymbolCorrector.java)
 * [SymbolCorrector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SymbolCorrector.java)

**Test output**  
The output of this test shows the number of generated variations and corrections for certain words.
~~~~
Initializing the SymbolCorrector took: 11 ms
Single variations for 'a'; 79
Single variations for 'an'; 133
Single variations for 'the'; 186
Single variations for 'piza'; 239
Double variations for 'stfu'; 5
Double variations for 'pizza'; 306

Correction for 'pizzas'; pizza
Correction for 'piza'; pizza
Correction for 'stuf'; stuff
Correction for 'staf'; stuff
Correction for 'coolset'; coolest
Correction for 'klevin'; kelvin
Correction for 'klevni'; kelvin
Correction for 'tenfold'; tenfold
Correction for 'maus'; mouse
Correction for 'excpton'; exception
Correction for 'amzaingg'; amazing
Correction for 'antidisestablishmantarianims'; antidisestablishmentarianism
Average correction time: 247 ms
~~~~

nl.zeesoft.zsd.test.TestSequenceClassifier
------------------------------------------
This test shows how to use the *SequenceClassifier* to classify a sequence.

**Example implementation**  
~~~~
// Create the SequenceClassifier
SequenceClassifier classifier = new SequenceClassifier();
// Initialize the SequenceClassifier
classifier.initialize("tab-separated-file-name.tsv");
// Use SequenceClassifier to classify a sequence
String context = classifier.classify(new ZStringSymbolParser("some sequence"));
~~~~

Class references;  
 * [TestSequenceClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceClassifier.java)
 * [SequenceClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequenceClassifier.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the classifier  
 * The classification results including the time it takes, for a set of input sequences  
~~~~
Initializing the SequenceClassifier took: 3577 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 2 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 2 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 9 ms

Context probabilities for 'Wat kost dat?', threshold: 0.5
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 1.0
'nlPrivatebankingUwvermogen': 0.028009059187044 / 0.6528488851714781
'nlPrivatebankingBetalen': 0.023943110276929132 / 0.5580777543239082
'nlPrivePensioen': 0.02363316176222444 / 0.5508533223665822
'nlPriveBeleggen': 0.022273672816980207 / 0.5191656874346602

Context probabilities for 'Wat kost dat?', threshold: 0.7
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 1.0
~~~~

nl.zeesoft.zsd.test.TestSequenceMatcher
---------------------------------------
This test shows how to use the *SequenceMatcher* to find a match for a sequence.

**Example implementation**  
~~~~
// Create the SequenceMatcher
SequenceMatcher matcher = new SequenceMatcher();
// Initialize the SequenceMatcher
matcher.initialize("tab-separated-file-name.tsv");
// Use SequenceMatcher to find a matching sequence
ZStringSymbolParserString match = matcher.match(new ZStringSymbolParser("some sequence"),"optionalContextSymbol");
~~~~

Class references;  
 * [TestSequenceMatcher](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceMatcher.java)
 * [SequenceMatcher](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequenceMatcher.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the classifier  
 * The matches found for a set of input sequences and how long it takes to find them  
~~~~
Initializing the SequenceMatcher took: 6931 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 24 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 42 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 35 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 55 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 51 ms

'Hypotheek berekenen' -> 'null'
Matching the input sequence took: 1 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 31 ms

'Fraude' -> 'En belangrijker: hoe kunt voorkomen slachtoffer te worden van CEO Fraude? [OUTPUT] Criminelen kunnen veel informatie over bedrijven op internet vinden. Check daarom regelmatig wat voor informatie u over uw bedrijf en de medewerkers online heeft staan. Maak het criminelen zo moeilijk mogelijk om online namen, functies en emailadressen te stelen.'
Matching the input sequence took: 12 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 12 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 52 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 100 ms

Matches for sequence: 'Wat kost dat?', threshold: 0.7
'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënter[ ...]': 0.1308245542490438 / 1.0
'Lees ook mijn blog Wat kost uw kind? [OUTPUT] Hiervoor zijn [ ...]': 0.11600853095056579 / 0.88674891052735
'Op de vraag 'Wat kost een kind? [OUTPUT] Twee kinderen koste[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost de LEI? [OUTPUT] Het jaar waarin u de LEI heeft aan[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een buitenlandse betaling? [OUTPUT] Bekijk het over[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een studie? [OUTPUT] De enige hulpmiddelen die de o[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een uitgebreid pensioenadvies? [OUTPUT] Voor een ui[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost een uitvaart? [OUTPUT] Elk jaar stuurt ABN AMRO Lev[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost het en waarop moet u letten? [OUTPUT] U wordt een d[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost het om ABN AMRO te bellen? [OUTPUT] Als u belt naar[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost het? [OUTPUT] Voor dit mandaat betaalt u kosten. De[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost studeren? [OUTPUT] Plus natuurlijk kosten voor bijv[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost uw kind en hoe zorgt u ervoor dat het later zijn of[ ...]': 0.11600853095056579 / 0.88674891052735
'Wat kost uw kind? [OUTPUT] Zo heeft u altijd overzicht, waar[ ...]': 0.11600853095056579 / 0.88674891052735

Matches for sequence: 'Wat kost dat?', threshold: 0.9
'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënter[ ...]': 0.1308245542490438 / 1.0
~~~~

nl.zeesoft.zsd.test.TestEntityValueTranslator
---------------------------------------------
This test shows how to use the *EntityValueTranslator* to translate a sequence to and from internal values.

**Example implementation**  
~~~~
// Create the EntityValueTranslator
EntityValueTranslator translator = new EntityValueTranslator();
// Initialize the EntityValueTranslator
translator.initialize();
// Use EntityValueTranslator to translate a sequence
ZStringSymbolParser translated = translator.translateToInternalValues(new ZStringSymbolParser("some sequence"));
ZStringSymbolParser retranslated = translator.translateToExternalValues(translated);
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestEntityValueTranslator.java)
 * [MockEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/MockEntityValueTranslator.java)
 * [EntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/EntityValueTranslator.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the translator  
 * The translation results including the time it takes, for a set of input sequences  
~~~~
Initializing the EntityValueTranslator took: 1876 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Eat EN_NUM:3|UN_ABC:three UN_ABC:donuts UN_ABC:at UN_TIM:09:00:00 UN_ABC:or UN_ABC:count UN_ABC:to UN_NUM:110'
Retranslating the sequence took: 0 ms
Retranslation: 'Eat three donuts at 09:00:00 or count to 110'

Sequence: 'Eet drie donuts om 9:00 of tel tot 110'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Eet NL_NUM:3|UN_ABC:drie UN_ABC:donuts UN_ABC:om UN_TIM:09:00:00 UN_ABC:of UN_ABC:tel UN_ABC:tot UN_NUM:110'
Retranslating the sequence took: 1 ms
Retranslation: 'Eet drie donuts om 09:00:00 of tel tot 110'

Sequence: 'I finished twohundredandtwentyfourth or 225th'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:I UN_ABC:finished EN_ORD:224|UN_ABC:twohundredandtwentyfourth UN_ABC:or EN_OR2:225'
Retranslating the sequence took: 0 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 0 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 0 ms
Translation: 'NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december'
Retranslating the sequence took: 1 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 0 ms
Translation: 'EN_DUR:33:41 UN_MTH:D NL_DUR:33:41'
Retranslating the sequence took: 0 ms
Retranslation: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'

Sequence: 'yesterday OR today OR the 1st of october'
Translating the sequence took: 2 ms
Translation: 'EN_DAT:2018-07-15|UN_ABC:yesterday UN_ABC:OR EN_DAT:2018-07-16|UN_ABC:today UN_ABC:OR EN_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen'

Sequence: 'gisteren OF vandaag OF 1 oktober'
Translating the sequence took: 1 ms
Translation: 'NL_DAT:2018-07-15|UN_ABC:gisteren UN_ABC:OF NL_DAT:2018-07-16|UN_ABC:vandaag UN_ABC:OF NL_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien'

Sequence: 'twelve o'clock OR five minutes to nine OR ten past one in the morning'
Translating the sequence took: 2 ms
Translation: 'EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 1 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 1 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:Andre|NL_NAM:firstName:UN_ABC:Andre NL_PRE:3|NL_NAM:preposition:NL_PRE:3 UN_ABC:Zee|NL_NAM:lastName:UN_ABC:Zee'
Retranslating the sequence took: 1 ms
Retranslation: 'Mijn naam is Andre van der Zee'

Sequence: 'Hoe heet jij? gekke henkie'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Hoe UN_ABC:heet UN_ABC:jij ? UN_ABC:gekke|NL_NAM:firstName:UN_ABC:Gekke UN_ABC:henkie|NL_NAM:lastName:UN_ABC:Henkie'
Retranslating the sequence took: 0 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies UN_ABC:is UN_ABC:mijn UN_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'to Germany or France'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France'
Retranslating the sequence took: 0 ms
Retranslation: 'to Germany or France'

Sequence: 'naar Duitsland of Frankrijk'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:naar NL_CNT:DE|UN_ABC:Duitsland UN_ABC:of NL_CNT:FR|UN_ABC:Frankrijk'
Retranslating the sequence took: 0 ms
Retranslation: 'naar Duitsland of Frankrijk'

Sequence: 'You asshole'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:You EN_PRF:1|UN_ABC:asshole'
Retranslating the sequence took: 0 ms
Retranslation: 'You asshole'

Sequence: 'Jij klootzak'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:5 UN_ABC:people ?'
Retranslating the sequence took: 0 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 1 ms
Translation: 'EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five'
Retranslating the sequence took: 0 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 0 ms
Translation: 'NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf'
Retranslating the sequence took: 0 ms
Retranslation: 'tien vermenigvuldigd met vijf'
~~~~

nl.zeesoft.zsd.test.TestBaseConfigurationToJson
-----------------------------------------------
This test shows how to convert a *BaseConfiguration* instance to JSON.

**Example implementation**  
~~~~
// Create the configuration
BaseConfiguration bc = new BaseConfiguration();
// Convert the configuration to JSON
JsFile json = bc.toJson();
~~~~

Class references;  
 * [TestBaseConfigurationToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestBaseConfigurationToJson.java)
 * [BaseConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/BaseConfiguration.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "name": "Test",
  "primaryLanguage": "EN",
  "debug": true,
  "dataDir": "testData/",
  "baseDir": "testBase/",
  "extendDir": "testExtend/",
  "overrideDir": "testOverride/",
  "generateReadFormat": true,
  "maxMsInterpretPerSymbol": 111,
  "maxMsInterpretPerSequence": 2222,
  "maxMsDialogPerSequence": 1111,
  "supportedLanguages": [
    {
      "code": "EN",
      "alphabet": "abcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Room"
      ]
    },
    {
      "code": "NL",
      "alphabet": "àáçèéêëïóöúüabcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Room"
      ]
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestEntityToJson
------------------------------------
This test shows how to use an *EntityToJson* instance to convert a list of entities into a JSON file.

**Example implementation**  
~~~~
// Create the translator
EntityValueTranslator translator = new EntityValueTranslator();
// Initialize the translator
translator.initialize();
// Create the EntityToJson instance
EntityToJson convertor = new EntityToJson();
// Convert the entities to JSON
JsFile json = convertor.getJsonForEntities(translator.getEntities(),"optionalContextSymbol");
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestEntityToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestEntityToJson.java)
 * [MockEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/MockEntityValueTranslator.java)
 * [EntityToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/EntityToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 333289 entity values took: 465 ms

Sample JSON;
{
  "sequences": [
    {
      "input": "My name is {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "I am {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "What is your name? {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "Who are you? {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "What is your name? My name is {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "Who are you? My name is {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "What is your name? I am {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "Who are you? I am {firstName} {preposition} {lastName}.",
      "context": "NAM"
    },
    {
      "input": "{firstName} {preposition} {lastName} is my name.",
      "context": "NAM"
    },
    {
      "input": "{firstName} {preposition} {lastName} am I.",
      "context": "NAM"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestDialogToJson
------------------------------------
This test shows how to use a *DialogToJson* instance to convert a list of dialogs into a JSON file.

**Example implementation**  
~~~~
// Create dialog set
DialogSet ds = new DialogSet();
// Initialize the dialog set
ds.initialize();
// Create the DialogToJson instance
DialogToJson convertor = new DialogToJson();
// Convert the dialogs to JSON
JsFile json = convertor.getJsonForDialogs(ds.getDialogs());
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestDialogToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogToJson.java)
 * [DialogToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/DialogToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 2858 dialog examples took: 282 ms

Sample JSON;
{
  "sequences": [
    {
      "input": "Hello.",
      "output": "Hello. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Hello!",
      "output": "Hello. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Hi.",
      "output": "Hi. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Hi!",
      "output": "Hi. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Good morning.",
      "output": "Good morning. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Good morning!",
      "output": "Good morning. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Good afternoon.",
      "output": "Good afternoon. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Good afternoon!",
      "output": "Good afternoon. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Good evening.",
      "output": "Good evening. My name is {selfName}.",
      "context": "EN"
    },
    {
      "input": "Good evening!",
      "output": "Good evening. My name is {selfName}.",
      "context": "EN"
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestDialogSetToJson
---------------------------------------
This test shows how to use a *DialogSetToJson* instance to convert a list of dialogs into a JSON file.

**Example implementation**  
~~~~
// Create the dialog set
DialogSet ds = new DialogSet();
// Initialize the dialog set
ds.initialize();
// Create the DialogToJson instance
DialogSetToJson convertor = new DialogSetToJson();
// Convert the english dialogs to JSON
JsFile json = convertor.getJsonForDialogs(ds,"Optional language code"));
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestDialogSetToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogSetToJson.java)
 * [DialogSetToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/DialogSetToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 7 dialogs took: 3 ms

Sample JSON;
{
  "dialogs": [
    {
      "language": "EN",
      "masterContext": "Generic",
      "context": "Handshake",
      "handler": "nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler",
      "examples": [
        {
          "input": "Hello.",
          "output": "Hello. My name is {selfName}."
        },
        {
          "input": "Hello!",
          "output": "Hello. My name is {selfName}."
        },
        {
          "input": "Hi.",
          "output": "Hi. My name is {selfName}."
        },
        {
          "input": "Hi!",
          "output": "Hi. My name is {selfName}."
        },
        {
          "input": "Good morning.",
          "output": "Good morning. My name is {selfName}."
        },
        {
          "input": "Good morning!",
          "output": "Good morning. My name is {selfName}."
        },
        {
          "input": "Good afternoon.",
          "output": "Good afternoon. My name is {selfName}."
        },
        {
          "input": "Good afternoon!",
          "output": "Good afternoon. My name is {selfName}."
        },
        {
          "input": "Good evening.",
          "output": "Good evening. My name is {selfName}."
        },
        {
          "input": "Good evening!",
          "output": "Good evening. My name is {selfName}."
        },
        {
          "input": "What is your name?",
          "output": "My name is {selfName}."
        },
        {
          "input": "Who are you?",
          "output": "My name is {selfName}."
        },
        {
          "input": "My name is {firstName} from {lastName}."
        },
        {
          "input": "I am {firstName} from the {lastName}."
        },
        {
          "input": "What is your name? {firstName} of {lastName}."
        },
        {
          "input": "Who are you? {firstName} of the {lastName}."
        },
        {
          "input": "What is your name? My name is {firstName} the {lastName}."
        },
        {
          "input": "Who are you? My name is {firstName} from {lastName}."
        },
        {
          "input": "What is your name? I am {firstName} from the {lastName}."
        },
        {
          "input": "Who are you? I am {firstName} of {lastName}."
        },
        {
          "input": "{firstName} of the {lastName} is my name."
        },
        {
          "input": "{firstName} the {lastName} am I."
        },
        {
          "input": "My name is {firstName} {lastName}."
        },
        {
          "input": "I am {firstName} {lastName}."
        },
        {
          "input": "What is your name? {firstName} {lastName}."
        },
        {
          "input": "Who are you? {firstName} {lastName}."
        },
        {
          "input": "What is your name? My name is {firstName} {lastName}."
        },
        {
          "input": "Who are you? My name is {firstName} {lastName}."
        },
        {
          "input": "What is your name? I am {firstName} {lastName}."
        },
        {
          "input": "Who are you? I am {firstName} {lastName}."
        },
        {
          "input": "{firstName} {lastName} is my name."
        },
        {
          "input": "{firstName} {lastName} am I."
        },
        {
          "input": "My name is {firstName}."
        },
        {
          "input": "I am {firstName}."
        },
        {
          "input": "What is your name? {firstName}."
        },
        {
          "input": "Who are you? {firstName}."
        },
        {
          "input": "What is your name? My name is {firstName}."
        },
        {
          "input": "Who are you? My name is {firstName}."
        },
        {
          "input": "What is your name? I am {firstName}."
        },
        {
          "input": "Who are you? I am {firstName}."
        },
        {
          "input": "{firstName} is my name."
        },
        {
          "input": "{firstName} am I."
        },
        {
          "input": "What is your firstname? {firstName}."
        },
        {
          "input": "What is your firstname? My firstname is {firstName}."
        },
        {
          "input": "{firstName} is my firstname."
        },
        {
          "input": "What is your lastname? from {lastName}."
        },
        {
          "input": "What is your lastname? My lastname is from the {lastName}."
        },
        {
          "input": "of {lastName} is my lastname."
        },
        {
          "input": "What is your lastname? {lastName}."
        },
        {
          "input": "What is your lastname? My lastname is {lastName}."
        },
        {
          "input": "{lastName} is my lastname."
        }
      ],
      "variables": [
        {
          "name": "firstName",
          "type": "ABC",
          "complexName": "firstName",
          "complexType": "NAM",
          "prompts": [
            "What is your name?",
            "What is your firstname?"
          ]
        },
        {
          "name": "lastName",
          "type": "ABC",
          "complexName": "lastName",
          "complexType": "NAM",
          "prompts": [
            "What is your lastname?"
          ]
        },
        {
          "name": "nextDialog",
          "type": "ABC",
          "prompts": [
            "What can I do for you {fullName}?",
            "How can I help you {fullName}?"
          ]
        },
        {
          "name": "preposition",
          "type": "PRE",
          "complexName": "preposition",
          "complexType": "NAM"
        }
      ]
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestRequestResponseToJson
---------------------------------------------
This test shows how to convert a *DialogRequest* and *DialogResponse* objects from and to JSON respectively.

**Example implementation**  
~~~~
// Create the dialog request
DialogRequest req = new DialogRequest();
// Create the JSON
JsFile json = new JsFile();
// Convert the dialog request from JSON
req.fromJson(json);
// Create the dialog response
DialogResponse res = new DialogResponse();
// Convert the dialog response to JSON
json = res.toJson();
~~~~

Class references;  
 * [TestRequestResponseToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestRequestResponseToJson.java)
 * [DialogRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogRequest.java)
 * [DialogResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogResponse.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
Dialog request JSON;
{
  "prompt": "test prompt?",
  "input": "test input.",
  "language": "EN",
  "masterContext": "Generic",
  "context": "Handshake",
  "appendDebugLog": true,
  "classifyLanguage": true,
  "correctInput": true,
  "classifyMasterContext": true,
  "classifyContext": true,
  "classifyContextThreshold": 0.8,
  "translateEntityValues": true,
  "translateEntityTypes": [
    "NAM"
  ],
  "matchThreshold": 0.8,
  "randomizeOutput": false,
  "dialogVariableValues": [
    {
      "name": "testVariable",
      "externalValue": "testExtVal",
      "internalValue": "testIntVal"
    }
  ]
}

Dialog response JSON;
{
  "responseLanguages": [
    {
      "symbol": "EN",
      "prob": 0.4,
      "probNormalized": 1.0
    },
    {
      "symbol": "NL",
      "prob": 0.2,
      "probNormalized": 0.5
    }
  ],
  "correctedInput": "Test corrected input.",
  "responseMasterContexts": [],
  "responseContexts": [],
  "entityValueTranslation": "Test entity value translation.",
  "entityValueTranslationCorrected": "Test corrected entity value translation.",
  "debugLog": "",
  "contextOutputs": [
    {
      "context": "testContext",
      "output": "Test output!",
      "prompt": "Test prompt!",
      "promptVariable": "testPromptVar",
      "dialogVariableValues": [
        {
          "name": "testVariable",
          "externalValue": "testExtVal",
          "internalValue": "testIntVal"
        }
      ]
    }
  ]
}
~~~~

nl.zeesoft.zsd.test.TestInitializer
-----------------------------------
This test shows how to use an *Initializer* instance to instantiate and initialize multiple classes simultaneously.

**Example implementation**  
~~~~
// Create the Initializer
Initializer init = new Initializer();
// Add a listener
init.addListener(this);
// Add a class to initialize
init.addClass("uniqueName",SequenceClassifier.class.getName(),"resources/nl-qna.txt");
// Start the initialization
init.start();
~~~~

Class references;  
 * [TestInitializer](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestInitializer.java)
 * [Initializer](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/initialize/Initializer.java)

**Test output**  
The output of this test shows the time it takes to initialize two objects simultaneously.  
~~~~
Initializing Translator took 2966 ms
Initializing Classifier took 5228 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestInterpreterConfiguration
------------------------------------------------
This test uses a lot of JSON files to initialize an *InterpreterConfiguration* instance.

**Example implementation**  
~~~~
// Create the InterpreterConfiguration
InterpreterConfiguration config = new InterpreterConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization
config.initialize();
~~~~

This test uses the *MockInterpreterConfiguration*.

Class references;  
 * [TestInterpreterConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestInterpreterConfiguration.java)
 * [InterpreterConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterConfiguration.java)

**Test output**  
The output of this test shows the time it takes to initialize all objects simultaneously.  
~~~~
Classes:
- nl.zeesoft.zsd.SequencePreprocessor (LanguagePreprocessor) <= resources/LanguagePreprocessor.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageClassifier) <= resources/LanguageClassifier.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextEN) <= resources/LanguageMasterContextEN.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENGeneric) <= resources/LanguageContextENGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENRoom) <= resources/LanguageContextENRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNL) <= resources/LanguageMasterContextNL.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLGeneric) <= resources/LanguageContextNLGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLRoom) <= resources/LanguageContextNLRoom.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)

Initializing LanguagePreprocessor took 65 ms
Initializing LanguageContextENRoom took 410 ms
Initializing LanguageContextNLRoom took 464 ms
Initializing LanguageContextENGeneric took 513 ms
Initializing LanguageContextNLGeneric took 845 ms
Initializing LanguageMasterContextEN took 950 ms
Initializing LanguageMasterContextNL took 1005 ms
Initializing EntityValueTranslator took 2183 ms
Initializing LanguageClassifier took 33361 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestLanguageClassifier
------------------------------------------
This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used as a language classifier.
~~~~
'Wat is your name?' (ENG) => 'What is your name?'
'Wat is your name?' (NLD) => 'Wat is jouw kamer?'

Classified sequence: 'Wie ben jij?' -> NL
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'Wie ben jij?' -> NL
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'what is name?' -> EN
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'twothousand' -> EN
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.3
'EN': 0.22592636334020982 / 1.0
'NL': 0.07742962626427816 / 0.3427206330395414

Context probabilities for 'wat is your name?', threshold: 0.7
'EN': 0.22592636334020982 / 1.0
~~~~

nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier
-------------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as master context classifiers.
~~~~
Classified sequence: 'Who are you?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Who are you?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Who are you?', threshold: 0.0
'Generic': 0.444554866310777 / 1.0
'Room': 0.09670530387277215 / 0.21753288784194286

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.25279810305193384 / 1.0
~~~~

nl.zeesoft.zsd.test.TestLanguageContextClassifier
-------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as context classifiers.
~~~~
Classified sequence: 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your goal?', threshold: 0.0
'QuestionAndAnswer': 0.5121450506920197 / 1.0
'Handshake': 0.20789649755214765 / 0.40593284514071576

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.33192974664786246 / 1.0
'Handshake': 0.1675886166877627 / 0.5048918284071541

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 1 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.46674840911703414 / 1.0
'QuestionAndAnswer': 0.23789070716419167 / 0.509676524906038

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 2 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.38004902824732023 / 1.0
'QuestionAndAnswer': 0.16155958698031891 / 0.4251019604638552

Classified sequence: 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.3659269698371966 / 1.0
'Handshake': 0.26522133831647887 / 0.7247930876329713
'Math': 0.21976353540027765 / 0.6005666526795004

Classified sequence: 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.23573145095505985 / 1.0
'Handshake': 0.2268834884826345 / 0.9624659228262582
'Language': 0.16349968711277804 / 0.6935845278615277
~~~~

nl.zeesoft.zsd.test.TestSequenceInterpreter
-------------------------------------------
This test show how to use a *SequenceInterpreter* to process an *InterpreterRequest*.

**Example implementation**  
~~~~
// Create the InterpreterConfiguration
InterpreterConfiguration config = new InterpreterConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization (and wait until it's done)
config.initialize();
// Create the sequence interpreter
SequenceInterpreter interpreter = new SequenceInterpreter(config);
// Create the interpreter request
InterpreterRequest request = new InterpreterRequest("The optional question that prompted the input","The input sequence");
// Use the interpreter to process the request
InterpreterResponse response = interpreter.handleInterpreterRequest(request);
~~~~

Class references;  
 * [TestSequenceInterpreter](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceInterpreter.java)
 * [InterpreterConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterConfiguration.java)
 * [InterpreterRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterRequest.java)
 * [InterpreterResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/InterpreterResponse.java)
 * [SequenceInterpreter](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/SequenceInterpreter.java)

**Test output**  
The output of this test shows several sequence interpreter requests and the debug log of corresponding responses.  
~~~~
Request prompt: '', input: 'Hallo.'
Response debug log;
21:08:36:233: Classify language for sequence: Hallo.
21:08:36:234: Classified language: NL
21:08:36:234: Translate sequence: Hallo.
21:08:36:234: Translated sequence: UN_ABC:Hallo .
21:08:36:234: Correction time limit: 200
21:08:36:234: Correcting sequence: Hallo.
21:08:36:234: Classify master context for sequence: Hallo.
21:08:36:234: Classified master context: Generic
21:08:36:234: Checking profanity for sequence: Hallo.
21:08:36:234: Classify context for sequence: Hallo.
21:08:36:234: Classified context: Handshake
Interpreting the request took: 2 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
21:08:36:234: Classify language for sequence: Mijn naam si gekste der henkies.
21:08:36:235: Classified language: NL
21:08:36:235: Translate sequence: Mijn naam si gekste der henkies.
21:08:36:238: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:08:36:238: Correction time limit: 700
21:08:36:238: Correcting sequence: Mijn naam si gekste der henkies.
21:08:36:703: Corrected sequence: Mijn naam is gekste der henkies.
21:08:36:703: Translate corrected sequence: Mijn naam is gekste der henkies.
21:08:36:704: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:08:36:704: Classify master context for sequence: Mijn naam is gekste der henkies.
21:08:36:705: Classified master context: Generic
21:08:36:705: Checking profanity for sequence: Mijn naam is gekste der henkies.
21:08:36:705: Classify context for sequence: Mijn naam is gekste der henkies.
21:08:36:705: Classified context: Handshake
Interpreting the request took: 471 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
21:08:36:705: Classify language for sequence: Albert einstein.
21:08:36:705: Classify language for sequence: What is your name? Albert einstein.
21:08:36:705: Classified language: EN
21:08:36:706: Translate sequence: What is your name? Albert einstein.
21:08:36:707: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:08:36:707: Correction time limit: 300
21:08:36:707: Correcting sequence: Albert einstein.
21:08:36:878: Classify master context for sequence: Albert einstein.
21:08:36:878: Classify master context for sequence: What is your name? Albert einstein.
21:08:36:878: Classified master context: Generic
21:08:36:878: Checking profanity for sequence: Albert einstein.
21:08:36:878: Classify context for sequence: Albert einstein.
21:08:36:878: Classify context for sequence: What is your name? Albert einstein.
21:08:36:879: Classified context: Handshake
Interpreting the request took: 174 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
21:08:36:879: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:08:36:879: Selected primary language: EN
21:08:36:879: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:08:36:880: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
21:08:36:881: Correction time limit: 1100
21:08:36:881: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:08:37:981: Classify master context for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1102 ms
~~~~

nl.zeesoft.zsd.test.TestDialogHandlerConfiguration
--------------------------------------------------
This test uses a lot of JSON files to initialize a *DialogHandlerConfiguration* instance.

**Example implementation**  
~~~~
// Create the DialogHandlerConfiguration
DialogHandlerConfiguration config = new DialogHandlerConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization
config.initialize();
~~~~

This test uses the *MockDialogHandlerConfiguration*.

Class references;  
 * [TestDialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogHandlerConfiguration.java)
 * [DialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerConfiguration.java)

**Test output**  
The output of this test shows the time it takes to initialize all remaining objects simultaneously.  
~~~~
Classes:
- nl.zeesoft.zsd.SequencePreprocessor (LanguagePreprocessor) <= resources/LanguagePreprocessor.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageClassifier) <= resources/LanguageClassifier.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextEN) <= resources/LanguageMasterContextEN.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENGeneric) <= resources/LanguageContextENGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENRoom) <= resources/LanguageContextENRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNL) <= resources/LanguageMasterContextNL.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLGeneric) <= resources/LanguageContextNLGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLRoom) <= resources/LanguageContextNLRoom.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)
- nl.zeesoft.zsd.dialog.DialogSet (DialogSet) <= resources/DialogSetEN.json, resources/DialogSetNL.json

Initializing DialogSet took 572 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestDialogHandler
-------------------------------------
This test show how to use a *DialogHandler* to process an *DialogRequest*.

**Example implementation**  
~~~~
// Create the DialogHandlerConfiguration
DialogHandlerConfiguration config = new DialogHandlerConfiguration();
// Add a listener
config.addListener(this);
// Start the initialization (and wait until it's done)
config.initialize();
// Create the dialog handler
DialogHandler handler = new DialogHandler(config);
// Create the handler request
DialogRequest request = new DialogRequest("The optional question that prompted the input","The input sequence");
// Use the handler to process the request
DialogResponse response = interpreter.handleDialogRequest(request);
~~~~

Class references;  
 * [TestDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogHandler.java)
 * [DialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerConfiguration.java)
 * [DialogRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogRequest.java)
 * [DialogResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogResponse.java)
 * [DialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/DialogHandler.java)

**Test output**  
The output of this test shows several dialog handler requests and the debug log of corresponding responses.  
~~~~
Request prompt: '', input: 'Hallo.'
Response debug log;
21:08:41:437: Classify language for sequence: Hallo.
21:08:41:437: Classified language: NL
21:08:41:437: Translate sequence: Hallo.
21:08:41:438: Translated sequence: UN_ABC:Hallo .
21:08:41:438: Correction time limit: 200
21:08:41:438: Correcting sequence: Hallo.
21:08:41:438: Classify master context for sequence: Hallo.
21:08:41:438: Classified master context: Generic
21:08:41:438: Checking profanity for sequence: Hallo.
21:08:41:438: Classify context for sequence: Hallo.
21:08:41:438: Classified context: Handshake
21:08:41:438: Handling dialog: NL/Generic/Handshake
21:08:41:439:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:08:41:439:     Found matches for sequence: 2
21:08:41:439:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.4276955602536998 / 1.0)
21:08:41:439:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.4276955602536998 / 1.0)
21:08:41:439:     Prompt variable: firstName
21:08:41:439:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
21:08:41:439:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 3 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
21:08:41:440: Classify language for sequence: Wie ben jij?
21:08:41:440: Classified language: NL
21:08:41:440: Translate sequence: Wie ben jij?
21:08:41:441: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
21:08:41:441: Correction time limit: 400
21:08:41:441: Correcting sequence: Wie ben jij?
21:08:41:441: Classify master context for sequence: Wie ben jij?
21:08:41:441: Classified master context: Generic
21:08:41:441: Checking profanity for sequence: Wie ben jij?
21:08:41:442: Classify context for sequence: Wie ben jij?
21:08:41:442: Classified context: Handshake
21:08:41:442: Handling dialog: NL/Generic/Handshake
21:08:41:442:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:08:41:442:     Found matches for sequence: 1
21:08:41:442:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (2.3026074700493306 / 1.0)
21:08:41:442:     Prompt variable: firstName
21:08:41:442:     Set dialog output: Mijn naam is Dyz Lecticus.
21:08:41:442:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 2 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
21:08:41:442: Classify language for sequence: Albert einstein.
21:08:41:442: Classify language for sequence: What is your name? Albert einstein.
21:08:41:443: Classified language: EN
21:08:41:446: Translate sequence: What is your name? Albert einstein.
21:08:41:448: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:08:41:448: Correction time limit: 300
21:08:41:448: Correcting sequence: Albert einstein.
21:08:41:621: Classify master context for sequence: Albert einstein.
21:08:41:621: Classify master context for sequence: What is your name? Albert einstein.
21:08:41:621: Classified master context: Generic
21:08:41:621: Checking profanity for sequence: Albert einstein.
21:08:41:622: Classify context for sequence: Albert einstein.
21:08:41:622: Classify context for sequence: What is your name? Albert einstein.
21:08:41:622: Classified context: Handshake
21:08:41:622: Handling dialog: EN/Generic/Handshake
21:08:41:622:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:08:41:622:     Updated variable firstName: UN_ABC:Albert = Albert
21:08:41:622:     Updated variable lastName: UN_ABC:Einstein = Einstein
21:08:41:622:     Failed to find matches for sequence: Albert einstein.
21:08:41:622:     Updated variable fullName: Albert Einstein
21:08:41:622:     Prompt variable: nextDialog
21:08:41:622:     Set dialog output: 
21:08:41:622:     Set dialog prompt: What can I do for you Albert Einstein?
21:08:41:622: Handling dialog: EN/Generic/QuestionAndAnswer
21:08:41:622:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:08:41:622:     Failed to find matches for sequence: Albert einstein.
Handling the request took: 180 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
21:08:41:623: Classify language for sequence: Mijn naam si gekste der henkies.
21:08:41:623: Classified language: NL
21:08:41:623: Translate sequence: Mijn naam si gekste der henkies.
21:08:41:624: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:08:41:624: Correction time limit: 700
21:08:41:624: Correcting sequence: Mijn naam si gekste der henkies.
21:08:42:043: Corrected sequence: Mijn naam is gekste der henkies.
21:08:42:043: Translate corrected sequence: Mijn naam is gekste der henkies.
21:08:42:045: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:08:42:045: Classify master context for sequence: Mijn naam is gekste der henkies.
21:08:42:045: Classified master context: Generic
21:08:42:045: Checking profanity for sequence: Mijn naam is gekste der henkies.
21:08:42:045: Classify context for sequence: Mijn naam is gekste der henkies.
21:08:42:045: Classified context: Handshake
21:08:42:046: Handling dialog: NL/Generic/Handshake
21:08:42:046:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:08:42:046:     Updated variable firstName: UN_ABC:Gekste = Gekste
21:08:42:046:     Updated variable lastName: UN_ABC:Henkies = Henkies
21:08:42:046:     Updated variable preposition: NL_PRE:6 = der
21:08:42:046:     Found matches for sequence: 18
21:08:42:046:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:046:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:047:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:047:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:047:     - Wie bent u? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:047:     - Hoe heet u? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:047:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:048:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:048:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:048:     - Hoe heet je? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:048:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:048:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.6433403805496829 / 1.0)
21:08:42:048:     Updated variable fullName: Gekste der Henkies
21:08:42:048:     Prompt variable: nextDialog
21:08:42:048:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
21:08:42:048:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 426 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
21:08:42:049: Classify language for sequence: Who created you?
21:08:42:049: Classified language: EN
21:08:42:049: Translate sequence: Who created you?
21:08:42:049: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
21:08:42:049: Correction time limit: 400
21:08:42:049: Correcting sequence: Who created you?
21:08:42:049: Classify master context for sequence: Who created you?
21:08:42:049: Classified master context: Generic
21:08:42:049: Checking profanity for sequence: Who created you?
21:08:42:049: Classify context for sequence: Who created you?
21:08:42:049: Classified context: QuestionAndAnswer
21:08:42:049: Handling dialog: EN/Generic/QuestionAndAnswer
21:08:42:049:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:08:42:049:     Found matches for sequence: 1
21:08:42:050:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.2964390448261416 / 1.0)
21:08:42:050:     Set dialog output: My software was written by André van der Zee.
Handling the request took: 77 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
21:08:42:125: Classify language for sequence: You asshole!
21:08:42:125: Classified language: EN
21:08:42:125: Translate sequence: You asshole!
21:08:42:125: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
21:08:42:125: Correction time limit: 300
21:08:42:125: Correcting sequence: You asshole!
21:08:42:125: Classify master context for sequence: You asshole!
21:08:42:125: Classified master context: Generic
21:08:42:126: Checking profanity for sequence: You asshole!
21:08:42:126: Detected profanity
21:08:42:126: Handling dialog: EN/Generic/Profanity
21:08:42:126:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:08:42:126:     Found matches for sequence: 2
21:08:42:126:     - Asshole! [OUTPUT] I do not appreciate that kind of language. (0.4194347319347319 / 1.0)
21:08:42:126:     - Asshole! [OUTPUT] I will pretend I did not read that. (0.4194347319347319 / 1.0)
21:08:42:126:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 1 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
21:08:42:126: Classify language for sequence: Jij klootzak!
21:08:42:126: Classified language: NL
21:08:42:127: Translate sequence: Jij klootzak!
21:08:42:127: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
21:08:42:127: Correction time limit: 300
21:08:42:127: Correcting sequence: Jij klootzak!
21:08:42:127: Classify master context for sequence: Jij klootzak!
21:08:42:127: Classified master context: Generic
21:08:42:127: Checking profanity for sequence: Jij klootzak!
21:08:42:128: Detected profanity
21:08:42:128: Handling dialog: NL/Generic/Profanity
21:08:42:128:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:08:42:128:     Found matches for sequence: 2
21:08:42:128:     - Klootzak! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.4437675070028011 / 1.0)
21:08:42:128:     - Klootzak! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.4437675070028011 / 1.0)
21:08:42:128:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 2 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
21:08:42:129: Classify language for sequence: Can I book a room for 5 people?
21:08:42:129: Classified language: EN
21:08:42:129: Translate sequence: Can I book a room for 5 people?
21:08:42:131: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:5 UN_ABC:people ?
21:08:42:131: Correction time limit: 900
21:08:42:132: Correcting sequence: Can I book a room for 5 people?
21:08:42:132: Classify master context for sequence: Can I book a room for 5 people?
21:08:42:132: Classified master context: Room
21:08:42:132: Checking profanity for sequence: Can I book a room for 5 people?
21:08:42:132: Classify context for sequence: Can I book a room for 5 people?
21:08:42:132: Classified context: Booking
21:08:42:132: Handling dialog: EN/Room/Booking
21:08:42:133:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:08:42:133:     Updated variable bookPeople: UN_NUM:5 = 5
21:08:42:138:     Found matches for sequence: 8
21:08:42:138:     - Can I book a room for {bookPeople} people? [OUTPUT] Okay. (3.5460670713700804 / 1.0)
21:08:42:138:     - Can I book a room for a duration of {bookDuration} for {bookPeople} people? [OUTPUT] Okay. (3.5460670713700804 / 1.0)
21:08:42:138:     - Can I book a room for {bookPeople} people on {bookDate}? [OUTPUT] Okay. (2.4972185286695074 / 0.7042220235571197)
21:08:42:138:     - Can I book a room for {bookPeople} people from {bookTime}? [OUTPUT] Okay. (2.4972185286695074 / 0.7042220235571197)
21:08:42:138:     - Can I book a room for {bookPeople} people for a duration of {bookDuration}? [OUTPUT] Okay. (2.4972185286695074 / 0.7042220235571197)
21:08:42:138:     - Can I book a room for a duration of {bookDuration}? [OUTPUT] Okay. (2.4972185286695074 / 0.7042220235571197)
21:08:42:138:     - Can I book a room for a duration of {bookDuration} on {bookDate}? [OUTPUT] Okay. (2.4972185286695074 / 0.7042220235571197)
21:08:42:139:     - Can I book a room for a duration of {bookDuration} from {bookTime}? [OUTPUT] Okay. (2.4972185286695074 / 0.7042220235571197)
21:08:42:139:     Prompt variable: bookDate
21:08:42:139:     Set dialog output: Okay.
21:08:42:139:     Set dialog prompt: On what date?
Handling the request took: 97 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
21:08:42:226: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:08:42:227: Classified language: EN
21:08:42:227: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:08:42:230: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
21:08:42:230: Correction time limit: 1332
21:08:42:230: Correcting sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:08:42:230: Classify master context for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:08:42:231: Classified master context: Room
21:08:42:231: Checking profanity for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:08:42:231: Classify context for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:08:42:232: Classified context: Booking
21:08:42:232: Handling dialog: EN/Room/Booking
21:08:42:232:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:08:42:232:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
21:08:42:232:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
21:08:42:232:     Updated variable bookDuration: EN_DUR:02:00 = two hours
21:08:42:232:     Updated variable bookPeople: UN_NUM:12 = 12
21:08:42:239:     Found matches for sequence: 8
21:08:42:239:     - Can I book a room for {bookPeople} people on {bookDate}? [OUTPUT] Okay. (3.7233046593036825 / 1.0)
21:08:42:239:     - Can I book a room for {bookPeople} people for a duration of {bookDuration}? [OUTPUT] Okay. (2.9526463484908225 / 0.7930176600275881)
21:08:42:239:     - Can I book a room for a duration of {bookDuration} for {bookPeople} people? [OUTPUT] Okay. (2.9526463484908225 / 0.7930176600275881)
21:08:42:239:     - Can I book a room for a duration of {bookDuration}? [OUTPUT] Okay. (2.8101719643652423 / 0.7547520875959662)
21:08:42:239:     - Can I book a room for a duration of {bookDuration} on {bookDate}? [OUTPUT] Okay. (2.8101719643652423 / 0.7547520875959662)
21:08:42:239:     - Can I book a room for a duration of {bookDuration} from {bookTime}? [OUTPUT] Okay. (2.8101719643652423 / 0.7547520875959662)
21:08:42:239:     - Can I book a room for {bookPeople} people? [OUTPUT] Okay. (2.6396929127950877 / 0.7089650604333712)
21:08:42:239:     - Can I book a room for {bookPeople} people from {bookTime}? [OUTPUT] Okay. (2.6396929127950877 / 0.7089650604333712)
21:08:42:239:     Prompt variable: bookConfirmation
21:08:42:240:     Set dialog output: Okay.
21:08:42:240:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 15 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
21:08:42:240: Classify language for sequence: What does it cost to book a room?
21:08:42:241: Classified language: EN
21:08:42:241: Translate sequence: What does it cost to book a room?
21:08:42:242: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
21:08:42:242: Correction time limit: 900
21:08:42:242: Correcting sequence: What does it cost to book a room?
21:08:42:242: Classify master context for sequence: What does it cost to book a room?
21:08:42:243: Classified master context: Room
21:08:42:243: Classify context for sequence: What does it cost to book a room?
21:08:42:243: Classified context: QuestionAndAnswer
21:08:42:243: Handling dialog: EN/Room/QuestionAndAnswer
21:08:42:243:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:08:42:243:     Found matches for sequence: 1
21:08:42:243:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (5.3062499999999995 / 1.0)
21:08:42:243:     Set dialog output: There are no costs attached to booking a room.
Handling the request took: 85 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
21:08:42:325: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:326: Classified language: EN
21:08:42:326: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:328: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three|EN_NAM:firstName:UN_ABC:Three EN_MTH:M|UN_ABC:times|EN_NAM:lastName:UN_ABC:Times EN_NUM:6|UN_ABC:six ?
21:08:42:328: Correction time limit: 1332
21:08:42:328: Correcting sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:328: Classify master context for sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:329: Classified master context: Generic
21:08:42:329: Checking profanity for sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:329: Classify context for sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:329: Classified context: Math
21:08:42:329: Handling dialog: EN/Generic/Math
21:08:42:330:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
21:08:42:330:     Updated variable number1: EN_NUM:10 = ten
21:08:42:330:     Updated variable number2: EN_NUM:40 = fourty
21:08:42:330:     Updated variable number3: EN_NUM:20 = twenty
21:08:42:330:     Updated variable number4: EN_NUM:3 = three
21:08:42:330:     Updated variable number5: EN_NUM:6 = six
21:08:42:330:     Updated variable operator1: EN_MTH:M = multiplied by
21:08:42:330:     Updated variable operator2: EN_MTH:D = divided by
21:08:42:330:     Updated variable operator3: EN_MTH:A = plus
21:08:42:330:     Updated variable operator4: EN_MTH:M = multiplied by
21:08:42:330:     Failed to find matches for sequence: How much is ten times fourty divided by twenty plus three times six?
21:08:42:330:     Calculate expression: 10 M 40 D 20 A 3 M 6
21:08:42:331:     Calculated expression: 38.0
21:08:42:331:     Updated variable exact: exactly
21:08:42:331:     Updated variable result: thirtyeight
21:08:42:331:     Prompt variable: nextDialog
21:08:42:331:     Set dialog output: Exactly thirtyeight.
21:08:42:331:     Set dialog prompt: What else can I do for you?
Handling the request took: 6 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
21:08:42:332: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:332: Classified language: NL
21:08:42:332: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:338: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie|NL_NAM:firstName:UN_ABC:Drie NL_MTH:M|UN_ABC:keer|NL_NAM:lastName:UN_ABC:Keer NL_NUM:6|UN_ABC:zes ?
21:08:42:338: Correction time limit: 1300
21:08:42:338: Correcting sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:338: Classify master context for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:338: Classified master context: Generic
21:08:42:338: Checking profanity for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:339: Classify context for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:339: Classified context: Math
21:08:42:339: Handling dialog: NL/Generic/Math
21:08:42:339:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
21:08:42:340:     Updated variable number1: NL_NUM:10 = tien
21:08:42:340:     Updated variable number2: NL_NUM:40 = veertig
21:08:42:340:     Updated variable number3: NL_NUM:20 = twintig
21:08:42:340:     Updated variable number4: NL_NUM:3 = drie
21:08:42:340:     Updated variable number5: NL_NUM:6 = zes
21:08:42:340:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
21:08:42:340:     Updated variable operator2: NL_MTH:D = gedeeld door
21:08:42:340:     Updated variable operator3: NL_MTH:A = plus
21:08:42:340:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
21:08:42:340:     Failed to find matches for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:08:42:340:     Calculate expression: 10 M 40 D 20 A 3 M 6
21:08:42:340:     Calculated expression: 38.0
21:08:42:341:     Updated variable exact: precies
21:08:42:341:     Updated variable result: achtendertig
21:08:42:341:     Prompt variable: nextDialog
21:08:42:341:     Set dialog output: Precies achtendertig.
21:08:42:341:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 94 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
21:08:42:425: Classify language for sequence: What languages do you speak?
21:08:42:425: Classified language: EN
21:08:42:425: Translate sequence: What languages do you speak?
21:08:42:427: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
21:08:42:427: Correction time limit: 600
21:08:42:427: Correcting sequence: What languages do you speak?
21:08:42:427: Classify master context for sequence: What languages do you speak?
21:08:42:427: Classified master context: Generic
21:08:42:427: Checking profanity for sequence: What languages do you speak?
21:08:42:427: Classify context for sequence: What languages do you speak?
21:08:42:427: Classified context: Language
21:08:42:427: Handling dialog: EN/Generic/Language
21:08:42:427:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
21:08:42:428:     Updated variable languages: English and Dutch
21:08:42:429:     Found matches for sequence: 1
21:08:42:430:     - What languages do you know? [OUTPUT] I speak {languages}. (1.9472233234526697 / 1.0)
21:08:42:430:     Prompt variable: language
21:08:42:430:     Set dialog output: I speak English and Dutch.
Handling the request took: 5 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
21:08:42:430: Classify language for sequence: Spreek je duits?
21:08:42:430: Classified language: NL
21:08:42:430: Translate sequence: Spreek je duits?
21:08:42:432: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
21:08:42:432: Correction time limit: 400
21:08:42:432: Correcting sequence: Spreek je duits?
21:08:42:432: Classify master context for sequence: Spreek je duits?
21:08:42:432: Classified master context: Generic
21:08:42:432: Checking profanity for sequence: Spreek je duits?
21:08:42:432: Classify context for sequence: Spreek je duits?
21:08:42:432: Classified context: Language
21:08:42:432: Handling dialog: NL/Generic/Language
21:08:42:432:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:08:42:432:     Updated variable language: NL_LNG:DE = Duits
21:08:42:432:     Updated variable languages: Engels en Nederlands
21:08:42:432:     Updated variable confirmation: Nee
21:08:42:434:     Found matches for sequence: 1
21:08:42:434:     - Spreek je duits? [OUTPUT] {confirmation}, ik spreek {languages}. (2.6452526077362033 / 1.0)
21:08:42:434:     Prompt variable: 
21:08:42:434:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 4 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
21:08:42:434: Classify language for sequence: Spreek je engels?
21:08:42:434: Classified language: NL
21:08:42:434: Translate sequence: Spreek je engels?
21:08:42:436: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
21:08:42:436: Correction time limit: 400
21:08:42:436: Correcting sequence: Spreek je engels?
21:08:42:436: Classify master context for sequence: Spreek je engels?
21:08:42:436: Classified master context: Generic
21:08:42:436: Checking profanity for sequence: Spreek je engels?
21:08:42:436: Classify context for sequence: Spreek je engels?
21:08:42:436: Classified context: Language
21:08:42:436: Handling dialog: NL/Generic/Language
21:08:42:437:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:08:42:437:     Updated variable language: NL_LNG:EN = Engels
21:08:42:437:     Updated variable languages: Engels en Nederlands
21:08:42:437:     Updated variable confirmation: Ja
21:08:42:438:     Found matches for sequence: 1
21:08:42:438:     - Spreek je engels? [OUTPUT] {confirmation}, ik spreek {languages}. (2.6452526077362033 / 1.0)
21:08:42:438:     Prompt variable: 
21:08:42:438:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 4 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (232 assertions).  
Total test duration: 71765 ms (total sleep duration: 30800 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 611 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 424 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 456 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 462 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 471 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117518 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117530 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117528 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 117568 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117571 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117607 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117585 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 548785 Kb / 535 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 548786 Kb / 535 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 548787 Kb / 535 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 548787 Kb / 535 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 548795 Kb / 535 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 556511 Kb / 543 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 556523 Kb / 543 Mb
