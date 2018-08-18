Zeesoft Smart Dialogs
=====================
Zeesoft Smart Dialogs (ZSD) is an open source library for Java application development.  

It provides support for;  
 * Context sensitive symbolic corrections; word spelling corrections  
 * Sequence context classification; sentence context classification  
 * Context sensitive sequence matching; find matching sentences  
 * Symbolic entity value translation; find variable values  
 * Dialog response generation; create a smart and scalable chatbot  

This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

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
Initializing the SymbolCorrector took: 14 ms
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
Average correction time: 227 ms
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
Initializing the SequenceClassifier took: 2775 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 2 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 2 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 1 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 5 ms

Context probabilities for 'Wat kost dat?', threshold: 0.4
'nlPriveKinderenengeldzaken': 0.042902821500088964 / 1.0
'nlPrivatebankingUwvermogen': 0.028009059187044 / 0.6528488851714781
'nlPrivatebankingBetalen': 0.023943110276929132 / 0.5580777543239082
'nlPrivePensioen': 0.02363316176222444 / 0.5508533223665822
'nlPriveBeleggen': 0.022273672816980207 / 0.5191656874346602
'nlPriveBetalen': 0.020269754198164815 / 0.4724573696889092
'nlZakelijkOndernemen': 0.018245720827610022 / 0.42528020744678036

Context probabilities for 'Wat kost dat?', threshold: 0.75
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
Initializing the SequenceMatcher took: 4777 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 20 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 16 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 10 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 21 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 17 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 13 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 16 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 42 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 38 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Geldt mijn ABN AMRO Rechtsbijstandverzekering ook in het buitenland? [OUTPUT] Dit is afhankelijk van welke modules u heeft afgesloten. Bekijk uw polisblad voor uw dekkingen of bekijk de modules.'
Matching the input sequence took: 39 ms

