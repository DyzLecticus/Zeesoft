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
Initializing the SymbolCorrector took: 5 ms
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
Average correction time: 209 ms
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
Initializing the SequenceClassifier took: 2472 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 2 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 1 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 3 ms

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
Initializing the SequenceMatcher took: 4186 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 18 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 16 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 12 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 17 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 11 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 17 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 22 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 30 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 28 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Geldt mijn ABN AMRO Rechtsbijstandverzekering ook in het buitenland? [OUTPUT] Dit is afhankelijk van welke modules u heeft afgesloten. Bekijk uw polisblad voor uw dekkingen of bekijk de modules.'
Matching the input sequence took: 34 ms

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
Initializing the EntityValueTranslator took: 1370 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Eat EN_NUM:3|UN_ABC:three UN_ABC:donuts UN_ABC:at UN_TIM:09:00:00 UN_ABC:or UN_ABC:count UN_ABC:to UN_NUM:110'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 1 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 0 ms
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
Translating the sequence took: 1 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 0 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 2 ms
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
Retranslating the sequence took: 1 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam si gekste der henkies.'

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
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?'
Retranslating the sequence took: 0 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 0 ms
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
Retranslating the sequence took: 0 ms
Retranslation: 'fifteen british pound sterling'

Sequence: 'vijftien euro'
Translating the sequence took: 1 ms
Translation: 'NL_NUM:15|UN_ABC:vijftien EN_CUR:EUR|NL_CUR:EUR|UN_ABC:euro'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien euro'
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
Converting 333841 entity values took: 363 ms

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
Converting 1618 dialog examples took: 118 ms

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
  "classificationSequence": "Test classification sequence.",
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
Initializing Translator took 2309 ms
Initializing Classifier took 3594 ms
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

Initializing LanguagePreprocessor took 18 ms
Initializing LanguageContextENSupport took 35 ms
Initializing LanguageContextENGeneric took 45 ms
Initializing LanguageContextENForeignTransfer took 33 ms
Initializing LanguageContextNLSupport took 41 ms
Initializing LanguageContextNLForeignTransfer took 56 ms
Initializing LanguageContextNLGeneric took 145 ms
Initializing LanguageMasterContextEN took 235 ms
Initializing LanguageContextNLRoom took 224 ms
Initializing LanguageContextENRoom took 272 ms
Initializing LanguageMasterContextNL took 329 ms
Initializing EntityValueTranslator took 2020 ms
Initializing LanguageClassifier took 27250 ms
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
'EN': 0.23014597452812524 / 0.993287195431769
'NL': 0.07877032686744392 / 0.33996491669179946

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.23014597452812524 / 0.993287195431769
~~~~

nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier
-------------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as master context classifiers.
~~~~
Classified sequence: 'Who are you?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Who are you?' -> Generic
Classifying the input sequence took: 1 ms
Context probabilities for 'Who are you?', threshold: 0.0
'Generic': 0.2590846027315587 / 0.8721142464808642

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.3385296807031824 / 1.0
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
'QuestionAndAnswer': 0.29309122070860794 / 0.7097316636851521
'Handshake': 0.1753073537702904 / 0.4245134997452878

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.32121880854683177 / 0.5312612869499098
'Handshake': 0.28008179416834095 / 0.4632250990975794

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.40359483999091195 / 0.9773204279164545
'QuestionAndAnswer': 0.11747584291244349 / 0.2844722719141324

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.596783306646541 / 0.9870152652441594
'QuestionAndAnswer': 0.14632368998770157 / 0.2420036118239711

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.3047191276853521 / 0.9223613595706619
'Handshake': 0.23491000277691293 / 0.7110545084055018
'Thanks': 0.06920303773529152 / 0.2094722719141324

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 1 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.33391722124524437 / 0.690328866077369
'Handshake': 0.3110677096612987 / 0.6430905794045711
'Language': 0.1210694929014987 / 0.2502948648158229
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
20:22:25:084: Classify language for sequence: Hallo.
20:22:25:084: Classified language: NL
20:22:25:084: Translate sequence: Hallo.
20:22:25:085: Translated sequence: UN_ABC:Hallo .
20:22:25:085: Correction time limit: 200
20:22:25:085: Correcting sequence: Hallo .
20:22:25:085: Corrected sequence: Hallo.
20:22:25:085: Translate corrected sequence: Hallo.
20:22:25:085: Translated corrected sequence: UN_ABC:Hallo .
20:22:25:086: Checking profanity for sequence: Hallo.
20:22:25:086: Classify master context for input sequence: Hallo.
20:22:25:086: Classified master context: Generic
20:22:25:086: Classify context for input sequence: Hallo.
20:22:25:086: Classified context: Handshake
Interpreting the request took: 2 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
20:22:25:086: Classify language for sequence: Mijn naam si gekste der henkies.
20:22:25:087: Classified language: NL
20:22:25:087: Translate sequence: Mijn naam si gekste der henkies.
20:22:25:089: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
20:22:25:089: Correction time limit: 700
20:22:25:089: Correcting sequence: Mijn naam si gekste [] henkies .
20:22:25:435: Corrected sequence: Mijn naam is gekste der henkies.
20:22:25:435: Translate corrected sequence: Mijn naam is gekste der henkies.
20:22:25:436: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
20:22:25:436: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:25:436: Classify master context for input sequence: Mijn naam is gekste der henkies.
20:22:25:436: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:25:437: Classified master context: Generic
20:22:25:437: Classify context for input sequence: Mijn naam is gekste der henkies.
20:22:25:437: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:25:437: Classified context: Handshake
Interpreting the request took: 351 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
20:22:25:437: Classify language for sequence: Albert einstein.
20:22:25:437: Classify language for sequence: What is your name? Albert einstein.
20:22:25:437: Classified language: EN
20:22:25:438: Translate sequence: Albert einstein.
20:22:25:439: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
20:22:25:439: Correction time limit: 300
20:22:25:439: Correcting sequence: [] [] .
20:22:25:439: Corrected sequence: Albert einstein.
20:22:25:439: Translate corrected sequence: Albert einstein.
20:22:25:440: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
20:22:25:440: Checking profanity for sequence: [ABC] [ABC].
20:22:25:440: Classify master context for input sequence: Albert einstein.
20:22:25:440: Classify master context for classification sequence: What is your name? [ABC] [ABC].
20:22:25:440: Classified master context: Generic
20:22:25:440: Classify context for input sequence: Albert einstein.
20:22:25:440: Classify context for classification sequence: What is your name? [ABC] [ABC].
20:22:25:441: Classified context: Handshake
Interpreting the request took: 4 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
20:22:25:441: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:22:25:441: Selected primary language: EN
20:22:25:441: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:22:25:443: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
20:22:25:444: Correction time limit: 1100
20:22:25:444: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
20:22:26:544: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:22:26:545: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:22:26:546: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
20:22:26:546: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
20:22:26:546: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
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

