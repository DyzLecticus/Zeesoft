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
Initializing the SequenceClassifier took: 3296 ms

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
Initializing the SequenceMatcher took: 6271 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 21 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 45 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 2 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 36 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 70 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 35 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 13 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 33 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 11 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 12 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 46 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 309 ms

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
Initializing the EntityValueTranslator took: 1600 ms

Sequence: 'Eat three donuts at 9:00 or count to 110'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Eat EN_NUM:3|UN_ABC:three UN_ABC:donuts UN_ABC:at UN_TIM:09:00:00 UN_ABC:or UN_ABC:count UN_ABC:to UN_NUM:110'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 3 ms
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
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Hoe UN_ABC:heet UN_ABC:jij ? UN_ABC:gekke|NL_NAM:firstName:UN_ABC:Gekke UN_ABC:henkie|NL_NAM:lastName:UN_ABC:Henkie'
Retranslating the sequence took: 1 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 2 ms
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
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 3 ms
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
Converting 333303 entity values took: 284 ms

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
Converting 1413 dialog examples took: 163 ms

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
Converting 11 dialogs took: 0 ms

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
Initializing Translator took 2299 ms
Initializing Classifier took 4356 ms
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

Initializing LanguagePreprocessor took 23 ms
Initializing LanguageContextENSupport took 30 ms
Initializing LanguageContextENGeneric took 82 ms
Initializing LanguageContextNLSupport took 91 ms
Initializing LanguageContextNLGeneric took 196 ms
Initializing LanguageContextENRoom took 286 ms
Initializing LanguageMasterContextEN took 376 ms
Initializing LanguageContextNLRoom took 403 ms
Initializing LanguageMasterContextNL took 425 ms
Initializing EntityValueTranslator took 1531 ms
Initializing LanguageClassifier took 32361 ms
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
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.23172344480160353 / 0.9929048022563941
'NL': 0.07951885349034646 / 0.34072793785750444

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.23172344480160353 / 0.9929048022563941
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
'Generic': 0.2722097687588485 / 0.8678766221553507

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.3504156785470154 / 1.0
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
'QuestionAndAnswer': 0.2938423645320198 / 0.6817142857142858
'Handshake': 0.17892212833980364 / 0.4150993377483444

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 2 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.32629496258263124 / 0.535748974491961
'Handshake': 0.276738476078939 / 0.4543813780888926

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.41082438912993846 / 0.9531125827814572
'QuestionAndAnswer': 0.12192118226600987 / 0.28285714285714286

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5929135718761879 / 0.9735143796913217
'QuestionAndAnswer': 0.15330496160627627 / 0.2517138950445566

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.2866995073891626 / 0.8314285714285715
'Handshake': 0.23852477734642613 / 0.6917218543046357
'Thanks': 0.05983984994950224 / 0.17353556485355648

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.32629496258263124 / 0.6696862181149512
'Handshake': 0.3119497436845728 / 0.6402441595681876
'Language': 0.12151050991313574 / 0.24938758845944475
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
18:45:17:420: Classify language for sequence: Hallo.
18:45:17:420: Classified language: NL
18:45:17:420: Translate sequence: Hallo.
18:45:17:421: Translated sequence: UN_ABC:Hallo .
18:45:17:423: Correction time limit: 200
18:45:17:423: Correcting sequence: Hallo .
18:45:17:423: Corrected sequence: Hallo.
18:45:17:423: Translate corrected sequence: Hallo.
18:45:17:424: Translated corrected sequence: UN_ABC:Hallo .
18:45:17:426: Checking profanity for sequence: Hallo.
18:45:17:426: Classify master context for input sequence: Hallo.
18:45:17:426: Classified master context: Generic
18:45:17:426: Classify context for input sequence: Hallo.
18:45:17:426: Classified context: Handshake
Interpreting the request took: 8 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
18:45:17:427: Classify language for sequence: Mijn naam si gekste der henkies.
18:45:17:427: Classified language: NL
18:45:17:427: Translate sequence: Mijn naam si gekste der henkies.
18:45:17:434: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
18:45:17:435: Correction time limit: 700
18:45:17:435: Correcting sequence: Mijn naam si gekste [] henkies .
18:45:18:112: Corrected sequence: Mijn naam is gekste der henkies.
18:45:18:112: Translate corrected sequence: Mijn naam is gekste der henkies.
18:45:18:114: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
18:45:18:114: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:18:115: Classify master context for input sequence: Mijn naam is gekste der henkies.
18:45:18:115: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:18:115: Classified master context: Generic
18:45:18:115: Classify context for input sequence: Mijn naam is gekste der henkies.
18:45:18:115: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:18:115: Classified context: Handshake
Interpreting the request took: 688 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
18:45:18:115: Classify language for sequence: Albert einstein.
18:45:18:116: Classify language for sequence: What is your name? Albert einstein.
18:45:18:116: Classified language: EN
18:45:18:119: Translate sequence: Albert einstein.
18:45:18:122: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
18:45:18:122: Correction time limit: 300
18:45:18:123: Correcting sequence: [] [] .
18:45:18:123: Corrected sequence: Albert einstein.
18:45:18:123: Translate corrected sequence: Albert einstein.
18:45:18:127: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
18:45:18:127: Checking profanity for sequence: [ABC] [ABC].
18:45:18:127: Classify master context for input sequence: Albert einstein.
18:45:18:127: Classify master context for classification sequence: What is your name? [ABC] [ABC].
18:45:18:128: Classified master context: Generic
18:45:18:128: Classify context for input sequence: Albert einstein.
18:45:18:128: Classify context for classification sequence: What is your name? [ABC] [ABC].
18:45:18:128: Classified context: Handshake
Interpreting the request took: 13 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
18:45:18:129: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
18:45:18:129: Selected primary language: EN
18:45:18:129: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
18:45:18:135: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
18:45:18:135: Correction time limit: 1100
18:45:18:135: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
18:45:19:235: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
18:45:19:235: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
18:45:19:237: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
18:45:19:238: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
18:45:19:238: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1110 ms
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