Matches for sequence: 'Wat kost dat?', threshold: 0.33
'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënter[ ...]': 0.03920302825787142 / 0.6630272952853598
'Lees ook mijn blog Wat kost uw kind? [OUTPUT] Hiervoor zijn [ ...]': 0.019577061142247915 / 0.3311000827129859
'Op de vraag 'Wat kost een kind? [OUTPUT] Twee kinderen koste[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost de LEI? [OUTPUT] Het jaar waarin u de LEI heeft aan[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost een buitenlandse betaling? [OUTPUT] Bekijk het over[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost een studie? [OUTPUT] De enige hulpmiddelen die de o[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost een uitgebreid pensioenadvies? [OUTPUT] Voor een ui[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost een uitvaart? [OUTPUT] Elk jaar stuurt ABN AMRO Lev[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost het en waarop moet u letten? [OUTPUT] U wordt een d[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost het om ABN AMRO te bellen? [OUTPUT] Als u belt naar[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost het? [OUTPUT] Voor dit mandaat betaalt u kosten. De[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost studeren? [OUTPUT] Plus natuurlijk kosten voor bijv[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost uw kind en hoe zorgt u ervoor dat het later zijn of[ ...]': 0.019577061142247915 / 0.3311000827129859
'Wat kost uw kind? [OUTPUT] Zo heeft u altijd overzicht, waar[ ...]': 0.019577061142247915 / 0.3311000827129859

Matches for sequence: 'Wat kost dat?', threshold: 0.5
'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënter[ ...]': 0.03920302825787142 / 0.6630272952853598

Matches for sequence: 'Wat kost de Betaalpas?', threshold: 0.25
'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft[ ...]': 0.0786603675772959 / 1.0
'Wat kost de LEI? [OUTPUT] Het jaar waarin u de LEI heeft aan[ ...]': 0.03927149662059724 / 0.49925391693608556
'Hoe veilig is mijn Betaalpas? [OUTPUT] U kunt uw betaalalert[ ...]': 0.01968465428367421 / 0.25024869435463815
'Hoeveel geld kan ik opnemen met mijn Betaalpas? [OUTPUT] Mog[ ...]': 0.01968465428367421 / 0.25024869435463815
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
Initializing the EntityValueTranslator took: 1531 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Eat EN_NUM:3|UN_ABC:three UN_ABC:donuts UN_ABC:at UN_TIM:09:00:00 UN_ABC:or UN_ABC:count UN_ABC:to UN_NUM:110'
Retranslating the sequence took: 0 ms
Retranslation: 'Eat three donuts at 09:00:00 or count to 110'

Sequence: 'Eet drie donuts om 9:00 of tel tot 110'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Eet NL_NUM:3|UN_ABC:drie UN_ABC:donuts UN_ABC:om UN_TIM:09:00:00 UN_ABC:of UN_ABC:tel UN_ABC:tot UN_NUM:110'
Retranslating the sequence took: 0 ms
Retranslation: 'Eet drie donuts om 09:00:00 of tel tot 110'

Sequence: 'I finished twohundredandtwentyfourth or 225th'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:I UN_ABC:finished EN_ORD:224|UN_ABC:twohundredandtwentyfourth UN_ABC:or EN_OR2:225'
Retranslating the sequence took: 0 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 0 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 3 ms
Translation: 'NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december'
Retranslating the sequence took: 0 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 1 ms
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
Translating the sequence took: 1 ms
Translation: 'EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 0 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 1 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 1 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:Andre|NL_NAM:firstName:UN_ABC:Andre NL_PRE:3|NL_NAM:preposition:NL_PRE:3 UN_ABC:Zee|NL_NAM:lastName:UN_ABC:Zee'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam is Andre van der Zee'

Sequence: 'Hoe heet jij? gekke henkie'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Hoe UN_ABC:heet UN_ABC:jij ? UN_ABC:gekke|NL_NAM:firstName:UN_ABC:Gekke UN_ABC:henkie|NL_NAM:lastName:UN_ABC:Henkie'
Retranslating the sequence took: 0 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies UN_ABC:is UN_ABC:mijn UN_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam si gekste der henkies.'

Sequence: 'to Germany or France'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France'
Retranslating the sequence took: 0 ms
Retranslation: 'to Germany or France'

Sequence: 'naar Duitsland of Frankrijk'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:naar NL_CNT:DE|UN_ABC:Duitsland UN_ABC:of NL_CNT:FR|UN_ABC:Frankrijk'
Retranslating the sequence took: 0 ms
Retranslation: 'naar Duitsland of Frankrijk'

Sequence: 'You asshole'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:You EN_PRF:1|UN_ABC:asshole'
Retranslating the sequence took: 0 ms
Retranslation: 'You asshole'

Sequence: 'Jij klootzak'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 5 ms
Translation: 'UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?'
Retranslating the sequence took: 0 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 1 ms
Translation: 'EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five'
Retranslating the sequence took: 0 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 1 ms
Translation: 'NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf'
Retranslating the sequence took: 0 ms
Retranslation: 'tien vermenigvuldigd met vijf'

Sequence: 'fifteen british pound'
Translating the sequence took: 0 ms
Translation: 'EN_NUM:15|UN_ABC:fifteen EN_CUR:GBP'
Retranslating the sequence took: 2 ms
Retranslation: 'fifteen british pound sterling'

Sequence: 'vijftien euro'
Translating the sequence took: 1 ms
Translation: 'NL_NUM:15|UN_ABC:vijftien EN_CUR:EUR|NL_CUR:EUR|UN_ABC:euro'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien euro'

Sequence: ':-) ]0: {;'
Translating the sequence took: 1 ms
Translation: 'UN_SML:7 UN_FRN:28 UN_SML:42'
Retranslating the sequence took: 0 ms
Retranslation: ':-) ]0: {;'
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
  "email": "test@email.com",
  "smiley": ":]",
  "frowny": ":[",
  "primaryLanguage": "EN",
  "debug": true,
  "selfTest": false,
  "dataDir": "testData/",
  "baseDir": "testBase/",
  "extendDir": "testExtend/",
  "overrideDir": "testOverride/",
  "generateReadFormat": true,
  "maxMsInterpretPerSymbol": 111,
  "maxMsInterpretPerSequence": 2222,
  "maxMsDialogPerSequence": 1111,
  "selfTestBaseLineFileName": "testFileName.json",
  "parameters": [
    {
      "key": "test1",
      "value": "test1Val"
    },
    {
      "key": "test2",
      "value": "test2Val"
    }
  ],
  "supportedLanguages": [
    {
      "code": "EN",
      "alphabet": "abcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Support",
        "Room",
        "ForeignTransfer"
      ]
    },
    {
      "code": "NL",
      "alphabet": "àáçèéêëïóöúüabcdefghijklmnopqrstuvwxyz",
      "supportedMasterContexts": [
        "Generic",
        "Support",
        "Room",
        "ForeignTransfer"
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
Converting 247356 entity values took: 282 ms

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
// Create an entity value translator
EntityValueTranslator t = new EntityValueTranslator();
// Initialize entity value translator
t.initialize();
// Initialize the dialog set
ds.initialize(t);
// Create the DialogToJson instance
DialogToJson convertor = new DialogToJson();
// Convert the dialogs to JSON
JsFile json = convertor.getJsonForDialogs(ds.getDialogs(),t);
~~~~

This test uses the *MockEntityValueTranslator*.

Class references;  
 * [TestDialogToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogToJson.java)
 * [DialogToJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/util/DialogToJson.java)

**Test output**  
The output of this test shows a sample of the converted JSON.  
~~~~
Converting 1588 dialog examples took: 89 ms

Sample JSON;
{
  "sequences": [
    {
      "input": "[FAILED_TO_CLASSIFY_LANGUAGE].",
      "output": "I do not understand what language you are speaking.",
      "context": "EN"
    },
    {
      "input": "[FAILED_TO_CLASSIFY_LANGUAGE].",
      "output": "I do not understand what you are saying.",
      "context": "EN"
    },
    {
      "input": "[FAILED_TO_CLASSIFY_CONTEXT].",
      "output": "I do not understand what you mean.",
      "context": "EN"
    },
    {
      "input": "[FAILED_TO_CLASSIFY_CONTEXT].",
      "output": "I do not understand what you are saying.",
      "context": "EN"
    },
    {
      "input": "Stop.",
      "output": "Okay.",
      "context": "EN"
    },
    {
      "input": "Abort.",
      "output": "Okay.",
      "context": "EN"
    },
    {
      "input": "Cancel.",
      "output": "Okay.",
      "context": "EN"
    },
    {
      "input": "Nothing.",
      "output": "Okay.",
      "context": "EN"
    },
    {
      "input": "Nevermind.",
      "output": "Okay.",
      "context": "EN"
    },
    {
      "input": "Leave it.",
      "output": "Okay.",
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
Converting 15 dialogs took: 1 ms

Sample JSON;
{
  "dialogs": [
    {
      "language": "EN",
      "masterContext": "Generic",
      "context": "Classification",
      "handler": "",
      "defaultFilterContext": "",
      "examples": [
        {
          "input": "[FAILED_TO_CLASSIFY_LANGUAGE].",
          "output": "I do not understand what language you are speaking."
        },
        {
          "input": "[FAILED_TO_CLASSIFY_LANGUAGE].",
          "output": "I do not understand what you are saying."
        },
        {
          "input": "[FAILED_TO_CLASSIFY_CONTEXT].",
          "output": "I do not understand what you mean."
        },
        {
          "input": "[FAILED_TO_CLASSIFY_CONTEXT].",
          "output": "I do not understand what you are saying."
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
  "isTestRequest": true,
  "appendDebugLog": true,
  "classifyLanguage": true,
  "minLanguageChangeDifference": 0.2,
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
  "filterContexts": [
    "testFilterContext"
  ],
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
  "classifiedLanguages": [
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
  "classificationSequence": "Test classification sequence.",
  "classifiedMasterContexts": [],
  "classifiedContexts": [],
  "entityValueTranslation": "Test entity value translation.",
  "entityValueTranslationCorrected": "Test corrected entity value translation.",
  "debugLog": "",
  "contextOutputs": [
    {
      "context": "testContext",
      "output": "Test output!",
      "prompt": "Test prompt!",
      "promptVariableName": "testPromptVarName",
      "promptVariableType": "testPromptVarType",
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
Initializing Translator took 2331 ms
Initializing Classifier took 3831 ms
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
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENSupport) <= resources/LanguageContextENSupport.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENRoom) <= resources/LanguageContextENRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENForeignTransfer) <= resources/LanguageContextENForeignTransfer.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNL) <= resources/LanguageMasterContextNL.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLGeneric) <= resources/LanguageContextNLGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLSupport) <= resources/LanguageContextNLSupport.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLRoom) <= resources/LanguageContextNLRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLForeignTransfer) <= resources/LanguageContextNLForeignTransfer.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)

Initializing LanguageContextENSupport took 30 ms
Initializing LanguageContextNLForeignTransfer took 18 ms
Initializing LanguagePreprocessor took 22 ms
Initializing LanguageContextENForeignTransfer took 67 ms
Initializing LanguageContextNLSupport took 68 ms
Initializing LanguageContextENGeneric took 94 ms
Initializing LanguageContextNLGeneric took 177 ms
Initializing LanguageMasterContextEN took 362 ms
Initializing LanguageContextENRoom took 353 ms
Initializing LanguageContextNLRoom took 415 ms
Initializing LanguageMasterContextNL took 454 ms
Initializing EntityValueTranslator took 2102 ms
Initializing LanguageClassifier took 23140 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestLanguageClassifier
------------------------------------------
This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used as a language classifier.
~~~~
'Wat is your name?' (ENG) => 'What is your name?'
'Wat is your name?' (NLD) => 'Wat is jouw naam?'

Classified sequence: 'Wie ben jij?' -> NL
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wie ben jij?' -> NL
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'what is name?' -> EN
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'twothousand' -> EN
Classifying the input sequence took: 1 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.24387505000264 / 0.8793757418922823
'NL': 0.08333906714744599 / 0.3005077969255268

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.24387505000264 / 0.8793757418922823
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
'Generic': 0.2588903897913244 / 0.871776991051593

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.33833951636047993 / 1.0
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
'QuestionAndAnswer': 0.29288159603352615 / 0.7101252236135958
'Handshake': 0.17520254143274955 / 0.4247987773815589

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.3210438058484369 / 0.5313563349491494
'Handshake': 0.2799942928191435 / 0.4634157038036602

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.40338521531583027 / 0.9780539989811515
'QuestionAndAnswer': 0.11737103057490261 / 0.2845796064400716

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5966083039481461 / 0.9874403306063926
'QuestionAndAnswer': 0.14623618863850413 / 0.24203402718372777

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.3045095030102703 / 0.9228980322003577
'Handshake': 0.23480519043937204 / 0.7116403464085583
'Thanks': 0.0690214326001128 / 0.2091880341880342

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.3337422185468495 / 0.6904666856762663
'Handshake': 0.31098020831210127 / 0.6433752213882095
'Language': 0.12102574222689998 / 0.2503855924514607
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
16:49:01:253: Classify language for sequence: Hallo.
16:49:01:254: Classified language: NL
16:49:01:254: Translate sequence: Hallo.
16:49:01:254: Translated sequence: UN_ABC:Hallo .
16:49:01:255: Correction time limit: 200
16:49:01:255: Correcting sequence: Hallo .
16:49:01:255: Corrected sequence: Hallo.
16:49:01:255: Translate corrected sequence: Hallo.
16:49:01:256: Translated corrected sequence: UN_ABC:Hallo .
16:49:01:256: Checking profanity for sequence: Hallo.
16:49:01:256: Classify master context for input sequence: Hallo.
16:49:01:256: Classified master context: Generic
16:49:01:256: Classify context for input sequence: Hallo.
16:49:01:256: Classified context: Handshake
Interpreting the request took: 4 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
16:49:01:257: Classify language for sequence: Mijn naam si gekste der henkies.
16:49:01:258: Classified language: NL
16:49:01:258: Translate sequence: Mijn naam si gekste der henkies.
16:49:01:261: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
16:49:01:261: Correction time limit: 700
16:49:01:261: Correcting sequence: Mijn naam si gekste [] henkies .
16:49:01:733: Corrected sequence: Mijn naam is gekste der henkies.
16:49:01:733: Translate corrected sequence: Mijn naam is gekste der henkies.
16:49:01:735: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
16:49:01:736: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:01:736: Classify master context for input sequence: Mijn naam is gekste der henkies.
16:49:01:736: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:01:736: Classified master context: Generic
16:49:01:736: Classify context for input sequence: Mijn naam is gekste der henkies.
16:49:01:736: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:01:736: Classified context: Handshake
Interpreting the request took: 480 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
16:49:01:737: Classify language for sequence: Albert einstein.
16:49:01:737: Classify language for sequence: What is your name? Albert einstein.
16:49:01:737: Classified language: EN
16:49:01:738: Translate sequence: Albert einstein.
16:49:01:741: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
16:49:01:741: Correction time limit: 300
16:49:01:741: Correcting sequence: [] [] .
16:49:01:741: Corrected sequence: Albert einstein.
16:49:01:741: Translate corrected sequence: Albert einstein.
16:49:01:743: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
16:49:01:743: Checking profanity for sequence: [ABC] [ABC].
16:49:01:743: Classify master context for input sequence: Albert einstein.
16:49:01:743: Classify master context for classification sequence: What is your name? [ABC] [ABC].
16:49:01:743: Classified master context: Generic
16:49:01:743: Classify context for input sequence: Albert einstein.
16:49:01:743: Classify context for classification sequence: What is your name? [ABC] [ABC].
16:49:01:743: Classified context: Handshake
Interpreting the request took: 7 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
16:49:01:744: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
16:49:01:744: Selected primary language: EN
16:49:01:744: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
16:49:01:746: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
16:49:01:746: Correction time limit: 1100
16:49:01:746: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
16:49:02:846: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
16:49:02:846: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
16:49:02:848: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
16:49:02:848: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
16:49:02:849: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
16:49:02:849: Master context is not supported: 
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
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENSupport) <= resources/LanguageContextENSupport.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENRoom) <= resources/LanguageContextENRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextENForeignTransfer) <= resources/LanguageContextENForeignTransfer.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageMasterContextNL) <= resources/LanguageMasterContextNL.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLGeneric) <= resources/LanguageContextNLGeneric.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLSupport) <= resources/LanguageContextNLSupport.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLRoom) <= resources/LanguageContextNLRoom.json
- nl.zeesoft.zsd.SequenceClassifier (LanguageContextNLForeignTransfer) <= resources/LanguageContextNLForeignTransfer.json
- nl.zeesoft.zsd.test.FixedDateEntityValueTranslator (EntityValueTranslator)
- nl.zeesoft.zsd.dialog.DialogSet (DialogSet) <= resources/DialogSetEN.json, resources/DialogSetNL.json

Initializing DialogSet took 427 ms
Initialized all classes
~~~~

nl.zeesoft.zsd.test.TestDialogHandler
-------------------------------------
This test show how to use a *DialogHandler* to process a *DialogRequest*.

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
Request prompt: '', input: 'What are you?'
Response debug log;
16:49:05:430: Classify language for sequence: What are you?
16:49:05:430: Classified language: EN
16:49:05:430: Translate sequence: What are you?
16:49:05:431: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
16:49:05:431: Correction time limit: 400
16:49:05:431: Correcting sequence: What are you ?
16:49:05:431: Corrected sequence: What are you?
16:49:05:432: Translate corrected sequence: What are you?
16:49:05:432: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
16:49:05:433: Checking profanity for sequence: What are you?
16:49:05:433: Classify master context for input sequence: What are you?
16:49:05:433: Classified master context: Generic
16:49:05:433: Classify context for input sequence: What are you?
16:49:05:433: Classified context: QuestionAndAnswer
16:49:05:433: Handling dialog: EN/Generic/QuestionAndAnswer
16:49:05:434:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:05:434:     Find matches for sequence: What are you?
16:49:05:434:     Found matches for sequence: 10
16:49:05:434:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.2090395480225989 / 0.8809523809523809)
16:49:05:434:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.0696798493408663 / 0.2936507936507936)
16:49:05:436:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.0696798493408663 / 0.2936507936507936)
16:49:05:436:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
16:49:05:436:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
16:49:05:436:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
16:49:05:436:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:05:436:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:05:436:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:05:436:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:05:436:     Set dialog output: I am an artificially intelligent personal assistant.
16:49:05:437: Handling dialog: EN/Generic/Handshake
16:49:05:437:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:05:437:     Find matches for sequence: What are you?
16:49:05:437:     Found matches for sequence: 2
16:49:05:437:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
16:49:05:437:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
16:49:05:437:     Set prompt variable: firstName
16:49:05:438:     Set dialog output: My name is Dyz Lecticus.
16:49:05:438:     Set dialog prompt: What is your name?
Handling the request took: 65 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
16:49:05:495: Classify language for sequence: Wat ben jij?
16:49:05:495: Classified language: NL
16:49:05:495: Translate sequence: Wat ben jij?
16:49:05:496: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
16:49:05:496: Correction time limit: 400
16:49:05:496: Correcting sequence: Wat ben jij ?
16:49:05:496: Corrected sequence: Wat ben jij?
16:49:05:496: Translate corrected sequence: Wat ben jij?
16:49:05:497: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
16:49:05:497: Checking profanity for sequence: Wat ben jij?
16:49:05:497: Classify master context for input sequence: Wat ben jij?
16:49:05:497: Classified master context: Generic
16:49:05:497: Classify context for input sequence: Wat ben jij?
16:49:05:497: Classified context: QuestionAndAnswer
16:49:05:497: Handling dialog: NL/Generic/QuestionAndAnswer
16:49:05:497:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:05:497:     Find matches for sequence: Wat ben jij?
16:49:05:497:     Found matches for sequence: 9
16:49:05:498:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.4464544138929089 / 0.9793650793650793)
16:49:05:498:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
16:49:05:498:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
16:49:05:498:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
16:49:05:498:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
16:49:05:498:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14688856729377714 / 0.3222222222222222)
16:49:05:498:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
16:49:05:498:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
16:49:05:498:     - Ben jij menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
16:49:05:498:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
16:49:05:498: Handling dialog: NL/Generic/Handshake
16:49:05:498:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:05:498:     Find matches for sequence: Wat ben jij?
16:49:05:498:     Found matches for sequence: 5
16:49:05:498:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
16:49:05:498:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
16:49:05:499:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
16:49:05:499:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
16:49:05:499:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
16:49:05:499:     Set prompt variable: firstName
16:49:05:499:     Set dialog output: Mijn naam is Dyz Lecticus.
16:49:05:499:     Set dialog prompt: Wat is uw naam?
16:49:05:499: Handling dialog: NL/Generic/Language
16:49:05:499:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
16:49:05:499:     Updated variable languages: Engels en Nederlands
16:49:05:499:     Find matches for sequence: Wat ben jij?
16:49:05:499:     Found matches for sequence: 2
16:49:05:499:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
16:49:05:499:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
16:49:05:499:     Set prompt variable: language
16:49:05:500:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 100 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
16:49:05:595: Classify language for sequence: What is the answer?
16:49:05:595: Classified language: EN
16:49:05:595: Translate sequence: What is the answer?
16:49:05:596: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
16:49:05:596: Correction time limit: 500
16:49:05:596: Correcting sequence: What is [] answer ?
16:49:05:597: Corrected sequence: What is the answer?
16:49:05:597: Translate corrected sequence: What is the answer?
16:49:05:598: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
16:49:05:598: Checking profanity for sequence: What is [PRE] answer?
16:49:05:598: Classify master context for input sequence: What is the answer?
16:49:05:598: Classify master context for classification sequence: What is [PRE] answer?
16:49:05:598: Selected input sequence master context classification.
16:49:05:598: Classified master context: Generic
16:49:05:598: Classify context for input sequence: What is the answer?
16:49:05:598: Classify context for classification sequence: What is [PRE] answer?
16:49:05:598: Selected input sequence context classification.
16:49:05:598: Classified context: QuestionAndAnswer
16:49:05:598: Handling dialog: EN/Generic/QuestionAndAnswer
16:49:05:598:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:05:598:     Find matches for sequence: What is the answer?
16:49:05:598:     Found matches for sequence: 9
16:49:05:599:     - What is the answer? [OUTPUT] Fourtytwo. (0.24858757062146894 / 0.838095238095238)
16:49:05:599:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.1713747645951036 / 0.5777777777777777)
16:49:05:599:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.1713747645951036 / 0.5777777777777777)
16:49:05:599:     - What is the question? [OUTPUT] I do not know. (0.1016949152542373 / 0.3428571428571428)
16:49:05:599:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.1016949152542373 / 0.3428571428571428)
16:49:05:599:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.1016949152542373 / 0.3428571428571428)
16:49:05:599:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.1016949152542373 / 0.3428571428571428)
16:49:05:599:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.04708097928436912 / 0.15873015873015872)
16:49:05:599:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.04708097928436912 / 0.15873015873015872)
16:49:05:599:     Set dialog output: Fourtytwo.
Handling the request took: 4 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
16:49:05:599: Classify language for sequence: Wat is het antwoord?
16:49:05:599: Classified language: NL
16:49:05:599: Translate sequence: Wat is het antwoord?
16:49:05:600: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
16:49:05:601: Correction time limit: 500
16:49:05:601: Correcting sequence: Wat is het antwoord ?
16:49:05:601: Corrected sequence: Wat is het antwoord?
16:49:05:601: Translate corrected sequence: Wat is het antwoord?
16:49:05:602: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
16:49:05:602: Checking profanity for sequence: Wat is het antwoord?
16:49:05:602: Classify master context for input sequence: Wat is het antwoord?
16:49:05:602: Classified master context: Generic
16:49:05:602: Classify context for input sequence: Wat is het antwoord?
16:49:05:602: Classified context: QuestionAndAnswer
16:49:05:602: Handling dialog: NL/Generic/QuestionAndAnswer
16:49:05:602:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:05:602:     Find matches for sequence: Wat is het antwoord?
16:49:05:602:     Found matches for sequence: 10
16:49:05:602:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.58465991316932 / 1.0)
16:49:05:602:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.4334298118668597 / 0.7413366336633662)
16:49:05:602:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.4334298118668597 / 0.7413366336633662)
16:49:05:602:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.28509406657018815 / 0.48762376237623756)
16:49:05:602:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.13820549927641101 / 0.23638613861386135)
16:49:05:602:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.13820549927641101 / 0.23638613861386135)
16:49:05:602:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.13820549927641101 / 0.23638613861386135)
16:49:05:602:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.13820549927641101 / 0.23638613861386135)
16:49:05:602:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.13820549927641101 / 0.23638613861386135)
16:49:05:602:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.13820549927641101 / 0.23638613861386135)
16:49:05:602:     Set dialog output: Tweeenveertig.
Handling the request took: 96 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
16:49:05:695: Classify language for sequence: Hallo.
16:49:05:695: Classified language: NL
16:49:05:695: Translate sequence: Hallo.
16:49:05:695: Translated sequence: UN_ABC:Hallo .
16:49:05:695: Correction time limit: 200
16:49:05:695: Correcting sequence: Hallo .
16:49:05:696: Corrected sequence: Hallo.
16:49:05:696: Translate corrected sequence: Hallo.
16:49:05:696: Translated corrected sequence: UN_ABC:Hallo .
16:49:05:696: Checking profanity for sequence: Hallo.
16:49:05:696: Classify master context for input sequence: Hallo.
16:49:05:696: Classified master context: Generic
16:49:05:696: Classify context for input sequence: Hallo.
16:49:05:696: Classified context: Handshake
16:49:05:696: Handling dialog: NL/Generic/Handshake
16:49:05:696:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:05:696:     Find matches for sequence: Hallo.
16:49:05:696:     Found matches for sequence: 1
16:49:05:696:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
16:49:05:696:     Set prompt variable: firstName
16:49:05:697:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
16:49:05:697:     Set dialog prompt: Wat is uw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
16:49:05:697: Classify language for sequence: Wie ben jij?
16:49:05:697: Classified language: NL
16:49:05:697: Translate sequence: Wie ben jij?
16:49:05:698: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
16:49:05:698: Correction time limit: 400
16:49:05:698: Correcting sequence: Wie ben jij ?
16:49:05:698: Corrected sequence: Wie ben jij?
16:49:05:698: Translate corrected sequence: Wie ben jij?
16:49:05:698: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
16:49:05:699: Checking profanity for sequence: Wie ben jij?
16:49:05:699: Classify master context for input sequence: Wie ben jij?
16:49:05:699: Classified master context: Generic
16:49:05:699: Classify context for input sequence: Wie ben jij?
16:49:05:699: Classified context: Handshake
16:49:05:699: Handling dialog: NL/Generic/Handshake
16:49:05:699:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:05:699:     Find matches for sequence: Wie ben jij?
16:49:05:699:     Found matches for sequence: 2
16:49:05:699:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
16:49:05:699:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
16:49:05:699:     Set prompt variable: firstName
16:49:05:699:     Set dialog output: Mijn naam is Dyz Lecticus.
16:49:05:699:     Set dialog prompt: Wat is uw naam?
16:49:05:700: Handling dialog: NL/Generic/QuestionAndAnswer
16:49:05:700:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:05:700:     Find matches for sequence: Wie ben jij?
16:49:05:700:     Found matches for sequence: 16
16:49:05:700:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.2952243125904487 / 0.6476190476190476)
16:49:05:700:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
16:49:05:700:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
16:49:05:700:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
16:49:05:700:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
16:49:05:701:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14688856729377714 / 0.3222222222222222)
16:49:05:701:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.14603932511243795 / 0.3203592814371257)
16:49:05:701:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
16:49:05:701:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
16:49:05:701:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
16:49:05:701:     - [... 6]
16:49:05:701:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
16:49:05:701: Handling dialog: NL/Generic/Language
16:49:05:701:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
16:49:05:701:     Updated variable languages: Engels en Nederlands
16:49:05:701:     Find matches for sequence: Wie ben jij?
16:49:05:701:     Found matches for sequence: 2
16:49:05:701:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
16:49:05:701:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
16:49:05:701:     Set prompt variable: language
16:49:05:701:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 98 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
16:49:05:802: Classify language for sequence: Albert einstein.
16:49:05:804: Classify language for sequence: What is your name? Albert einstein.
16:49:05:804: Classified language: EN
16:49:05:805: Translate sequence: Albert einstein.
16:49:05:808: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
16:49:05:808: Correction time limit: 300
16:49:05:808: Correcting sequence: [] [] .
16:49:05:808: Corrected sequence: Albert einstein.
16:49:05:808: Translate corrected sequence: Albert einstein.
16:49:05:811: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
16:49:05:811: Checking profanity for sequence: [ABC] [ABC].
16:49:05:811: Classify master context for input sequence: Albert einstein.
16:49:05:811: Classify master context for classification sequence: What is your name? [ABC] [ABC].
16:49:05:811: Classified master context: Generic
16:49:05:811: Classify context for input sequence: Albert einstein.
16:49:05:811: Classify context for classification sequence: What is your name? [ABC] [ABC].
16:49:05:812: Classified context: Handshake
16:49:05:812: Handling dialog: EN/Generic/Handshake
16:49:05:812:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:05:812:     Updated variable firstName: UN_ABC:Albert = Albert
16:49:05:812:     Updated variable lastName: UN_ABC:Einstein = Einstein
16:49:05:812:     Find matches for sequence: [ABC] [ABC].
16:49:05:812:     Found matches for sequence: 0
16:49:05:812:     Updated session variable fullName: Albert Einstein
16:49:05:812:     Set prompt variable: nextDialog
16:49:05:812:     Set dialog output: 
16:49:05:812:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 10 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
16:49:05:813: Classify language for sequence: Mijn naam si gekste der henkies.
16:49:05:813: Classified language: NL
16:49:05:814: Translate sequence: Mijn naam si gekste der henkies.
16:49:05:815: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
16:49:05:815: Correction time limit: 700
16:49:05:815: Correcting sequence: Mijn naam si gekste [] henkies .
16:49:06:383: Corrected sequence: Mijn naam is gekste der henkies.
16:49:06:383: Translate corrected sequence: Mijn naam is gekste der henkies.
16:49:06:385: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
16:49:06:386: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:06:386: Classify master context for input sequence: Mijn naam is gekste der henkies.
16:49:06:386: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:06:386: Classified master context: Generic
16:49:06:386: Classify context for input sequence: Mijn naam is gekste der henkies.
16:49:06:386: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:06:386: Classified context: Handshake
16:49:06:386: Handling dialog: NL/Generic/Handshake
16:49:06:387:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:06:387:     Updated variable firstName: UN_ABC:Gekste = Gekste
16:49:06:387:     Updated variable lastName: UN_ABC:Henkies = Henkies
16:49:06:387:     Updated variable preposition: NL_PRE:6 = der
16:49:06:387:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:06:387:     Found matches for sequence: 0
16:49:06:387:     Updated session variable fullName: Gekste der Henkies
16:49:06:387:     Set prompt variable: nextDialog
16:49:06:387:     Set dialog output: 
16:49:06:387:     Set dialog prompt: Wat kan ik voor u doen Gekste der Henkies?
Handling the request took: 575 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
16:49:06:388: Classify language for sequence: Mijn naam is jan de lange.
16:49:06:388: Classified language: NL
16:49:06:390: Translate sequence: Mijn naam is jan de lange.
16:49:06:394: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|EN_NAM:firstName:UN_ABC:Jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
16:49:06:394: Correction time limit: 700
16:49:06:395: Correcting sequence: Mijn naam is [] [] [] .
16:49:06:395: Corrected sequence: Mijn naam is jan de lange.
16:49:06:395: Translate corrected sequence: Mijn naam is jan de lange.
16:49:06:398: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|EN_NAM:firstName:UN_ABC:Jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
16:49:06:398: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:06:398: Classify master context for input sequence: Mijn naam is jan de lange.
16:49:06:398: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
16:49:06:398: Classified master context: Generic
16:49:06:398: Classify context for input sequence: Mijn naam is jan de lange.
16:49:06:399: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
16:49:06:399: Classified context: Handshake
16:49:06:399: Handling dialog: NL/Generic/Handshake
16:49:06:399:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:06:399:     Updated variable firstName: UN_ABC:Jan = Jan
16:49:06:399:     Updated variable lastName: UN_ABC:Lange = Lange
16:49:06:399:     Updated variable preposition: NL_PRE:5 = de
16:49:06:399:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
16:49:06:399:     Found matches for sequence: 0
16:49:06:399:     Updated session variable fullName: Jan de Lange
16:49:06:399:     Set prompt variable: nextDialog
16:49:06:399:     Set dialog output: 
16:49:06:399:     Set dialog prompt: Wat kan ik voor u doen Jan de Lange?
Handling the request took: 12 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
16:49:06:399: Classify language for sequence: Who created you?
16:49:06:400: Classified language: EN
16:49:06:400: Translate sequence: Who created you?
16:49:06:400: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
16:49:06:400: Correction time limit: 400
16:49:06:400: Correcting sequence: Who created you ?
16:49:06:400: Corrected sequence: Who created you?
16:49:06:400: Translate corrected sequence: Who created you?
16:49:06:401: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
16:49:06:401: Checking profanity for sequence: Who created you?
16:49:06:401: Classify master context for input sequence: Who created you?
16:49:06:401: Classified master context: Generic
16:49:06:401: Classify context for input sequence: Who created you?
16:49:06:401: Classified context: QuestionAndAnswer
16:49:06:402: Handling dialog: EN/Generic/QuestionAndAnswer
16:49:06:402:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:06:402:     Find matches for sequence: Who created you?
16:49:06:402:     Found matches for sequence: 5
16:49:06:402:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.21657250470809794 / 0.9126984126984126)
16:49:06:402:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.06214689265536724 / 0.2619047619047619)
16:49:06:402:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:06:402:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:06:402:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
16:49:06:402:     Set dialog output: My software was written by André van der Zee.
16:49:06:402: Handling dialog: EN/Generic/Handshake
16:49:06:402:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
16:49:06:402:     Find matches for sequence: Who created you?
16:49:06:402:     Found matches for sequence: 1
16:49:06:402:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
16:49:06:402:     Set prompt variable: firstName
16:49:06:402:     Set dialog output: My name is Dyz Lecticus.
16:49:06:402:     Set dialog prompt: What is your name?
Handling the request took: 97 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
16:49:06:496: Classify language for sequence: You asshole!
16:49:06:496: Classified language: EN
16:49:06:496: Translate sequence: You asshole!
16:49:06:497: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
16:49:06:497: Correction time limit: 300
16:49:06:497: Correcting sequence: You [] !
16:49:06:497: Corrected sequence: You asshole!
16:49:06:497: Translate corrected sequence: You asshole!
16:49:06:497: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
16:49:06:498: Checking profanity for sequence: You [PRF]!
16:49:06:498: Handling dialog: EN/Generic/Profanity
16:49:06:498:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:06:498:     Find matches for sequence: You [PRF]!
16:49:06:498:     Found matches for sequence: 2
16:49:06:499:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
16:49:06:499:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
16:49:06:499:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 3 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
16:49:06:499: Classify language for sequence: Jij klootzak!
16:49:06:499: Classified language: NL
16:49:06:499: Translate sequence: Jij klootzak!
16:49:06:500: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
16:49:06:500: Correction time limit: 300
16:49:06:500: Correcting sequence: Jij [] !
16:49:06:500: Corrected sequence: Jij klootzak!
16:49:06:500: Translate corrected sequence: Jij klootzak!
16:49:06:500: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
16:49:06:500: Checking profanity for sequence: Jij [PRF]!
16:49:06:500: Handling dialog: NL/Generic/Profanity
16:49:06:500:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:06:500:     Find matches for sequence: Jij [PRF]!
16:49:06:500:     Found matches for sequence: 2
16:49:06:501:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
16:49:06:502:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
16:49:06:502:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 3 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
16:49:06:502: Classify language for sequence: Can I book a room for 5 people?
16:49:06:502: Classified language: EN
16:49:06:502: Translate sequence: Can I book a room for 5 people?
16:49:06:503: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
16:49:06:503: Correction time limit: 900
16:49:06:503: Correcting sequence: Can I book a room for [] people ?
16:49:06:503: Corrected sequence: Can I book a room for 5 people?
16:49:06:503: Translate corrected sequence: Can I book a room for 5 people?
16:49:06:504: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
16:49:06:504: Checking profanity for sequence: Can I book a room for [NUM] people?
16:49:06:505: Classify master context for input sequence: Can I book a room for 5 people?
16:49:06:505: Classify master context for classification sequence: Can I book a room for [NUM] people?
16:49:06:505: Classified master context: Room
16:49:06:505: Classify context for input sequence: Can I book a room for 5 people?
16:49:06:505: Classify context for classification sequence: Can I book a room for [NUM] people?
16:49:06:505: Classified context: Booking
16:49:06:505: Handling dialog: EN/Room/Booking
16:49:06:505:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
16:49:06:505:     Updated variable bookPeople: UN_NUM:5 = 5
16:49:06:505:     Find matches for sequence: Can I book a room for [NUM] people?
16:49:06:507:     Found matches for sequence: 329
16:49:06:508:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
16:49:06:508:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
16:49:06:508:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
16:49:06:508:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
16:49:06:508:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
16:49:06:508:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
16:49:06:508:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
16:49:06:508:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
16:49:06:508:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
16:49:06:508:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
16:49:06:508:     - [... 319]
16:49:06:509:     Set prompt variable: bookDate
16:49:06:509:     Set dialog output: Okay.
16:49:06:509:     Set dialog prompt: On what date?
Handling the request took: 94 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
16:49:06:596: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
16:49:06:597: Classified language: EN
16:49:06:597: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
16:49:06:600: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
16:49:06:600: Correction time limit: 1332
16:49:06:600: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
16:49:06:601: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
16:49:06:601: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
16:49:06:603: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
16:49:06:604: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
16:49:06:604: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
16:49:06:604: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
16:49:06:604: Classified master context: Room
16:49:06:604: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
16:49:06:605: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
16:49:06:605: Classified context: Booking
16:49:06:605: Handling dialog: EN/Room/Booking
16:49:06:605:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
16:49:06:605:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
16:49:06:605:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
16:49:06:605:     Updated variable bookDuration: EN_DUR:02:00 = two hours
16:49:06:605:     Updated variable bookPeople: UN_NUM:12 = 12
16:49:06:605:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
16:49:06:608:     Found matches for sequence: 263
16:49:06:608:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
16:49:06:608:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
16:49:06:608:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
16:49:06:608:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
16:49:06:608:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
16:49:06:608:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
16:49:06:608:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
16:49:06:609:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
16:49:06:609:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
16:49:06:609:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
16:49:06:609:     - [... 253]
16:49:06:609:     Set prompt variable: bookConfirmation
16:49:06:609:     Set dialog output: Okay.
16:49:06:609:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 100 ms