Initializing DialogSet took 249 ms
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
20:22:28:782: Classify language for sequence: What are you?
20:22:28:782: Classified language: EN
20:22:28:782: Translate sequence: What are you?
20:22:28:783: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
20:22:28:783: Correction time limit: 400
20:22:28:783: Correcting sequence: What are you ?
20:22:28:783: Corrected sequence: What are you?
20:22:28:783: Translate corrected sequence: What are you?
20:22:28:784: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
20:22:28:784: Checking profanity for sequence: What are you?
20:22:28:784: Classify master context for input sequence: What are you?
20:22:28:784: Classified master context: Generic
20:22:28:784: Classify context for input sequence: What are you?
20:22:28:784: Classified context: QuestionAndAnswer
20:22:28:784: Handling dialog: EN/Generic/QuestionAndAnswer
20:22:28:785:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:28:785:     Find matches for sequence: What are you?
20:22:28:785:     Found matches for sequence: 10
20:22:28:785:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.2090395480225989 / 0.8809523809523809)
20:22:28:785:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.0696798493408663 / 0.2936507936507936)
20:22:28:786:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.0696798493408663 / 0.2936507936507936)
20:22:28:786:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
20:22:28:786:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
20:22:28:786:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
20:22:28:786:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:28:786:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:28:786:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:28:787:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:28:787:     Set dialog output: I am an artificially intelligent personal assistant.
20:22:28:787: Handling dialog: EN/Generic/Handshake
20:22:28:787:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:28:788:     Find matches for sequence: What are you?
20:22:28:788:     Found matches for sequence: 2
20:22:28:788:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
20:22:28:788:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
20:22:28:788:     Set prompt variable: firstName
20:22:28:788:     Set dialog output: My name is Dyz Lecticus.
20:22:28:788:     Set dialog prompt: What is your name?
Handling the request took: 42 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
20:22:28:824: Classify language for sequence: Wat ben jij?
20:22:28:824: Classified language: NL
20:22:28:824: Translate sequence: Wat ben jij?
20:22:28:825: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
20:22:28:825: Correction time limit: 400
20:22:28:825: Correcting sequence: Wat ben jij ?
20:22:28:825: Corrected sequence: Wat ben jij?
20:22:28:825: Translate corrected sequence: Wat ben jij?
20:22:28:826: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
20:22:28:826: Checking profanity for sequence: Wat ben jij?
20:22:28:826: Classify master context for input sequence: Wat ben jij?
20:22:28:826: Classified master context: Generic
20:22:28:826: Classify context for input sequence: Wat ben jij?
20:22:28:826: Classified context: QuestionAndAnswer
20:22:28:826: Handling dialog: NL/Generic/QuestionAndAnswer
20:22:28:826:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:28:826:     Find matches for sequence: Wat ben jij?
20:22:28:827:     Found matches for sequence: 9
20:22:28:827:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.4464544138929089 / 0.9793650793650793)
20:22:28:827:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
20:22:28:827:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
20:22:28:827:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
20:22:28:827:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
20:22:28:827:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14688856729377714 / 0.3222222222222222)
20:22:28:827:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
20:22:28:827:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
20:22:28:827:     - Ben jij menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
20:22:28:827:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
20:22:28:827: Handling dialog: NL/Generic/Handshake
20:22:28:827:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:28:827:     Find matches for sequence: Wat ben jij?
20:22:28:827:     Found matches for sequence: 5
20:22:28:827:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
20:22:28:827:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
20:22:28:827:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
20:22:28:827:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
20:22:28:827:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
20:22:28:827:     Set prompt variable: firstName
20:22:28:828:     Set dialog output: Mijn naam is Dyz Lecticus.
20:22:28:828:     Set dialog prompt: Wat is uw naam?
20:22:28:828: Handling dialog: NL/Generic/Language
20:22:28:828:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
20:22:28:828:     Updated variable languages: Engels en Nederlands
20:22:28:828:     Find matches for sequence: Wat ben jij?
20:22:28:828:     Found matches for sequence: 2
20:22:28:828:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
20:22:28:828:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
20:22:28:828:     Set prompt variable: language
20:22:28:828:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 100 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
20:22:28:924: Classify language for sequence: What is the answer?
20:22:28:924: Classified language: EN
20:22:28:924: Translate sequence: What is the answer?
20:22:28:925: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
20:22:28:925: Correction time limit: 500
20:22:28:925: Correcting sequence: What is [] answer ?
20:22:28:925: Corrected sequence: What is the answer?
20:22:28:925: Translate corrected sequence: What is the answer?
20:22:28:926: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
20:22:28:926: Checking profanity for sequence: What is [PRE] answer?
20:22:28:926: Classify master context for input sequence: What is the answer?
20:22:28:926: Classify master context for classification sequence: What is [PRE] answer?
20:22:28:926: Selected input sequence master context classification.
20:22:28:926: Classified master context: Generic
20:22:28:926: Classify context for input sequence: What is the answer?
20:22:28:926: Classified context: QuestionAndAnswer
20:22:28:926: Handling dialog: EN/Generic/QuestionAndAnswer
20:22:28:926:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:28:926:     Find matches for sequence: What is the answer?
20:22:28:926:     Found matches for sequence: 9
20:22:28:926:     - What is the answer? [OUTPUT] Fourtytwo. (0.24858757062146894 / 0.838095238095238)
20:22:28:926:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.1713747645951036 / 0.5777777777777777)
20:22:28:927:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.1713747645951036 / 0.5777777777777777)
20:22:28:927:     - What is the question? [OUTPUT] I do not know. (0.1016949152542373 / 0.3428571428571428)
20:22:28:927:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.1016949152542373 / 0.3428571428571428)
20:22:28:927:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.1016949152542373 / 0.3428571428571428)
20:22:28:927:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.1016949152542373 / 0.3428571428571428)
20:22:28:927:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.04708097928436912 / 0.15873015873015872)
20:22:28:927:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.04708097928436912 / 0.15873015873015872)
20:22:28:927:     Set dialog output: Fourtytwo.
Handling the request took: 3 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
20:22:28:927: Classify language for sequence: Wat is het antwoord?
20:22:28:927: Classified language: NL
20:22:28:927: Translate sequence: Wat is het antwoord?
20:22:28:928: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
20:22:28:928: Correction time limit: 500
20:22:28:928: Correcting sequence: Wat is het antwoord ?
20:22:28:928: Corrected sequence: Wat is het antwoord?
20:22:28:928: Translate corrected sequence: Wat is het antwoord?
20:22:28:929: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
20:22:28:929: Checking profanity for sequence: Wat is het antwoord?
20:22:28:929: Classify master context for input sequence: Wat is het antwoord?
20:22:28:929: Classified master context: Generic
20:22:28:929: Classify context for input sequence: Wat is het antwoord?
20:22:28:929: Classified context: QuestionAndAnswer
20:22:28:929: Handling dialog: NL/Generic/QuestionAndAnswer
20:22:28:929:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:28:929:     Find matches for sequence: Wat is het antwoord?
20:22:28:930:     Found matches for sequence: 10
20:22:28:930:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.58465991316932 / 1.0)
20:22:28:930:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.4334298118668597 / 0.7413366336633662)
20:22:28:930:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.4334298118668597 / 0.7413366336633662)
20:22:28:930:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.28509406657018815 / 0.48762376237623756)
20:22:28:930:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.13820549927641101 / 0.23638613861386135)
20:22:28:930:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.13820549927641101 / 0.23638613861386135)
20:22:28:930:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.13820549927641101 / 0.23638613861386135)
20:22:28:930:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.13820549927641101 / 0.23638613861386135)
20:22:28:930:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.13820549927641101 / 0.23638613861386135)
20:22:28:930:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.13820549927641101 / 0.23638613861386135)
20:22:28:930:     Set dialog output: Tweeenveertig.
Handling the request took: 97 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
20:22:29:024: Classify language for sequence: Hallo.
20:22:29:024: Classified language: NL
20:22:29:024: Translate sequence: Hallo.
20:22:29:024: Translated sequence: UN_ABC:Hallo .
20:22:29:024: Correction time limit: 200
20:22:29:024: Correcting sequence: Hallo .
20:22:29:024: Corrected sequence: Hallo.
20:22:29:024: Translate corrected sequence: Hallo.
20:22:29:025: Translated corrected sequence: UN_ABC:Hallo .
20:22:29:025: Checking profanity for sequence: Hallo.
20:22:29:025: Classify master context for input sequence: Hallo.
20:22:29:025: Classified master context: Generic
20:22:29:025: Classify context for input sequence: Hallo.
20:22:29:025: Classified context: Handshake
20:22:29:025: Handling dialog: NL/Generic/Handshake
20:22:29:025:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:29:025:     Find matches for sequence: Hallo.
20:22:29:025:     Found matches for sequence: 1
20:22:29:025:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
20:22:29:025:     Set prompt variable: firstName
20:22:29:025:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
20:22:29:025:     Set dialog prompt: Wat is uw naam?
Handling the request took: 1 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
20:22:29:025: Classify language for sequence: Wie ben jij?
20:22:29:026: Classified language: NL
20:22:29:026: Translate sequence: Wie ben jij?
20:22:29:026: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
20:22:29:026: Correction time limit: 400
20:22:29:026: Correcting sequence: Wie ben jij ?
20:22:29:026: Corrected sequence: Wie ben jij?
20:22:29:026: Translate corrected sequence: Wie ben jij?
20:22:29:027: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
20:22:29:027: Checking profanity for sequence: Wie ben jij?
20:22:29:027: Classify master context for input sequence: Wie ben jij?
20:22:29:027: Classified master context: Generic
20:22:29:027: Classify context for input sequence: Wie ben jij?
20:22:29:027: Classified context: Handshake
20:22:29:027: Handling dialog: NL/Generic/Handshake
20:22:29:027:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:29:028:     Find matches for sequence: Wie ben jij?
20:22:29:028:     Found matches for sequence: 2
20:22:29:028:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
20:22:29:028:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
20:22:29:028:     Set prompt variable: firstName
20:22:29:028:     Set dialog output: Mijn naam is Dyz Lecticus.
20:22:29:028:     Set dialog prompt: Wat is uw naam?
20:22:29:028: Handling dialog: NL/Generic/QuestionAndAnswer
20:22:29:028:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:29:028:     Find matches for sequence: Wie ben jij?
20:22:29:028:     Found matches for sequence: 16
20:22:29:028:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.2952243125904487 / 0.6476190476190476)
20:22:29:028:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
20:22:29:028:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
20:22:29:029:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
20:22:29:029:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
20:22:29:029:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14688856729377714 / 0.3222222222222222)
20:22:29:029:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.14603932511243795 / 0.3203592814371257)
20:22:29:029:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
20:22:29:029:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
20:22:29:029:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
20:22:29:029:     - [... 6]
20:22:29:029:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
20:22:29:029: Handling dialog: NL/Generic/Language
20:22:29:029:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
20:22:29:029:     Updated variable languages: Engels en Nederlands
20:22:29:029:     Find matches for sequence: Wie ben jij?
20:22:29:029:     Found matches for sequence: 2
20:22:29:029:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
20:22:29:029:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
20:22:29:029:     Set prompt variable: language
20:22:29:029:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 99 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
20:22:29:124: Classify language for sequence: Albert einstein.
20:22:29:124: Classify language for sequence: What is your name? Albert einstein.
20:22:29:124: Classified language: EN
20:22:29:125: Translate sequence: Albert einstein.
20:22:29:126: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
20:22:29:126: Correction time limit: 300
20:22:29:126: Correcting sequence: [] [] .
20:22:29:127: Corrected sequence: Albert einstein.
20:22:29:127: Translate corrected sequence: Albert einstein.
20:22:29:128: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
20:22:29:128: Checking profanity for sequence: [ABC] [ABC].
20:22:29:128: Classify master context for input sequence: Albert einstein.
20:22:29:128: Classify master context for classification sequence: What is your name? [ABC] [ABC].
20:22:29:128: Classified master context: Generic
20:22:29:128: Classify context for input sequence: Albert einstein.
20:22:29:129: Classify context for classification sequence: What is your name? [ABC] [ABC].
20:22:29:129: Classified context: Handshake
20:22:29:129: Handling dialog: EN/Generic/Handshake
20:22:29:129:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:29:129:     Updated variable firstName: UN_ABC:Albert = Albert
20:22:29:129:     Updated variable lastName: UN_ABC:Einstein = Einstein
20:22:29:129:     Find matches for sequence: [ABC] [ABC].
20:22:29:129:     Found matches for sequence: 0
20:22:29:129:     Updated session variable fullName: Albert Einstein
20:22:29:129:     Set prompt variable: nextDialog
20:22:29:129:     Set dialog output: 
20:22:29:129:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 5 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
20:22:29:130: Classify language for sequence: Mijn naam si gekste der henkies.
20:22:29:130: Classified language: NL
20:22:29:130: Translate sequence: Mijn naam si gekste der henkies.
20:22:29:131: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
20:22:29:131: Correction time limit: 700
20:22:29:131: Correcting sequence: Mijn naam si gekste [] henkies .
20:22:29:501: Corrected sequence: Mijn naam is gekste der henkies.
20:22:29:501: Translate corrected sequence: Mijn naam is gekste der henkies.
20:22:29:503: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
20:22:29:503: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:29:503: Classify master context for input sequence: Mijn naam is gekste der henkies.
20:22:29:503: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:29:503: Classified master context: Generic
20:22:29:503: Classify context for input sequence: Mijn naam is gekste der henkies.
20:22:29:503: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:29:503: Classified context: Handshake
20:22:29:504: Handling dialog: NL/Generic/Handshake
20:22:29:504:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:29:504:     Updated variable firstName: UN_ABC:Gekste = Gekste
20:22:29:504:     Updated variable lastName: UN_ABC:Henkies = Henkies
20:22:29:504:     Updated variable preposition: NL_PRE:6 = der
20:22:29:504:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:29:504:     Found matches for sequence: 0
20:22:29:504:     Updated session variable fullName: Gekste der Henkies
20:22:29:504:     Set prompt variable: nextDialog
20:22:29:504:     Set dialog output: 
20:22:29:504:     Set dialog prompt: Wat kan ik voor u doen Gekste der Henkies?
Handling the request took: 375 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
20:22:29:505: Classify language for sequence: Mijn naam is jan de lange.
20:22:29:505: Classified language: NL
20:22:29:506: Translate sequence: Mijn naam is jan de lange.
20:22:29:508: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
20:22:29:509: Correction time limit: 700
20:22:29:509: Correcting sequence: Mijn naam is [] [] [] .
20:22:29:509: Corrected sequence: Mijn naam is jan de lange.
20:22:29:509: Translate corrected sequence: Mijn naam is jan de lange.
20:22:29:511: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
20:22:29:512: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:29:512: Classify master context for input sequence: Mijn naam is jan de lange.
20:22:29:512: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
20:22:29:512: Classified master context: Generic
20:22:29:512: Classify context for input sequence: Mijn naam is jan de lange.
20:22:29:512: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
20:22:29:512: Classified context: Handshake
20:22:29:513: Handling dialog: NL/Generic/Handshake
20:22:29:513:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:29:513:     Updated variable firstName: UN_ABC:Jan = Jan
20:22:29:513:     Updated variable lastName: UN_ABC:Lange = Lange
20:22:29:513:     Updated variable preposition: NL_PRE:5 = de
20:22:29:513:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
20:22:29:513:     Found matches for sequence: 0
20:22:29:513:     Updated session variable fullName: Jan de Lange
20:22:29:513:     Set prompt variable: nextDialog
20:22:29:513:     Set dialog output: 
20:22:29:513:     Set dialog prompt: Wat kan ik voor u doen Jan de Lange?
Handling the request took: 21 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
20:22:29:525: Classify language for sequence: Who created you?
20:22:29:525: Classified language: EN
20:22:29:525: Translate sequence: Who created you?
20:22:29:526: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
20:22:29:526: Correction time limit: 400
20:22:29:526: Correcting sequence: Who created you ?
20:22:29:526: Corrected sequence: Who created you?
20:22:29:526: Translate corrected sequence: Who created you?
20:22:29:527: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
20:22:29:527: Checking profanity for sequence: Who created you?
20:22:29:527: Classify master context for input sequence: Who created you?
20:22:29:527: Classified master context: Generic
20:22:29:527: Classify context for input sequence: Who created you?
20:22:29:527: Classified context: QuestionAndAnswer
20:22:29:527: Handling dialog: EN/Generic/QuestionAndAnswer
20:22:29:527:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:29:527:     Find matches for sequence: Who created you?
20:22:29:527:     Found matches for sequence: 5
20:22:29:527:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.21657250470809794 / 0.9126984126984126)
20:22:29:527:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.06214689265536724 / 0.2619047619047619)
20:22:29:528:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:29:528:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:29:528:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
20:22:29:528:     Set dialog output: My software was written by André van der Zee.
20:22:29:528: Handling dialog: EN/Generic/Handshake
20:22:29:528:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
20:22:29:528:     Find matches for sequence: Who created you?
20:22:29:528:     Found matches for sequence: 1
20:22:29:528:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
20:22:29:528:     Set prompt variable: firstName
20:22:29:528:     Set dialog output: My name is Dyz Lecticus.
20:22:29:528:     Set dialog prompt: What is your name?
Handling the request took: 3 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
20:22:29:528: Classify language for sequence: You asshole!
20:22:29:529: Classified language: EN
20:22:29:529: Translate sequence: You asshole!
20:22:29:529: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
20:22:29:529: Correction time limit: 300
20:22:29:529: Correcting sequence: You [] !
20:22:29:529: Corrected sequence: You asshole!
20:22:29:529: Translate corrected sequence: You asshole!
20:22:29:529: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
20:22:29:530: Checking profanity for sequence: You [PRF]!
20:22:29:530: Handling dialog: EN/Generic/Profanity
20:22:29:530:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:29:530:     Find matches for sequence: You [PRF]!
20:22:29:530:     Found matches for sequence: 2
20:22:29:530:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
20:22:29:530:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
20:22:29:530:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 2 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
20:22:29:530: Classify language for sequence: Jij klootzak!
20:22:29:530: Classified language: NL
20:22:29:530: Translate sequence: Jij klootzak!
20:22:29:531: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
20:22:29:531: Correction time limit: 300
20:22:29:531: Correcting sequence: Jij [] !
20:22:29:531: Corrected sequence: Jij klootzak!
20:22:29:531: Translate corrected sequence: Jij klootzak!
20:22:29:532: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
20:22:29:532: Checking profanity for sequence: Jij [PRF]!
20:22:29:532: Handling dialog: NL/Generic/Profanity
20:22:29:532:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:29:532:     Find matches for sequence: Jij [PRF]!
20:22:29:532:     Found matches for sequence: 2
20:22:29:532:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
20:22:29:532:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
20:22:29:532:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 95 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
20:22:29:625: Classify language for sequence: Can I book a room for 5 people?
20:22:29:625: Classified language: EN
20:22:29:625: Translate sequence: Can I book a room for 5 people?
20:22:29:626: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
20:22:29:626: Correction time limit: 900
20:22:29:626: Correcting sequence: Can I book a room for [] people ?
20:22:29:626: Corrected sequence: Can I book a room for 5 people?
20:22:29:626: Translate corrected sequence: Can I book a room for 5 people?
20:22:29:627: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
20:22:29:627: Checking profanity for sequence: Can I book a room for [NUM] people?
20:22:29:627: Classify master context for input sequence: Can I book a room for 5 people?
20:22:29:627: Classify master context for classification sequence: Can I book a room for [NUM] people?
20:22:29:627: Classified master context: Room
20:22:29:627: Classify context for input sequence: Can I book a room for 5 people?
20:22:29:627: Classify context for classification sequence: Can I book a room for [NUM] people?
20:22:29:627: Classified context: Booking
20:22:29:627: Handling dialog: EN/Room/Booking
20:22:29:627:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
20:22:29:628:     Updated variable bookPeople: UN_NUM:5 = 5
20:22:29:628:     Find matches for sequence: Can I book a room for [NUM] people?
20:22:29:629:     Found matches for sequence: 329
20:22:29:629:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
20:22:29:629:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
20:22:29:629:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
20:22:29:629:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
20:22:29:629:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
20:22:29:629:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
20:22:29:629:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
20:22:29:629:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
20:22:29:629:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
20:22:29:629:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
20:22:29:629:     - [... 319]
20:22:29:629:     Set prompt variable: bookDate
20:22:29:629:     Set dialog output: Okay.
20:22:29:629:     Set dialog prompt: On what date?
Handling the request took: 4 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
20:22:29:630: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:22:29:630: Classified language: EN
20:22:29:630: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
20:22:29:631: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
20:22:29:632: Correction time limit: 1332
20:22:29:632: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
20:22:29:632: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
20:22:29:632: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
20:22:29:634: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
20:22:29:634: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
20:22:29:634: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
20:22:29:635: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
20:22:29:635: Classified master context: Room
20:22:29:635: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
20:22:29:635: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
20:22:29:635: Classified context: Booking
20:22:29:635: Handling dialog: EN/Room/Booking
20:22:29:635:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
20:22:29:635:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
20:22:29:636:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
20:22:29:636:     Updated variable bookDuration: EN_DUR:02:00 = two hours
20:22:29:636:     Updated variable bookPeople: UN_NUM:12 = 12
20:22:29:636:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
20:22:29:638:     Found matches for sequence: 263
20:22:29:638:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
20:22:29:638:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
20:22:29:639:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
20:22:29:639:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
20:22:29:639:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
20:22:29:639:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
20:22:29:639:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
20:22:29:639:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
20:22:29:639:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
20:22:29:639:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
20:22:29:639:     - [... 253]
20:22:29:639:     Set prompt variable: bookConfirmation
20:22:29:639:     Set dialog output: Okay.
20:22:29:639:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 96 ms