Initializing DialogSet took 375 ms
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
18:45:21:705: Classify language for sequence: What are you?
18:45:21:705: Classified language: EN
18:45:21:705: Translate sequence: What are you?
18:45:21:707: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
18:45:21:707: Correction time limit: 400
18:45:21:707: Correcting sequence: What are you ?
18:45:21:707: Corrected sequence: What are you?
18:45:21:707: Translate corrected sequence: What are you?
18:45:21:710: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
18:45:21:710: Checking profanity for sequence: What are you?
18:45:21:710: Classify master context for input sequence: What are you?
18:45:21:710: Classified master context: Generic
18:45:21:710: Classify context for input sequence: What are you?
18:45:21:710: Classified context: QuestionAndAnswer
18:45:21:710: Handling dialog: EN/Generic/QuestionAndAnswer
18:45:21:711:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:21:711:     Find matches for sequence: What are you?
18:45:21:712:     Found matches for sequence: 10
18:45:21:712:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.1785714285714286 / 0.8333333333333334)
18:45:21:712:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.060416666666666674 / 0.28194444444444444)
18:45:21:714:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.060416666666666674 / 0.28194444444444444)
18:45:21:714:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.060416666666666674 / 0.28194444444444444)
18:45:21:714:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.059523809523809534 / 0.2777777777777778)
18:45:21:714:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.059523809523809534 / 0.2777777777777778)
18:45:21:714:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:21:714:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:21:714:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:21:714:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:21:714:     Set dialog output: I am an artificially intelligent personal assistant.
18:45:21:714: Handling dialog: EN/Generic/Handshake
18:45:21:714:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:21:715:     Find matches for sequence: What are you?
18:45:21:715:     Found matches for sequence: 2
18:45:21:715:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
18:45:21:715:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
18:45:21:715:     Prompt variable: firstName
18:45:21:715:     Set dialog output: My name is Dyz Lecticus.
18:45:21:715:     Set dialog prompt: What is your name?
Handling the request took: 88 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
18:45:21:793: Classify language for sequence: Wat ben jij?
18:45:21:793: Classified language: NL
18:45:21:793: Translate sequence: Wat ben jij?
18:45:21:795: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
18:45:21:795: Correction time limit: 400
18:45:21:795: Correcting sequence: Wat ben jij ?
18:45:21:795: Corrected sequence: Wat ben jij?
18:45:21:795: Translate corrected sequence: Wat ben jij?
18:45:21:796: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
18:45:21:796: Checking profanity for sequence: Wat ben jij?
18:45:21:796: Classify master context for input sequence: Wat ben jij?
18:45:21:796: Classified master context: Generic
18:45:21:796: Classify context for input sequence: Wat ben jij?
18:45:21:796: Classified context: QuestionAndAnswer
18:45:21:796: Handling dialog: NL/Generic/QuestionAndAnswer
18:45:21:796:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:21:796:     Find matches for sequence: Wat ben jij?
18:45:21:797:     Found matches for sequence: 9
18:45:21:798:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.5660550458715596 / 0.9793650793650792)
18:45:21:798:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18807339449541285 / 0.32539682539682535)
18:45:21:798:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18807339449541285 / 0.32539682539682535)
18:45:21:798:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.18623853211009175 / 0.3222222222222222)
18:45:21:798:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.18623853211009175 / 0.3222222222222222)
18:45:21:798:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18623853211009175 / 0.3222222222222222)
18:45:21:798:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.16456422018348624 / 0.28472222222222215)
18:45:21:798:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.16456422018348624 / 0.28472222222222215)
18:45:21:798:     - Ben jij een menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.16456422018348624 / 0.28472222222222215)
18:45:21:798:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
18:45:21:798: Handling dialog: NL/Generic/Handshake
18:45:21:798:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:21:798:     Find matches for sequence: Wat ben jij?
18:45:21:799:     Found matches for sequence: 5
18:45:21:799:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
18:45:21:799:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
18:45:21:799:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
18:45:21:799:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
18:45:21:799:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
18:45:21:799:     Prompt variable: firstName
18:45:21:799:     Set dialog output: Mijn naam is Dyz Lecticus.
18:45:21:799:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 6 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
18:45:21:799: Classify language for sequence: What is the answer?
18:45:21:799: Classified language: EN
18:45:21:800: Translate sequence: What is the answer?
18:45:21:800: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
18:45:21:801: Correction time limit: 500
18:45:21:801: Correcting sequence: What is [] answer ?
18:45:21:801: Corrected sequence: What is the answer?
18:45:21:801: Translate corrected sequence: What is the answer?
18:45:21:802: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
18:45:21:803: Checking profanity for sequence: What is [PRE] answer?
18:45:21:803: Classify master context for input sequence: What is the answer?
18:45:21:803: Classify master context for classification sequence: What is [PRE] answer?
18:45:21:803: Selected input sequence master context classification.
18:45:21:803: Classified master context: Generic
18:45:21:803: Classify context for input sequence: What is the answer?
18:45:21:803: Classified context: QuestionAndAnswer
18:45:21:803: Handling dialog: EN/Generic/QuestionAndAnswer
18:45:21:803:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:21:803:     Find matches for sequence: What is the answer?
18:45:21:805:     Found matches for sequence: 7
18:45:21:805:     - What is the answer? [OUTPUT] Fourtytwo. (0.21904761904761907 / 0.8177777777777777)
18:45:21:805:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.15000000000000002 / 0.5599999999999999)
18:45:21:805:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.15000000000000002 / 0.5599999999999999)
18:45:21:805:     - What is the question? [OUTPUT] I do not know. (0.09047619047619049 / 0.3377777777777778)
18:45:21:805:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.09047619047619049 / 0.3377777777777778)
18:45:21:805:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.040476190476190485 / 0.1511111111111111)
18:45:21:805:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.040476190476190485 / 0.1511111111111111)
18:45:21:805:     Set dialog output: Fourtytwo.
Handling the request took: 94 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
18:45:21:893: Classify language for sequence: Wat is het antwoord?
18:45:21:893: Classified language: NL
18:45:21:893: Translate sequence: Wat is het antwoord?
18:45:21:894: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
18:45:21:894: Correction time limit: 500
18:45:21:894: Correcting sequence: Wat is het antwoord ?
18:45:21:894: Corrected sequence: Wat is het antwoord?
18:45:21:894: Translate corrected sequence: Wat is het antwoord?
18:45:21:895: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
18:45:21:895: Checking profanity for sequence: Wat is het antwoord?
18:45:21:895: Classify master context for input sequence: Wat is het antwoord?
18:45:21:895: Classified master context: Generic
18:45:21:895: Classify context for input sequence: Wat is het antwoord?
18:45:21:896: Classified context: QuestionAndAnswer
18:45:21:896: Handling dialog: NL/Generic/QuestionAndAnswer
18:45:21:896:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:21:896:     Find matches for sequence: Wat is het antwoord?
18:45:21:897:     Found matches for sequence: 7
18:45:21:897:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.7467889908256881 / 1.0)
18:45:21:897:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.555045871559633 / 0.7432432432432433)
18:45:21:897:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.555045871559633 / 0.7432432432432433)
18:45:21:897:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.3669724770642202 / 0.4914004914004914)
18:45:21:897:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.18073394495412845 / 0.24201474201474205)
18:45:21:897:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18073394495412845 / 0.24201474201474205)
18:45:21:897:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18073394495412845 / 0.24201474201474205)
18:45:21:897:     Set dialog output: Tweeenveertig.
Handling the request took: 5 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
18:45:21:898: Classify language for sequence: Hallo.
18:45:21:898: Classified language: NL
18:45:21:898: Translate sequence: Hallo.
18:45:21:898: Translated sequence: UN_ABC:Hallo .
18:45:21:898: Correction time limit: 200
18:45:21:899: Correcting sequence: Hallo .
18:45:21:899: Corrected sequence: Hallo.
18:45:21:899: Translate corrected sequence: Hallo.
18:45:21:899: Translated corrected sequence: UN_ABC:Hallo .
18:45:21:899: Checking profanity for sequence: Hallo.
18:45:21:899: Classify master context for input sequence: Hallo.
18:45:21:899: Classified master context: Generic
18:45:21:899: Classify context for input sequence: Hallo.
18:45:21:899: Classified context: Handshake
18:45:21:899: Handling dialog: NL/Generic/Handshake
18:45:21:900:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:21:900:     Find matches for sequence: Hallo.
18:45:21:900:     Found matches for sequence: 2
18:45:21:900:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
18:45:21:900:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
18:45:21:900:     Prompt variable: firstName
18:45:21:900:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
18:45:21:900:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
18:45:21:900: Classify language for sequence: Wie ben jij?
18:45:21:900: Classified language: NL
18:45:21:900: Translate sequence: Wie ben jij?
18:45:21:902: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
18:45:21:902: Correction time limit: 400
18:45:21:902: Correcting sequence: Wie ben jij ?
18:45:21:902: Corrected sequence: Wie ben jij?
18:45:21:902: Translate corrected sequence: Wie ben jij?
18:45:21:904: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
18:45:21:904: Checking profanity for sequence: Wie ben jij?
18:45:21:904: Classify master context for input sequence: Wie ben jij?
18:45:21:904: Classified master context: Generic
18:45:21:904: Classify context for input sequence: Wie ben jij?
18:45:21:904: Classified context: Handshake
18:45:21:904: Handling dialog: NL/Generic/Handshake
18:45:21:905:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:21:905:     Find matches for sequence: Wie ben jij?
18:45:21:905:     Found matches for sequence: 2
18:45:21:905:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
18:45:21:905:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
18:45:21:905:     Prompt variable: firstName
18:45:21:905:     Set dialog output: Mijn naam is Dyz Lecticus.
18:45:21:905:     Set dialog prompt: Wat is jouw naam?
18:45:21:905: Handling dialog: NL/Generic/QuestionAndAnswer
18:45:21:905:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:21:905:     Find matches for sequence: Wie ben jij?
18:45:21:907:     Found matches for sequence: 15
18:45:21:907:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.3743119266055046 / 0.6476190476190475)
18:45:21:907:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18807339449541285 / 0.32539682539682535)
18:45:21:907:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18807339449541285 / 0.32539682539682535)
18:45:21:907:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.18623853211009175 / 0.3222222222222222)
18:45:21:907:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.18623853211009175 / 0.3222222222222222)
18:45:21:907:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18623853211009175 / 0.3222222222222222)
18:45:21:907:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.18390325271059219 / 0.3181818181818182)
18:45:21:907:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.18390325271059219 / 0.3181818181818182)
18:45:21:907:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.18390325271059219 / 0.3181818181818182)
18:45:21:907:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.18390325271059219 / 0.3181818181818182)
18:45:21:907:     - [... 5]
18:45:21:907:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
Handling the request took: 93 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
18:45:21:994: Classify language for sequence: Albert einstein.
18:45:21:994: Classify language for sequence: What is your name? Albert einstein.
18:45:21:994: Classified language: EN
18:45:21:996: Translate sequence: Albert einstein.
18:45:21:997: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
18:45:21:998: Correction time limit: 300
18:45:21:998: Correcting sequence: [] [] .
18:45:21:998: Corrected sequence: Albert einstein.
18:45:21:998: Translate corrected sequence: Albert einstein.
18:45:22:000: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
18:45:22:000: Checking profanity for sequence: [ABC] [ABC].
18:45:22:000: Classify master context for input sequence: Albert einstein.
18:45:22:000: Classify master context for classification sequence: What is your name? [ABC] [ABC].
18:45:22:000: Classified master context: Generic
18:45:22:000: Classify context for input sequence: Albert einstein.
18:45:22:000: Classify context for classification sequence: What is your name? [ABC] [ABC].
18:45:22:001: Classified context: Handshake
18:45:22:001: Handling dialog: EN/Generic/Handshake
18:45:22:001:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:22:001:     Updated variable firstName: UN_ABC:Albert = Albert
18:45:22:001:     Updated variable lastName: UN_ABC:Einstein = Einstein
18:45:22:001:     Find matches for sequence: [ABC] [ABC].
18:45:22:001:     Found matches for sequence: 0
18:45:22:001:     Updated variable fullName: Albert Einstein
18:45:22:001:     Prompt variable: nextDialog
18:45:22:001:     Set dialog output: 
18:45:22:001:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 8 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
18:45:22:002: Classify language for sequence: Mijn naam si gekste der henkies.
18:45:22:002: Classified language: NL
18:45:22:002: Translate sequence: Mijn naam si gekste der henkies.
18:45:22:004: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
18:45:22:005: Correction time limit: 700
18:45:22:005: Correcting sequence: Mijn naam si gekste [] henkies .
18:45:22:436: Corrected sequence: Mijn naam is gekste der henkies.
18:45:22:436: Translate corrected sequence: Mijn naam is gekste der henkies.
18:45:22:437: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
18:45:22:438: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:22:438: Classify master context for input sequence: Mijn naam is gekste der henkies.
18:45:22:438: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:22:438: Classified master context: Generic
18:45:22:438: Classify context for input sequence: Mijn naam is gekste der henkies.
18:45:22:439: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:22:439: Classified context: Handshake
18:45:22:439: Handling dialog: NL/Generic/Handshake
18:45:22:439:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:22:439:     Updated variable firstName: UN_ABC:Gekste = Gekste
18:45:22:439:     Updated variable lastName: UN_ABC:Henkies = Henkies
18:45:22:439:     Updated variable preposition: NL_PRE:6 = der
18:45:22:439:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:22:440:     Found matches for sequence: 18
18:45:22:440:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:440:     - [... 8]
18:45:22:440:     Updated variable fullName: Gekste der Henkies
18:45:22:440:     Prompt variable: nextDialog
18:45:22:440:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
18:45:22:440:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 439 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
18:45:22:441: Classify language for sequence: Mijn naam is jan de lange.
18:45:22:441: Classified language: NL
18:45:22:442: Translate sequence: Mijn naam is jan de lange.
18:45:22:444: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
18:45:22:445: Correction time limit: 700
18:45:22:445: Correcting sequence: Mijn naam is [] [] [] .
18:45:22:445: Corrected sequence: Mijn naam is jan de lange.
18:45:22:445: Translate corrected sequence: Mijn naam is jan de lange.
18:45:22:447: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
18:45:22:447: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:22:447: Classify master context for input sequence: Mijn naam is jan de lange.
18:45:22:447: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
18:45:22:447: Classified master context: Generic
18:45:22:447: Classify context for input sequence: Mijn naam is jan de lange.
18:45:22:447: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
18:45:22:448: Classified context: Handshake
18:45:22:448: Handling dialog: NL/Generic/Handshake
18:45:22:448:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:22:448:     Updated variable firstName: UN_ABC:Jan = Jan
18:45:22:448:     Updated variable lastName: UN_ABC:Lange = Lange
18:45:22:448:     Updated variable preposition: NL_PRE:5 = de
18:45:22:448:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
18:45:22:448:     Found matches for sequence: 18
18:45:22:448:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:448:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:448:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:448:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:448:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:448:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:450:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:450:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:450:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:450:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
18:45:22:450:     - [... 8]
18:45:22:450:     Updated variable fullName: Jan de Lange
18:45:22:450:     Prompt variable: nextDialog
18:45:22:450:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
18:45:22:450:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 54 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
18:45:22:494: Classify language for sequence: Who created you?
18:45:22:494: Classified language: EN
18:45:22:494: Translate sequence: Who created you?
18:45:22:495: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
18:45:22:495: Correction time limit: 400
18:45:22:495: Correcting sequence: Who created you ?
18:45:22:495: Corrected sequence: Who created you?
18:45:22:495: Translate corrected sequence: Who created you?
18:45:22:496: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
18:45:22:497: Checking profanity for sequence: Who created you?
18:45:22:497: Classify master context for input sequence: Who created you?
18:45:22:497: Classified master context: Generic
18:45:22:497: Classify context for input sequence: Who created you?
18:45:22:497: Classified context: QuestionAndAnswer
18:45:22:498: Handling dialog: EN/Generic/QuestionAndAnswer
18:45:22:498:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:22:498:     Find matches for sequence: Who created you?
18:45:22:498:     Found matches for sequence: 5
18:45:22:498:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.18809523809523812 / 0.8777777777777779)
18:45:22:498:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.05000000000000001 / 0.23333333333333336)
18:45:22:498:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:22:498:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:22:498:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.05000000000000001 / 0.23333333333333336)
18:45:22:498:     Set dialog output: My software was written by André van der Zee.
18:45:22:498: Handling dialog: EN/Generic/Handshake
18:45:22:498:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
18:45:22:499:     Find matches for sequence: Who created you?
18:45:22:499:     Found matches for sequence: 1
18:45:22:499:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
18:45:22:499:     Prompt variable: firstName
18:45:22:499:     Set dialog output: My name is Dyz Lecticus.
18:45:22:499:     Set dialog prompt: What is your name?
Handling the request took: 5 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
18:45:22:499: Classify language for sequence: You asshole!
18:45:22:499: Classified language: EN
18:45:22:499: Translate sequence: You asshole!
18:45:22:500: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
18:45:22:500: Correction time limit: 300
18:45:22:500: Correcting sequence: You [] !
18:45:22:500: Corrected sequence: You asshole!
18:45:22:500: Translate corrected sequence: You asshole!
18:45:22:500: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
18:45:22:501: Checking profanity for sequence: You [PRF]!
18:45:22:501: Handling dialog: EN/Generic/Profanity
18:45:22:501:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:22:501:     Find matches for sequence: You [PRF]!
18:45:22:501:     Found matches for sequence: 2
18:45:22:501:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
18:45:22:501:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
18:45:22:501:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 2 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
18:45:22:501: Classify language for sequence: Jij klootzak!
18:45:22:501: Classified language: NL
18:45:22:501: Translate sequence: Jij klootzak!
18:45:22:502: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
18:45:22:502: Correction time limit: 300
18:45:22:502: Correcting sequence: Jij [] !
18:45:22:502: Corrected sequence: Jij klootzak!
18:45:22:502: Translate corrected sequence: Jij klootzak!
18:45:22:502: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
18:45:22:502: Checking profanity for sequence: Jij [PRF]!
18:45:22:502: Handling dialog: NL/Generic/Profanity
18:45:22:502:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:22:502:     Find matches for sequence: Jij [PRF]!
18:45:22:503:     Found matches for sequence: 2
18:45:22:503:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
18:45:22:503:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
18:45:22:503:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 93 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
18:45:22:594: Classify language for sequence: Can I book a room for 5 people?
18:45:22:594: Classified language: EN
18:45:22:594: Translate sequence: Can I book a room for 5 people?
18:45:22:595: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
18:45:22:595: Correction time limit: 900
18:45:22:595: Correcting sequence: Can I book a room for [] people ?
18:45:22:595: Corrected sequence: Can I book a room for 5 people?
18:45:22:595: Translate corrected sequence: Can I book a room for 5 people?
18:45:22:596: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
18:45:22:596: Checking profanity for sequence: Can I book a room for [NUM] people?
18:45:22:596: Classify master context for input sequence: Can I book a room for 5 people?
18:45:22:596: Classify master context for classification sequence: Can I book a room for [NUM] people?
18:45:22:596: Classified master context: Room
18:45:22:596: Classify context for input sequence: Can I book a room for 5 people?
18:45:22:596: Classify context for classification sequence: Can I book a room for [NUM] people?
18:45:22:596: Classified context: Booking
18:45:22:596: Handling dialog: EN/Room/Booking
18:45:22:597:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
18:45:22:597:     Updated variable bookPeople: UN_NUM:5 = 5
18:45:22:597:     Find matches for sequence: Can I book a room for [NUM] people?
18:45:22:602:     Found matches for sequence: 329
18:45:22:602:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
18:45:22:602:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
18:45:22:602:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
18:45:22:602:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
18:45:22:602:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
18:45:22:603:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
18:45:22:603:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
18:45:22:603:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
18:45:22:603:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
18:45:22:603:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
18:45:22:603:     - [... 319]
18:45:22:603:     Prompt variable: bookDate
18:45:22:603:     Set dialog output: Okay.
18:45:22:603:     Set dialog prompt: On what date?
Handling the request took: 9 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
18:45:22:603: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
18:45:22:604: Classified language: EN
18:45:22:604: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
18:45:22:605: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
18:45:22:605: Correction time limit: 1332
18:45:22:605: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
18:45:22:605: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
18:45:22:605: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
18:45:22:607: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
18:45:22:607: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
18:45:22:607: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
18:45:22:607: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
18:45:22:607: Classified master context: Room
18:45:22:608: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
18:45:22:608: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
18:45:22:608: Classified context: Booking
18:45:22:608: Handling dialog: EN/Room/Booking
18:45:22:608:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
18:45:22:608:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
18:45:22:608:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
18:45:22:608:     Updated variable bookDuration: EN_DUR:02:00 = two hours
18:45:22:608:     Updated variable bookPeople: UN_NUM:12 = 12
18:45:22:608:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
18:45:22:618:     Found matches for sequence: 263
18:45:22:618:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
18:45:22:618:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
18:45:22:618:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
18:45:22:619:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
18:45:22:619:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
18:45:22:619:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
18:45:22:619:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
18:45:22:619:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
18:45:22:619:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
18:45:22:619:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
18:45:22:619:     - [... 253]
18:45:22:619:     Prompt variable: bookConfirmation
18:45:22:619:     Set dialog output: Okay.
18:45:22:619:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 91 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
18:45:22:694: Classify language for sequence: What does it cost to book a room?
18:45:22:694: Classified language: EN
18:45:22:695: Translate sequence: What does it cost to book a room?
18:45:22:695: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
18:45:22:695: Correction time limit: 900
18:45:22:696: Correcting sequence: What does it cost to book a room ?
18:45:22:696: Corrected sequence: What does it cost to book a room?
18:45:22:696: Translate corrected sequence: What does it cost to book a room?
18:45:22:697: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
18:45:22:697: Checking profanity for sequence: What does it cost to book a room?
18:45:22:697: Classify master context for input sequence: What does it cost to book a room?
18:45:22:697: Classified master context: Room
18:45:22:698: Classify context for input sequence: What does it cost to book a room?
18:45:22:698: Classified context: QuestionAndAnswer
18:45:22:698: Handling dialog: EN/Room/QuestionAndAnswer
18:45:22:698:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
18:45:22:698:     Find matches for sequence: What does it cost to book a room?
18:45:22:698:     Found matches for sequence: 4
18:45:22:698:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
18:45:22:698:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
18:45:22:698:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
18:45:22:698:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
18:45:22:698:     Set dialog output: There are no costs attached to booking a room.
18:45:22:698: Handling dialog: EN/Room/Booking
18:45:22:698:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
18:45:22:699:     Find matches for sequence: What does it cost to book a room?
18:45:22:701:     Found matches for sequence: 276
18:45:22:701:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
18:45:22:701:     - [... 266]
18:45:22:701:     Prompt variable: bookDate
18:45:22:701:     Set dialog output: Okay.
18:45:22:701:     Set dialog prompt: On what date?
Handling the request took: 7 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
18:45:22:702: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
18:45:22:702: Classified language: EN
18:45:22:702: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
18:45:22:705: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
18:45:22:706: Correction time limit: 1300
18:45:22:706: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
18:45:22:707: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
18:45:22:707: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
18:45:22:709: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
18:45:22:709: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:710: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
18:45:22:710: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:710: Classified master context: Generic
18:45:22:710: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
18:45:22:710: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:710: Classified context: Math
18:45:22:710: Handling dialog: EN/Generic/Math
18:45:22:711:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
18:45:22:711:     Updated variable number1: EN_NUM:10 = ten
18:45:22:711:     Updated variable number2: EN_NUM:40 = fourty
18:45:22:711:     Updated variable number3: EN_NUM:20 = twenty
18:45:22:711:     Updated variable number4: EN_NUM:3 = three
18:45:22:711:     Updated variable number5: EN_NUM:6 = six
18:45:22:711:     Updated variable operator1: EN_MTH:M = multiplied by
18:45:22:711:     Updated variable operator2: EN_MTH:D = divided by
18:45:22:711:     Updated variable operator3: EN_MTH:A = plus
18:45:22:711:     Updated variable operator4: EN_MTH:M = multiplied by
18:45:22:711:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:711:     Found matches for sequence: 0
18:45:22:711:     Calculate expression: 10 M 40 D 20 A 3 M 6
18:45:22:711:     Calculated expression: 38.0
18:45:22:712:     Updated variable exact: exactly
18:45:22:712:     Updated variable result: thirtyeight
18:45:22:712:     Prompt variable: nextDialog
18:45:22:712:     Set dialog output: Exactly thirtyeight.
18:45:22:712:     Set dialog prompt: What else can I do for you?
Handling the request took: 93 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
18:45:22:794: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
18:45:22:794: Classified language: NL
18:45:22:794: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
18:45:22:795: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
18:45:22:796: Correction time limit: 1200
18:45:22:796: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
18:45:22:796: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
18:45:22:796: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
18:45:22:797: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
18:45:22:797: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:798: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
18:45:22:798: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:798: Classified master context: Generic
18:45:22:798: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
18:45:22:798: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:798: Classified context: Math
18:45:22:798: Handling dialog: NL/Generic/Math
18:45:22:798:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
18:45:22:798:     Updated variable number1: NL_NUM:10 = tien
18:45:22:798:     Updated variable number2: NL_NUM:40 = veertig
18:45:22:798:     Updated variable number3: NL_NUM:20 = twintig
18:45:22:798:     Updated variable number4: NL_NUM:3 = drie
18:45:22:798:     Updated variable number5: NL_NUM:6 = zes
18:45:22:798:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
18:45:22:798:     Updated variable operator2: NL_MTH:D = gedeeld door
18:45:22:798:     Updated variable operator3: NL_MTH:A = plus
18:45:22:798:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
18:45:22:798:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
18:45:22:798:     Found matches for sequence: 0
18:45:22:799:     Calculate expression: 10 M 40 D 20 A 3 M 6
18:45:22:799:     Calculated expression: 38.0
18:45:22:799:     Updated variable exact: precies
18:45:22:799:     Updated variable result: achtendertig
18:45:22:799:     Prompt variable: nextDialog
18:45:22:799:     Set dialog output: Precies achtendertig.
18:45:22:799:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 5 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
18:45:22:799: Classify language for sequence: What languages do you speak?
18:45:22:799: Classified language: EN
18:45:22:799: Translate sequence: What languages do you speak?
18:45:22:800: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
18:45:22:800: Correction time limit: 600
18:45:22:800: Correcting sequence: What languages do you speak ?
18:45:22:800: Corrected sequence: What languages do you speak?
18:45:22:800: Translate corrected sequence: What languages do you speak?
18:45:22:800: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
18:45:22:800: Checking profanity for sequence: What languages do you speak?
18:45:22:800: Classify master context for input sequence: What languages do you speak?
18:45:22:800: Classified master context: Generic
18:45:22:800: Classify context for input sequence: What languages do you speak?
18:45:22:801: Classified context: Language
18:45:22:801: Handling dialog: EN/Generic/Language
18:45:22:801:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
18:45:22:801:     Updated variable languages: English and Dutch
18:45:22:801:     Find matches for sequence: What languages do you speak?
18:45:22:801:     Found matches for sequence: 6
18:45:22:801:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.5959595959595958)
18:45:22:801:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.3838383838383838)
18:45:22:801:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
18:45:22:801:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
18:45:22:801:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.3194444444444444)
18:45:22:801:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.2884615384615385 / 0.1515151515151515)
18:45:22:801:     Prompt variable: language
18:45:22:801:     Set dialog output: I speak English and Dutch.
Handling the request took: 95 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
18:45:22:894: Classify language for sequence: Spreek je duits?
18:45:22:894: Classified language: NL
18:45:22:894: Translate sequence: Spreek je duits?
18:45:22:894: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
18:45:22:895: Correction time limit: 400
18:45:22:895: Correcting sequence: Spreek je [] ?
18:45:22:895: Corrected sequence: Spreek je Duits?
18:45:22:895: Translate corrected sequence: Spreek je Duits?
18:45:22:896: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
18:45:22:896: Checking profanity for sequence: Spreek je [LNG]?
18:45:22:896: Classify master context for input sequence: Spreek je Duits?
18:45:22:896: Classify master context for classification sequence: Spreek je [LNG]?
18:45:22:896: Classified master context: Generic
18:45:22:896: Classify context for input sequence: Spreek je Duits?
18:45:22:896: Classify context for classification sequence: Spreek je [LNG]?
18:45:22:896: Classified context: Language
18:45:22:896: Handling dialog: NL/Generic/Language
18:45:22:896:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
18:45:22:896:     Updated variable language: NL_LNG:DE = Duits
18:45:22:896:     Updated variable languages: Engels en Nederlands
18:45:22:896:     Updated variable confirmation: Nee
18:45:22:896:     Find matches for sequence: Spreek je [LNG]?
18:45:22:897:     Found matches for sequence: 5
18:45:22:897:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
18:45:22:897:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
18:45:22:897:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
18:45:22:897:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
18:45:22:897:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
18:45:22:897:     Prompt variable: 
18:45:22:897:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 3 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
18:45:22:897: Classify language for sequence: Spreek je engels?
18:45:22:897: Classified language: NL
18:45:22:897: Translate sequence: Spreek je engels?
18:45:22:898: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
18:45:22:898: Correction time limit: 400
18:45:22:898: Correcting sequence: Spreek je [] ?
18:45:22:898: Corrected sequence: Spreek je Engels?
18:45:22:898: Translate corrected sequence: Spreek je Engels?
18:45:22:899: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
18:45:22:899: Checking profanity for sequence: Spreek je [LNG]?
18:45:22:899: Classify master context for input sequence: Spreek je Engels?
18:45:22:899: Classify master context for classification sequence: Spreek je [LNG]?
18:45:22:899: Classified master context: Generic
18:45:22:899: Classify context for input sequence: Spreek je Engels?
18:45:22:899: Classify context for classification sequence: Spreek je [LNG]?
18:45:22:899: Classified context: Language
18:45:22:899: Handling dialog: NL/Generic/Language
18:45:22:899:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
18:45:22:899:     Updated variable language: NL_LNG:EN = Engels
18:45:22:900:     Updated variable languages: Engels en Nederlands
18:45:22:900:     Updated variable confirmation: Ja
18:45:22:900:     Find matches for sequence: Spreek je [LNG]?
18:45:22:900:     Found matches for sequence: 5
18:45:22:900:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
18:45:22:900:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
18:45:22:900:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
18:45:22:900:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
18:45:22:900:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
18:45:22:900:     Prompt variable: 
18:45:22:900:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 97 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (252 assertions).  
Total test duration: 66276 ms (total sleep duration: 30100 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 613 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 424 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 457 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 463 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 472 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117542 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117555 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117553 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 117786 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117623 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117657 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117634 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 546408 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 546409 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 546410 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 546410 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 546417 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 550616 Kb / 537 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 550631 Kb / 537 Mb
