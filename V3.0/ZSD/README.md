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
Initializing the SymbolCorrector took: 6 ms
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
Average correction time: 230 ms
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
Initializing the SequenceClassifier took: 3279 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 4 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 3 ms

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
Initializing the SequenceMatcher took: 6488 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 19 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 127 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 98 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 116 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 38 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 36 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 74 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 33 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 43 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 132 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 337 ms

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
Initializing the EntityValueTranslator took: 1596 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 2 ms
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
Retranslating the sequence took: 1 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 1 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 1 ms
Translation: 'NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december'
Retranslating the sequence took: 0 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 2 ms
Translation: 'EN_DUR:33:41 UN_MTH:D NL_DUR:33:41'
Retranslating the sequence took: 0 ms
Retranslation: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'

Sequence: 'yesterday OR today OR the 1st of october'
Translating the sequence took: 3 ms
Translation: 'EN_DAT:2018-07-15|UN_ABC:yesterday UN_ABC:OR EN_DAT:2018-07-16|UN_ABC:today UN_ABC:OR EN_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen'

Sequence: 'gisteren OF vandaag OF 1 oktober'
Translating the sequence took: 0 ms
Translation: 'NL_DAT:2018-07-15|UN_ABC:gisteren UN_ABC:OF NL_DAT:2018-07-16|UN_ABC:vandaag UN_ABC:OF NL_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien'

Sequence: 'twelve o'clock OR five minutes to nine OR ten past one in the morning'
Translating the sequence took: 0 ms
Translation: 'EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00'
Retranslating the sequence took: 1 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 0 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 1 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 0 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 10 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:Andre|NL_NAM:firstName:UN_ABC:Andre NL_PRE:3|NL_NAM:preposition:NL_PRE:3 UN_ABC:Zee|NL_NAM:lastName:UN_ABC:Zee'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam is Andre van der Zee'