Request prompt: '', input: 'Ik wil morgen een kamer boeken om 1 uur.'
Response debug log;
20:22:29:725: Classify language for sequence: Ik wil morgen een kamer boeken om 1 uur.
20:22:29:726: Classified language: NL
20:22:29:726: Translate sequence: Ik wil morgen een kamer boeken om 1 uur.
20:22:29:727: Translated sequence: UN_ABC:Ik UN_ABC:wil NL_DAT:2018-07-17|UN_ABC:morgen NL_NUM:1|UN_ABC:een UN_ABC:kamer UN_ABC:boeken UN_ABC:om NL_DUR:01:00 .
20:22:29:727: Correction time limit: 900
20:22:29:727: Correcting sequence: Ik wil [] [] kamer boeken om [] .
20:22:29:727: Corrected sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
20:22:29:727: Translate corrected sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
20:22:29:729: Translated corrected sequence: UN_ABC:Ik UN_ABC:wil NL_DAT:2018-07-17 NL_NUM:1|UN_ABC:een UN_ABC:kamer UN_ABC:boeken UN_ABC:om NL_TIM:13:00:00|NL_DUR:01:00 .
20:22:29:729: Checking profanity for sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
20:22:29:729: Classify master context for input sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
20:22:29:729: Classify master context for classification sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
20:22:29:729: Classified master context: Room
20:22:29:729: Classify context for input sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
20:22:29:729: Classify context for classification sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
20:22:29:729: Classified context: Booking
20:22:29:729: Handling dialog: NL/Room/Booking
20:22:29:730:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchRoomBookingHandler
20:22:29:730:     Updated variable bookDate: NL_DAT:2018-07-17 = zeventien juli tweeduizendachttien
20:22:29:730:     Updated variable bookTime: NL_TIM:13:00:00 = een uur
20:22:29:730:     Updated variable bookPeople: NL_NUM:1 = een
20:22:29:730:     Find matches for sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
20:22:29:733:     Found matches for sequence: 275
20:22:29:733:     - Ik wil een kamer boeken om [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
20:22:29:733:     - Ik wil een kamer boeken om [TIM] van [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
20:22:29:733:     - Ik wil graag een kamer boeken om [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
20:22:29:733:     - Ik wil graag een kamer boeken om [TIM] van [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
20:22:29:733:     - Ik wil een kamer boeken om [TIM] voor [NUM] personen. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
20:22:29:733:     - Ik wil een kamer boeken om [TIM] op [DAT]. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
20:22:29:733:     - Ik wil graag een kamer boeken om [TIM] voor [NUM] personen. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
20:22:29:733:     - Ik wil graag een kamer boeken om [TIM] op [DAT]. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
20:22:29:733:     - Kan ik een kamer boeken om [TIM] voor [NUM] personen? [OUTPUT] Okee. (1.2078575952853052 / 0.5009434150826488)
20:22:29:733:     - Kan ik een kamer boeken om [TIM] op [DAT]? [OUTPUT] Okee. (1.2078575952853052 / 0.5009434150826488)
20:22:29:733:     - [... 265]
20:22:29:733:     Updated variable bookPeople: 
20:22:29:733:     Set prompt variable: bookDuration
20:22:29:733:     Set dialog output: Okee.
20:22:29:733:     Set dialog prompt: Hoe lang?
Handling the request took: 8 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
20:22:29:734: Classify language for sequence: What does it cost to book a room?
20:22:29:734: Classified language: EN
20:22:29:734: Translate sequence: What does it cost to book a room?
20:22:29:735: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
20:22:29:735: Correction time limit: 900
20:22:29:735: Correcting sequence: What does it cost to book a room ?
20:22:29:735: Corrected sequence: What does it cost to book a room?
20:22:29:735: Translate corrected sequence: What does it cost to book a room?
20:22:29:735: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
20:22:29:735: Checking profanity for sequence: What does it cost to book a room?
20:22:29:736: Classify master context for input sequence: What does it cost to book a room?
20:22:29:736: Classified master context: Room
20:22:29:736: Classify context for input sequence: What does it cost to book a room?
20:22:29:736: Classified context: QuestionAndAnswer
20:22:29:736: Handling dialog: EN/Room/QuestionAndAnswer
20:22:29:736:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:29:736:     Find matches for sequence: What does it cost to book a room?
20:22:29:736:     Found matches for sequence: 6
20:22:29:736:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
20:22:29:736:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
20:22:29:736:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5)
20:22:29:736:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5)
20:22:29:736:     - Are there any costs attached to booking van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.125)
20:22:29:736:     - Are there any costs attached to reserving van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.125)
20:22:29:736:     Set dialog output: There are no costs attached to booking a room.
20:22:29:736: Handling dialog: EN/Room/Booking
20:22:29:736:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
20:22:29:736:     Find matches for sequence: What does it cost to book a room?
20:22:29:738:     Found matches for sequence: 276
20:22:29:738:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
20:22:29:738:     - [... 266]
20:22:29:738:     Set prompt variable: bookDate
20:22:29:738:     Set dialog output: Okay.
20:22:29:738:     Set dialog prompt: On what date?
Handling the request took: 92 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
20:22:29:825: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
20:22:29:825: Classified language: EN
20:22:29:825: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
20:22:29:826: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
20:22:29:827: Correction time limit: 1300
20:22:29:827: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
20:22:29:827: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
20:22:29:827: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
20:22:29:828: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
20:22:29:828: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:828: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
20:22:29:829: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:829: Classified master context: Generic
20:22:29:829: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
20:22:29:829: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:829: Classified context: Math
20:22:29:829: Handling dialog: EN/Generic/Math
20:22:29:829:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
20:22:29:830:     Updated variable number1: EN_NUM:10 = ten
20:22:29:830:     Updated variable number2: EN_NUM:40 = fourty
20:22:29:830:     Updated variable number3: EN_NUM:20 = twenty
20:22:29:830:     Updated variable number4: EN_NUM:3 = three
20:22:29:830:     Updated variable number5: EN_NUM:6 = six
20:22:29:830:     Updated variable operator1: EN_MTH:M = multiplied by
20:22:29:830:     Updated variable operator2: EN_MTH:D = divided by
20:22:29:830:     Updated variable operator3: EN_MTH:A = plus
20:22:29:830:     Updated variable operator4: EN_MTH:M = multiplied by
20:22:29:830:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:830:     Found matches for sequence: 8
20:22:29:830:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (5.5 / 0.7521367521367521)
20:22:29:830:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (5.5 / 0.7521367521367521)
20:22:29:830:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (4.125 / 0.5641025641025641)
20:22:29:830:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (4.125 / 0.5641025641025641)
20:22:29:830:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.375 / 0.46153846153846156)
20:22:29:830:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (3.375 / 0.46153846153846156)
20:22:29:830:     - How much is [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (2.625 / 0.358974358974359)
20:22:29:830:     - How much is [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (2.625 / 0.358974358974359)
20:22:29:830:     Calculate expression: 10 M 40 D 20 A 3 M 6
20:22:29:830:     Calculated expression: 38.0
20:22:29:830:     Updated variable exact: exactly
20:22:29:830:     Updated variable result: thirtyeight
20:22:29:830:     Set prompt variable: nextDialog
20:22:29:830:     Set dialog output: Exactly thirtyeight.
20:22:29:830:     Set dialog prompt: What else can I do for you?
Handling the request took: 100 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
20:22:29:925: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:22:29:925: Classified language: NL
20:22:29:925: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
20:22:29:926: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
20:22:29:926: Correction time limit: 1200
20:22:29:926: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
20:22:29:927: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
20:22:29:927: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
20:22:29:928: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
20:22:29:928: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:928: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
20:22:29:928: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:928: Classified master context: Generic
20:22:29:928: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
20:22:29:928: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:928: Classified context: Math
20:22:29:928: Handling dialog: NL/Generic/Math
20:22:29:928:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
20:22:29:928:     Updated variable number1: NL_NUM:10 = tien
20:22:29:928:     Updated variable number2: NL_NUM:40 = veertig
20:22:29:928:     Updated variable number3: NL_NUM:20 = twintig
20:22:29:929:     Updated variable number4: NL_NUM:3 = drie
20:22:29:929:     Updated variable number5: NL_NUM:6 = zes
20:22:29:929:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
20:22:29:929:     Updated variable operator2: NL_MTH:D = gedeeld door
20:22:29:929:     Updated variable operator3: NL_MTH:A = plus
20:22:29:929:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
20:22:29:929:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
20:22:29:929:     Found matches for sequence: 8
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (5.318181818181818 / 0.7222222222222223)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (5.318181818181818 / 0.7222222222222223)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.8181818181818183 / 0.5185185185185186)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (3.8181818181818183 / 0.5185185185185186)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.0 / 0.4074074074074075)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (3.0 / 0.4074074074074075)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (2.1818181818181817 / 0.29629629629629634)
20:22:29:929:     - Hoeveel is [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (2.1818181818181817 / 0.29629629629629634)
20:22:29:929:     Calculate expression: 10 M 40 D 20 A 3 M 6
20:22:29:929:     Calculated expression: 38.0
20:22:29:929:     Updated variable exact: precies
20:22:29:929:     Updated variable result: achtendertig
20:22:29:929:     Set prompt variable: nextDialog
20:22:29:929:     Set dialog output: Precies achtendertig.
20:22:29:929:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 100 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
20:22:30:026: Classify language for sequence: What languages do you speak?
20:22:30:027: Classified language: EN
20:22:30:027: Translate sequence: What languages do you speak?
20:22:30:028: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
20:22:30:029: Correction time limit: 600
20:22:30:029: Correcting sequence: What languages do you speak ?
20:22:30:029: Corrected sequence: What languages do you speak?
20:22:30:029: Translate corrected sequence: What languages do you speak?
20:22:30:030: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
20:22:30:031: Checking profanity for sequence: What languages do you speak?
20:22:30:031: Classify master context for input sequence: What languages do you speak?
20:22:30:031: Classified master context: Generic
20:22:30:031: Classify context for input sequence: What languages do you speak?
20:22:30:031: Classified context: Language
20:22:30:031: Handling dialog: EN/Generic/Language
20:22:30:032:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
20:22:30:032:     Updated variable languages: English and Dutch
20:22:30:032:     Find matches for sequence: What languages do you speak?
20:22:30:032:     Found matches for sequence: 6
20:22:30:032:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1800000000000002 / 0.595959595959596)
20:22:30:032:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.76 / 0.38383838383838387)
20:22:30:032:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.6799999999999999 / 0.3434343434343434)
20:22:30:032:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.6799999999999999 / 0.3434343434343434)
20:22:30:032:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.6325000000000001 / 0.3194444444444445)
20:22:30:032:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.3 / 0.15151515151515152)
20:22:30:032:     Set prompt variable: language
20:22:30:033:     Set dialog output: I speak English and Dutch.
20:22:30:033: Handling dialog: EN/Generic/QuestionAndAnswer
20:22:30:033:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
20:22:30:033:     Find matches for sequence: What languages do you speak?
20:22:30:033:     Found matches for sequence: 17
20:22:30:033:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07721280602636535 / 0.2169312169312169)
20:22:30:033:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06756120527306968 / 0.18981481481481477)
20:22:30:033:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06756120527306968 / 0.18981481481481477)
20:22:30:033:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06756120527306968 / 0.18981481481481477)
20:22:30:033:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
20:22:30:033:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
20:22:30:033:     - What is the answer? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
20:22:30:033:     - What is the question? [OUTPUT] I do not know. (0.06746427384513128 / 0.1895424836601307)
20:22:30:034:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06746427384513128 / 0.1895424836601307)
20:22:30:034:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06746427384513128 / 0.1895424836601307)
20:22:30:034:     - [... 7]
20:22:30:034:     Set dialog output: My goal is to understand and help people.
Handling the request took: 9 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
20:22:30:034: Classify language for sequence: Spreek je duits?
20:22:30:034: Classified language: NL
20:22:30:034: Translate sequence: Spreek je duits?
20:22:30:035: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
20:22:30:036: Correction time limit: 400
20:22:30:036: Correcting sequence: Spreek je [] ?
20:22:30:036: Corrected sequence: Spreek je Duits?
20:22:30:036: Translate corrected sequence: Spreek je Duits?
20:22:30:037: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
20:22:30:037: Checking profanity for sequence: Spreek je [LNG]?
20:22:30:037: Classify master context for input sequence: Spreek je Duits?
20:22:30:037: Classify master context for classification sequence: Spreek je [LNG]?
20:22:30:037: Classified master context: Generic
20:22:30:037: Classify context for input sequence: Spreek je Duits?
20:22:30:037: Classify context for classification sequence: Spreek je [LNG]?
20:22:30:037: Classified context: Language
20:22:30:038: Handling dialog: NL/Generic/Language
20:22:30:038:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
20:22:30:038:     Updated variable language: NL_LNG:DE = Duits
20:22:30:038:     Updated variable languages: Engels en Nederlands
20:22:30:038:     Updated variable confirmation: Nee
20:22:30:038:     Find matches for sequence: Spreek je [LNG]?
20:22:30:038:     Found matches for sequence: 5
20:22:30:038:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.5079365079365077 / 0.9313725490196076)
20:22:30:038:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
20:22:30:038:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
20:22:30:038:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
20:22:30:038:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
20:22:30:038:     Set prompt variable: 
20:22:30:038:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 91 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
20:22:30:125: Classify language for sequence: Spreek je engels?
20:22:30:125: Classified language: NL
20:22:30:125: Translate sequence: Spreek je engels?
20:22:30:126: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
20:22:30:126: Correction time limit: 400
20:22:30:126: Correcting sequence: Spreek je [] ?
20:22:30:126: Corrected sequence: Spreek je Engels?
20:22:30:126: Translate corrected sequence: Spreek je Engels?
20:22:30:126: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
20:22:30:126: Checking profanity for sequence: Spreek je [LNG]?
20:22:30:126: Classify master context for input sequence: Spreek je Engels?
20:22:30:126: Classify master context for classification sequence: Spreek je [LNG]?
20:22:30:126: Classified master context: Generic
20:22:30:126: Classify context for input sequence: Spreek je Engels?
20:22:30:126: Classify context for classification sequence: Spreek je [LNG]?
20:22:30:127: Classified context: Language
20:22:30:127: Handling dialog: NL/Generic/Language
20:22:30:127:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
20:22:30:127:     Updated variable language: NL_LNG:EN = Engels
20:22:30:127:     Updated variable languages: Engels en Nederlands
20:22:30:127:     Updated variable confirmation: Ja
20:22:30:127:     Find matches for sequence: Spreek je [LNG]?
20:22:30:127:     Found matches for sequence: 5
20:22:30:127:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.5079365079365077 / 0.9313725490196076)
20:22:30:127:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
20:22:30:127:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
20:22:30:127:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
20:22:30:127:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
20:22:30:127:     Set prompt variable: 
20:22:30:127:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 2 ms

