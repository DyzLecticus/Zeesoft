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
Average correction time: 233 ms
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
Initializing the SequenceClassifier took: 2903 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 2 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 2 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 4 ms

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
Initializing the SequenceMatcher took: 4935 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 22 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 21 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 1 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 13 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 25 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 9 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 11 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 9 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 22 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 31 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Geldt mijn ABN AMRO Rechtsbijstandverzekering ook in het buitenland? [OUTPUT] Dit is afhankelijk van welke modules u heeft afgesloten. Bekijk uw polisblad voor uw dekkingen of bekijk de modules.'
Matching the input sequence took: 37 ms

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
Initializing the EntityValueTranslator took: 1749 ms

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
Retranslating the sequence took: 0 ms
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
Translating the sequence took: 2 ms
Translation: 'EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 5 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 3 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 0 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:Andre|NL_NAM:firstName:UN_ABC:Andre NL_PRE:3|NL_NAM:preposition:NL_PRE:3 UN_ABC:Zee|NL_NAM:lastName:UN_ABC:Zee'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam is Andre van der Zee'

Sequence: 'Hoe heet jij? gekke henkie'
Translating the sequence took: 4 ms
Translation: 'UN_ABC:Hoe UN_ABC:heet UN_ABC:jij ? UN_ABC:gekke|NL_NAM:firstName:UN_ABC:Gekke UN_ABC:henkie|NL_NAM:lastName:UN_ABC:Henkie'
Retranslating the sequence took: 1 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies UN_ABC:is UN_ABC:mijn UN_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam si gekste der henkies.'

Sequence: 'to Germany or France'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 1 ms
Translation: 'EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five'
Retranslating the sequence took: 0 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 0 ms
Translation: 'NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf'
Retranslating the sequence took: 0 ms
Retranslation: 'tien vermenigvuldigd met vijf'

Sequence: 'fifteen british pound'
Translating the sequence took: 0 ms
Translation: 'EN_NUM:15|UN_ABC:fifteen EN_CUR:GBP'
Retranslating the sequence took: 0 ms
Retranslation: 'fifteen british pound sterling'

Sequence: 'vijftien euro'
Translating the sequence took: 0 ms
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
Converting 247313 entity values took: 249 ms

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
Converting 1585 dialog examples took: 146 ms

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
Initializing Translator took 2656 ms
Initializing Classifier took 5651 ms
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

Initializing LanguagePreprocessor took 29 ms
Initializing LanguageContextNLSupport took 42 ms
Initializing LanguageContextENForeignTransfer took 52 ms
Initializing LanguageContextNLForeignTransfer took 62 ms
Initializing LanguageContextENSupport took 66 ms
Initializing LanguageContextENGeneric took 132 ms
Initializing LanguageContextNLGeneric took 207 ms
Initializing LanguageContextENRoom took 314 ms
Initializing LanguageContextNLRoom took 347 ms
Initializing LanguageMasterContextEN took 393 ms
Initializing LanguageMasterContextNL took 429 ms
Initializing EntityValueTranslator took 1847 ms
Initializing LanguageClassifier took 23202 ms
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
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'twothousand' -> EN
Classifying the input sequence took: 2 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.2438947276082825 / 0.8792387434243605
'NL': 0.08337219349832051 / 0.3005561594825824

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.2438947276082825 / 0.8792387434243605
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
Classifying the input sequence took: 1 ms
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
21:50:15:698: Classify language for sequence: Hallo.
21:50:15:698: Classified language: NL
21:50:15:698: Translate sequence: Hallo.
21:50:15:704: Translated sequence: UN_ABC:Hallo .
21:50:15:705: Correction time limit: 200
21:50:15:705: Correcting sequence: Hallo .
21:50:15:705: Corrected sequence: Hallo.
21:50:15:705: Translate corrected sequence: Hallo.
21:50:15:705: Translated corrected sequence: UN_ABC:Hallo .
21:50:15:705: Checking profanity for sequence: Hallo.
21:50:15:706: Classify master context for input sequence: Hallo.
21:50:15:706: Classified master context: Generic
21:50:15:706: Classify context for input sequence: Hallo.
21:50:15:706: Classified context: Handshake
Interpreting the request took: 8 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
21:50:15:706: Classify language for sequence: Mijn naam si gekste der henkies.
21:50:15:706: Classified language: NL
21:50:15:706: Translate sequence: Mijn naam si gekste der henkies.
21:50:15:709: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
21:50:15:709: Correction time limit: 700
21:50:15:709: Correcting sequence: Mijn naam si gekste [] henkies .
21:50:16:171: Corrected sequence: Mijn naam is gekste der henkies.
21:50:16:171: Translate corrected sequence: Mijn naam is gekste der henkies.
21:50:16:172: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:50:16:172: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:16:172: Classify master context for input sequence: Mijn naam is gekste der henkies.
21:50:16:172: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:16:172: Classified master context: Generic
21:50:16:172: Classify context for input sequence: Mijn naam is gekste der henkies.
21:50:16:173: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:16:173: Classified context: Handshake
Interpreting the request took: 467 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
21:50:16:173: Classify language for sequence: Albert einstein.
21:50:16:173: Classify language for sequence: What is your name? Albert einstein.
21:50:16:173: Classified language: EN
21:50:16:174: Translate sequence: Albert einstein.
21:50:16:175: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:50:16:175: Correction time limit: 300
21:50:16:175: Correcting sequence: [] [] .
21:50:16:175: Corrected sequence: Albert einstein.
21:50:16:175: Translate corrected sequence: Albert einstein.
21:50:16:176: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:50:16:176: Checking profanity for sequence: [ABC] [ABC].
21:50:16:176: Classify master context for input sequence: Albert einstein.
21:50:16:176: Classify master context for classification sequence: What is your name? [ABC] [ABC].
21:50:16:176: Classified master context: Generic
21:50:16:176: Classify context for input sequence: Albert einstein.
21:50:16:176: Classify context for classification sequence: What is your name? [ABC] [ABC].
21:50:16:176: Classified context: Handshake
Interpreting the request took: 3 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
21:50:16:177: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:50:16:177: Selected primary language: EN
21:50:16:177: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:50:16:178: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
21:50:16:178: Correction time limit: 1100
21:50:16:178: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
21:50:17:279: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:50:17:279: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:50:17:280: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
21:50:17:280: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:50:17:280: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1105 ms
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