Request prompt: '', input: 'Ik wil morgen een kamer boeken om 1 uur.'
Response debug log;
16:49:06:696: Classify language for sequence: Ik wil morgen een kamer boeken om 1 uur.
16:49:06:697: Classified language: NL
16:49:06:697: Translate sequence: Ik wil morgen een kamer boeken om 1 uur.
16:49:06:699: Translated sequence: UN_ABC:Ik UN_ABC:wil NL_DAT:2018-07-17|UN_ABC:morgen NL_NUM:1|UN_ABC:een UN_ABC:kamer UN_ABC:boeken UN_ABC:om NL_DUR:01:00 .
16:49:06:699: Correction time limit: 900
16:49:06:699: Correcting sequence: Ik wil [] [] kamer boeken om [] .
16:49:06:699: Corrected sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
16:49:06:699: Translate corrected sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
16:49:06:700: Translated corrected sequence: UN_ABC:Ik UN_ABC:wil NL_DAT:2018-07-17 NL_NUM:1|UN_ABC:een UN_ABC:kamer UN_ABC:boeken UN_ABC:om NL_TIM:13:00:00|NL_DUR:01:00 .
16:49:06:701: Checking profanity for sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
16:49:06:701: Classify master context for input sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
16:49:06:701: Classify master context for classification sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
16:49:06:701: Classified master context: Room
16:49:06:701: Classify context for input sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
16:49:06:701: Classify context for classification sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
16:49:06:701: Classified context: Booking
16:49:06:701: Handling dialog: NL/Room/Booking
16:49:06:701:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchRoomBookingHandler
16:49:06:701:     Updated variable bookDate: NL_DAT:2018-07-17 = zeventien juli tweeduizendachttien
16:49:06:701:     Updated variable bookTime: NL_TIM:13:00:00 = een uur
16:49:06:701:     Updated variable bookPeople: NL_NUM:1 = een
16:49:06:701:     Find matches for sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
16:49:06:703:     Found matches for sequence: 275
16:49:06:703:     - Ik wil een kamer boeken om [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
16:49:06:703:     - Ik wil een kamer boeken om [TIM] van [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
16:49:06:703:     - Ik wil graag een kamer boeken om [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
16:49:06:703:     - Ik wil graag een kamer boeken om [TIM] van [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
16:49:06:703:     - Ik wil een kamer boeken om [TIM] voor [NUM] personen. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
16:49:06:703:     - Ik wil een kamer boeken om [TIM] op [DAT]. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
16:49:06:703:     - Ik wil graag een kamer boeken om [TIM] voor [NUM] personen. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
16:49:06:703:     - Ik wil graag een kamer boeken om [TIM] op [DAT]. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
16:49:06:703:     - Kan ik een kamer boeken om [TIM] voor [NUM] personen? [OUTPUT] Okee. (1.2078575952853052 / 0.5009434150826488)
16:49:06:703:     - Kan ik een kamer boeken om [TIM] op [DAT]? [OUTPUT] Okee. (1.2078575952853052 / 0.5009434150826488)
16:49:06:703:     - [... 265]
16:49:06:703:     Updated variable bookPeople: 
16:49:06:703:     Set prompt variable: bookDuration
16:49:06:703:     Set dialog output: Okee.
16:49:06:703:     Set dialog prompt: Hoe lang?
Handling the request took: 7 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
16:49:06:703: Classify language for sequence: What does it cost to book a room?
16:49:06:703: Classified language: EN
16:49:06:703: Translate sequence: What does it cost to book a room?
16:49:06:704: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
16:49:06:705: Correction time limit: 900
16:49:06:705: Correcting sequence: What does it cost to book a room ?
16:49:06:705: Corrected sequence: What does it cost to book a room?
16:49:06:705: Translate corrected sequence: What does it cost to book a room?
16:49:06:706: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
16:49:06:706: Checking profanity for sequence: What does it cost to book a room?
16:49:06:706: Classify master context for input sequence: What does it cost to book a room?
16:49:06:706: Classified master context: Room
16:49:06:706: Classify context for input sequence: What does it cost to book a room?
16:49:06:706: Classified context: QuestionAndAnswer
16:49:06:706: Handling dialog: EN/Room/QuestionAndAnswer
16:49:06:706:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:06:706:     Find matches for sequence: What does it cost to book a room?
16:49:06:706:     Found matches for sequence: 6
16:49:06:706:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
16:49:06:706:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
16:49:06:706:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5)
16:49:06:706:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5)
16:49:06:706:     - Are there any costs attached to booking van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.125)
16:49:06:706:     - Are there any costs attached to reserving van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.125)
16:49:06:706:     Set dialog output: There are no costs attached to booking a room.
16:49:06:706: Handling dialog: EN/Room/Booking
16:49:06:706:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
16:49:06:707:     Find matches for sequence: What does it cost to book a room?
16:49:06:709:     Found matches for sequence: 276
16:49:06:709:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:709:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:710:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:710:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
16:49:06:710:     - [... 266]
16:49:06:710:     Set prompt variable: bookDate
16:49:06:710:     Set dialog output: Okay.
16:49:06:710:     Set dialog prompt: On what date?
Handling the request took: 93 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
16:49:06:796: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
16:49:06:797: Classified language: EN
16:49:06:797: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
16:49:06:799: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|NL_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
16:49:06:800: Correction time limit: 1300
16:49:06:800: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
16:49:06:801: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
16:49:06:801: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
16:49:06:805: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|NL_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
16:49:06:805: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:805: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
16:49:06:806: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:806: Classified master context: Generic
16:49:06:806: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
16:49:06:806: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:806: Classified context: Math
16:49:06:806: Handling dialog: EN/Generic/Math
16:49:06:807:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
16:49:06:807:     Updated variable number1: EN_NUM:10 = ten
16:49:06:807:     Updated variable number2: EN_NUM:40 = fourty
16:49:06:807:     Updated variable number3: EN_NUM:20 = twenty
16:49:06:807:     Updated variable number4: EN_NUM:3 = three
16:49:06:807:     Updated variable number5: EN_NUM:6 = six
16:49:06:807:     Updated variable operator1: EN_MTH:M = multiplied by
16:49:06:807:     Updated variable operator2: EN_MTH:D = divided by
16:49:06:807:     Updated variable operator3: EN_MTH:A = plus
16:49:06:807:     Updated variable operator4: EN_MTH:M = multiplied by
16:49:06:807:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:807:     Found matches for sequence: 8
16:49:06:807:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (5.5 / 0.7521367521367521)
16:49:06:807:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (5.5 / 0.7521367521367521)
16:49:06:807:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (4.125 / 0.5641025641025641)
16:49:06:807:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (4.125 / 0.5641025641025641)
16:49:06:807:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.375 / 0.46153846153846156)
16:49:06:807:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (3.375 / 0.46153846153846156)
16:49:06:807:     - How much is [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (2.625 / 0.358974358974359)
16:49:06:807:     - How much is [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (2.625 / 0.358974358974359)
16:49:06:807:     Calculate expression: 10 M 40 D 20 A 3 M 6
16:49:06:808:     Calculated expression: 38.0
16:49:06:808:     Updated variable exact: exactly
16:49:06:808:     Updated variable result: thirtyeight
16:49:06:808:     Set prompt variable: nextDialog
16:49:06:808:     Set dialog output: Exactly thirtyeight.
16:49:06:808:     Set dialog prompt: What else can I do for you?
Handling the request took: 100 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
16:49:06:896: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
16:49:06:896: Classified language: NL
16:49:06:896: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
16:49:06:898: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig EN_MTH:A|NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
16:49:06:898: Correction time limit: 1200
16:49:06:898: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
16:49:06:899: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
16:49:06:899: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
16:49:06:900: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig EN_MTH:A|NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
16:49:06:900: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:900: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
16:49:06:900: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:901: Classified master context: Generic
16:49:06:901: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
16:49:06:901: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:901: Classified context: Math
16:49:06:901: Handling dialog: NL/Generic/Math
16:49:06:901:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
16:49:06:901:     Updated variable number1: NL_NUM:10 = tien
16:49:06:901:     Updated variable number2: NL_NUM:40 = veertig
16:49:06:901:     Updated variable number3: NL_NUM:20 = twintig
16:49:06:901:     Updated variable number4: NL_NUM:3 = drie
16:49:06:901:     Updated variable number5: NL_NUM:6 = zes
16:49:06:901:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
16:49:06:901:     Updated variable operator2: NL_MTH:D = gedeeld door
16:49:06:901:     Updated variable operator3: EN_MTH:A = plus
16:49:06:901:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
16:49:06:901:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
16:49:06:901:     Found matches for sequence: 8
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (5.318181818181818 / 0.7222222222222223)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (5.318181818181818 / 0.7222222222222223)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.8181818181818183 / 0.5185185185185186)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (3.8181818181818183 / 0.5185185185185186)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.0 / 0.4074074074074075)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (3.0 / 0.4074074074074075)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (2.1818181818181817 / 0.29629629629629634)
16:49:06:901:     - Hoeveel is [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (2.1818181818181817 / 0.29629629629629634)
16:49:06:901:     Calculate expression: 10 M 40 D 20 A 3 M 6
16:49:06:902:     Calculated expression: 38.0
16:49:06:902:     Updated variable exact: precies
16:49:06:902:     Updated variable result: achtendertig
16:49:06:902:     Set prompt variable: nextDialog
16:49:06:902:     Set dialog output: Precies achtendertig.
16:49:06:902:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 100 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
16:49:06:996: Classify language for sequence: What languages do you speak?
16:49:06:996: Classified language: EN
16:49:06:996: Translate sequence: What languages do you speak?
16:49:06:997: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
16:49:06:997: Correction time limit: 600
16:49:06:997: Correcting sequence: What languages do you speak ?
16:49:06:997: Corrected sequence: What languages do you speak?
16:49:06:997: Translate corrected sequence: What languages do you speak?
16:49:06:998: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
16:49:06:998: Checking profanity for sequence: What languages do you speak?
16:49:06:998: Classify master context for input sequence: What languages do you speak?
16:49:06:998: Classified master context: Generic
16:49:06:998: Classify context for input sequence: What languages do you speak?
16:49:06:998: Classified context: Language
16:49:06:998: Handling dialog: EN/Generic/Language
16:49:06:998:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
16:49:06:999:     Updated variable languages: English and Dutch
16:49:06:999:     Find matches for sequence: What languages do you speak?
16:49:06:999:     Found matches for sequence: 6
16:49:06:999:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1800000000000002 / 0.595959595959596)
16:49:06:999:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.76 / 0.38383838383838387)
16:49:06:999:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.6799999999999999 / 0.3434343434343434)
16:49:06:999:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.6799999999999999 / 0.3434343434343434)
16:49:06:999:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.6325000000000001 / 0.3194444444444445)
16:49:06:999:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.3 / 0.15151515151515152)
16:49:06:999:     Set prompt variable: language
16:49:06:999:     Set dialog output: I speak English and Dutch.
16:49:06:999: Handling dialog: EN/Generic/QuestionAndAnswer
16:49:06:999:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
16:49:06:999:     Find matches for sequence: What languages do you speak?
16:49:06:999:     Found matches for sequence: 17
16:49:06:999:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07721280602636535 / 0.2169312169312169)
16:49:06:999:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06756120527306968 / 0.18981481481481477)
16:49:06:999:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06756120527306968 / 0.18981481481481477)
16:49:06:999:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06756120527306968 / 0.18981481481481477)
16:49:06:999:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
16:49:06:999:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
16:49:06:999:     - What is the answer? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
16:49:06:999:     - What is the question? [OUTPUT] I do not know. (0.06746427384513128 / 0.1895424836601307)
16:49:06:999:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06746427384513128 / 0.1895424836601307)
16:49:06:999:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06746427384513128 / 0.1895424836601307)
16:49:06:999:     - [... 7]
16:49:06:999:     Set dialog output: My goal is to understand and help people.
Handling the request took: 3 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
16:49:07:000: Classify language for sequence: Spreek je duits?
16:49:07:000: Classified language: NL
16:49:07:000: Translate sequence: Spreek je duits?
16:49:07:000: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
16:49:07:000: Correction time limit: 400
16:49:07:000: Correcting sequence: Spreek je [] ?
16:49:07:001: Corrected sequence: Spreek je Duits?
16:49:07:001: Translate corrected sequence: Spreek je Duits?
16:49:07:001: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
16:49:07:001: Checking profanity for sequence: Spreek je [LNG]?
16:49:07:001: Classify master context for input sequence: Spreek je Duits?
16:49:07:001: Classify master context for classification sequence: Spreek je [LNG]?
16:49:07:001: Classified master context: Generic
16:49:07:001: Classify context for input sequence: Spreek je Duits?
16:49:07:001: Classify context for classification sequence: Spreek je [LNG]?
16:49:07:001: Classified context: Language
16:49:07:001: Handling dialog: NL/Generic/Language
16:49:07:001:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
16:49:07:002:     Updated variable language: NL_LNG:DE = Duits
16:49:07:002:     Updated variable languages: Engels en Nederlands
16:49:07:002:     Updated variable confirmation: Nee
16:49:07:002:     Find matches for sequence: Spreek je [LNG]?
16:49:07:002:     Found matches for sequence: 5
16:49:07:002:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.5079365079365077 / 0.9313725490196076)
16:49:07:002:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
16:49:07:002:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
16:49:07:002:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
16:49:07:002:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
16:49:07:002:     Set prompt variable: 
16:49:07:002:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 97 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
16:49:07:096: Classify language for sequence: Spreek je engels?
16:49:07:096: Classified language: NL
16:49:07:096: Translate sequence: Spreek je engels?
16:49:07:097: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
16:49:07:097: Correction time limit: 400
16:49:07:097: Correcting sequence: Spreek je [] ?
16:49:07:097: Corrected sequence: Spreek je Engels?
16:49:07:097: Translate corrected sequence: Spreek je Engels?
16:49:07:097: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
16:49:07:097: Checking profanity for sequence: Spreek je [LNG]?
16:49:07:097: Classify master context for input sequence: Spreek je Engels?
16:49:07:097: Classify master context for classification sequence: Spreek je [LNG]?
16:49:07:097: Classified master context: Generic
16:49:07:097: Classify context for input sequence: Spreek je Engels?
16:49:07:097: Classify context for classification sequence: Spreek je [LNG]?
16:49:07:097: Classified context: Language
16:49:07:098: Handling dialog: NL/Generic/Language
16:49:07:098:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
16:49:07:098:     Updated variable language: NL_LNG:EN = Engels
16:49:07:098:     Updated variable languages: Engels en Nederlands
16:49:07:098:     Updated variable confirmation: Ja
16:49:07:098:     Find matches for sequence: Spreek je [LNG]?
16:49:07:098:     Found matches for sequence: 5
16:49:07:098:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.5079365079365077 / 0.9313725490196076)
16:49:07:098:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
16:49:07:098:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
16:49:07:098:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
16:49:07:098:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
16:49:07:098:     Set prompt variable: 
16:49:07:098:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 2 ms