Request prompt: '', input: 'Wat kost overboeken naar buitenland?'
Response debug log;
20:22:30:127: Classify language for sequence: Wat kost overboeken naar buitenland?
20:22:30:128: Classified language: NL
20:22:30:128: Translate sequence: Wat kost overboeken naar buitenland?
20:22:30:128: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
20:22:30:128: Correction time limit: 600
20:22:30:128: Correcting sequence: Wat kost overboeken naar buitenland ?
20:22:30:129: Corrected sequence: Wat kost overboeken naar buitenland?
20:22:30:129: Translate corrected sequence: Wat kost overboeken naar buitenland?
20:22:30:129: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
20:22:30:129: Checking profanity for sequence: Wat kost overboeken naar buitenland?
20:22:30:130: Classify master context for input sequence: Wat kost overboeken naar buitenland?
20:22:30:130: Classified master context: ForeignTransfer
20:22:30:130: Classify context for input sequence: Wat kost overboeken naar buitenland?
20:22:30:130: Classified context: QuestionAndAnswer
20:22:30:130: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
20:22:30:130:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
20:22:30:130:     Updated variable durationDays: dagen
20:22:30:130:     Updated variable cost: vijf
20:22:30:130:     Updated variable costCurrency: euro
20:22:30:130:     Updated variable duration: drie
20:22:30:130:     Find matches for sequence: Wat kost overboeken naar buitenland?
20:22:30:130:     Found matches for sequence: 23
20:22:30:130:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.9481409001956947 / 0.5031152647975079)
20:22:30:130:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6550880626223092 / 0.3476116303219108)
20:22:30:130:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6550880626223092 / 0.3476116303219108)
20:22:30:130:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6550880626223092 / 0.3476116303219108)
20:22:30:130:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6550880626223092 / 0.3476116303219108)
20:22:30:130:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
20:22:30:130:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
20:22:30:130:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
20:22:30:130:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
20:22:30:130:     - Een buitenlandoverboeking. Wat kost dat? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
20:22:30:130:     - [... 13]
20:22:30:130:     Set prompt variable: 
20:22:30:130:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
20:22:30:130: Handling dialog: NL/ForeignTransfer/Cost
20:22:30:130:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
20:22:30:130:     Updated variable cost: vijf
20:22:30:130:     Updated variable costCurrency: euro
20:22:30:130:     Updated variable duration: drie
20:22:30:130:     Find matches for sequence: Wat kost overboeken naar buitenland?
20:22:30:130:     Found matches for sequence: 8
20:22:30:130:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5155502392344498 / 0.342063492063492)
20:22:30:130:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.31100478468899523 / 0.20634920634920634)
20:22:30:130:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.31100478468899523 / 0.20634920634920634)
20:22:30:130:     - Een overboeking naar [CNT]. Wat kost dat? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
20:22:30:130:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
20:22:30:130:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
20:22:30:130:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
20:22:30:130:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
Handling the request took: 98 ms