Initializing DialogSet took 257 ms
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
21:50:19:245: Classify language for sequence: What are you?
21:50:19:245: Classified language: EN
21:50:19:245: Translate sequence: What are you?
21:50:19:249: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
21:50:19:249: Correction time limit: 400
21:50:19:249: Correcting sequence: What are you ?
21:50:19:251: Corrected sequence: What are you?
21:50:19:251: Translate corrected sequence: What are you?
21:50:19:252: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
21:50:19:252: Checking profanity for sequence: What are you?
21:50:19:253: Classify master context for input sequence: What are you?
21:50:19:253: Classified master context: Generic
21:50:19:253: Classify context for input sequence: What are you?
21:50:19:253: Classified context: QuestionAndAnswer
21:50:19:253: Handling dialog: EN/Generic/QuestionAndAnswer
21:50:19:254:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:19:254:     Find matches for sequence: What are you?
21:50:19:254:     Found matches for sequence: 10
21:50:19:254:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.2090395480225989 / 0.8809523809523809)
21:50:19:254:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.0696798493408663 / 0.2936507936507936)
21:50:19:256:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.0696798493408663 / 0.2936507936507936)
21:50:19:256:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
21:50:19:256:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
21:50:19:256:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06756120527306968 / 0.28472222222222215)
21:50:19:256:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:19:257:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:19:257:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:19:257:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:19:257:     Set dialog output: I am an artificially intelligent personal assistant.
21:50:19:258: Handling dialog: EN/Generic/Handshake
21:50:19:258:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:19:258:     Find matches for sequence: What are you?
21:50:19:258:     Found matches for sequence: 2
21:50:19:258:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
21:50:19:258:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
21:50:19:258:     Set prompt variable: firstName
21:50:19:259:     Set dialog output: My name is Dyz Lecticus.
21:50:19:259:     Set dialog prompt: What is your name?
Handling the request took: 68 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
21:50:19:312: Classify language for sequence: Wat ben jij?
21:50:19:312: Classified language: NL
21:50:19:312: Translate sequence: Wat ben jij?
21:50:19:313: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
21:50:19:313: Correction time limit: 400
21:50:19:313: Correcting sequence: Wat ben jij ?
21:50:19:313: Corrected sequence: Wat ben jij?
21:50:19:313: Translate corrected sequence: Wat ben jij?
21:50:19:313: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
21:50:19:313: Checking profanity for sequence: Wat ben jij?
21:50:19:313: Classify master context for input sequence: Wat ben jij?
21:50:19:314: Classified master context: Generic
21:50:19:314: Classify context for input sequence: Wat ben jij?
21:50:19:314: Classified context: QuestionAndAnswer
21:50:19:314: Handling dialog: NL/Generic/QuestionAndAnswer
21:50:19:314:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:19:314:     Find matches for sequence: Wat ben jij?
21:50:19:314:     Found matches for sequence: 9
21:50:19:314:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.4464544138929089 / 0.9793650793650793)
21:50:19:314:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
21:50:19:314:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
21:50:19:314:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
21:50:19:314:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
21:50:19:314:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14688856729377714 / 0.3222222222222222)
21:50:19:314:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
21:50:19:314:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
21:50:19:314:     - Ben jij menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.12979377713458756 / 0.2847222222222222)
21:50:19:314:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
21:50:19:315: Handling dialog: NL/Generic/Handshake
21:50:19:315:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:19:315:     Find matches for sequence: Wat ben jij?
21:50:19:315:     Found matches for sequence: 5
21:50:19:315:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
21:50:19:315:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
21:50:19:315:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
21:50:19:315:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
21:50:19:315:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
21:50:19:315:     Set prompt variable: firstName
21:50:19:315:     Set dialog output: Mijn naam is Dyz Lecticus.
21:50:19:315:     Set dialog prompt: Wat is uw naam?
21:50:19:315: Handling dialog: NL/Generic/Language
21:50:19:315:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:50:19:316:     Updated variable languages: Engels en Nederlands
21:50:19:316:     Find matches for sequence: Wat ben jij?
21:50:19:316:     Found matches for sequence: 2
21:50:19:316:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
21:50:19:316:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
21:50:19:316:     Set prompt variable: language
21:50:19:316:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 102 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
21:50:19:414: Classify language for sequence: What is the answer?
21:50:19:414: Classified language: EN
21:50:19:414: Translate sequence: What is the answer?
21:50:19:415: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
21:50:19:415: Correction time limit: 500
21:50:19:415: Correcting sequence: What is [] answer ?
21:50:19:416: Corrected sequence: What is the answer?
21:50:19:416: Translate corrected sequence: What is the answer?
21:50:19:416: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
21:50:19:416: Checking profanity for sequence: What is [PRE] answer?
21:50:19:416: Classify master context for input sequence: What is the answer?
21:50:19:416: Classify master context for classification sequence: What is [PRE] answer?
21:50:19:417: Selected input sequence master context classification.
21:50:19:417: Classified master context: Generic
21:50:19:417: Classify context for input sequence: What is the answer?
21:50:19:417: Classified context: QuestionAndAnswer
21:50:19:417: Handling dialog: EN/Generic/QuestionAndAnswer
21:50:19:417:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:19:417:     Find matches for sequence: What is the answer?
21:50:19:417:     Found matches for sequence: 9
21:50:19:417:     - What is the answer? [OUTPUT] Fourtytwo. (0.24858757062146894 / 0.838095238095238)
21:50:19:417:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.1713747645951036 / 0.5777777777777777)
21:50:19:417:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.1713747645951036 / 0.5777777777777777)
21:50:19:417:     - What is the question? [OUTPUT] I do not know. (0.1016949152542373 / 0.3428571428571428)
21:50:19:417:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.1016949152542373 / 0.3428571428571428)
21:50:19:417:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.1016949152542373 / 0.3428571428571428)
21:50:19:417:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.1016949152542373 / 0.3428571428571428)
21:50:19:417:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.04708097928436912 / 0.15873015873015872)
21:50:19:417:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.04708097928436912 / 0.15873015873015872)
21:50:19:417:     Set dialog output: Fourtytwo.
Handling the request took: 3 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
21:50:19:418: Classify language for sequence: Wat is het antwoord?
21:50:19:418: Classified language: NL
21:50:19:418: Translate sequence: Wat is het antwoord?
21:50:19:419: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
21:50:19:420: Correction time limit: 500
21:50:19:420: Correcting sequence: Wat is het antwoord ?
21:50:19:420: Corrected sequence: Wat is het antwoord?
21:50:19:420: Translate corrected sequence: Wat is het antwoord?
21:50:19:420: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
21:50:19:421: Checking profanity for sequence: Wat is het antwoord?
21:50:19:421: Classify master context for input sequence: Wat is het antwoord?
21:50:19:421: Classified master context: Generic
21:50:19:421: Classify context for input sequence: Wat is het antwoord?
21:50:19:421: Classified context: QuestionAndAnswer
21:50:19:421: Handling dialog: NL/Generic/QuestionAndAnswer
21:50:19:421:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:19:421:     Find matches for sequence: Wat is het antwoord?
21:50:19:421:     Found matches for sequence: 10
21:50:19:421:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.58465991316932 / 1.0)
21:50:19:421:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.4334298118668597 / 0.7413366336633662)
21:50:19:421:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.4334298118668597 / 0.7413366336633662)
21:50:19:421:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.28509406657018815 / 0.48762376237623756)
21:50:19:421:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.13820549927641101 / 0.23638613861386135)
21:50:19:421:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.13820549927641101 / 0.23638613861386135)
21:50:19:421:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.13820549927641101 / 0.23638613861386135)
21:50:19:421:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.13820549927641101 / 0.23638613861386135)
21:50:19:421:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.13820549927641101 / 0.23638613861386135)
21:50:19:422:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.13820549927641101 / 0.23638613861386135)
21:50:19:422:     Set dialog output: Tweeenveertig.
Handling the request took: 97 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
21:50:19:514: Classify language for sequence: Hallo.
21:50:19:514: Classified language: NL
21:50:19:514: Translate sequence: Hallo.
21:50:19:515: Translated sequence: UN_ABC:Hallo .
21:50:19:515: Correction time limit: 200
21:50:19:515: Correcting sequence: Hallo .
21:50:19:515: Corrected sequence: Hallo.
21:50:19:515: Translate corrected sequence: Hallo.
21:50:19:515: Translated corrected sequence: UN_ABC:Hallo .
21:50:19:515: Checking profanity for sequence: Hallo.
21:50:19:515: Classify master context for input sequence: Hallo.
21:50:19:515: Classified master context: Generic
21:50:19:515: Classify context for input sequence: Hallo.
21:50:19:515: Classified context: Handshake
21:50:19:515: Handling dialog: NL/Generic/Handshake
21:50:19:515:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:19:515:     Find matches for sequence: Hallo.
21:50:19:516:     Found matches for sequence: 1
21:50:19:516:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
21:50:19:516:     Set prompt variable: firstName
21:50:19:516:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
21:50:19:516:     Set dialog prompt: Wat is uw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
21:50:19:516: Classify language for sequence: Wie ben jij?
21:50:19:516: Classified language: NL
21:50:19:516: Translate sequence: Wie ben jij?
21:50:19:517: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
21:50:19:517: Correction time limit: 400
21:50:19:517: Correcting sequence: Wie ben jij ?
21:50:19:517: Corrected sequence: Wie ben jij?
21:50:19:517: Translate corrected sequence: Wie ben jij?
21:50:19:517: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
21:50:19:517: Checking profanity for sequence: Wie ben jij?
21:50:19:517: Classify master context for input sequence: Wie ben jij?
21:50:19:518: Classified master context: Generic
21:50:19:518: Classify context for input sequence: Wie ben jij?
21:50:19:518: Classified context: Handshake
21:50:19:518: Handling dialog: NL/Generic/Handshake
21:50:19:518:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:19:518:     Find matches for sequence: Wie ben jij?
21:50:19:518:     Found matches for sequence: 2
21:50:19:518:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
21:50:19:518:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
21:50:19:518:     Set prompt variable: firstName
21:50:19:518:     Set dialog output: Mijn naam is Dyz Lecticus.
21:50:19:518:     Set dialog prompt: Wat is uw naam?
21:50:19:518: Handling dialog: NL/Generic/QuestionAndAnswer
21:50:19:518:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:19:518:     Find matches for sequence: Wie ben jij?
21:50:19:519:     Found matches for sequence: 16
21:50:19:519:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.2952243125904487 / 0.6476190476190476)
21:50:19:519:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
21:50:19:519:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.1483357452966715 / 0.32539682539682535)
21:50:19:520:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
21:50:19:520:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.14688856729377714 / 0.3222222222222222)
21:50:19:520:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14688856729377714 / 0.3222222222222222)
21:50:19:520:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.14603932511243795 / 0.3203592814371257)
21:50:19:520:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
21:50:19:520:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
21:50:19:520:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.14603932511243795 / 0.3203592814371257)
21:50:19:520:     - [... 6]
21:50:19:520:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
21:50:19:520: Handling dialog: NL/Generic/Language
21:50:19:520:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:50:19:520:     Updated variable languages: Engels en Nederlands
21:50:19:520:     Find matches for sequence: Wie ben jij?
21:50:19:520:     Found matches for sequence: 2
21:50:19:520:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
21:50:19:520:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.49206349206349204 / 0.30392156862745096)
21:50:19:520:     Set prompt variable: language
21:50:19:520:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 98 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
21:50:19:614: Classify language for sequence: Albert einstein.
21:50:19:614: Classify language for sequence: What is your name? Albert einstein.
21:50:19:614: Classified language: EN
21:50:19:615: Translate sequence: Albert einstein.
21:50:19:617: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:50:19:617: Correction time limit: 300
21:50:19:617: Correcting sequence: [] [] .
21:50:19:617: Corrected sequence: Albert einstein.
21:50:19:617: Translate corrected sequence: Albert einstein.
21:50:19:619: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:50:19:619: Checking profanity for sequence: [ABC] [ABC].
21:50:19:619: Classify master context for input sequence: Albert einstein.
21:50:19:619: Classify master context for classification sequence: What is your name? [ABC] [ABC].
21:50:19:619: Classified master context: Generic
21:50:19:619: Classify context for input sequence: Albert einstein.
21:50:19:619: Classify context for classification sequence: What is your name? [ABC] [ABC].
21:50:19:621: Classified context: Handshake
21:50:19:621: Handling dialog: EN/Generic/Handshake
21:50:19:621:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:19:622:     Updated variable firstName: UN_ABC:Albert = Albert
21:50:19:622:     Updated variable lastName: UN_ABC:Einstein = Einstein
21:50:19:622:     Find matches for sequence: [ABC] [ABC].
21:50:19:622:     Found matches for sequence: 0
21:50:19:622:     Updated session variable fullName: Albert Einstein
21:50:19:622:     Set prompt variable: nextDialog
21:50:19:622:     Set dialog output: 
21:50:19:622:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 8 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
21:50:19:623: Classify language for sequence: Mijn naam si gekste der henkies.
21:50:19:623: Classified language: NL
21:50:19:623: Translate sequence: Mijn naam si gekste der henkies.
21:50:19:624: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
21:50:19:624: Correction time limit: 700
21:50:19:624: Correcting sequence: Mijn naam si gekste [] henkies .
21:50:20:250: Corrected sequence: Mijn naam is gekste der henkies.
21:50:20:250: Translate corrected sequence: Mijn naam is gekste der henkies.
21:50:20:251: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:50:20:252: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:20:252: Classify master context for input sequence: Mijn naam is gekste der henkies.
21:50:20:252: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:20:252: Classified master context: Generic
21:50:20:252: Classify context for input sequence: Mijn naam is gekste der henkies.
21:50:20:253: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:20:253: Classified context: Handshake
21:50:20:253: Handling dialog: NL/Generic/Handshake
21:50:20:253:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:20:254:     Updated variable firstName: UN_ABC:Gekste = Gekste
21:50:20:254:     Updated variable lastName: UN_ABC:Henkies = Henkies
21:50:20:254:     Updated variable preposition: NL_PRE:6 = der
21:50:20:254:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:20:254:     Found matches for sequence: 0
21:50:20:254:     Updated session variable fullName: Gekste der Henkies
21:50:20:254:     Set prompt variable: nextDialog
21:50:20:254:     Set dialog output: 
21:50:20:254:     Set dialog prompt: Wat kan ik voor u doen Gekste der Henkies?
Handling the request took: 632 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
21:50:20:254: Classify language for sequence: Mijn naam is jan de lange.
21:50:20:255: Classified language: NL
21:50:20:255: Translate sequence: Mijn naam is jan de lange.
21:50:20:257: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
21:50:20:258: Correction time limit: 700
21:50:20:258: Correcting sequence: Mijn naam is [] [] [] .
21:50:20:258: Corrected sequence: Mijn naam is jan de lange.
21:50:20:258: Translate corrected sequence: Mijn naam is jan de lange.
21:50:20:260: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
21:50:20:260: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:20:260: Classify master context for input sequence: Mijn naam is jan de lange.
21:50:20:260: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
21:50:20:260: Classified master context: Generic
21:50:20:260: Classify context for input sequence: Mijn naam is jan de lange.
21:50:20:261: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
21:50:20:261: Classified context: Handshake
21:50:20:261: Handling dialog: NL/Generic/Handshake
21:50:20:261:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:20:261:     Updated variable firstName: UN_ABC:Jan = Jan
21:50:20:261:     Updated variable lastName: UN_ABC:Lange = Lange
21:50:20:261:     Updated variable preposition: NL_PRE:5 = de
21:50:20:261:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:50:20:261:     Found matches for sequence: 0
21:50:20:261:     Updated session variable fullName: Jan de Lange
21:50:20:261:     Set prompt variable: nextDialog
21:50:20:261:     Set dialog output: 
21:50:20:261:     Set dialog prompt: Wat kan ik voor u doen Jan de Lange?
Handling the request took: 61 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
21:50:20:315: Classify language for sequence: Who created you?
21:50:20:315: Classified language: EN
21:50:20:315: Translate sequence: Who created you?
21:50:20:316: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
21:50:20:316: Correction time limit: 400
21:50:20:316: Correcting sequence: Who created you ?
21:50:20:316: Corrected sequence: Who created you?
21:50:20:316: Translate corrected sequence: Who created you?
21:50:20:316: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
21:50:20:316: Checking profanity for sequence: Who created you?
21:50:20:316: Classify master context for input sequence: Who created you?
21:50:20:316: Classified master context: Generic
21:50:20:317: Classify context for input sequence: Who created you?
21:50:20:317: Classified context: QuestionAndAnswer
21:50:20:317: Handling dialog: EN/Generic/QuestionAndAnswer
21:50:20:317:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:20:317:     Find matches for sequence: Who created you?
21:50:20:317:     Found matches for sequence: 5
21:50:20:317:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.21657250470809794 / 0.9126984126984126)
21:50:20:317:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.06214689265536724 / 0.2619047619047619)
21:50:20:317:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:20:317:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:20:317:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.06214689265536724 / 0.2619047619047619)
21:50:20:317:     Set dialog output: My software was written by André van der Zee.
21:50:20:317: Handling dialog: EN/Generic/Handshake
21:50:20:317:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:50:20:317:     Find matches for sequence: Who created you?
21:50:20:317:     Found matches for sequence: 1
21:50:20:317:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
21:50:20:318:     Set prompt variable: firstName
21:50:20:318:     Set dialog output: My name is Dyz Lecticus.
21:50:20:318:     Set dialog prompt: What is your name?
Handling the request took: 3 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
21:50:20:318: Classify language for sequence: You asshole!
21:50:20:318: Classified language: EN
21:50:20:318: Translate sequence: You asshole!
21:50:20:319: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
21:50:20:319: Correction time limit: 300
21:50:20:319: Correcting sequence: You [] !
21:50:20:319: Corrected sequence: You asshole!
21:50:20:319: Translate corrected sequence: You asshole!
21:50:20:319: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
21:50:20:319: Checking profanity for sequence: You [PRF]!
21:50:20:320: Handling dialog: EN/Generic/Profanity
21:50:20:320:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:20:320:     Find matches for sequence: You [PRF]!
21:50:20:320:     Found matches for sequence: 2
21:50:20:320:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
21:50:20:320:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
21:50:20:320:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 2 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
21:50:20:321: Classify language for sequence: Jij klootzak!
21:50:20:321: Classified language: NL
21:50:20:321: Translate sequence: Jij klootzak!
21:50:20:321: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
21:50:20:321: Correction time limit: 300
21:50:20:321: Correcting sequence: Jij [] !
21:50:20:321: Corrected sequence: Jij klootzak!
21:50:20:321: Translate corrected sequence: Jij klootzak!
21:50:20:322: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
21:50:20:322: Checking profanity for sequence: Jij [PRF]!
21:50:20:322: Handling dialog: NL/Generic/Profanity
21:50:20:322:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:20:322:     Find matches for sequence: Jij [PRF]!
21:50:20:322:     Found matches for sequence: 2
21:50:20:322:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
21:50:20:322:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
21:50:20:322:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 95 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
21:50:20:415: Classify language for sequence: Can I book a room for 5 people?
21:50:20:416: Classified language: EN
21:50:20:416: Translate sequence: Can I book a room for 5 people?
21:50:20:417: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
21:50:20:417: Correction time limit: 900
21:50:20:417: Correcting sequence: Can I book a room for [] people ?
21:50:20:417: Corrected sequence: Can I book a room for 5 people?
21:50:20:417: Translate corrected sequence: Can I book a room for 5 people?
21:50:20:418: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
21:50:20:418: Checking profanity for sequence: Can I book a room for [NUM] people?
21:50:20:419: Classify master context for input sequence: Can I book a room for 5 people?
21:50:20:419: Classify master context for classification sequence: Can I book a room for [NUM] people?
21:50:20:419: Classified master context: Room
21:50:20:419: Classify context for input sequence: Can I book a room for 5 people?
21:50:20:419: Classify context for classification sequence: Can I book a room for [NUM] people?
21:50:20:419: Classified context: Booking
21:50:20:419: Handling dialog: EN/Room/Booking
21:50:20:420:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:50:20:420:     Updated variable bookPeople: UN_NUM:5 = 5
21:50:20:420:     Find matches for sequence: Can I book a room for [NUM] people?
21:50:20:422:     Found matches for sequence: 329
21:50:20:422:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
21:50:20:422:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
21:50:20:422:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
21:50:20:423:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
21:50:20:423:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
21:50:20:423:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
21:50:20:423:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
21:50:20:423:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
21:50:20:423:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
21:50:20:423:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
21:50:20:423:     - [... 319]
21:50:20:423:     Set prompt variable: bookDate
21:50:20:423:     Set dialog output: Okay.
21:50:20:423:     Set dialog prompt: On what date?
Handling the request took: 8 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
21:50:20:424: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:50:20:424: Classified language: EN
21:50:20:424: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:50:20:426: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
21:50:20:427: Correction time limit: 1332
21:50:20:427: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
21:50:20:427: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:50:20:427: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:50:20:429: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
21:50:20:429: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:50:20:429: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:50:20:430: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:50:20:430: Classified master context: Room
21:50:20:430: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:50:20:430: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:50:20:430: Classified context: Booking
21:50:20:430: Handling dialog: EN/Room/Booking
21:50:20:430:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:50:20:430:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
21:50:20:430:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
21:50:20:430:     Updated variable bookDuration: EN_DUR:02:00 = two hours
21:50:20:431:     Updated variable bookPeople: UN_NUM:12 = 12
21:50:20:431:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:50:20:433:     Found matches for sequence: 263
21:50:20:433:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
21:50:20:433:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
21:50:20:433:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
21:50:20:433:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
21:50:20:434:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
21:50:20:434:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
21:50:20:434:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
21:50:20:434:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
21:50:20:434:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
21:50:20:434:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
21:50:20:434:     - [... 253]
21:50:20:434:     Set prompt variable: bookConfirmation
21:50:20:434:     Set dialog output: Okay.
21:50:20:434:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 92 ms