Request prompt: '', input: 'Wat kost overboeken naar buitenland?'
Response debug log;
16:49:07:098: Classify language for sequence: Wat kost overboeken naar buitenland?
16:49:07:098: Classified language: NL
16:49:07:098: Translate sequence: Wat kost overboeken naar buitenland?
16:49:07:099: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
16:49:07:099: Correction time limit: 600
16:49:07:099: Correcting sequence: Wat kost overboeken naar buitenland ?
16:49:07:099: Corrected sequence: Wat kost overboeken naar buitenland?
16:49:07:099: Translate corrected sequence: Wat kost overboeken naar buitenland?
16:49:07:100: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
16:49:07:100: Checking profanity for sequence: Wat kost overboeken naar buitenland?
16:49:07:100: Classify master context for input sequence: Wat kost overboeken naar buitenland?
16:49:07:100: Classified master context: ForeignTransfer
16:49:07:100: Classify context for input sequence: Wat kost overboeken naar buitenland?
16:49:07:100: Classified context: QuestionAndAnswer
16:49:07:100: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
16:49:07:101:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
16:49:07:101:     Updated variable durationDays: dagen
16:49:07:101:     Updated variable cost: vijf
16:49:07:101:     Updated variable costCurrency: euro
16:49:07:101:     Updated variable duration: drie
16:49:07:101:     Find matches for sequence: Wat kost overboeken naar buitenland?
16:49:07:101:     Found matches for sequence: 23
16:49:07:101:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.9481409001956947 / 0.5031152647975079)
16:49:07:101:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6550880626223092 / 0.3476116303219108)
16:49:07:101:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6550880626223092 / 0.3476116303219108)
16:49:07:101:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6550880626223092 / 0.3476116303219108)
16:49:07:101:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6550880626223092 / 0.3476116303219108)
16:49:07:101:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
16:49:07:101:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
16:49:07:101:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
16:49:07:101:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
16:49:07:101:     - Een buitenlandoverboeking. Wat kost dat? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
16:49:07:101:     - [... 13]
16:49:07:101:     Set prompt variable: 
16:49:07:101:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
16:49:07:101: Handling dialog: NL/ForeignTransfer/Cost
16:49:07:101:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
16:49:07:101:     Updated variable cost: vijf
16:49:07:101:     Updated variable costCurrency: euro
16:49:07:101:     Updated variable duration: drie
16:49:07:101:     Find matches for sequence: Wat kost overboeken naar buitenland?
16:49:07:101:     Found matches for sequence: 8
16:49:07:101:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5155502392344498 / 0.342063492063492)
16:49:07:101:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.31100478468899523 / 0.20634920634920634)
16:49:07:101:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.31100478468899523 / 0.20634920634920634)
16:49:07:101:     - Een overboeking naar [CNT]. Wat kost dat? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
16:49:07:101:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
16:49:07:101:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
16:49:07:101:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
16:49:07:101:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
Handling the request took: 98 ms