Request prompt: '', input: 'Wat kost overboeken naar italie?'
Response debug log;
20:22:30:225: Classify language for sequence: Wat kost overboeken naar italie?
20:22:30:225: Classified language: NL
20:22:30:225: Translate sequence: Wat kost overboeken naar italie?
20:22:30:226: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:italie ?
20:22:30:226: Correction time limit: 600
20:22:30:226: Correcting sequence: Wat kost overboeken naar italie ?
20:22:30:226: Corrected sequence: Wat kost overboeken naar italië?
20:22:30:226: Translate corrected sequence: Wat kost overboeken naar italië?
20:22:30:227: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italië ?
20:22:30:227: Checking profanity for sequence: Wat kost overboeken naar [CNT]?
20:22:30:227: Classify master context for input sequence: Wat kost overboeken naar italië?
20:22:30:227: Classify master context for classification sequence: Wat kost overboeken naar [CNT]?
20:22:30:227: Classified master context: ForeignTransfer
20:22:30:227: Classify context for input sequence: Wat kost overboeken naar italië?
20:22:30:227: Classify context for classification sequence: Wat kost overboeken naar [CNT]?
20:22:30:227: Classified context: Cost
20:22:30:227: Handling dialog: NL/ForeignTransfer/Cost
20:22:30:227:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
20:22:30:227:     Updated variable cost: vijf
20:22:30:227:     Updated variable costCurrency: euro
20:22:30:227:     Updated variable duration: drie
20:22:30:227:     Updated variable transferToCountry: NL_CNT:IT = Italië
20:22:30:227:     Find matches for sequence: Wat kost overboeken naar [CNT]?
20:22:30:227:     Found matches for sequence: 14
20:22:30:227:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.8313397129186603 / 0.5515873015873015)
20:22:30:227:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7320574162679426 / 0.48571428571428565)
20:22:30:227:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7320574162679426 / 0.48571428571428565)
20:22:30:227:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
20:22:30:227:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
20:22:30:227:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
20:22:30:227:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
20:22:30:227:     - Wat zijn de kosten van een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
20:22:30:227:     - Zijn er kosten verbonden aan geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
20:22:30:227:     - Hoeveel kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
20:22:30:227:     - [... 4]
20:22:30:227:     Set prompt variable: 
20:22:30:227:     Set dialog output: Een overboeking naar Italië kost vijf euro.
20:22:30:227: Handling dialog: NL/ForeignTransfer/Duration
20:22:30:227:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
20:22:30:227:     Updated variable cost: vijf
20:22:30:227:     Updated variable costCurrency: euro
20:22:30:227:     Updated variable duration: drie
20:22:30:227:     Updated variable transferToCountry: NL_CNT:IT = Italië
20:22:30:227:     Find matches for sequence: Wat kost overboeken naar [CNT]?
20:22:30:228:     Found matches for sequence: 9
20:22:30:228:     - Hoeveel tijd kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.8907407407407407 / 0.6362433862433863)
20:22:30:228:     - Hoelang duurt geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.674074074074074 / 0.4814814814814815)
20:22:30:228:     - Hoeveel tijd kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.6018518518518519 / 0.42989417989417994)
20:22:30:228:     - Hoeveel tijd kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.6018518518518519 / 0.42989417989417994)
20:22:30:228:     - Hoelang duurt een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
20:22:30:228:     - Hoelang duurt het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
20:22:30:228:     - Hoelang duurt geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
20:22:30:228:     - Hoelang duurt het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
20:22:30:228:     - Een overboeking naar [CNT]. Hoe lang duurt dat? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.1851851851851852 / 0.1322751322751323)
20:22:30:228:     Updated variable durationDays: dagen
20:22:30:228:     Set prompt variable: 
20:22:30:228:     Set dialog output: Een overboeking naar Italië duurt drie dagen.
20:22:30:228: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
20:22:30:228:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
20:22:30:228:     Updated variable durationDays: dagen
20:22:30:228:     Updated variable cost: vijf
20:22:30:228:     Updated variable costCurrency: euro
20:22:30:228:     Updated variable duration: drie
20:22:30:228:     Find matches for sequence: Wat kost overboeken naar [CNT]?
20:22:30:228:     Found matches for sequence: 11
20:22:30:228:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6942270058708415 / 0.368380062305296)
20:22:30:228:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.40117416829745595 / 0.21287642782969887)
20:22:30:228:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.40117416829745595 / 0.21287642782969887)
20:22:30:228:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.40117416829745595 / 0.21287642782969887)
20:22:30:228:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.40117416829745595 / 0.21287642782969887)
20:22:30:228:     - Een buitenlandoverboeking. Wat kost dat? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
20:22:30:228:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
20:22:30:228:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
20:22:30:228:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
20:22:30:228:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
20:22:30:228:     - [... 1]
20:22:30:228:     Set prompt variable: 
20:22:30:228:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
Handling the request took: 100 ms

