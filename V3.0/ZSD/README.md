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
Initializing the SymbolCorrector took: 7 ms
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
Average correction time: 239 ms
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
Initializing the SequenceClassifier took: 3335 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 3 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 2 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 10 ms

Context probabilities for 'Wat kost dat?', threshold: 0.3
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 0.7256006222836385
'nlPrivatebankingUwvermogen': 0.028009059187044 / 0.47370755733760417
'nlPrivatebankingBetalen': 0.023943110276929132 / 0.4049415658200833
'nlPrivePensioen': 0.02363316176222444 / 0.39969951349620175
'nlPriveBeleggen': 0.022273672816980207 / 0.3767069458709024
'nlPriveBetalen': 0.020269754198164815 / 0.3428153614487636
'nlZakelijkOndernemen': 0.018245720827610022 / 0.3085835831682987

Context probabilities for 'Wat kost dat?', threshold: 0.7
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 0.7256006222836385
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
Initializing the SequenceMatcher took: 6493 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 19 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 47 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 36 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 62 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 43 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 11 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 27 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 8 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 14 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 42 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 307 ms

Matches for sequence: 'Wat kost dat?', threshold: 0.165
'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënter[ ...]': 0.03920302825787142 / 0.3315136476426799
'Lees ook mijn blog Wat kost uw kind? [OUTPUT] Hiervoor zijn [ ...]': 0.019577061142247915 / 0.16555004135649296
'Op de vraag 'Wat kost een kind? [OUTPUT] Twee kinderen koste[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost de LEI? [OUTPUT] Het jaar waarin u de LEI heeft aan[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost een buitenlandse betaling? [OUTPUT] Bekijk het over[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost een studie? [OUTPUT] De enige hulpmiddelen die de o[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost een uitgebreid pensioenadvies? [OUTPUT] Voor een ui[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost een uitvaart? [OUTPUT] Elk jaar stuurt ABN AMRO Lev[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost het en waarop moet u letten? [OUTPUT] U wordt een d[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost het om ABN AMRO te bellen? [OUTPUT] Als u belt naar[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost het? [OUTPUT] Voor dit mandaat betaalt u kosten. De[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost studeren? [OUTPUT] Plus natuurlijk kosten voor bijv[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost uw kind en hoe zorgt u ervoor dat het later zijn of[ ...]': 0.019577061142247915 / 0.16555004135649296
'Wat kost uw kind? [OUTPUT] Zo heeft u altijd overzicht, waar[ ...]': 0.019577061142247915 / 0.16555004135649296

Matches for sequence: 'Wat kost dat?', threshold: 0.3
'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënter[ ...]': 0.03920302825787142 / 0.3315136476426799
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
Initializing the EntityValueTranslator took: 1783 ms

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
Translating the sequence took: 2 ms
Translation: 'UN_ABC:I UN_ABC:finished EN_ORD:224|UN_ABC:twohundredandtwentyfourth|EN_NAM:firstName:UN_ABC:Twohundredandtwentyfourth UN_ABC:or|EN_NAM:lastName:UN_ABC:Or EN_OR2:225'
Retranslating the sequence took: 1 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 0 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 1 ms
Translation: 'NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december'
Retranslating the sequence took: 0 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 0 ms
Translation: 'EN_DUR:33:41 UN_MTH:D NL_DUR:33:41'
Retranslating the sequence took: 1 ms
Retranslation: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'

Sequence: 'yesterday OR today OR the 1st of october'
Translating the sequence took: 1 ms
Translation: 'EN_DAT:2018-07-15|UN_ABC:yesterday UN_ABC:OR EN_DAT:2018-07-16|UN_ABC:today UN_ABC:OR EN_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen'

Sequence: 'gisteren OF vandaag OF 1 oktober'
Translating the sequence took: 1 ms
Translation: 'NL_DAT:2018-07-15|UN_ABC:gisteren UN_ABC:OF NL_DAT:2018-07-16|UN_ABC:vandaag UN_ABC:OF NL_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien'

Sequence: 'twelve o'clock OR five minutes to nine OR ten past one in the morning'
Translating the sequence took: 11 ms
Translation: 'EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 1 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 0 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:Andre|NL_NAM:firstName:UN_ABC:Andre NL_PRE:3|NL_NAM:preposition:NL_PRE:3 UN_ABC:Zee|NL_NAM:lastName:UN_ABC:Zee'
Retranslating the sequence took: 0 ms
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

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam si gekste der henkies.'

Sequence: 'Mijn naam is {firstName} {preposition} {lastName}.'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Mijn|NL_NAM:firstName:UN_ABC:Mijn UN_ABC:naam|NL_NAM:lastName:UN_ABC:Naam UN_ABC:is {firstName} {preposition} {lastName} .'
Retranslating the sequence took: 1 ms
Retranslation: 'Mijn naam is {firstName} {preposition} {lastName}.'

Sequence: 'to Germany or France'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France'
Retranslating the sequence took: 0 ms
Retranslation: 'to Germany or France'

Sequence: 'naar Duitsland of Frankrijk'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:naar NL_CNT:DE|UN_ABC:Duitsland UN_ABC:of NL_CNT:FR|UN_ABC:Frankrijk'
Retranslating the sequence took: 0 ms
Retranslation: 'naar Duitsland of Frankrijk'

Sequence: 'You asshole'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:You EN_PRF:1|UN_ABC:asshole'
Retranslating the sequence took: 0 ms
Retranslation: 'You asshole'

Sequence: 'Jij klootzak'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 6 ms
Translation: 'UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:5 UN_ABC:people ?'
Retranslating the sequence took: 1 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 1 ms
Translation: 'EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five'
Retranslating the sequence took: 0 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 0 ms
Translation: 'NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf'
Retranslating the sequence took: 1 ms
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
        "Room",
        "Support"
      ]
    },
    {
      "code": "NL",
      "alphabet": "àáçèéêëïóöúüabcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Room",
        "Support"
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
Converting 333289 entity values took: 344 ms

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
Converting 1878 dialog examples took: 208 ms

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
Converting 8 dialogs took: 1 ms

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
  "classifyMasterContextThreshold": 0.8,
  "classifyContext": true,
  "classifyContextThreshold": 0.8,
  "checkProfanity": true,
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
Initializing Translator took 2083 ms
Initializing Classifier took 4241 ms
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
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENSupport) <= resources/LanguageContextENSupport.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNL) <= resources/LanguageMasterContextNL.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLGeneric) <= resources/LanguageContextNLGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLRoom) <= resources/LanguageContextNLRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLSupport) <= resources/LanguageContextNLSupport.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)

Initializing LanguagePreprocessor took 16 ms
Initializing LanguageContextENSupport took 45 ms
Initializing LanguageContextNLSupport took 39 ms
Initializing LanguageContextENGeneric took 280 ms
Initializing LanguageContextENRoom took 386 ms
Initializing LanguageContextNLGeneric took 356 ms
Initializing LanguageMasterContextEN took 476 ms
Initializing LanguageContextNLRoom took 498 ms
Initializing LanguageMasterContextNL took 566 ms
Initializing EntityValueTranslator took 1665 ms
Initializing LanguageClassifier took 31163 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestLanguageClassifier
------------------------------------------
This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used as a language classifier.
~~~~
'Wat is your name?' (ENG) => 'What is your name?'
'Wat is your name?' (NLD) => 'Wat is jouw naam?'

Classified sequence: 'Wie ben jij?' -> NL
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'Wie ben jij?' -> NL
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'what is name?' -> EN
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'twothousand' -> EN
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.22968830574325017 / 0.6608871820842418
'NL': 0.07892825143775159 / 0.22710198288388503

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.22968830574325017 / 0.6608871820842418
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
'Generic': 0.3690525237919113 / 0.900673069546439
'Support': 0.09836617721307059 / 0.24006275816718062
'Room': 0.08061482280435678 / 0.19674055920311706

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.37126782531194297 / 0.8368910095429433
~~~~

nl.zeesoft.zsd.test.TestLanguageContextClassifier
-------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as context classifiers.
~~~~
Classified sequence: 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 1 ms
Classified sequence (case insensitive): 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your goal?', threshold: 0.0
'QuestionAndAnswer': 0.48360224607292635 / 0.344343645220472
'Handshake': 0.19362509524260096 / 0.13786861339751516

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 1 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.46869478668519876 / 0.344342492759267
'Handshake': 0.23597113670643083 / 0.1733641844139082

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.43820560449794077 / 0.312019467308485
'QuestionAndAnswer': 0.22361930485464498 / 0.1592256595179351

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5168140682846566 / 0.37969495207064274
'QuestionAndAnswer': 0.22994210699898704 / 0.16893475362577515

Classified sequence: 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.34451986637287657 / 0.3066394635288164
'Handshake': 0.25094993600693216 / 0.22335766746893962
'Math': 0.2126278342455043 / 0.1892491301322199

Classified sequence: 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.3383052309830621 / 0.310684239134844
'Handshake': 0.29526600850130263 / 0.27115896176669657
'Language': 0.1513480392156863 / 0.1389912011458973
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
22:35:23:525: Classify language for sequence: Hallo.
22:35:23:525: Classified language: NL
22:35:23:525: Translate sequence: Hallo.
22:35:23:526: Translated sequence: UN_ABC:Hallo .
22:35:23:526: Correction time limit: 200
22:35:23:526: Correcting sequence: Hallo .
22:35:23:526: Corrected sequence: Hallo.
22:35:23:526: Translate corrected sequence: Hallo.
22:35:23:526: Translated corrected sequence: UN_ABC:Hallo .
22:35:23:526: Classify master context for sequence: Hallo.
22:35:23:526: Classified master context: Generic
22:35:23:526: Checking profanity for sequence: Hallo.
22:35:23:526: Classify context for sequence: Hallo.
22:35:23:526: Classified context: Handshake
Interpreting the request took: 2 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
22:35:23:527: Classify language for sequence: Mijn naam si gekste der henkies.
22:35:23:527: Classified language: NL
22:35:23:531: Translate sequence: Mijn naam si gekste der henkies.
22:35:23:534: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
22:35:23:535: Correction time limit: 700
22:35:23:535: Correcting sequence: Mijn naam si [] [] [] .
22:35:23:536: Corrected sequence: Mijn naam is gekste der henkies.
22:35:23:536: Translate corrected sequence: Mijn naam is gekste der henkies.
22:35:23:538: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
22:35:23:538: Classify master context for sequence: Mijn naam is gekste der henkies.
22:35:23:538: Classified master context: Generic
22:35:23:538: Checking profanity for sequence: Mijn naam is gekste der henkies.
22:35:23:538: Classify context for sequence: Mijn naam is gekste der henkies.
22:35:23:538: Classified context: Handshake
Interpreting the request took: 11 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
22:35:23:539: Classify language for sequence: Albert einstein.
22:35:23:539: Classify language for sequence: What is your name? Albert einstein.
22:35:23:539: Classified language: EN
22:35:23:540: Translate sequence: What is your name? Albert einstein.
22:35:23:544: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
22:35:23:545: Correction time limit: 300
22:35:23:545: Correcting sequence: [] [] .
22:35:23:545: Corrected sequence: Albert einstein.
22:35:23:545: Translate corrected sequence: What is your name? Albert einstein.
22:35:23:547: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
22:35:23:547: Classify master context for sequence: Albert einstein.
22:35:23:547: Classify master context for sequence: What is your name? Albert einstein.
22:35:23:548: Classified master context: Generic
22:35:23:548: Checking profanity for sequence: Albert einstein.
22:35:23:548: Classify context for sequence: Albert einstein.
22:35:23:548: Classify context for sequence: What is your name? Albert einstein.
22:35:23:548: Classified context: Handshake
Interpreting the request took: 10 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
22:35:23:548: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
22:35:23:549: Selected primary language: EN
22:35:23:549: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
22:35:23:551: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
22:35:23:552: Correction time limit: 1100
22:35:23:552: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
22:35:24:652: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
22:35:24:652: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
22:35:24:654: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
22:35:24:654: Classify master context for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1106 ms
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
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENSupport) <= resources/LanguageContextENSupport.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNL) <= resources/LanguageMasterContextNL.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLGeneric) <= resources/LanguageContextNLGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLRoom) <= resources/LanguageContextNLRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLSupport) <= resources/LanguageContextNLSupport.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)
- nl.zeesoft.zsd.dialog.DialogSet (DialogSet) <= resources/DialogSetEN.json, resources/DialogSetNL.json