Request prompt: '', input: 'Wat kost overboeken naar italie?'
Response debug log;
16:49:07:196: Classify language for sequence: Wat kost overboeken naar italie?
16:49:07:196: Classified language: NL
16:49:07:196: Translate sequence: Wat kost overboeken naar italie?
16:49:07:197: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italie ?
16:49:07:198: Correction time limit: 600
16:49:07:198: Correcting sequence: Wat kost overboeken naar [] ?
16:49:07:198: Corrected sequence: Wat kost overboeken naar Italië?
16:49:07:198: Translate corrected sequence: Wat kost overboeken naar Italië?
16:49:07:199: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:Italië ?
16:49:07:199: Checking profanity for sequence: Wat kost overboeken naar [CNT]?
16:49:07:199: Classify master context for input sequence: Wat kost overboeken naar Italië?
16:49:07:199: Classify master context for classification sequence: Wat kost overboeken naar [CNT]?
16:49:07:199: Classified master context: ForeignTransfer
16:49:07:199: Classify context for input sequence: Wat kost overboeken naar Italië?
16:49:07:199: Classify context for classification sequence: Wat kost overboeken naar [CNT]?
16:49:07:199: Classified context: Cost
16:49:07:199: Handling dialog: NL/ForeignTransfer/Cost
16:49:07:199:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
16:49:07:199:     Updated variable cost: vijf
16:49:07:199:     Updated variable costCurrency: euro
16:49:07:199:     Updated variable duration: drie
16:49:07:200:     Updated variable transferToCountry: NL_CNT:IT = Italië
16:49:07:200:     Find matches for sequence: Wat kost overboeken naar [CNT]?
16:49:07:200:     Found matches for sequence: 14
16:49:07:200:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.8313397129186603 / 0.5515873015873015)
16:49:07:200:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7320574162679426 / 0.48571428571428565)
16:49:07:200:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7320574162679426 / 0.48571428571428565)
16:49:07:200:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
16:49:07:200:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
16:49:07:200:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
16:49:07:200:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
16:49:07:200:     - Wat zijn de kosten van een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
16:49:07:200:     - Zijn er kosten verbonden aan geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
16:49:07:200:     - Hoeveel kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
16:49:07:200:     - [... 4]
16:49:07:200:     Set prompt variable: 
16:49:07:200:     Set dialog output: Een overboeking naar Italië kost vijf euro.
16:49:07:200: Handling dialog: NL/ForeignTransfer/Duration
16:49:07:200:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
16:49:07:200:     Updated variable cost: vijf
16:49:07:200:     Updated variable costCurrency: euro
16:49:07:200:     Updated variable duration: drie
16:49:07:200:     Updated variable transferToCountry: NL_CNT:IT = Italië
16:49:07:200:     Find matches for sequence: Wat kost overboeken naar [CNT]?
16:49:07:200:     Found matches for sequence: 9
16:49:07:200:     - Hoeveel tijd kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.8907407407407407 / 0.6362433862433863)
16:49:07:200:     - Hoelang duurt geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.674074074074074 / 0.4814814814814815)
16:49:07:200:     - Hoeveel tijd kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.6018518518518519 / 0.42989417989417994)
16:49:07:200:     - Hoeveel tijd kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.6018518518518519 / 0.42989417989417994)
16:49:07:200:     - Hoelang duurt een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
16:49:07:200:     - Hoelang duurt het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
16:49:07:200:     - Hoelang duurt geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
16:49:07:200:     - Hoelang duurt het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
16:49:07:200:     - Een overboeking naar [CNT]. Hoe lang duurt dat? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.1851851851851852 / 0.1322751322751323)
16:49:07:200:     Updated variable durationDays: dagen
16:49:07:200:     Set prompt variable: 
16:49:07:200:     Set dialog output: Een overboeking naar Italië duurt drie dagen.
16:49:07:200: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
16:49:07:201:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
16:49:07:201:     Updated variable durationDays: dagen
16:49:07:201:     Updated variable cost: vijf
16:49:07:201:     Updated variable costCurrency: euro
16:49:07:201:     Updated variable duration: drie
16:49:07:201:     Find matches for sequence: Wat kost overboeken naar [CNT]?
16:49:07:201:     Found matches for sequence: 11
16:49:07:201:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6942270058708415 / 0.368380062305296)
16:49:07:201:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.40117416829745595 / 0.21287642782969887)
16:49:07:201:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.40117416829745595 / 0.21287642782969887)
16:49:07:201:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.40117416829745595 / 0.21287642782969887)
16:49:07:201:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.40117416829745595 / 0.21287642782969887)
16:49:07:201:     - Een buitenlandoverboeking. Wat kost dat? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
16:49:07:201:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
16:49:07:201:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
16:49:07:201:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
16:49:07:201:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
16:49:07:201:     - [... 1]
16:49:07:201:     Set prompt variable: 
16:49:07:201:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
Handling the request took: 100 ms