Request prompt: '', input: 'Je begrijpt me niet.'
Response debug log;
20:22:30:325: Classify language for sequence: Je begrijpt me niet.
20:22:30:325: Classified language: NL
20:22:30:325: Translate sequence: Je begrijpt me niet.
20:22:30:325: Translated sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
20:22:30:325: Correction time limit: 500
20:22:30:326: Correcting sequence: Je begrijpt me niet .
20:22:30:326: Corrected sequence: Je begrijpt me niet.
20:22:30:326: Translate corrected sequence: Je begrijpt me niet.
20:22:30:326: Translated corrected sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
20:22:30:326: Checking profanity for sequence: Je begrijpt me niet.
20:22:30:326: Classify master context for input sequence: Je begrijpt me niet.
20:22:30:326: Classified master context: Support
20:22:30:326: Classify context for input sequence: Je begrijpt me niet.
20:22:30:326: Classified context: Request
20:22:30:326: Handling dialog: NL/Support/Request
20:22:30:326:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequestHandler
20:22:30:326:     Find matches for sequence: Je begrijpt me niet.
20:22:30:326:     Selected filter context: supportNoTransferToHuman
20:22:30:326:     Found matches for sequence: 11
20:22:30:326:     - Jij begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.48233695652173914 / 0.7513227513227514)
20:22:30:326:     - U begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.48233695652173914 / 0.7513227513227514)
20:22:30:326:     - Je begrijpt mij niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.2984035326086957 / 0.4648148148148149)
20:22:30:326:     - Je begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.2984035326086957 / 0.4648148148148149)
20:22:30:326:     - Je kan me niet helpen. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16440217391304346 / 0.2560846560846561)
20:22:30:326:     - Je begrijpt mij verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16168478260869565 / 0.2518518518518519)
20:22:30:326:     - Jij begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16168478260869565 / 0.2518518518518519)
20:22:30:326:     - U begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16168478260869565 / 0.2518518518518519)
20:22:30:326:     - Je begrijpt er niks van. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16168478260869565 / 0.2518518518518519)
20:22:30:326:     - Jij begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.15625 / 0.24338624338624343)
20:22:30:326:     - [... 1]
20:22:30:327:     Set prompt variable: nextDialog
20:22:30:327:     Set dialog output: Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar dyz.lecticus@zeesoft.nl.
20:22:30:327:     Set dialog prompt: Kan ik u ergens anders mee proberen te helpen?
Handling the request took: 2 ms