Request prompt: '', input: 'Ik wil morgen een kamer boeken om 1 uur.'
Response debug log;
21:50:20:515: Classify language for sequence: Ik wil morgen een kamer boeken om 1 uur.
21:50:20:516: Classified language: NL
21:50:20:516: Translate sequence: Ik wil morgen een kamer boeken om 1 uur.
21:50:20:517: Translated sequence: UN_ABC:Ik UN_ABC:wil NL_DAT:2018-07-17|UN_ABC:morgen NL_NUM:1|UN_ABC:een UN_ABC:kamer UN_ABC:boeken UN_ABC:om NL_DUR:01:00 .
21:50:20:517: Correction time limit: 900
21:50:20:517: Correcting sequence: Ik wil [] [] kamer boeken om [] .
21:50:20:517: Corrected sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
21:50:20:517: Translate corrected sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
21:50:20:519: Translated corrected sequence: UN_ABC:Ik UN_ABC:wil NL_DAT:2018-07-17 NL_NUM:1|UN_ABC:een UN_ABC:kamer UN_ABC:boeken UN_ABC:om NL_TIM:13:00:00|NL_DUR:01:00 .
21:50:20:519: Checking profanity for sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
21:50:20:519: Classify master context for input sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
21:50:20:519: Classify master context for classification sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
21:50:20:520: Classified master context: Room
21:50:20:520: Classify context for input sequence: Ik wil zeventien juli tweeduizendachttien een kamer boeken om een uur.
21:50:20:520: Classify context for classification sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
21:50:20:520: Classified context: Booking
21:50:20:520: Handling dialog: NL/Room/Booking
21:50:20:520:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchRoomBookingHandler
21:50:20:520:     Updated variable bookDate: NL_DAT:2018-07-17 = zeventien juli tweeduizendachttien
21:50:20:520:     Updated variable bookTime: NL_TIM:13:00:00 = een uur
21:50:20:520:     Updated variable bookPeople: NL_NUM:1 = een
21:50:20:520:     Find matches for sequence: Ik wil [DAT] [NUM] kamer boeken om [TIM].
21:50:20:523:     Found matches for sequence: 275
21:50:20:523:     - Ik wil een kamer boeken om [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
21:50:20:523:     - Ik wil een kamer boeken om [TIM] van [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
21:50:20:523:     - Ik wil graag een kamer boeken om [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
21:50:20:523:     - Ik wil graag een kamer boeken om [TIM] van [TIM]. [OUTPUT] Okee. (1.2549157303370786 / 0.5204601718363187)
21:50:20:523:     - Ik wil een kamer boeken om [TIM] voor [NUM] personen. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
21:50:20:523:     - Ik wil een kamer boeken om [TIM] op [DAT]. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
21:50:20:523:     - Ik wil graag een kamer boeken om [TIM] voor [NUM] personen. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
21:50:20:524:     - Ik wil graag een kamer boeken om [TIM] op [DAT]. [OUTPUT] Okee. (1.2436136814276273 / 0.5157727922973472)
21:50:20:524:     - Kan ik een kamer boeken om [TIM] voor [NUM] personen? [OUTPUT] Okee. (1.2078575952853052 / 0.5009434150826488)
21:50:20:524:     - Kan ik een kamer boeken om [TIM] op [DAT]? [OUTPUT] Okee. (1.2078575952853052 / 0.5009434150826488)
21:50:20:524:     - [... 265]
21:50:20:524:     Updated variable bookPeople: 
21:50:20:524:     Set prompt variable: bookDuration
21:50:20:524:     Set dialog output: Okee.
21:50:20:524:     Set dialog prompt: Hoe lang?
Handling the request took: 9 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
21:50:20:524: Classify language for sequence: What does it cost to book a room?
21:50:20:525: Classified language: EN
21:50:20:525: Translate sequence: What does it cost to book a room?
21:50:20:525: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
21:50:20:526: Correction time limit: 900
21:50:20:526: Correcting sequence: What does it cost to book a room ?
21:50:20:526: Corrected sequence: What does it cost to book a room?
21:50:20:526: Translate corrected sequence: What does it cost to book a room?
21:50:20:526: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
21:50:20:527: Checking profanity for sequence: What does it cost to book a room?
21:50:20:527: Classify master context for input sequence: What does it cost to book a room?
21:50:20:527: Classified master context: Room
21:50:20:527: Classify context for input sequence: What does it cost to book a room?
21:50:20:527: Classified context: QuestionAndAnswer
21:50:20:527: Handling dialog: EN/Room/QuestionAndAnswer
21:50:20:527:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:20:527:     Find matches for sequence: What does it cost to book a room?
21:50:20:527:     Found matches for sequence: 6
21:50:20:527:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
21:50:20:527:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
21:50:20:527:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5)
21:50:20:527:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5)
21:50:20:527:     - Are there any costs attached to booking van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.125)
21:50:20:527:     - Are there any costs attached to reserving van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.125)
21:50:20:527:     Set dialog output: There are no costs attached to booking a room.
21:50:20:527: Handling dialog: EN/Room/Booking
21:50:20:527:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:50:20:527:     Find matches for sequence: What does it cost to book a room?
21:50:20:529:     Found matches for sequence: 276
21:50:20:529:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:50:20:529:     - [... 266]
21:50:20:529:     Set prompt variable: bookDate
21:50:20:529:     Set dialog output: Okay.
21:50:20:529:     Set dialog prompt: On what date?
Handling the request took: 91 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
21:50:20:615: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
21:50:20:616: Classified language: EN
21:50:20:616: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
21:50:20:618: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
21:50:20:618: Correction time limit: 1300
21:50:20:618: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
21:50:20:618: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:50:20:618: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:50:20:620: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
21:50:20:620: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:620: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:50:20:620: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:620: Classified master context: Generic
21:50:20:620: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:50:20:621: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:621: Classified context: Math
21:50:20:621: Handling dialog: EN/Generic/Math
21:50:20:621:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
21:50:20:622:     Updated variable number1: EN_NUM:10 = ten
21:50:20:622:     Updated variable number2: EN_NUM:40 = fourty
21:50:20:622:     Updated variable number3: EN_NUM:20 = twenty
21:50:20:622:     Updated variable number4: EN_NUM:3 = three
21:50:20:622:     Updated variable number5: EN_NUM:6 = six
21:50:20:622:     Updated variable operator1: EN_MTH:M = multiplied by
21:50:20:622:     Updated variable operator2: EN_MTH:D = divided by
21:50:20:622:     Updated variable operator3: EN_MTH:A = plus
21:50:20:622:     Updated variable operator4: EN_MTH:M = multiplied by
21:50:20:622:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:623:     Found matches for sequence: 8
21:50:20:623:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (5.5 / 0.7521367521367521)
21:50:20:623:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (5.5 / 0.7521367521367521)
21:50:20:623:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (4.125 / 0.5641025641025641)
21:50:20:623:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (4.125 / 0.5641025641025641)
21:50:20:623:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.375 / 0.46153846153846156)
21:50:20:623:     - How much is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (3.375 / 0.46153846153846156)
21:50:20:623:     - How much is [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (2.625 / 0.358974358974359)
21:50:20:623:     - How much is [NUM] [MTH] [NUM]? [OUTPUT] That is {exact} {result}. (2.625 / 0.358974358974359)
21:50:20:623:     Calculate expression: 10 M 40 D 20 A 3 M 6
21:50:20:624:     Calculated expression: 38.0
21:50:20:624:     Updated variable exact: exactly
21:50:20:624:     Updated variable result: thirtyeight
21:50:20:624:     Set prompt variable: nextDialog
21:50:20:624:     Set dialog output: Exactly thirtyeight.
21:50:20:624:     Set dialog prompt: What else can I do for you?
Handling the request took: 100 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
21:50:20:715: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:50:20:716: Classified language: NL
21:50:20:716: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:50:20:717: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
21:50:20:717: Correction time limit: 1200
21:50:20:717: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
21:50:20:717: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:50:20:717: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:50:20:719: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
21:50:20:719: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:719: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:50:20:719: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:720: Classified master context: Generic
21:50:20:720: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:50:20:720: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:720: Classified context: Math
21:50:20:720: Handling dialog: NL/Generic/Math
21:50:20:720:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
21:50:20:720:     Updated variable number1: NL_NUM:10 = tien
21:50:20:720:     Updated variable number2: NL_NUM:40 = veertig
21:50:20:720:     Updated variable number3: NL_NUM:20 = twintig
21:50:20:720:     Updated variable number4: NL_NUM:3 = drie
21:50:20:720:     Updated variable number5: NL_NUM:6 = zes
21:50:20:720:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
21:50:20:720:     Updated variable operator2: NL_MTH:D = gedeeld door
21:50:20:720:     Updated variable operator3: NL_MTH:A = plus
21:50:20:720:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
21:50:20:720:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:50:20:720:     Found matches for sequence: 8
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (5.318181818181818 / 0.7222222222222223)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (5.318181818181818 / 0.7222222222222223)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.8181818181818183 / 0.5185185185185186)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (3.8181818181818183 / 0.5185185185185186)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (3.0 / 0.4074074074074075)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (3.0 / 0.4074074074074075)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM]? [OUTPUT] {exact} {result}. (2.1818181818181817 / 0.29629629629629634)
21:50:20:720:     - Hoeveel is [NUM] [MTH] [NUM]? [OUTPUT] Dat is {exact} {result}. (2.1818181818181817 / 0.29629629629629634)
21:50:20:720:     Calculate expression: 10 M 40 D 20 A 3 M 6
21:50:20:721:     Calculated expression: 38.0
21:50:20:721:     Updated variable exact: precies
21:50:20:721:     Updated variable result: achtendertig
21:50:20:721:     Set prompt variable: nextDialog
21:50:20:721:     Set dialog output: Precies achtendertig.
21:50:20:721:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 100 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
21:50:20:815: Classify language for sequence: What languages do you speak?
21:50:20:815: Classified language: EN
21:50:20:815: Translate sequence: What languages do you speak?
21:50:20:816: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
21:50:20:816: Correction time limit: 600
21:50:20:816: Correcting sequence: What languages do you speak ?
21:50:20:816: Corrected sequence: What languages do you speak?
21:50:20:816: Translate corrected sequence: What languages do you speak?
21:50:20:816: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
21:50:20:817: Checking profanity for sequence: What languages do you speak?
21:50:20:817: Classify master context for input sequence: What languages do you speak?
21:50:20:817: Classified master context: Generic
21:50:20:817: Classify context for input sequence: What languages do you speak?
21:50:20:817: Classified context: Language
21:50:20:817: Handling dialog: EN/Generic/Language
21:50:20:817:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
21:50:20:817:     Updated variable languages: English and Dutch
21:50:20:817:     Find matches for sequence: What languages do you speak?
21:50:20:817:     Found matches for sequence: 6
21:50:20:817:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1800000000000002 / 0.595959595959596)
21:50:20:817:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.76 / 0.38383838383838387)
21:50:20:817:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.6799999999999999 / 0.3434343434343434)
21:50:20:817:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.6799999999999999 / 0.3434343434343434)
21:50:20:817:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.6325000000000001 / 0.3194444444444445)
21:50:20:817:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.3 / 0.15151515151515152)
21:50:20:817:     Set prompt variable: language
21:50:20:817:     Set dialog output: I speak English and Dutch.
21:50:20:817: Handling dialog: EN/Generic/QuestionAndAnswer
21:50:20:817:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:50:20:817:     Find matches for sequence: What languages do you speak?
21:50:20:818:     Found matches for sequence: 17
21:50:20:818:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07721280602636535 / 0.2169312169312169)
21:50:20:818:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06756120527306968 / 0.18981481481481477)
21:50:20:818:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06756120527306968 / 0.18981481481481477)
21:50:20:818:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06756120527306968 / 0.18981481481481477)
21:50:20:818:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
21:50:20:818:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
21:50:20:818:     - What is the answer? [OUTPUT] Fourtytwo. (0.06746427384513128 / 0.1895424836601307)
21:50:20:818:     - What is the question? [OUTPUT] I do not know. (0.06746427384513128 / 0.1895424836601307)
21:50:20:818:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06746427384513128 / 0.1895424836601307)
21:50:20:818:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06746427384513128 / 0.1895424836601307)
21:50:20:818:     - [... 7]
21:50:20:818:     Set dialog output: My goal is to understand and help people.
Handling the request took: 3 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
21:50:20:818: Classify language for sequence: Spreek je duits?
21:50:20:818: Classified language: NL
21:50:20:818: Translate sequence: Spreek je duits?
21:50:20:819: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
21:50:20:819: Correction time limit: 400
21:50:20:819: Correcting sequence: Spreek je [] ?
21:50:20:819: Corrected sequence: Spreek je Duits?
21:50:20:819: Translate corrected sequence: Spreek je Duits?
21:50:20:819: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
21:50:20:820: Checking profanity for sequence: Spreek je [LNG]?
21:50:20:820: Classify master context for input sequence: Spreek je Duits?
21:50:20:820: Classify master context for classification sequence: Spreek je [LNG]?
21:50:20:820: Classified master context: Generic
21:50:20:820: Classify context for input sequence: Spreek je Duits?
21:50:20:820: Classify context for classification sequence: Spreek je [LNG]?
21:50:20:820: Classified context: Language
21:50:20:820: Handling dialog: NL/Generic/Language
21:50:20:820:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:50:20:820:     Updated variable language: NL_LNG:DE = Duits
21:50:20:820:     Updated variable languages: Engels en Nederlands
21:50:20:820:     Updated variable confirmation: Nee
21:50:20:820:     Find matches for sequence: Spreek je [LNG]?
21:50:20:820:     Found matches for sequence: 5
21:50:20:820:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.5079365079365077 / 0.9313725490196076)
21:50:20:820:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
21:50:20:820:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
21:50:20:820:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
21:50:20:820:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
21:50:20:820:     Set prompt variable: 
21:50:20:820:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 97 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
21:50:20:915: Classify language for sequence: Spreek je engels?
21:50:20:915: Classified language: NL
21:50:20:915: Translate sequence: Spreek je engels?
21:50:20:916: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
21:50:20:916: Correction time limit: 400
21:50:20:916: Correcting sequence: Spreek je [] ?
21:50:20:916: Corrected sequence: Spreek je Engels?
21:50:20:916: Translate corrected sequence: Spreek je Engels?
21:50:20:917: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
21:50:20:917: Checking profanity for sequence: Spreek je [LNG]?
21:50:20:917: Classify master context for input sequence: Spreek je Engels?
21:50:20:917: Classify master context for classification sequence: Spreek je [LNG]?
21:50:20:917: Classified master context: Generic
21:50:20:917: Classify context for input sequence: Spreek je Engels?
21:50:20:917: Classify context for classification sequence: Spreek je [LNG]?
21:50:20:917: Classified context: Language
21:50:20:917: Handling dialog: NL/Generic/Language
21:50:20:917:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:50:20:917:     Updated variable language: NL_LNG:EN = Engels
21:50:20:917:     Updated variable languages: Engels en Nederlands
21:50:20:917:     Updated variable confirmation: Ja
21:50:20:917:     Find matches for sequence: Spreek je [LNG]?
21:50:20:917:     Found matches for sequence: 5
21:50:20:917:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.5079365079365077 / 0.9313725490196076)
21:50:20:917:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
21:50:20:917:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.4603174603174603 / 0.28431372549019607)
21:50:20:917:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
21:50:20:917:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.45833333333333326 / 0.2830882352941176)
21:50:20:917:     Set prompt variable: 
21:50:20:917:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 2 ms