Request prompt: '', input: 'Je begrijpt me niet.'
Response debug log;
16:49:07:296: Classify language for sequence: Je begrijpt me niet.
16:49:07:296: Classified language: NL
16:49:07:296: Translate sequence: Je begrijpt me niet.
16:49:07:297: Translated sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
16:49:07:297: Correction time limit: 500
16:49:07:297: Correcting sequence: Je begrijpt me niet .
16:49:07:297: Corrected sequence: Je begrijpt me niet.
16:49:07:297: Translate corrected sequence: Je begrijpt me niet.
16:49:07:298: Translated corrected sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
16:49:07:298: Checking profanity for sequence: Je begrijpt me niet.
16:49:07:298: Classify master context for input sequence: Je begrijpt me niet.
16:49:07:298: Classified master context: Support
16:49:07:298: Classify context for input sequence: Je begrijpt me niet.
16:49:07:298: Classified context: Request
16:49:07:298: Handling dialog: NL/Support/Request
16:49:07:298:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequestHandler
16:49:07:298:     Find matches for sequence: Je begrijpt me niet.
16:49:07:298:     Selected filter context: supportNoTransferToHuman
16:49:07:298:     Found matches for sequence: 11
16:49:07:298:     - Jij begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.4803664921465969 / 0.752820512820513)
16:49:07:298:     - U begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.4803664921465969 / 0.752820512820513)
16:49:07:298:     - Je begrijpt mij niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.29728403141361254 / 0.46589743589743593)
16:49:07:298:     - Je begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.29728403141361254 / 0.46589743589743593)
16:49:07:298:     - Je kan me niet helpen. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16361256544502617 / 0.25641025641025644)
16:49:07:298:     - Je begrijpt mij verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
16:49:07:298:     - Jij begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
16:49:07:298:     - U begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
16:49:07:298:     - Je begrijpt er niks van. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
16:49:07:298:     - Jij begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.1557591623036649 / 0.24410256410256412)
16:49:07:298:     - [... 1]
16:49:07:298:     Set prompt variable: nextDialog
16:49:07:298:     Set dialog output: Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar dyz.lecticus@zeesoft.nl.
16:49:07:298:     Set dialog prompt: Kan ik u ergens anders mee proberen te helpen?
Handling the request took: 3 ms