Request prompt: '', input: 'Je begrijpt me niet.'
Response debug log;
20:22:30:327: Classify language for sequence: Je begrijpt me niet.
20:22:30:327: Classified language: NL
20:22:30:327: Translate sequence: Je begrijpt me niet.
20:22:30:328: Translated sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
20:22:30:328: Correction time limit: 500
20:22:30:328: Correcting sequence: Je begrijpt me niet .
20:22:30:328: Corrected sequence: Je begrijpt me niet.
20:22:30:328: Translate corrected sequence: Je begrijpt me niet.
20:22:30:328: Translated corrected sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
20:22:30:328: Checking profanity for sequence: Je begrijpt me niet.
20:22:30:328: Classify master context for input sequence: Je begrijpt me niet.
20:22:30:329: Classified master context: Support
20:22:30:329: Classify context for input sequence: Je begrijpt me niet.
20:22:30:329: Classified context: Request
20:22:30:329: Handling dialog: NL/Support/Request
20:22:30:329:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequestHandler
20:22:30:329:     Find matches for sequence: Je begrijpt me niet.
20:22:30:329:     Selected filter context: supportTransferToHuman
20:22:30:329:     Found matches for sequence: 11
20:22:30:329:     - Jij begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. (0.9644970414201184 / 0.7010752688172044)
20:22:30:329:     - U begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. (0.9644970414201184 / 0.7010752688172044)
20:22:30:329:     - Je begrijpt mij niet. [OUTPUT] Ik ben nog aan het leren. (0.5894970414201184 / 0.42849462365591406)
20:22:30:329:     - Je begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. (0.5894970414201184 / 0.42849462365591406)
20:22:30:329:     - Je kan me niet helpen. [OUTPUT] Ik ben nog aan het leren. (0.33727810650887574 / 0.2451612903225807)
20:22:30:329:     - Je begrijpt mij verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3254437869822485 / 0.23655913978494628)
20:22:30:329:     - Jij begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3254437869822485 / 0.23655913978494628)
20:22:30:329:     - U begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3254437869822485 / 0.23655913978494628)
20:22:30:329:     - Je begrijpt er niks van. [OUTPUT] Ik ben nog aan het leren. (0.3254437869822485 / 0.23655913978494628)
20:22:30:329:     - Jij begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. (0.30177514792899407 / 0.21935483870967745)
20:22:30:329:     - [... 1]
20:22:30:329:     Set prompt variable: supportConfirmation
20:22:30:329:     Set dialog output: Ik ben nog aan het leren.
20:22:30:329:     Set dialog prompt: Zal ik u doorverbinden met een mens?
Handling the request took: 98 ms
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
  "timeStamp": "2018-08-15 20:22:45:728",
  "totals": {
    "tests": 1367,
    "successful": 1343,
    "successPercentage": 98.24433,
    "durationMs": 13979,
    "averageRequestMs": 2,
    "outputTests": 1192,
    "succesfulOutputTests": 1168,
    "successPercentageOutputTests": 97.98658,
    "averageOutputTestRequestMs": 2,
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
    ]
  },
  "errors": [
    {
      "dialog": "EN/Generic/QuestionAndAnswer",
      "input": "Who is André van der Zee?",
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.9703890011331747)"
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
All 20 tests have been executed successfully (287 assertions).  
Total test duration: 71237 ms (total sleep duration: 38500 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 650 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 428 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 460 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 467 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 476 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117723 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117738 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117735 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 118051 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117835 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117869 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117844 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 547744 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 547744 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 547745 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 547746 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 547753 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 552668 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 552688 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerTester: 552717 Kb / 539 Mb