Request prompt: '', input: 'Wat kost overboeken naar buitenland?'
Response debug log;
21:50:20:918: Classify language for sequence: Wat kost overboeken naar buitenland?
21:50:20:918: Classified language: NL
21:50:20:918: Translate sequence: Wat kost overboeken naar buitenland?
21:50:20:919: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
21:50:20:920: Correction time limit: 600
21:50:20:920: Correcting sequence: Wat kost overboeken naar buitenland ?
21:50:20:920: Corrected sequence: Wat kost overboeken naar buitenland?
21:50:20:920: Translate corrected sequence: Wat kost overboeken naar buitenland?
21:50:20:920: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
21:50:20:921: Checking profanity for sequence: Wat kost overboeken naar buitenland?
21:50:20:921: Classify master context for input sequence: Wat kost overboeken naar buitenland?
21:50:20:921: Classified master context: ForeignTransfer
21:50:20:921: Classify context for input sequence: Wat kost overboeken naar buitenland?
21:50:20:921: Classified context: QuestionAndAnswer
21:50:20:921: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
21:50:20:921:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
21:50:20:921:     Updated variable durationDays: dagen
21:50:20:921:     Updated variable cost: vijf
21:50:20:921:     Updated variable costCurrency: euro
21:50:20:921:     Updated variable duration: drie
21:50:20:921:     Find matches for sequence: Wat kost overboeken naar buitenland?
21:50:20:922:     Found matches for sequence: 23
21:50:20:922:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.9481409001956947 / 0.5031152647975079)
21:50:20:922:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6550880626223092 / 0.3476116303219108)
21:50:20:922:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6550880626223092 / 0.3476116303219108)
21:50:20:922:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6550880626223092 / 0.3476116303219108)
21:50:20:922:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6550880626223092 / 0.3476116303219108)
21:50:20:922:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
21:50:20:922:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
21:50:20:922:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
21:50:20:922:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6049412915851271 / 0.3210020768431983)
21:50:20:922:     - Een buitenlandoverboeking. Wat kost dat? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
21:50:20:922:     - [... 13]
21:50:20:922:     Set prompt variable: 
21:50:20:922:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
21:50:20:922: Handling dialog: NL/ForeignTransfer/Cost
21:50:20:922:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
21:50:20:922:     Updated variable cost: vijf
21:50:20:922:     Updated variable costCurrency: euro
21:50:20:922:     Updated variable duration: drie
21:50:20:922:     Find matches for sequence: Wat kost overboeken naar buitenland?
21:50:20:922:     Found matches for sequence: 8
21:50:20:922:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5155502392344498 / 0.342063492063492)
21:50:20:922:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.31100478468899523 / 0.20634920634920634)
21:50:20:922:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.31100478468899523 / 0.20634920634920634)
21:50:20:922:     - Een overboeking naar [CNT]. Wat kost dat? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
21:50:20:922:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
21:50:20:922:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
21:50:20:922:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
21:50:20:922:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2822966507177034 / 0.1873015873015873)
Handling the request took: 98 ms