Request prompt: '', input: 'Je begrijpt me niet.'
Response debug log;
16:49:07:299: Classify language for sequence: Je begrijpt me niet.
16:49:07:299: Classified language: NL
16:49:07:299: Translate sequence: Je begrijpt me niet.
16:49:07:301: Translated sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
16:49:07:301: Correction time limit: 500
16:49:07:301: Correcting sequence: Je begrijpt me niet .
16:49:07:302: Corrected sequence: Je begrijpt me niet.
16:49:07:302: Translate corrected sequence: Je begrijpt me niet.
16:49:07:303: Translated corrected sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
16:49:07:303: Checking profanity for sequence: Je begrijpt me niet.
16:49:07:303: Classify master context for input sequence: Je begrijpt me niet.
16:49:07:303: Classified master context: Support
16:49:07:303: Classify context for input sequence: Je begrijpt me niet.
16:49:07:303: Classified context: Request
16:49:07:303: Handling dialog: NL/Support/Request
16:49:07:304:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequestHandler
16:49:07:304:     Find matches for sequence: Je begrijpt me niet.
16:49:07:304:     Selected filter context: supportTransferToHuman
16:49:07:304:     Found matches for sequence: 11
16:49:07:304:     - Jij begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. (0.9943181818181818 / 0.7070707070707071)
16:49:07:304:     - U begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. (0.9943181818181818 / 0.7070707070707071)
16:49:07:304:     - Je begrijpt mij niet. [OUTPUT] Ik ben nog aan het leren. (0.6086647727272727 / 0.43282828282828284)
16:49:07:304:     - Je begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. (0.6086647727272727 / 0.43282828282828284)
16:49:07:304:     - Je kan me niet helpen. [OUTPUT] Ik ben nog aan het leren. (0.34659090909090906 / 0.24646464646464644)
16:49:07:304:     - Je begrijpt mij verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
16:49:07:304:     - Jij begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
16:49:07:304:     - U begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
16:49:07:304:     - Je begrijpt er niks van. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
16:49:07:304:     - Jij begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. (0.3125 / 0.2222222222222222)
16:49:07:304:     - [... 1]
16:49:07:304:     Set prompt variable: supportConfirmation
16:49:07:304:     Set dialog output: Ik ben nog aan het leren.
16:49:07:304:     Set dialog prompt: Zal ik u doorverbinden met een mens?
Handling the request took: 97 ms
~~~~