Initializing DialogSet took 387 ms
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
22:35:27:017: Classify language for sequence: Hallo.
22:35:27:018: Classified language: NL
22:35:27:018: Translate sequence: Hallo.
22:35:27:018: Translated sequence: UN_ABC:Hallo .
22:35:27:018: Correction time limit: 200
22:35:27:018: Correcting sequence: Hallo .
22:35:27:018: Corrected sequence: Hallo.
22:35:27:018: Translate corrected sequence: Hallo.
22:35:27:019: Translated corrected sequence: UN_ABC:Hallo .
22:35:27:019: Classify master context for sequence: Hallo.
22:35:27:019: Classified master context: Generic
22:35:27:019: Checking profanity for sequence: Hallo.
22:35:27:019: Classify context for sequence: Hallo.
22:35:27:019: Classified context: Handshake
22:35:27:019: Handling dialog: NL/Generic/Handshake
22:35:27:019:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
22:35:27:020:     Found matches for sequence: 2
22:35:27:020:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.3095238095238096)
22:35:27:020:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.3095238095238096)
22:35:27:020:     Prompt variable: firstName
22:35:27:020:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
22:35:27:020:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 3 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
22:35:27:021: Classify language for sequence: Wie ben jij?
22:35:27:021: Classified language: NL
22:35:27:021: Translate sequence: Wie ben jij?
22:35:27:022: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
22:35:27:022: Correction time limit: 400
22:35:27:022: Correcting sequence: Wie ben jij ?
22:35:27:022: Corrected sequence: Wie ben jij?
22:35:27:022: Translate corrected sequence: Wie ben jij?
22:35:27:023: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
22:35:27:023: Classify master context for sequence: Wie ben jij?
22:35:27:024: Classified master context: Generic
22:35:27:024: Checking profanity for sequence: Wie ben jij?
22:35:27:024: Classify context for sequence: Wie ben jij?
22:35:27:024: Classified context: Handshake
22:35:27:024: Handling dialog: NL/Generic/Handshake
22:35:27:024:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
22:35:27:024:     Found matches for sequence: 2
22:35:27:024:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.4880952380952382)
22:35:27:024:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.15952380952380957)
22:35:27:024:     Prompt variable: firstName
22:35:27:024:     Set dialog output: Mijn naam is Dyz Lecticus.
22:35:27:024:     Set dialog prompt: Wat is jouw naam?
22:35:27:024: Handling dialog: NL/Generic/QuestionAndAnswer
22:35:27:025:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:026:     Found matches for sequence: 12
22:35:27:028:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.39921722113502933 / 0.32380952380952377)
22:35:27:028:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.20058708414872797 / 0.16269841269841268)
22:35:27:028:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.20058708414872797 / 0.16269841269841268)
22:35:27:028:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.19863013698630136 / 0.1611111111111111)
22:35:27:028:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.19863013698630136 / 0.1611111111111111)
22:35:27:028:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.19863013698630136 / 0.1611111111111111)
22:35:27:028:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.19613947696139475 / 0.15909090909090906)
22:35:27:028:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.19613947696139475 / 0.15909090909090906)
22:35:27:028:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.19613947696139475 / 0.15909090909090906)
22:35:27:028:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.19613947696139475 / 0.15909090909090906)
22:35:27:028:     - [... 2]
22:35:27:028:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
22:35:27:028: Handling dialog: NL/Generic/Language
22:35:27:028:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
22:35:27:029:     Updated variable languages: Engels en Nederlands
22:35:27:029:     Found matches for sequence: 2
22:35:27:029:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.15196078431372548)
22:35:27:029:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.15196078431372548)
22:35:27:029:     Prompt variable: language
22:35:27:029:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 21 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
22:35:27:041: Classify language for sequence: Albert einstein.
22:35:27:041: Classify language for sequence: What is your name? Albert einstein.
22:35:27:042: Classified language: EN
22:35:27:043: Translate sequence: What is your name? Albert einstein.
22:35:27:045: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
22:35:27:045: Correction time limit: 300
22:35:27:045: Correcting sequence: [] [] .
22:35:27:045: Corrected sequence: Albert einstein.
22:35:27:045: Translate corrected sequence: What is your name? Albert einstein.
22:35:27:048: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
22:35:27:048: Classify master context for sequence: Albert einstein.
22:35:27:048: Classify master context for sequence: What is your name? Albert einstein.
22:35:27:048: Classified master context: Generic
22:35:27:048: Checking profanity for sequence: Albert einstein.
22:35:27:048: Classify context for sequence: Albert einstein.
22:35:27:048: Classify context for sequence: What is your name? Albert einstein.
22:35:27:048: Classified context: Handshake
22:35:27:048: Handling dialog: EN/Generic/Handshake
22:35:27:048:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
22:35:27:049:     Updated variable firstName: UN_ABC:Albert = Albert
22:35:27:049:     Updated variable lastName: UN_ABC:Einstein = Einstein
22:35:27:049:     Failed to find matches for sequence: Albert einstein.
22:35:27:049:     Updated variable fullName: Albert Einstein
22:35:27:049:     Prompt variable: nextDialog
22:35:27:049:     Set dialog output: 
22:35:27:049:     Set dialog prompt: What can I do for you Albert Einstein?
22:35:27:049: Handling dialog: EN/Generic/QuestionAndAnswer
22:35:27:049:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:049:     Failed to find matches for sequence: Albert einstein.
Handling the request took: 8 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
22:35:27:050: Classify language for sequence: Mijn naam si gekste der henkies.
22:35:27:050: Classified language: NL
22:35:27:050: Translate sequence: Mijn naam si gekste der henkies.
22:35:27:052: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
22:35:27:052: Correction time limit: 700
22:35:27:052: Correcting sequence: Mijn naam si [] [] [] .
22:35:27:053: Corrected sequence: Mijn naam is gekste der henkies.
22:35:27:053: Translate corrected sequence: Mijn naam is gekste der henkies.
22:35:27:054: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
22:35:27:054: Classify master context for sequence: Mijn naam is gekste der henkies.
22:35:27:054: Classified master context: Generic
22:35:27:054: Checking profanity for sequence: Mijn naam is gekste der henkies.
22:35:27:055: Classify context for sequence: Mijn naam is gekste der henkies.
22:35:27:055: Classified context: Handshake
22:35:27:055: Handling dialog: NL/Generic/Handshake
22:35:27:055:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
22:35:27:055:     Updated variable firstName: UN_ABC:Gekste = Gekste
22:35:27:055:     Updated variable lastName: UN_ABC:Henkies = Henkies
22:35:27:055:     Updated variable preposition: NL_PRE:6 = der
22:35:27:056:     Failed to find matches for sequence: Mijn naam is gekste der henkies.
22:35:27:056:     Updated variable fullName: Gekste der Henkies
22:35:27:056:     Prompt variable: nextDialog
22:35:27:056:     Set dialog output: 
22:35:27:056:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 7 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
22:35:27:056: Classify language for sequence: Mijn naam is jan de lange.
22:35:27:057: Classified language: NL
22:35:27:058: Translate sequence: What is your firstname? Mijn naam is jan de lange.
22:35:27:061: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
22:35:27:061: Correction time limit: 700
22:35:27:061: Correcting sequence: Mijn naam is [] [] [] .
22:35:27:062: Corrected sequence: Mijn naam is jan de lange.
22:35:27:062: Translate corrected sequence: What is your firstname? Mijn naam is jan de lange.
22:35:27:064: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
22:35:27:064: Classify master context for sequence: Mijn naam is jan de lange.
22:35:27:064: Classified master context: Generic
22:35:27:064: Checking profanity for sequence: Mijn naam is jan de lange.
22:35:27:065: Classify context for sequence: Mijn naam is jan de lange.
22:35:27:065: Classified context: Handshake
22:35:27:065: Handling dialog: NL/Generic/Handshake
22:35:27:066:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
22:35:27:067:     Updated variable firstName: UN_ABC:Jan = Jan
22:35:27:068:     Updated variable lastName: UN_ABC:Lange = Lange
22:35:27:068:     Updated variable preposition: NL_PRE:5 = de
22:35:27:068:     Failed to find matches for sequence: Mijn naam is jan de lange.
22:35:27:068:     Updated variable fullName: Jan de Lange
22:35:27:068:     Prompt variable: nextDialog
22:35:27:068:     Set dialog output: 
22:35:27:068:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 85 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
22:35:27:141: Classify language for sequence: Who created you?
22:35:27:141: Classified language: EN
22:35:27:141: Translate sequence: Who created you?
22:35:27:142: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
22:35:27:142: Correction time limit: 400
22:35:27:142: Correcting sequence: Who created you ?
22:35:27:142: Corrected sequence: Who created you?
22:35:27:142: Translate corrected sequence: Who created you?
22:35:27:143: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
22:35:27:143: Classify master context for sequence: Who created you?
22:35:27:143: Classified master context: Generic
22:35:27:143: Checking profanity for sequence: Who created you?
22:35:27:144: Classify context for sequence: Who created you?
22:35:27:144: Classified context: QuestionAndAnswer
22:35:27:144: Handling dialog: EN/Generic/QuestionAndAnswer
22:35:27:144:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:144:     Found matches for sequence: 5
22:35:27:144:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.19648093841642228 / 0.42948717948717946)
22:35:27:144:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.04985337243401759 / 0.10897435897435896)
22:35:27:144:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.04985337243401759 / 0.10897435897435896)
22:35:27:144:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.04985337243401759 / 0.10897435897435896)
22:35:27:144:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.04985337243401759 / 0.10897435897435896)
22:35:27:144:     Set dialog output: My software was written by André van der Zee.
22:35:27:144: Handling dialog: EN/Generic/Math
22:35:27:145:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
22:35:27:145:     Failed to find matches for sequence: Who created you?
22:35:27:145: Handling dialog: EN/Generic/Handshake
22:35:27:145:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
22:35:27:145:     Found matches for sequence: 1
22:35:27:146:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.3281378178835111)
22:35:27:146:     Prompt variable: firstName
22:35:27:146:     Set dialog output: My name is Dyz Lecticus.
22:35:27:146:     Set dialog prompt: What is your name?
Handling the request took: 5 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
22:35:27:146: Classify language for sequence: You asshole!
22:35:27:147: Classified language: EN
22:35:27:147: Translate sequence: You asshole!
22:35:27:148: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
22:35:27:148: Correction time limit: 300
22:35:27:148: Correcting sequence: You [] !
22:35:27:148: Corrected sequence: You asshole!
22:35:27:148: Translate corrected sequence: You asshole!
22:35:27:149: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
22:35:27:149: Classify master context for sequence: You asshole!
22:35:27:149: Classified master context: Generic
22:35:27:149: Checking profanity for sequence: You asshole!
22:35:27:149: Handling dialog: EN/Generic/Profanity
22:35:27:149:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:150:     Found matches for sequence: 2
22:35:27:150:     - Asshole! [OUTPUT] I do not appreciate that kind of language. (0.19163995726495725 / 0.19350053937432576)
22:35:27:150:     - Asshole! [OUTPUT] I will pretend I did not read that. (0.19163995726495725 / 0.19350053937432576)
22:35:27:150:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 4 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
22:35:27:151: Classify language for sequence: Jij klootzak!
22:35:27:151: Classified language: NL
22:35:27:151: Translate sequence: Jij klootzak!
22:35:27:152: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
22:35:27:152: Correction time limit: 300
22:35:27:152: Correcting sequence: Jij [] !
22:35:27:152: Corrected sequence: Jij klootzak!
22:35:27:152: Translate corrected sequence: Jij klootzak!
22:35:27:153: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
22:35:27:153: Classify master context for sequence: Jij klootzak!
22:35:27:153: Classified master context: Generic
22:35:27:153: Checking profanity for sequence: Jij klootzak!
22:35:27:154: Handling dialog: NL/Generic/Profanity
22:35:27:154:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:155:     Found matches for sequence: 2
22:35:27:155:     - Klootzak! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.20330882352941176 / 0.1936274509803922)
22:35:27:155:     - Klootzak! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.20330882352941176 / 0.1936274509803922)
22:35:27:155:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 90 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
22:35:27:241: Classify language for sequence: Can I book a room for 5 people?
22:35:27:241: Classified language: EN
22:35:27:242: Translate sequence: Can I book a room for 5 people?
22:35:27:243: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:5 UN_ABC:people ?
22:35:27:244: Correction time limit: 900
22:35:27:244: Correcting sequence: Can I book [] [] for [] people ?
22:35:27:244: Corrected sequence: Can I book a room for 5 people?
22:35:27:244: Translate corrected sequence: Can I book a room for 5 people?
22:35:27:247: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:5 UN_ABC:people ?
22:35:27:247: Classify master context for sequence: Can I book a room for 5 people?
22:35:27:247: Classified master context: Room
22:35:27:247: Checking profanity for sequence: Can I book a room for 5 people?
22:35:27:247: Classify context for sequence: Can I book a room for 5 people?
22:35:27:247: Classified context: Booking
22:35:27:247: Handling dialog: EN/Room/Booking
22:35:27:247:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
22:35:27:248:     Updated variable bookPeople: UN_NUM:5 = 5
22:35:27:250:     Found matches for sequence: 187
22:35:27:250:     - Can I book a room for {bookPeople} people? [OUTPUT] Okay. (1.6727086183310533 / 0.3681872929840409)
22:35:27:250:     - Can I book a room for a duration of {bookDuration} for {bookPeople} people? [OUTPUT] Okay. (1.6727086183310533 / 0.3681872929840409)
22:35:27:250:     - Can I book a room for {bookPeople} people on {bookDate}? [OUTPUT] Okay. (1.4309165526675787 / 0.3149653718759409)
22:35:27:250:     - Can I book a room for {bookPeople} people from {bookTime}? [OUTPUT] Okay. (1.4309165526675787 / 0.3149653718759409)
22:35:27:250:     - Can I book a room for {bookPeople} people for a duration of {bookDuration}? [OUTPUT] Okay. (1.4309165526675787 / 0.3149653718759409)
22:35:27:250:     - Can I book a room for a duration of {bookDuration}? [OUTPUT] Okay. (1.4309165526675787 / 0.3149653718759409)
22:35:27:250:     - Can I book a room for a duration of {bookDuration} on {bookDate}? [OUTPUT] Okay. (1.4309165526675787 / 0.3149653718759409)
22:35:27:250:     - Can I book a room for a duration of {bookDuration} from {bookTime}? [OUTPUT] Okay. (1.4309165526675787 / 0.3149653718759409)
22:35:27:250:     - May I book a room for {bookPeople} people? [OUTPUT] Okay. (1.3703830369357046 / 0.3016410719662752)
22:35:27:250:     - May I book a room for a duration of {bookDuration} for {bookPeople} people? [OUTPUT] Okay. (1.3703830369357046 / 0.3016410719662752)
22:35:27:250:     - [... 177]
22:35:27:250:     Prompt variable: bookDate
22:35:27:250:     Set dialog output: Okay.
22:35:27:250:     Set dialog prompt: On what date?
22:35:27:251: Handling dialog: EN/Room/QuestionAndAnswer
22:35:27:251:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:251:     Found matches for sequence: 1
22:35:27:251:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (0.5 / 0.11594202898550725)
22:35:27:251:     Set dialog output: There are no costs attached to booking a room.
Handling the request took: 10 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
22:35:27:252: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
22:35:27:253: Classified language: EN
22:35:27:253: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
22:35:27:256: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
22:35:27:257: Correction time limit: 1332
22:35:27:257: Correcting sequence: Can I book [] [] for [] people on [] at [] for [] ?
22:35:27:258: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
22:35:27:258: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
22:35:27:260: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a|EN_NAM:firstName:UN_ABC:A UN_ABC:room|EN_NAM:lastName:UN_ABC:Room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
22:35:27:260: Classify master context for sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
22:35:27:260: Classified master context: Room
22:35:27:260: Checking profanity for sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
22:35:27:261: Classify context for sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
22:35:27:261: Classified context: Booking
22:35:27:261: Handling dialog: EN/Room/Booking
22:35:27:261:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
22:35:27:261:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
22:35:27:261:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
22:35:27:261:     Updated variable bookDuration: EN_DUR:02:00 = two hours
22:35:27:261:     Updated variable bookPeople: UN_NUM:12 = 12
22:35:27:268:     Found matches for sequence: 119
22:35:27:268:     - Can I book a room for {bookPeople} people for a duration of {bookDuration}? [OUTPUT] Okay. (2.2671107202570955 / 0.2363796800039847)
22:35:27:268:     - Can I book a room for a duration of {bookDuration} for {bookPeople} people? [OUTPUT] Okay. (2.2671107202570955 / 0.2363796800039847)
22:35:27:268:     - Can I book a room for {bookPeople} people on {bookDate}? [OUTPUT] Okay. (2.1304909560723515 / 0.22213505760788602)
22:35:27:268:     - May I book a room for {bookPeople} people for a duration of {bookDuration}? [OUTPUT] Okay. (1.9647851388617465 / 0.2048577858376746)
22:35:27:268:     - May I book a room for a duration of {bookDuration} for {bookPeople} people? [OUTPUT] Okay. (1.9647851388617465 / 0.2048577858376746)
22:35:27:268:     - Can I book a room for {bookPeople} people? [OUTPUT] Okay. (1.8811749506003954 / 0.19614019239607602)
22:35:27:268:     - Can I book a room for {bookPeople} people from {bookTime}? [OUTPUT] Okay. (1.8811749506003954 / 0.19614019239607602)
22:35:27:268:     - Can I book a room for a duration of {bookDuration}? [OUTPUT] Okay. (1.8811749506003954 / 0.19614019239607602)
22:35:27:268:     - Can I book a room for a duration of {bookDuration} on {bookDate}? [OUTPUT] Okay. (1.8811749506003954 / 0.19614019239607602)
22:35:27:268:     - Can I book a room for a duration of {bookDuration} from {bookTime}? [OUTPUT] Okay. (1.8811749506003954 / 0.19614019239607602)
22:35:27:268:     - [... 109]
22:35:27:268:     Prompt variable: bookConfirmation
22:35:27:268:     Set dialog output: Okay.
22:35:27:268:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 90 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
22:35:27:341: Classify language for sequence: What does it cost to book a room?
22:35:27:342: Classified language: EN
22:35:27:342: Translate sequence: What does it cost to book a room?
22:35:27:344: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book|EN_NAM:firstName:UN_ABC:Book UN_ABC:a|EN_NAM:lastName:UN_ABC:A UN_ABC:room ?
22:35:27:345: Correction time limit: 900
22:35:27:345: Correcting sequence: What does it cost to [] [] room ?
22:35:27:346: Corrected sequence: What does it cost to book a room?
22:35:27:346: Translate corrected sequence: What does it cost to book a room?
22:35:27:348: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book|EN_NAM:firstName:UN_ABC:Book UN_ABC:a|EN_NAM:lastName:UN_ABC:A UN_ABC:room ?
22:35:27:348: Classify master context for sequence: What does it cost to book a room?
22:35:27:348: Classified master context: Room
22:35:27:348: Classify context for sequence: What does it cost to book a room?
22:35:27:349: Classified context: QuestionAndAnswer
22:35:27:349: Handling dialog: EN/Room/QuestionAndAnswer
22:35:27:349:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
22:35:27:349:     Found matches for sequence: 4
22:35:27:349:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 0.5024154589371981)
22:35:27:349:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.3442028985507246)
22:35:27:349:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.27838164251207725)
22:35:27:349:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.27838164251207725)
22:35:27:349:     Set dialog output: There are no costs attached to booking a room.
22:35:27:349: Handling dialog: EN/Room/Booking
22:35:27:349:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
22:35:27:354:     Found matches for sequence: 126
22:35:27:354:     - I want to book a room for {bookPeople} people. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room for {bookPeople} people on {bookDate}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room for {bookPeople} people from {bookTime}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room for {bookPeople} people for a duration of {bookDuration}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room on {bookDate}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room on {bookDate} for {bookPeople} people. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room on {bookDate} from {bookTime}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room on {bookDate} for a duration of {bookDuration}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room from {bookTime}. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - I want to book a room from {bookTime} for {bookPeople} people. [OUTPUT] Okay. (0.8207934336525309 / 0.18066847335140018)
22:35:27:354:     - [... 116]
22:35:27:354:     Prompt variable: bookDate
22:35:27:354:     Set dialog output: Okay.
22:35:27:354:     Set dialog prompt: On what date?
Handling the request took: 13 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
22:35:27:355: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
22:35:27:356: Classified language: EN
22:35:27:356: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
22:35:27:359: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three|EN_NAM:firstName:UN_ABC:Three EN_MTH:M|UN_ABC:times|EN_NAM:lastName:UN_ABC:Times EN_NUM:6|UN_ABC:six ?
22:35:27:360: Correction time limit: 1300
22:35:27:360: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
22:35:27:361: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
22:35:27:361: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
22:35:27:365: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three|EN_NAM:firstName:UN_ABC:Three EN_MTH:M EN_NUM:6|UN_ABC:six ?
22:35:27:366: Classify master context for sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
22:35:27:367: Classified master context: Generic
22:35:27:367: Checking profanity for sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
22:35:27:368: Classify context for sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
22:35:27:368: Classified context: Math
22:35:27:368: Handling dialog: EN/Generic/Math
22:35:27:369:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
22:35:27:369:     Updated variable number1: EN_NUM:10 = ten
22:35:27:369:     Updated variable number2: EN_NUM:40 = fourty
22:35:27:369:     Updated variable number3: EN_NUM:20 = twenty
22:35:27:369:     Updated variable number4: EN_NUM:3 = three
22:35:27:369:     Updated variable number5: EN_NUM:6 = six
22:35:27:369:     Updated variable operator1: EN_MTH:M = multiplied by
22:35:27:370:     Updated variable operator2: EN_MTH:D = divided by
22:35:27:370:     Updated variable operator3: EN_MTH:A = plus
22:35:27:370:     Updated variable operator4: EN_MTH:M = multiplied by
22:35:27:370:     Failed to find matches for sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
22:35:27:370:     Calculate expression: 10 M 40 D 20 A 3 M 6
22:35:27:370:     Calculated expression: 38.0
22:35:27:370:     Updated variable exact: exactly
22:35:27:371:     Updated variable result: thirtyeight
22:35:27:371:     Prompt variable: nextDialog
22:35:27:371:     Set dialog output: Exactly thirtyeight.
22:35:27:371:     Set dialog prompt: What else can I do for you?
Handling the request took: 87 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
22:35:27:441: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
22:35:27:442: Classified language: NL
22:35:27:442: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
22:35:27:447: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie|NL_NAM:firstName:UN_ABC:Drie NL_MTH:M|UN_ABC:keer|NL_NAM:lastName:UN_ABC:Keer NL_NUM:6|UN_ABC:zes ?
22:35:27:448: Correction time limit: 1200
22:35:27:448: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
22:35:27:448: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
22:35:27:448: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
22:35:27:454: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie|NL_NAM:firstName:UN_ABC:Drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
22:35:27:454: Classify master context for sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
22:35:27:454: Classified master context: Generic
22:35:27:454: Checking profanity for sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
22:35:27:454: Classify context for sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
22:35:27:455: Classified context: Math
22:35:27:455: Handling dialog: NL/Generic/Math
22:35:27:455:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
22:35:27:455:     Updated variable number1: NL_NUM:10 = tien
22:35:27:455:     Updated variable number2: NL_NUM:40 = veertig
22:35:27:455:     Updated variable number3: NL_NUM:20 = twintig
22:35:27:455:     Updated variable number4: NL_NUM:3 = drie
22:35:27:455:     Updated variable number5: NL_NUM:6 = zes
22:35:27:455:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
22:35:27:455:     Updated variable operator2: NL_MTH:D = gedeeld door
22:35:27:455:     Updated variable operator3: NL_MTH:A = plus
22:35:27:455:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
22:35:27:455:     Failed to find matches for sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
22:35:27:456:     Calculate expression: 10 M 40 D 20 A 3 M 6
22:35:27:456:     Calculated expression: 38.0
22:35:27:456:     Updated variable exact: precies
22:35:27:456:     Updated variable result: achtendertig
22:35:27:456:     Prompt variable: nextDialog
22:35:27:456:     Set dialog output: Precies achtendertig.
22:35:27:456:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 15 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
22:35:27:456: Classify language for sequence: What languages do you speak?
22:35:27:457: Classified language: EN
22:35:27:457: Translate sequence: What languages do you speak?
22:35:27:458: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
22:35:27:459: Correction time limit: 600
22:35:27:459: Correcting sequence: What languages do you speak ?
22:35:27:459: Corrected sequence: What languages do you speak?
22:35:27:459: Translate corrected sequence: What languages do you speak?
22:35:27:461: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
22:35:27:461: Classify master context for sequence: What languages do you speak?
22:35:27:461: Classified master context: Generic
22:35:27:461: Checking profanity for sequence: What languages do you speak?
22:35:27:461: Classify context for sequence: What languages do you speak?
22:35:27:461: Classified context: Language
22:35:27:461: Handling dialog: EN/Generic/Language
22:35:27:461:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
22:35:27:462:     Updated variable languages: English and Dutch
22:35:27:462:     Found matches for sequence: 5
22:35:27:462:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.2979797979797979)
22:35:27:462:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.1919191919191919)
22:35:27:462:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.1717171717171717)
22:35:27:462:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.1717171717171717)
22:35:27:462:     - Do you speak {language}? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.1597222222222222)
22:35:27:462:     Prompt variable: language
22:35:27:462:     Set dialog output: I speak English and Dutch.
Handling the request took: 85 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
22:35:27:541: Classify language for sequence: Spreek je duits?
22:35:27:541: Classified language: NL
22:35:27:541: Translate sequence: Spreek je duits?
22:35:27:543: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
22:35:27:543: Correction time limit: 400
22:35:27:543: Correcting sequence: Spreek je [] ?
22:35:27:543: Corrected sequence: Spreek je Duits?
22:35:27:543: Translate corrected sequence: Spreek je Duits?
22:35:27:545: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
22:35:27:546: Classify master context for sequence: Spreek je Duits?
22:35:27:546: Classified master context: Generic
22:35:27:546: Checking profanity for sequence: Spreek je Duits?
22:35:27:546: Classify context for sequence: Spreek je Duits?
22:35:27:546: Classified context: Language
22:35:27:546: Handling dialog: NL/Generic/Language
22:35:27:546:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
22:35:27:546:     Updated variable language: NL_LNG:DE = Duits
22:35:27:546:     Updated variable languages: Engels en Nederlands
22:35:27:546:     Updated variable confirmation: Nee
22:35:27:547:     Found matches for sequence: 3
22:35:27:547:     - Spreek je {language}? [OUTPUT] {confirmation}, ik spreek {languages}. (0.5 / 0.16176470588235295)
22:35:27:547:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.14154411764705882)
22:35:27:547:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.14154411764705882)
22:35:27:547:     Prompt variable: 
22:35:27:547:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 6 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
22:35:27:547: Classify language for sequence: Spreek je engels?
22:35:27:547: Classified language: NL
22:35:27:547: Translate sequence: Spreek je engels?
22:35:27:549: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
22:35:27:549: Correction time limit: 400
22:35:27:549: Correcting sequence: Spreek je [] ?
22:35:27:549: Corrected sequence: Spreek je Engels?
22:35:27:549: Translate corrected sequence: Spreek je Engels?
22:35:27:551: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
22:35:27:551: Classify master context for sequence: Spreek je Engels?
22:35:27:552: Classified master context: Generic
22:35:27:552: Checking profanity for sequence: Spreek je Engels?
22:35:27:552: Classify context for sequence: Spreek je Engels?
22:35:27:552: Classified context: Language
22:35:27:552: Handling dialog: NL/Generic/Language
22:35:27:552:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
22:35:27:552:     Updated variable language: NL_LNG:EN = Engels
22:35:27:552:     Updated variable languages: Engels en Nederlands
22:35:27:552:     Updated variable confirmation: Ja
22:35:27:552:     Found matches for sequence: 3
22:35:27:552:     - Spreek je {language}? [OUTPUT] {confirmation}, ik spreek {languages}. (0.5 / 0.16176470588235295)
22:35:27:552:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.14154411764705882)
22:35:27:554:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.14154411764705882)
22:35:27:554:     Prompt variable: 
22:35:27:554:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 7 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (241 assertions).  
Total test duration: 64518 ms (total sleep duration: 30700 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 612 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 425 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 456 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 463 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 472 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117519 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117531 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117530 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 117721 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117585 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117617 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117596 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 546820 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 546820 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 546821 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 546822 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 546828 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 551399 Kb / 538 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 551420 Kb / 538 Mb