Request prompt: '', input: 'Wat kost overboeken naar italie?'
Response debug log;
21:50:21:015: Classify language for sequence: Wat kost overboeken naar italie?
21:50:21:015: Classified language: NL
21:50:21:015: Translate sequence: Wat kost overboeken naar italie?
21:50:21:016: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:italie ?
21:50:21:016: Correction time limit: 600
21:50:21:016: Correcting sequence: Wat kost overboeken naar italie ?
21:50:21:016: Corrected sequence: Wat kost overboeken naar italië?
21:50:21:016: Translate corrected sequence: Wat kost overboeken naar italië?
21:50:21:017: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italië ?
21:50:21:017: Checking profanity for sequence: Wat kost overboeken naar [CNT]?
21:50:21:017: Classify master context for input sequence: Wat kost overboeken naar italië?
21:50:21:017: Classify master context for classification sequence: Wat kost overboeken naar [CNT]?
21:50:21:017: Classified master context: ForeignTransfer
21:50:21:017: Classify context for input sequence: Wat kost overboeken naar italië?
21:50:21:017: Classify context for classification sequence: Wat kost overboeken naar [CNT]?
21:50:21:017: Classified context: Cost
21:50:21:017: Handling dialog: NL/ForeignTransfer/Cost
21:50:21:017:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
21:50:21:017:     Updated variable cost: vijf
21:50:21:017:     Updated variable costCurrency: euro
21:50:21:017:     Updated variable duration: drie
21:50:21:017:     Updated variable transferToCountry: NL_CNT:IT = Italië
21:50:21:017:     Find matches for sequence: Wat kost overboeken naar [CNT]?
21:50:21:017:     Found matches for sequence: 14
21:50:21:017:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.8313397129186603 / 0.5515873015873015)
21:50:21:017:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7320574162679426 / 0.48571428571428565)
21:50:21:017:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7320574162679426 / 0.48571428571428565)
21:50:21:017:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
21:50:21:017:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
21:50:21:017:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
21:50:21:017:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5454545454545454 / 0.36190476190476184)
21:50:21:017:     - Wat zijn de kosten van een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
21:50:21:017:     - Zijn er kosten verbonden aan geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
21:50:21:017:     - Hoeveel kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.4210526315789474 / 0.27936507936507937)
21:50:21:017:     - [... 4]
21:50:21:017:     Set prompt variable: 
21:50:21:017:     Set dialog output: Een overboeking naar Italië kost vijf euro.
21:50:21:017: Handling dialog: NL/ForeignTransfer/Duration
21:50:21:018:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
21:50:21:018:     Updated variable cost: vijf
21:50:21:018:     Updated variable costCurrency: euro
21:50:21:018:     Updated variable duration: drie
21:50:21:018:     Updated variable transferToCountry: NL_CNT:IT = Italië
21:50:21:018:     Find matches for sequence: Wat kost overboeken naar [CNT]?
21:50:21:018:     Found matches for sequence: 9
21:50:21:018:     - Hoeveel tijd kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.8907407407407407 / 0.6362433862433863)
21:50:21:018:     - Hoelang duurt geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.674074074074074 / 0.4814814814814815)
21:50:21:018:     - Hoeveel tijd kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.6018518518518519 / 0.42989417989417994)
21:50:21:018:     - Hoeveel tijd kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.6018518518518519 / 0.42989417989417994)
21:50:21:018:     - Hoelang duurt een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
21:50:21:018:     - Hoelang duurt het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
21:50:21:018:     - Hoelang duurt geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
21:50:21:018:     - Hoelang duurt het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.3851851851851852 / 0.2751322751322752)
21:50:21:018:     - Een overboeking naar [CNT]. Hoe lang duurt dat? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.1851851851851852 / 0.1322751322751323)
21:50:21:018:     Updated variable durationDays: dagen
21:50:21:018:     Set prompt variable: 
21:50:21:018:     Set dialog output: Een overboeking naar Italië duurt drie dagen.
21:50:21:018: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
21:50:21:018:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
21:50:21:018:     Updated variable durationDays: dagen
21:50:21:018:     Updated variable cost: vijf
21:50:21:018:     Updated variable costCurrency: euro
21:50:21:018:     Updated variable duration: drie
21:50:21:019:     Find matches for sequence: Wat kost overboeken naar [CNT]?
21:50:21:019:     Found matches for sequence: 11
21:50:21:019:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6942270058708415 / 0.368380062305296)
21:50:21:019:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.40117416829745595 / 0.21287642782969887)
21:50:21:019:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.40117416829745595 / 0.21287642782969887)
21:50:21:019:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.40117416829745595 / 0.21287642782969887)
21:50:21:019:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.40117416829745595 / 0.21287642782969887)
21:50:21:019:     - Een buitenlandoverboeking. Wat kost dat? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
21:50:21:019:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
21:50:21:019:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
21:50:21:019:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
21:50:21:019:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.3933463796477495 / 0.20872274143302183)
21:50:21:019:     - [... 1]
21:50:21:019:     Set prompt variable: 
21:50:21:019:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
Handling the request took: 100 ms