nl.zeesoft.zsd.test.TestDialogHandlerTester
-------------------------------------------
This test show how to use a *DialogHandlerTester* to test dialog handling for a *DialogHandlerConfiguration*.

**Example implementation**  
~~~~
// Create the DialogHandlerConfiguration
DialogHandlerConfiguration config = new DialogHandlerConfiguration();
// Start the initialization (and wait until it's done)
config.initialize();
// Create the tester
DialogHandlerTester tester = new DialogHandlerTester(config);
// Initialize the tester
tester.initialize();
// Start the tester (and wait until it's done)
tester.start();
// Get the test results summary
JsFile summary = tester.getSummary();
~~~~

Class references;  
 * [TestDialogHandlerTester](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestDialogHandlerTester.java)
 * [DialogHandlerTester](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerTester.java)
 * [DialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerConfiguration.java)

**Test output**  
The output of this test shows the test results summary.  
~~~~
{
  "timeStamp": "2018-08-18 16:49:22:558",
  "totals": {
    "tests": 1371,
    "successful": 1347,
    "successPercentage": 98.24945,
    "durationMs": 13982,
    "averageRequestMs": 3,
    "errorsPerDialog": [
      {
        "id": "NL/ForeignTransfer/QuestionAndAnswer",
        "errors": 13
      },
      {
        "id": "EN/ForeignTransfer/QuestionAndAnswer",
        "errors": 9
      },
      {
        "id": "NL/Generic/Goodbye",
        "errors": 1
      },
      {
        "id": "EN/Generic/QuestionAndAnswer",
        "errors": 1
      }
    ],
    "outputTests": 1196,
    "succesfulOutputTests": 1172,
    "successPercentageOutputTests": 97.99331,
    "averageOutputTestRequestMs": 3
  },
  "errors": [
    {
      "dialog": "EN/Generic/QuestionAndAnswer",
      "input": "Who is André van der Zee?",
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.972095668805503)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "Money transfer to a foreign country. How much does that cost?",
      "error": "Response context does not match expected context: Cost (0.9025900025900028) <> QuestionAndAnswer (0.7968790468790469)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "What does it cost to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6445286195286196) <> QuestionAndAnswer (0.5777918069584739)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "What are the costs of transferring money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.762149270482604) <> QuestionAndAnswer (0.7156285072951742)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much does it cost to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6876974876974878) <> QuestionAndAnswer (0.5845312095312097)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "Are there any costs attached to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.7555555555555558) <> QuestionAndAnswer (0.6523892773892777)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "What does it cost to transfer money abroad?",
      "error": "Response context does not match expected context: Cost (0.7646090534979425) <> QuestionAndAnswer (0.6037224092779648)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much does it cost to transfer abroad?",
      "error": "Response context does not match expected context: Cost (0.7842124953236065) <> QuestionAndAnswer (0.6159745604190049)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "Are there any costs attached to transferring money abroad?",
      "error": "Response context does not match expected context: Cost (0.8292929292929295) <> QuestionAndAnswer (0.775294612794613)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much time does it take to transfer money abroad?",
      "error": "Response context does not match expected context: Duration (0.676659063755838) <> QuestionAndAnswer (0.6199494949494951)"
    },
    {
      "dialog": "NL/Generic/Goodbye",
      "input": "Tot de volgende keer.",
      "error": "Response output does not match expected output",
      "output": "Tot de volgende keer.",
      "expectedOutput": "Dag."
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.8935810810810809) <> QuestionAndAnswer (0.7469916779127305)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat zijn de kosten van een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Cost (1.0) <> QuestionAndAnswer (0.7978067748230566)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9215882549215881) <> QuestionAndAnswer (0.851645674014095)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9215882549215881) <> QuestionAndAnswer (0.8451979307242464)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Zijn er kosten verbonden aan geld overmaken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9599144599144599) <> QuestionAndAnswer (0.8730626099047153)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Zijn er kosten verbonden aan geld overboeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9599144599144599) <> QuestionAndAnswer (0.8730626099047153)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9043418418418417) <> QuestionAndAnswer (0.7518274853801169)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9287620954287619) <> QuestionAndAnswer (0.8548695456590194)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9287620954287619) <> QuestionAndAnswer (0.8484218023691708)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoelang duurt een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.8931327160493826) <> QuestionAndAnswer (0.7663349077822762)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoelang duurt het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.8973765432098767) <> QuestionAndAnswer (0.8645411605937923)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoelang duurt het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.8752572016460906) <> QuestionAndAnswer (0.8580934173039436)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel tijd kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.9382716049382719) <> QuestionAndAnswer (0.8751859925948996)"
    }
  ]
}
~~~~

Test results
------------
All 20 tests have been executed successfully (289 assertions).  
Total test duration: 69106 ms (total sleep duration: 33700 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 651 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 428 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 460 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 467 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 476 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117817 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117833 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117828 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 118145 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117929 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117963 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117938 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 473937 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 473938 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 473939 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 473939 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 473946 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 478812 Kb / 467 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 478833 Kb / 467 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerTester: 478862 Kb / 467 Mb