Sequence: 'Hoe heet jij? gekke henkie'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Hoe UN_ABC:heet UN_ABC:jij ? UN_ABC:gekke|NL_NAM:firstName:UN_ABC:Gekke UN_ABC:henkie|NL_NAM:lastName:UN_ABC:Henkie'
Retranslating the sequence took: 0 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies UN_ABC:is UN_ABC:mijn UN_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 2 ms
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
Translating the sequence took: 4 ms
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
Converting 333335 entity values took: 410 ms

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
Converting 1493 dialog examples took: 179 ms

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
      "input": "Nothing.",
      "output": "Allright.",
      "context": "EN"
    },
    {
      "input": "Nevermind.",
      "output": "Allright.",
      "context": "EN"
    },
    {
      "input": "Nothing.",
      "output": "Fine.",
      "context": "EN"
    },
    {
      "input": "Nevermind.",
      "output": "Fine.",
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
Converting 13 dialogs took: 1 ms

Sample JSON;
{
  "dialogs": [
    {
      "language": "EN",
      "masterContext": "Generic",
      "context": "Classification",
      "handler": "",
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
Initializing Translator took 2375 ms
Initializing Classifier took 4338 ms
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

Initializing LanguagePreprocessor took 17 ms
Initializing LanguageContextENSupport took 23 ms
Initializing LanguageContextNLForeignTransfer took 45 ms
Initializing LanguageContextENForeignTransfer took 64 ms
Initializing LanguageContextENGeneric took 120 ms
Initializing LanguageContextNLSupport took 166 ms
Initializing LanguageContextNLGeneric took 327 ms
Initializing LanguageMasterContextEN took 381 ms
Initializing LanguageContextENRoom took 376 ms
Initializing LanguageContextNLRoom took 463 ms
Initializing LanguageMasterContextNL took 498 ms
Initializing EntityValueTranslator took 2209 ms
Initializing LanguageClassifier took 32857 ms
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
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'twothousand' -> EN
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.23111870477588764 / 0.993167144614846
'NL': 0.0791904562558703 / 0.340298546570061

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.23111870477588764 / 0.993167144614846
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
'Generic': 0.2609055935646821 / 0.8717053633401909

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.3340605636687014 / 1.0
~~~~

nl.zeesoft.zsd.test.TestLanguageContextClassifier
-------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as context classifiers.
~~~~
Classified sequence: 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 2 ms
Classified sequence (case insensitive): 'What is your goal?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your goal?', threshold: 0.0
'QuestionAndAnswer': 0.303145171111923 / 0.7294139194139194
'Handshake': 0.17583543639166005 / 0.4230871115639328

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.32192175587721233 / 0.5466886824357929
'Handshake': 0.27270108663263076 / 0.4631019650839167

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.4046510052336513 / 0.9736525725929701
'QuestionAndAnswer': 0.12478687127024723 / 0.30025641025641026

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5848387929835714 / 0.9931753394619214
'QuestionAndAnswer': 0.14767184139375567 / 0.2507768516123899

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.309097552064304 / 0.9296703296703297
'Handshake': 0.23543808539828254 / 0.708125318390219
'Thanks': 0.058296503975430444 / 0.17533794657225618

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.3318885332858502 / 0.7045178745499037
'Handshake': 0.3079123542382646 / 0.6536223327991904
'Language': 0.11949181518998161 / 0.25365178733442206
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
01:55:04:234: Classify language for sequence: Hallo.
01:55:04:234: Classified language: NL
01:55:04:234: Translate sequence: Hallo.
01:55:04:234: Translated sequence: UN_ABC:Hallo .
01:55:04:235: Correction time limit: 200
01:55:04:235: Correcting sequence: Hallo .
01:55:04:235: Corrected sequence: Hallo.
01:55:04:235: Translate corrected sequence: Hallo.
01:55:04:235: Translated corrected sequence: UN_ABC:Hallo .
01:55:04:235: Checking profanity for sequence: Hallo.
01:55:04:236: Classify master context for input sequence: Hallo.
01:55:04:236: Classified master context: Generic
01:55:04:236: Classify context for input sequence: Hallo.
01:55:04:236: Classified context: Handshake
Interpreting the request took: 3 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
01:55:04:236: Classify language for sequence: Mijn naam si gekste der henkies.
01:55:04:237: Classified language: NL
01:55:04:237: Translate sequence: Mijn naam si gekste der henkies.
01:55:04:239: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
01:55:04:240: Correction time limit: 700
01:55:04:240: Correcting sequence: Mijn naam si gekste [] henkies .
01:55:04:737: Corrected sequence: Mijn naam is gekste der henkies.
01:55:04:737: Translate corrected sequence: Mijn naam is gekste der henkies.
01:55:04:741: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
01:55:04:741: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:04:741: Classify master context for input sequence: Mijn naam is gekste der henkies.
01:55:04:741: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:04:742: Classified master context: Generic
01:55:04:742: Classify context for input sequence: Mijn naam is gekste der henkies.
01:55:04:742: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:04:742: Classified context: Handshake
Interpreting the request took: 506 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
01:55:04:742: Classify language for sequence: Albert einstein.
01:55:04:742: Classify language for sequence: What is your name? Albert einstein.
01:55:04:742: Classified language: EN
01:55:04:743: Translate sequence: Albert einstein.
01:55:04:745: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:55:04:745: Correction time limit: 300
01:55:04:746: Correcting sequence: [] [] .
01:55:04:746: Corrected sequence: Albert einstein.
01:55:04:746: Translate corrected sequence: Albert einstein.
01:55:04:748: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:55:04:748: Checking profanity for sequence: [ABC] [ABC].
01:55:04:749: Classify master context for input sequence: Albert einstein.
01:55:04:749: Classify master context for classification sequence: What is your name? [ABC] [ABC].
01:55:04:749: Classified master context: Generic
01:55:04:749: Classify context for input sequence: Albert einstein.
01:55:04:749: Classify context for classification sequence: What is your name? [ABC] [ABC].
01:55:04:749: Classified context: Handshake
Interpreting the request took: 7 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
01:55:04:750: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:55:04:750: Selected primary language: EN
01:55:04:750: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:55:04:754: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
01:55:04:754: Correction time limit: 1100
01:55:04:754: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
01:55:05:854: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:55:05:854: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:55:05:855: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
01:55:05:855: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:55:05:856: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1107 ms
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

Initializing DialogSet took 360 ms
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
Request prompt: '', input: 'What are you?'
Response debug log;
01:55:08:393: Classify language for sequence: What are you?
01:55:08:393: Classified language: EN
01:55:08:393: Translate sequence: What are you?
01:55:08:394: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
01:55:08:394: Correction time limit: 400
01:55:08:394: Correcting sequence: What are you ?
01:55:08:394: Corrected sequence: What are you?
01:55:08:395: Translate corrected sequence: What are you?
01:55:08:396: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
01:55:08:397: Checking profanity for sequence: What are you?
01:55:08:397: Classify master context for input sequence: What are you?
01:55:08:397: Classified master context: Generic
01:55:08:397: Classify context for input sequence: What are you?
01:55:08:397: Classified context: QuestionAndAnswer
01:55:08:397: Handling dialog: EN/Generic/QuestionAndAnswer
01:55:08:397:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:08:398:     Find matches for sequence: What are you?
01:55:08:398:     Found matches for sequence: 10
01:55:08:399:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.21428571428571425 / 0.8809523809523809)
01:55:08:399:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
01:55:08:400:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
01:55:08:401:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
01:55:08:401:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
01:55:08:401:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
01:55:08:401:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:08:401:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:08:401:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:08:401:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:08:401:     Set dialog output: I am an artificially intelligent personal assistant.
01:55:08:401: Handling dialog: EN/Generic/Handshake
01:55:08:401:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:08:401:     Find matches for sequence: What are you?
01:55:08:402:     Found matches for sequence: 2
01:55:08:402:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
01:55:08:402:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
01:55:08:402:     Prompt variable: firstName
01:55:08:402:     Set dialog output: My name is Dyz Lecticus.
01:55:08:402:     Set dialog prompt: What is your name?
Handling the request took: 75 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
01:55:08:468: Classify language for sequence: Wat ben jij?
01:55:08:468: Classified language: NL
01:55:08:468: Translate sequence: Wat ben jij?
01:55:08:469: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
01:55:08:471: Correction time limit: 400
01:55:08:471: Correcting sequence: Wat ben jij ?
01:55:08:471: Corrected sequence: Wat ben jij?
01:55:08:471: Translate corrected sequence: Wat ben jij?
01:55:08:472: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
01:55:08:473: Checking profanity for sequence: Wat ben jij?
01:55:08:473: Classify master context for input sequence: Wat ben jij?
01:55:08:473: Classified master context: Generic
01:55:08:473: Classify context for input sequence: Wat ben jij?
01:55:08:473: Classified context: QuestionAndAnswer
01:55:08:473: Handling dialog: NL/Generic/QuestionAndAnswer
01:55:08:473:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:08:473:     Find matches for sequence: Wat ben jij?
01:55:08:473:     Found matches for sequence: 9
01:55:08:474:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.46601208459214505 / 0.9793650793650793)
01:55:08:474:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:55:08:474:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:55:08:474:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:55:08:474:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:55:08:474:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
01:55:08:474:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
01:55:08:474:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
01:55:08:474:     - Ben jij een menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
01:55:08:474:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
01:55:08:474: Handling dialog: NL/Generic/Handshake
01:55:08:474:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:08:474:     Find matches for sequence: Wat ben jij?
01:55:08:474:     Found matches for sequence: 5
01:55:08:474:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
01:55:08:475:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
01:55:08:475:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
01:55:08:475:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
01:55:08:475:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
01:55:08:475:     Prompt variable: firstName
01:55:08:475:     Set dialog output: Mijn naam is Dyz Lecticus.
01:55:08:475:     Set dialog prompt: Wat is jouw naam?
01:55:08:475: Handling dialog: NL/Generic/Language
01:55:08:476:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:55:08:476:     Updated variable languages: Engels en Nederlands
01:55:08:476:     Find matches for sequence: Wat ben jij?
01:55:08:476:     Found matches for sequence: 2
01:55:08:476:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:55:08:476:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:55:08:476:     Prompt variable: language
01:55:08:476:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 100 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
01:55:08:568: Classify language for sequence: What is the answer?
01:55:08:568: Classified language: EN
01:55:08:568: Translate sequence: What is the answer?
01:55:08:569: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
01:55:08:569: Correction time limit: 500
01:55:08:569: Correcting sequence: What is [] answer ?
01:55:08:569: Corrected sequence: What is the answer?
01:55:08:569: Translate corrected sequence: What is the answer?
01:55:08:570: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
01:55:08:570: Checking profanity for sequence: What is [PRE] answer?
01:55:08:570: Classify master context for input sequence: What is the answer?
01:55:08:570: Classify master context for classification sequence: What is [PRE] answer?
01:55:08:570: Selected input sequence master context classification.
01:55:08:570: Classified master context: Generic
01:55:08:570: Classify context for input sequence: What is the answer?
01:55:08:570: Classified context: QuestionAndAnswer
01:55:08:570: Handling dialog: EN/Generic/QuestionAndAnswer
01:55:08:570:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:08:570:     Find matches for sequence: What is the answer?
01:55:08:570:     Found matches for sequence: 9
01:55:08:570:     - What is the answer? [OUTPUT] Fourtytwo. (0.2548262548262548 / 0.8380952380952382)
01:55:08:570:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
01:55:08:570:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
01:55:08:570:     - What is the question? [OUTPUT] I do not know. (0.10424710424710423 / 0.34285714285714286)
01:55:08:570:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.10424710424710423 / 0.34285714285714286)
01:55:08:570:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.10424710424710423 / 0.34285714285714286)
01:55:08:570:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.10424710424710423 / 0.34285714285714286)
01:55:08:570:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
01:55:08:570:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
01:55:08:570:     Set dialog output: Fourtytwo.
Handling the request took: 3 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
01:55:08:571: Classify language for sequence: Wat is het antwoord?
01:55:08:571: Classified language: NL
01:55:08:571: Translate sequence: Wat is het antwoord?
01:55:08:571: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
01:55:08:571: Correction time limit: 500
01:55:08:571: Correcting sequence: Wat is het antwoord ?
01:55:08:571: Corrected sequence: Wat is het antwoord?
01:55:08:571: Translate corrected sequence: Wat is het antwoord?
01:55:08:572: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
01:55:08:572: Checking profanity for sequence: Wat is het antwoord?
01:55:08:572: Classify master context for input sequence: Wat is het antwoord?
01:55:08:572: Classified master context: Generic
01:55:08:572: Classify context for input sequence: Wat is het antwoord?
01:55:08:572: Classified context: QuestionAndAnswer
01:55:08:572: Handling dialog: NL/Generic/QuestionAndAnswer
01:55:08:572:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:08:572:     Find matches for sequence: Wat is het antwoord?
01:55:08:572:     Found matches for sequence: 10
01:55:08:572:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.6102719033232629 / 1.0)
01:55:08:572:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
01:55:08:572:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
01:55:08:572:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.297583081570997 / 0.4876237623762376)
01:55:08:573:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.14425981873111782 / 0.23638613861386137)
01:55:08:573:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
01:55:08:573:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
01:55:08:573:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.14425981873111782 / 0.23638613861386137)
01:55:08:573:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
01:55:08:573:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
01:55:08:573:     Set dialog output: Tweeenveertig.
Handling the request took: 97 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
01:55:08:668: Classify language for sequence: Hallo.
01:55:08:668: Classified language: NL
01:55:08:668: Translate sequence: Hallo.
01:55:08:668: Translated sequence: UN_ABC:Hallo .
01:55:08:668: Correction time limit: 200
01:55:08:668: Correcting sequence: Hallo .
01:55:08:669: Corrected sequence: Hallo.
01:55:08:669: Translate corrected sequence: Hallo.
01:55:08:669: Translated corrected sequence: UN_ABC:Hallo .
01:55:08:669: Checking profanity for sequence: Hallo.
01:55:08:669: Classify master context for input sequence: Hallo.
01:55:08:669: Classified master context: Generic
01:55:08:669: Classify context for input sequence: Hallo.
01:55:08:669: Classified context: Handshake
01:55:08:669: Handling dialog: NL/Generic/Handshake
01:55:08:669:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:08:669:     Find matches for sequence: Hallo.
01:55:08:669:     Found matches for sequence: 2
01:55:08:669:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
01:55:08:669:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
01:55:08:669:     Prompt variable: firstName
01:55:08:669:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
01:55:08:669:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
01:55:08:670: Classify language for sequence: Wie ben jij?
01:55:08:670: Classified language: NL
01:55:08:670: Translate sequence: Wie ben jij?
01:55:08:671: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
01:55:08:671: Correction time limit: 400
01:55:08:671: Correcting sequence: Wie ben jij ?
01:55:08:671: Corrected sequence: Wie ben jij?
01:55:08:671: Translate corrected sequence: Wie ben jij?
01:55:08:672: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
01:55:08:672: Checking profanity for sequence: Wie ben jij?
01:55:08:672: Classify master context for input sequence: Wie ben jij?
01:55:08:672: Classified master context: Generic
01:55:08:672: Classify context for input sequence: Wie ben jij?
01:55:08:672: Classified context: Handshake
01:55:08:672: Handling dialog: NL/Generic/Handshake
01:55:08:672:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:08:672:     Find matches for sequence: Wie ben jij?
01:55:08:672:     Found matches for sequence: 2
01:55:08:672:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
01:55:08:672:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
01:55:08:672:     Prompt variable: firstName
01:55:08:672:     Set dialog output: Mijn naam is Dyz Lecticus.
01:55:08:672:     Set dialog prompt: Wat is jouw naam?
01:55:08:672: Handling dialog: NL/Generic/QuestionAndAnswer
01:55:08:672:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:08:673:     Find matches for sequence: Wie ben jij?
01:55:08:673:     Found matches for sequence: 15
01:55:08:673:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.3081570996978852 / 0.6476190476190475)
01:55:08:673:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:55:08:673:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:55:08:673:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:55:08:673:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:55:08:673:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
01:55:08:673:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.15312375306390014 / 0.3218029350104822)
01:55:08:673:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
01:55:08:673:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
01:55:08:673:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
01:55:08:673:     - [... 5]
01:55:08:673:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
01:55:08:673: Handling dialog: NL/Generic/Language
01:55:08:673:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:55:08:673:     Updated variable languages: Engels en Nederlands
01:55:08:673:     Find matches for sequence: Wie ben jij?
01:55:08:673:     Found matches for sequence: 2
01:55:08:673:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:55:08:674:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:55:08:674:     Prompt variable: language
01:55:08:674:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 98 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
01:55:08:768: Classify language for sequence: Albert einstein.
01:55:08:768: Classify language for sequence: What is your name? Albert einstein.
01:55:08:768: Classified language: EN
01:55:08:769: Translate sequence: Albert einstein.
01:55:08:770: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:55:08:770: Correction time limit: 300
01:55:08:770: Correcting sequence: [] [] .
01:55:08:770: Corrected sequence: Albert einstein.
01:55:08:770: Translate corrected sequence: Albert einstein.
01:55:08:771: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:55:08:771: Checking profanity for sequence: [ABC] [ABC].
01:55:08:771: Classify master context for input sequence: Albert einstein.
01:55:08:771: Classify master context for classification sequence: What is your name? [ABC] [ABC].
01:55:08:771: Classified master context: Generic
01:55:08:771: Classify context for input sequence: Albert einstein.
01:55:08:771: Classify context for classification sequence: What is your name? [ABC] [ABC].
01:55:08:771: Classified context: Handshake
01:55:08:771: Handling dialog: EN/Generic/Handshake
01:55:08:771:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:08:771:     Updated variable firstName: UN_ABC:Albert = Albert
01:55:08:771:     Updated variable lastName: UN_ABC:Einstein = Einstein
01:55:08:771:     Find matches for sequence: [ABC] [ABC].
01:55:08:772:     Found matches for sequence: 0
01:55:08:772:     Updated variable fullName: Albert Einstein
01:55:08:772:     Prompt variable: nextDialog
01:55:08:772:     Set dialog output: 
01:55:08:772:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 4 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
01:55:08:772: Classify language for sequence: Mijn naam si gekste der henkies.
01:55:08:772: Classified language: NL
01:55:08:772: Translate sequence: Mijn naam si gekste der henkies.
01:55:08:773: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
01:55:08:773: Correction time limit: 700
01:55:08:773: Correcting sequence: Mijn naam si gekste [] henkies .
01:55:09:163: Corrected sequence: Mijn naam is gekste der henkies.
01:55:09:163: Translate corrected sequence: Mijn naam is gekste der henkies.
01:55:09:164: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
01:55:09:164: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:09:164: Classify master context for input sequence: Mijn naam is gekste der henkies.
01:55:09:164: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:09:164: Classified master context: Generic
01:55:09:164: Classify context for input sequence: Mijn naam is gekste der henkies.
01:55:09:164: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:09:164: Classified context: Handshake
01:55:09:164: Handling dialog: NL/Generic/Handshake
01:55:09:165:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:09:165:     Updated variable firstName: UN_ABC:Gekste = Gekste
01:55:09:165:     Updated variable lastName: UN_ABC:Henkies = Henkies
01:55:09:165:     Updated variable preposition: NL_PRE:6 = der
01:55:09:165:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:09:165:     Found matches for sequence: 18
01:55:09:165:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:165:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:165:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:165:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:165:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:165:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:165:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:166:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:166:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:166:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:166:     - [... 8]
01:55:09:166:     Updated variable fullName: Gekste der Henkies
01:55:09:166:     Prompt variable: nextDialog
01:55:09:166:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
01:55:09:166:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 394 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
01:55:09:166: Classify language for sequence: Mijn naam is jan de lange.
01:55:09:167: Classified language: NL
01:55:09:167: Translate sequence: Mijn naam is jan de lange.
01:55:09:168: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
01:55:09:168: Correction time limit: 700
01:55:09:168: Correcting sequence: Mijn naam is [] [] [] .
01:55:09:169: Corrected sequence: Mijn naam is jan de lange.
01:55:09:169: Translate corrected sequence: Mijn naam is jan de lange.
01:55:09:171: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
01:55:09:171: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:09:171: Classify master context for input sequence: Mijn naam is jan de lange.
01:55:09:171: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
01:55:09:172: Classified master context: Generic
01:55:09:172: Classify context for input sequence: Mijn naam is jan de lange.
01:55:09:172: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
01:55:09:172: Classified context: Handshake
01:55:09:172: Handling dialog: NL/Generic/Handshake
01:55:09:172:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:09:172:     Updated variable firstName: UN_ABC:Jan = Jan
01:55:09:172:     Updated variable lastName: UN_ABC:Lange = Lange
01:55:09:172:     Updated variable preposition: NL_PRE:5 = de
01:55:09:172:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:55:09:173:     Found matches for sequence: 18
01:55:09:173:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:55:09:173:     - [... 8]
01:55:09:173:     Updated variable fullName: Jan de Lange
01:55:09:173:     Prompt variable: nextDialog
01:55:09:173:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
01:55:09:173:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 7 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
01:55:09:173: Classify language for sequence: Who created you?
01:55:09:173: Classified language: EN
01:55:09:174: Translate sequence: Who created you?
01:55:09:174: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
01:55:09:174: Correction time limit: 400
01:55:09:174: Correcting sequence: Who created you ?
01:55:09:174: Corrected sequence: Who created you?
01:55:09:174: Translate corrected sequence: Who created you?
01:55:09:175: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
01:55:09:175: Checking profanity for sequence: Who created you?
01:55:09:175: Classify master context for input sequence: Who created you?
01:55:09:175: Classified master context: Generic
01:55:09:175: Classify context for input sequence: Who created you?
01:55:09:175: Classified context: QuestionAndAnswer
01:55:09:176: Handling dialog: EN/Generic/QuestionAndAnswer
01:55:09:176:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:09:176:     Find matches for sequence: Who created you?
01:55:09:176:     Found matches for sequence: 5
01:55:09:176:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.222007722007722 / 0.9126984126984129)
01:55:09:176:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.0637065637065637 / 0.2619047619047619)
01:55:09:176:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:09:176:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:09:176:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:55:09:176:     Set dialog output: My software was written by André van der Zee.
01:55:09:176: Handling dialog: EN/Generic/Handshake
01:55:09:176:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:55:09:176:     Find matches for sequence: Who created you?
01:55:09:176:     Found matches for sequence: 1
01:55:09:176:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
01:55:09:176:     Prompt variable: firstName
01:55:09:176:     Set dialog output: My name is Dyz Lecticus.
01:55:09:176:     Set dialog prompt: What is your name?
Handling the request took: 96 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
01:55:09:269: Classify language for sequence: You asshole!
01:55:09:270: Classified language: EN
01:55:09:270: Translate sequence: You asshole!
01:55:09:271: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
01:55:09:271: Correction time limit: 300
01:55:09:271: Correcting sequence: You [] !
01:55:09:272: Corrected sequence: You asshole!
01:55:09:272: Translate corrected sequence: You asshole!
01:55:09:272: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
01:55:09:272: Checking profanity for sequence: You [PRF]!
01:55:09:272: Handling dialog: EN/Generic/Profanity
01:55:09:272:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:09:273:     Find matches for sequence: You [PRF]!
01:55:09:273:     Found matches for sequence: 2
01:55:09:273:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
01:55:09:273:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
01:55:09:273:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 4 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
01:55:09:273: Classify language for sequence: Jij klootzak!
01:55:09:273: Classified language: NL
01:55:09:273: Translate sequence: Jij klootzak!
01:55:09:274: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
01:55:09:274: Correction time limit: 300
01:55:09:274: Correcting sequence: Jij [] !
01:55:09:274: Corrected sequence: Jij klootzak!
01:55:09:275: Translate corrected sequence: Jij klootzak!
01:55:09:275: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
01:55:09:275: Checking profanity for sequence: Jij [PRF]!
01:55:09:276: Handling dialog: NL/Generic/Profanity
01:55:09:276:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:09:276:     Find matches for sequence: Jij [PRF]!
01:55:09:276:     Found matches for sequence: 2
01:55:09:276:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
01:55:09:276:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
01:55:09:276:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 4 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
01:55:09:277: Classify language for sequence: Can I book a room for 5 people?
01:55:09:277: Classified language: EN
01:55:09:277: Translate sequence: Can I book a room for 5 people?
01:55:09:279: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
01:55:09:279: Correction time limit: 900
01:55:09:279: Correcting sequence: Can I book a room for [] people ?
01:55:09:280: Corrected sequence: Can I book a room for 5 people?
01:55:09:280: Translate corrected sequence: Can I book a room for 5 people?
01:55:09:281: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
01:55:09:281: Checking profanity for sequence: Can I book a room for [NUM] people?
01:55:09:282: Classify master context for input sequence: Can I book a room for 5 people?
01:55:09:282: Classify master context for classification sequence: Can I book a room for [NUM] people?
01:55:09:282: Classified master context: Room
01:55:09:282: Classify context for input sequence: Can I book a room for 5 people?
01:55:09:282: Classify context for classification sequence: Can I book a room for [NUM] people?
01:55:09:282: Classified context: Booking
01:55:09:282: Handling dialog: EN/Room/Booking
01:55:09:283:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
01:55:09:283:     Updated variable bookPeople: UN_NUM:5 = 5
01:55:09:283:     Find matches for sequence: Can I book a room for [NUM] people?
01:55:09:285:     Found matches for sequence: 329
01:55:09:285:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
01:55:09:285:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
01:55:09:285:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
01:55:09:286:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
01:55:09:286:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
01:55:09:286:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
01:55:09:286:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
01:55:09:286:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
01:55:09:286:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
01:55:09:286:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
01:55:09:286:     - [... 319]
01:55:09:286:     Prompt variable: bookDate
01:55:09:287:     Set dialog output: Okay.
01:55:09:287:     Set dialog prompt: On what date?
Handling the request took: 94 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
01:55:09:373: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
01:55:09:374: Classified language: EN
01:55:09:374: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
01:55:09:375: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
01:55:09:376: Correction time limit: 1332
01:55:09:376: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
01:55:09:377: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:55:09:377: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:55:09:379: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
01:55:09:380: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:55:09:381: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:55:09:381: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:55:09:381: Classified master context: Room
01:55:09:382: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:55:09:382: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:55:09:382: Classified context: Booking
01:55:09:382: Handling dialog: EN/Room/Booking
01:55:09:383:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
01:55:09:383:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
01:55:09:383:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
01:55:09:384:     Updated variable bookDuration: EN_DUR:02:00 = two hours
01:55:09:384:     Updated variable bookPeople: UN_NUM:12 = 12
01:55:09:384:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:55:09:387:     Found matches for sequence: 263
01:55:09:387:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
01:55:09:387:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
01:55:09:387:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
01:55:09:388:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
01:55:09:388:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
01:55:09:388:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
01:55:09:388:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
01:55:09:388:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
01:55:09:388:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
01:55:09:388:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
01:55:09:388:     - [... 253]
01:55:09:389:     Prompt variable: bookConfirmation
01:55:09:389:     Set dialog output: Okay.
01:55:09:389:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 101 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
01:55:09:473: Classify language for sequence: What does it cost to book a room?
01:55:09:475: Classified language: EN
01:55:09:475: Translate sequence: What does it cost to book a room?
01:55:09:477: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
01:55:09:478: Correction time limit: 900
01:55:09:478: Correcting sequence: What does it cost to book a room ?
01:55:09:478: Corrected sequence: What does it cost to book a room?
01:55:09:478: Translate corrected sequence: What does it cost to book a room?
01:55:09:480: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
01:55:09:480: Checking profanity for sequence: What does it cost to book a room?
01:55:09:481: Classify master context for input sequence: What does it cost to book a room?
01:55:09:481: Classified master context: Room
01:55:09:481: Classify context for input sequence: What does it cost to book a room?
01:55:09:481: Classified context: QuestionAndAnswer
01:55:09:482: Handling dialog: EN/Room/QuestionAndAnswer
01:55:09:482:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:09:482:     Find matches for sequence: What does it cost to book a room?
01:55:09:482:     Found matches for sequence: 4
01:55:09:482:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
01:55:09:482:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
01:55:09:482:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
01:55:09:482:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
01:55:09:482:     Set dialog output: There are no costs attached to booking a room.
01:55:09:482: Handling dialog: EN/Room/Booking
01:55:09:482:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
01:55:09:483:     Find matches for sequence: What does it cost to book a room?
01:55:09:485:     Found matches for sequence: 276
01:55:09:485:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:485:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:55:09:486:     - [... 266]
01:55:09:486:     Prompt variable: bookDate
01:55:09:486:     Set dialog output: Okay.
01:55:09:486:     Set dialog prompt: On what date?
Handling the request took: 14 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
01:55:09:487: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
01:55:09:488: Classified language: EN
01:55:09:488: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
01:55:09:491: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
01:55:09:492: Correction time limit: 1300
01:55:09:492: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
01:55:09:493: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:55:09:493: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:55:09:495: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
01:55:09:495: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:495: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:55:09:496: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:496: Classified master context: Generic
01:55:09:496: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:55:09:496: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:496: Classified context: Math
01:55:09:496: Handling dialog: EN/Generic/Math
01:55:09:497:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
01:55:09:497:     Updated variable number1: EN_NUM:10 = ten
01:55:09:497:     Updated variable number2: EN_NUM:40 = fourty
01:55:09:497:     Updated variable number3: EN_NUM:20 = twenty
01:55:09:497:     Updated variable number4: EN_NUM:3 = three
01:55:09:497:     Updated variable number5: EN_NUM:6 = six
01:55:09:497:     Updated variable operator1: EN_MTH:M = multiplied by
01:55:09:497:     Updated variable operator2: EN_MTH:D = divided by
01:55:09:497:     Updated variable operator3: EN_MTH:A = plus
01:55:09:497:     Updated variable operator4: EN_MTH:M = multiplied by
01:55:09:497:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:497:     Found matches for sequence: 0
01:55:09:497:     Calculate expression: 10 M 40 D 20 A 3 M 6
01:55:09:498:     Calculated expression: 38.0
01:55:09:498:     Updated variable exact: exactly
01:55:09:498:     Updated variable result: thirtyeight
01:55:09:498:     Prompt variable: nextDialog
01:55:09:498:     Set dialog output: Exactly thirtyeight.
01:55:09:498:     Set dialog prompt: What else can I do for you?
Handling the request took: 87 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
01:55:09:573: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
01:55:09:574: Classified language: NL
01:55:09:574: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
01:55:09:575: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
01:55:09:576: Correction time limit: 1200
01:55:09:576: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
01:55:09:576: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:55:09:576: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:55:09:578: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
01:55:09:578: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:579: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:55:09:579: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:579: Classified master context: Generic
01:55:09:579: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:55:09:579: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:579: Classified context: Math
01:55:09:579: Handling dialog: NL/Generic/Math
01:55:09:580:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
01:55:09:580:     Updated variable number1: NL_NUM:10 = tien
01:55:09:580:     Updated variable number2: NL_NUM:40 = veertig
01:55:09:580:     Updated variable number3: NL_NUM:20 = twintig
01:55:09:580:     Updated variable number4: NL_NUM:3 = drie
01:55:09:580:     Updated variable number5: NL_NUM:6 = zes
01:55:09:580:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
01:55:09:580:     Updated variable operator2: NL_MTH:D = gedeeld door
01:55:09:580:     Updated variable operator3: NL_MTH:A = plus
01:55:09:580:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
01:55:09:580:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:55:09:580:     Found matches for sequence: 0
01:55:09:580:     Calculate expression: 10 M 40 D 20 A 3 M 6
01:55:09:581:     Calculated expression: 38.0
01:55:09:581:     Updated variable exact: precies
01:55:09:581:     Updated variable result: achtendertig
01:55:09:581:     Prompt variable: nextDialog
01:55:09:581:     Set dialog output: Precies achtendertig.
01:55:09:581:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 8 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
01:55:09:581: Classify language for sequence: What languages do you speak?
01:55:09:581: Classified language: EN
01:55:09:581: Translate sequence: What languages do you speak?
01:55:09:582: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
01:55:09:582: Correction time limit: 600
01:55:09:582: Correcting sequence: What languages do you speak ?
01:55:09:582: Corrected sequence: What languages do you speak?
01:55:09:582: Translate corrected sequence: What languages do you speak?
01:55:09:583: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
01:55:09:583: Checking profanity for sequence: What languages do you speak?
01:55:09:583: Classify master context for input sequence: What languages do you speak?
01:55:09:583: Classified master context: Generic
01:55:09:583: Classify context for input sequence: What languages do you speak?
01:55:09:583: Classified context: Language
01:55:09:583: Handling dialog: EN/Generic/Language
01:55:09:583:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
01:55:09:584:     Updated variable languages: English and Dutch
01:55:09:584:     Find matches for sequence: What languages do you speak?
01:55:09:584:     Found matches for sequence: 6
01:55:09:584:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.5959595959595958)
01:55:09:584:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.3838383838383838)
01:55:09:584:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
01:55:09:584:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
01:55:09:584:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.3194444444444444)
01:55:09:584:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.2884615384615385 / 0.1515151515151515)
01:55:09:584:     Prompt variable: language
01:55:09:584:     Set dialog output: I speak English and Dutch.
01:55:09:584: Handling dialog: EN/Generic/QuestionAndAnswer
01:55:09:584:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:55:09:584:     Find matches for sequence: What languages do you speak?
01:55:09:584:     Found matches for sequence: 17
01:55:09:584:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07915057915057915 / 0.21693121693121695)
01:55:09:584:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06925675675675676 / 0.18981481481481485)
01:55:09:584:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
01:55:09:584:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
01:55:09:584:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
01:55:09:584:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
01:55:09:584:     - What is the answer? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
01:55:09:584:     - What is the question? [OUTPUT] I do not know. (0.06887007489417127 / 0.18875502008032127)
01:55:09:584:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06887007489417127 / 0.18875502008032127)
01:55:09:584:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06887007489417127 / 0.18875502008032127)
01:55:09:584:     - [... 7]
01:55:09:584:     Set dialog output: My goal is to understand and help people.
Handling the request took: 93 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
01:55:09:674: Classify language for sequence: Spreek je duits?
01:55:09:674: Classified language: NL
01:55:09:674: Translate sequence: Spreek je duits?
01:55:09:674: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
01:55:09:675: Correction time limit: 400
01:55:09:675: Correcting sequence: Spreek je [] ?
01:55:09:675: Corrected sequence: Spreek je Duits?
01:55:09:675: Translate corrected sequence: Spreek je Duits?
01:55:09:675: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
01:55:09:675: Checking profanity for sequence: Spreek je [LNG]?
01:55:09:675: Classify master context for input sequence: Spreek je Duits?
01:55:09:675: Classify master context for classification sequence: Spreek je [LNG]?
01:55:09:675: Classified master context: Generic
01:55:09:675: Classify context for input sequence: Spreek je Duits?
01:55:09:675: Classify context for classification sequence: Spreek je [LNG]?
01:55:09:675: Classified context: Language
01:55:09:675: Handling dialog: NL/Generic/Language
01:55:09:675:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:55:09:675:     Updated variable language: NL_LNG:DE = Duits
01:55:09:675:     Updated variable languages: Engels en Nederlands
01:55:09:675:     Updated variable confirmation: Nee
01:55:09:675:     Find matches for sequence: Spreek je [LNG]?
01:55:09:675:     Found matches for sequence: 5
01:55:09:675:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
01:55:09:675:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:55:09:675:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:55:09:675:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:55:09:675:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:55:09:675:     Prompt variable: 
01:55:09:675:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 1 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
01:55:09:676: Classify language for sequence: Spreek je engels?
01:55:09:676: Classified language: NL
01:55:09:676: Translate sequence: Spreek je engels?
01:55:09:676: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
01:55:09:676: Correction time limit: 400
01:55:09:676: Correcting sequence: Spreek je [] ?
01:55:09:676: Corrected sequence: Spreek je Engels?
01:55:09:676: Translate corrected sequence: Spreek je Engels?
01:55:09:676: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
01:55:09:676: Checking profanity for sequence: Spreek je [LNG]?
01:55:09:676: Classify master context for input sequence: Spreek je Engels?
01:55:09:676: Classify master context for classification sequence: Spreek je [LNG]?
01:55:09:676: Classified master context: Generic
01:55:09:676: Classify context for input sequence: Spreek je Engels?
01:55:09:676: Classify context for classification sequence: Spreek je [LNG]?
01:55:09:677: Classified context: Language
01:55:09:677: Handling dialog: NL/Generic/Language
01:55:09:677:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:55:09:677:     Updated variable language: NL_LNG:EN = Engels
01:55:09:677:     Updated variable languages: Engels en Nederlands
01:55:09:677:     Updated variable confirmation: Ja
01:55:09:677:     Find matches for sequence: Spreek je [LNG]?
01:55:09:677:     Found matches for sequence: 5
01:55:09:677:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
01:55:09:677:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:55:09:677:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:55:09:677:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:55:09:677:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:55:09:677:     Prompt variable: 
01:55:09:677:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 99 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (252 assertions).  
Total test duration: 68125 ms (total sleep duration: 30100 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 613 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 424 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 456 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 462 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 472 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117552 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117564 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117562 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 117845 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117653 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117687 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117663 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 546725 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 546726 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 546727 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 546727 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 546734 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 551396 Kb / 538 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 551412 Kb / 538 Mb