Request prompt: '', input: 'Je begrijpt me niet.'
Response debug log;
21:50:21:115: Classify language for sequence: Je begrijpt me niet.
21:50:21:116: Classified language: NL
21:50:21:116: Translate sequence: Je begrijpt me niet.
21:50:21:116: Translated sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
21:50:21:116: Correction time limit: 500
21:50:21:116: Correcting sequence: Je begrijpt me niet .
21:50:21:116: Corrected sequence: Je begrijpt me niet.
21:50:21:116: Translate corrected sequence: Je begrijpt me niet.
21:50:21:117: Translated corrected sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
21:50:21:117: Checking profanity for sequence: Je begrijpt me niet.
21:50:21:117: Classify master context for input sequence: Je begrijpt me niet.
21:50:21:117: Classified master context: Support
21:50:21:117: Classify context for input sequence: Je begrijpt me niet.
21:50:21:117: Classified context: Request
21:50:21:117: Handling dialog: NL/Support/Request
21:50:21:117:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequestHandler
21:50:21:118:     Find matches for sequence: Je begrijpt me niet.
21:50:21:118:     Selected filter context: supportNoTransferToHuman
21:50:21:118:     Found matches for sequence: 11
21:50:21:118:     - Jij begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.4803664921465969 / 0.752820512820513)
21:50:21:118:     - U begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.4803664921465969 / 0.752820512820513)
21:50:21:118:     - Je begrijpt mij niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.29728403141361254 / 0.46589743589743593)
21:50:21:118:     - Je begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.29728403141361254 / 0.46589743589743593)
21:50:21:118:     - Je kan me niet helpen. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16361256544502617 / 0.25641025641025644)
21:50:21:118:     - Je begrijpt mij verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
21:50:21:118:     - Jij begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
21:50:21:118:     - U begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
21:50:21:118:     - Je begrijpt er niks van. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.16099476439790575 / 0.25230769230769234)
21:50:21:118:     - Jij begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar {selfEmail}. (0.1557591623036649 / 0.24410256410256412)
21:50:21:118:     - [... 1]
21:50:21:118:     Set prompt variable: nextDialog
21:50:21:118:     Set dialog output: Ik ben nog aan het leren. Helaas kan ik u op dit moment niet doorverbinden met een mens. U kunt een e-mail sturen naar dyz.lecticus@zeesoft.nl.
21:50:21:118:     Set dialog prompt: Kan ik u ergens anders mee proberen te helpen?
Handling the request took: 100 ms

Request prompt: '', input: 'Je begrijpt me niet.'
Response debug log;
21:50:21:215: Classify language for sequence: Je begrijpt me niet.
21:50:21:215: Classified language: NL
21:50:21:215: Translate sequence: Je begrijpt me niet.
21:50:21:216: Translated sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
21:50:21:216: Correction time limit: 500
21:50:21:216: Correcting sequence: Je begrijpt me niet .
21:50:21:216: Corrected sequence: Je begrijpt me niet.
21:50:21:216: Translate corrected sequence: Je begrijpt me niet.
21:50:21:216: Translated corrected sequence: UN_ABC:Je UN_ABC:begrijpt UN_ABC:me UN_ABC:niet .
21:50:21:216: Checking profanity for sequence: Je begrijpt me niet.
21:50:21:216: Classify master context for input sequence: Je begrijpt me niet.
21:50:21:216: Classified master context: Support
21:50:21:216: Classify context for input sequence: Je begrijpt me niet.
21:50:21:216: Classified context: Request
21:50:21:216: Handling dialog: NL/Support/Request
21:50:21:216:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchSupportRequestHandler
21:50:21:216:     Find matches for sequence: Je begrijpt me niet.
21:50:21:216:     Selected filter context: supportTransferToHuman
21:50:21:217:     Found matches for sequence: 11
21:50:21:217:     - Jij begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. (0.9943181818181818 / 0.7070707070707071)
21:50:21:217:     - U begrijpt me niet. [OUTPUT] Ik ben nog aan het leren. (0.9943181818181818 / 0.7070707070707071)
21:50:21:217:     - Je begrijpt mij niet. [OUTPUT] Ik ben nog aan het leren. (0.6086647727272727 / 0.43282828282828284)
21:50:21:217:     - Je begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. (0.6086647727272727 / 0.43282828282828284)
21:50:21:217:     - Je kan me niet helpen. [OUTPUT] Ik ben nog aan het leren. (0.34659090909090906 / 0.24646464646464644)
21:50:21:217:     - Je begrijpt mij verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
21:50:21:217:     - Jij begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
21:50:21:217:     - U begrijpt me verkeerd. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
21:50:21:217:     - Je begrijpt er niks van. [OUTPUT] Ik ben nog aan het leren. (0.3352272727272727 / 0.23838383838383836)
21:50:21:217:     - Jij begrijpt het niet. [OUTPUT] Ik ben nog aan het leren. (0.3125 / 0.2222222222222222)
21:50:21:217:     - [... 1]
21:50:21:217:     Set prompt variable: supportConfirmation
21:50:21:217:     Set dialog output: Ik ben nog aan het leren.
21:50:21:217:     Set dialog prompt: Zal ik u doorverbinden met een mens?
Handling the request took: 2 ms
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
  "timeStamp": "2018-08-15 21:50:36:512",
  "totals": {
    "tests": 1371,
    "successful": 1347,
    "successPercentage": 98.24945,
    "durationMs": 14078,
    "averageRequestMs": 2,
    "outputTests": 1196,
    "succesfulOutputTests": 1172,
    "successPercentageOutputTests": 97.99331,
    "averageOutputTestRequestMs": 3,
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
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.9717873814287314)"
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
Total test duration: 70960 ms (total sleep duration: 34500 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 651 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 428 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 460 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 467 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 476 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117808 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117824 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117820 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 118136 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117920 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117955 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117929 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 473865 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 473866 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 473866 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 473867 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 473874 Kb / 462 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 478740 Kb / 467 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 478760 Kb / 467 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerTester: 478789 Kb / 467 Mb
