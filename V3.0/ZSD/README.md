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
Average correction time: 216 ms
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
Initializing the SequenceClassifier took: 2930 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 2 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 1 ms

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
Initializing the SequenceMatcher took: 5882 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 18 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 149 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 3 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 92 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 45 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 33 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 35 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 65 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 34 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 36 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 116 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 326 ms

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
Initializing the EntityValueTranslator took: 1551 ms

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
Retranslating the sequence took: 0 ms
Retranslation: 'februari march october december'

Sequence: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'
Translating the sequence took: 2 ms
Translation: 'EN_DUR:33:41 UN_MTH:D NL_DUR:33:41'
Retranslating the sequence took: 0 ms
Retranslation: 'thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten'

Sequence: 'yesterday OR today OR the 1st of october'
Translating the sequence took: 2 ms
Translation: 'EN_DAT:2018-07-15|UN_ABC:yesterday UN_ABC:OR EN_DAT:2018-07-16|UN_ABC:today UN_ABC:OR EN_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'july fifteenth twothousandeighteen OR july sixteenth twothousandeighteen OR october first twothousandeighteen'

Sequence: 'gisteren OF vandaag OF 1 oktober'
Translating the sequence took: 2 ms
Translation: 'NL_DAT:2018-07-15|UN_ABC:gisteren UN_ABC:OF NL_DAT:2018-07-16|UN_ABC:vandaag UN_ABC:OF NL_DAT:2018-10-01'
Retranslating the sequence took: 0 ms
Retranslation: 'vijftien juli tweeduizendachttien OF zestien juli tweeduizendachttien OF een oktober tweeduizendachttien'

Sequence: 'twelve o'clock OR five minutes to nine OR ten past one in the morning'
Translating the sequence took: 5 ms
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
Retranslating the sequence took: 1 ms
Retranslation: 'Mijn naam is Andre van der Zee'

Sequence: 'Hoe heet jij? gekke henkie'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Hoe UN_ABC:heet UN_ABC:jij ? UN_ABC:gekke|NL_NAM:firstName:UN_ABC:Gekke UN_ABC:henkie|NL_NAM:lastName:UN_ABC:Henkie'
Retranslating the sequence took: 0 ms
Retranslation: 'Hoe heet jij? gekke henkie'

Sequence: 'gekste der henkies is mijn naam'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies UN_ABC:is UN_ABC:mijn UN_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 7 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .'
Retranslating the sequence took: 0 ms
Retranslation: 'Mijn naam si gekste der henkies.'

Sequence: 'Mijn naam is {firstName} {preposition} {lastName}.'
Translating the sequence took: 5 ms
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
Translating the sequence took: 3 ms
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
Converting 333291 entity values took: 391 ms

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
Converting 1486 dialog examples took: 144 ms

Sample JSON;
{
  "sequences": [
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
    },
    {
      "input": "Thanks..",
      "output": "You're welcome. Is there anything else I can do for you?",
      "context": "EN"
    },
    {
      "input": "Nice, thanks.",
      "output": "You're welcome. Is there anything else I can do for you?",
      "context": "EN"
    },
    {
      "input": "Excellent, thanks.",
      "output": "You're welcome. Is there anything else I can do for you?",
      "context": "EN"
    },
    {
      "input": "Perfect, thanks.",
      "output": "You're welcome. Is there anything else I can do for you?",
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
Converting 10 dialogs took: 0 ms

Sample JSON;
{
  "dialogs": [
    {
      "language": "EN",
      "masterContext": "Generic",
      "context": "Cancel",
      "handler": "",
      "examples": [
        {
          "input": "Nothing.",
          "output": "Okay."
        },
        {
          "input": "Nevermind.",
          "output": "Okay."
        },
        {
          "input": "Nothing.",
          "output": "Allright."
        },
        {
          "input": "Nevermind.",
          "output": "Allright."
        },
        {
          "input": "Nothing.",
          "output": "Fine."
        },
        {
          "input": "Nevermind.",
          "output": "Fine."
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
Initializing Translator took 1854 ms
Initializing Classifier took 3772 ms
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

Initializing LanguagePreprocessor took 15 ms
Initializing LanguageContextNLSupport took 23 ms
Initializing LanguageContextENSupport took 30 ms
Initializing LanguageContextENGeneric took 63 ms
Initializing LanguageContextNLGeneric took 151 ms
Initializing LanguageMasterContextEN took 338 ms
Initializing LanguageContextNLRoom took 367 ms
Initializing LanguageContextENRoom took 372 ms
Initializing LanguageMasterContextNL took 394 ms
Initializing EntityValueTranslator took 2247 ms
Initializing LanguageClassifier took 29691 ms
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
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'what is name?' -> EN
Classifying the input sequence took: 0 ms

Classified sequence (case insensitive): 'twothousand' -> EN
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.23044441140538982 / 0.9930656880528737
'NL': 0.07894082701802448 / 0.34018367475273836

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.23044441140538982 / 0.9930656880528737
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
'Generic': 0.26862527707074685 / 0.8561187071088062
'Support': 0.1034415664031473 / 0.32967210329595653
'Room': 0.0856902119944335 / 0.27309787933781493

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.34124956748575225 / 1.0
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
'QuestionAndAnswer': 0.26290240452616687 / 0.6300745762711863
'Handshake': 0.1546486132663197 / 0.3706324392518238

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.3197854699830984 / 0.5798154068890631
'Handshake': 0.2403872589450532 / 0.43585543884635924

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.3610771618077203 / 0.8653611979595195
'QuestionAndAnswer': 0.11545120226308345 / 0.27669152542372877

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5263029731746592 / 0.9542602812889441
'QuestionAndAnswer': 0.15576050358659055 / 0.28241539482415395

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.2309024045261669 / 0.6917288135593219
'Handshake': 0.21290104045078573 / 0.6378009982995996
'Math': 0.13696369636963696 / 0.41031073446327676

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.3032565443632637 / 0.6873077155094408
'Handshake': 0.30054986057106947 / 0.6811732241410006
'Language': 0.11575903967208315 / 0.2623589913735899
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
11:22:53:393: Classify language for sequence: Hallo.
11:22:53:393: Classified language: NL
11:22:53:393: Translate sequence: Hallo.
11:22:53:394: Translated sequence: UN_ABC:Hallo .
11:22:53:394: Correction time limit: 200
11:22:53:394: Correcting sequence: Hallo .
11:22:53:395: Corrected sequence: Hallo.
11:22:53:395: Translate corrected sequence: Hallo.
11:22:53:395: Translated corrected sequence: UN_ABC:Hallo .
11:22:53:396: Classify master context for sequence: Hallo .
11:22:53:397: Classified master context: Generic
11:22:53:397: Checking profanity for sequence: Hallo .
11:22:53:397: Classify context for sequence: Hallo .
11:22:53:397: Classified context: Handshake
Interpreting the request took: 5 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
11:22:53:398: Classify language for sequence: Mijn naam si gekste der henkies.
11:22:53:399: Classified language: NL
11:22:53:399: Translate sequence: Mijn naam si gekste der henkies.
11:22:53:401: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
11:22:53:401: Correction time limit: 700
11:22:53:402: Correcting sequence: Mijn naam si [] [] [] .
11:22:53:402: Corrected sequence: Mijn naam is gekste der henkies.
11:22:53:402: Translate corrected sequence: Mijn naam is gekste der henkies.
11:22:53:404: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
11:22:53:404: Classify master context for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:53:405: Classified master context: Generic
11:22:53:405: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:53:405: Classify context for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:53:405: Classified context: Handshake
Interpreting the request took: 7 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
11:22:53:406: Classify language for sequence: Albert einstein.
11:22:53:406: Classify language for sequence: What is your name? Albert einstein.
11:22:53:406: Classified language: EN
11:22:53:408: Translate sequence: Albert einstein.
11:22:53:410: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
11:22:53:410: Correction time limit: 300
11:22:53:410: Correcting sequence: [] [] .
11:22:53:410: Corrected sequence: Albert einstein.
11:22:53:410: Translate corrected sequence: Albert einstein.
11:22:53:413: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
11:22:53:413: Classify master context for sequence: [ABC] [ABC] .
11:22:53:413: Classify master context for sequence: What is your name? [ABC] [ABC] .
11:22:53:413: Classified master context: Generic
11:22:53:413: Checking profanity for sequence: [ABC] [ABC] .
11:22:53:413: Classify context for sequence: [ABC] [ABC] .
11:22:53:413: Classified context: Handshake
Interpreting the request took: 7 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
11:22:53:414: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
11:22:53:415: Selected primary language: EN
11:22:53:415: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
11:22:53:417: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
11:22:53:417: Correction time limit: 1100
11:22:53:417: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
11:22:54:518: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
11:22:54:518: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
11:22:54:519: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
11:22:54:519: Classify master context for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
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

Initializing DialogSet took 388 ms
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
11:22:56:878: Classify language for sequence: What are you?
11:22:56:878: Classified language: EN
11:22:56:878: Translate sequence: What are you?
11:22:56:880: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
11:22:56:880: Correction time limit: 400
11:22:56:880: Correcting sequence: What are you ?
11:22:56:880: Corrected sequence: What are you?
11:22:56:880: Translate corrected sequence: What are you?
11:22:56:881: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
11:22:56:881: Classify master context for sequence: What are you ?
11:22:56:881: Classified master context: Generic
11:22:56:881: Checking profanity for sequence: What are you ?
11:22:56:882: Classify context for sequence: What are you ?
11:22:56:882: Classified context: QuestionAndAnswer
11:22:56:882: Handling dialog: EN/Generic/QuestionAndAnswer
11:22:56:882:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
11:22:56:883:     Found matches for sequence: 10
11:22:56:883:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.18796992481203006 / 0.8333333333333334)
11:22:56:885:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06359649122807018 / 0.2819444444444445)
11:22:56:885:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06359649122807018 / 0.2819444444444445)
11:22:56:885:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06359649122807018 / 0.2819444444444445)
11:22:56:885:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.06265664160401002 / 0.2777777777777778)
11:22:56:885:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.06265664160401002 / 0.2777777777777778)
11:22:56:885:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:56:885:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:56:885:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:56:885:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:56:885:     Set dialog output: I am an artificially intelligent personal assistant.
11:22:56:885: Handling dialog: EN/Generic/Handshake
11:22:56:885:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:56:886:     Found matches for sequence: 2
11:22:56:886:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
11:22:56:886:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
11:22:56:886:     Prompt variable: firstName
11:22:56:886:     Set dialog output: My name is Dyz Lecticus.
11:22:56:886:     Set dialog prompt: What is your name?
11:22:56:886: Handling dialog: EN/Generic/Math
11:22:56:886:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
11:22:56:887:     Failed to find matches for sequence: What are you ?
Handling the request took: 36 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
11:22:56:914: Classify language for sequence: Hallo.
11:22:56:914: Classified language: NL
11:22:56:914: Translate sequence: Hallo.
11:22:56:914: Translated sequence: UN_ABC:Hallo .
11:22:56:915: Correction time limit: 200
11:22:56:915: Correcting sequence: Hallo .
11:22:56:915: Corrected sequence: Hallo.
11:22:56:915: Translate corrected sequence: Hallo.
11:22:56:915: Translated corrected sequence: UN_ABC:Hallo .
11:22:56:915: Classify master context for sequence: Hallo .
11:22:56:915: Classified master context: Generic
11:22:56:915: Checking profanity for sequence: Hallo .
11:22:56:915: Classify context for sequence: Hallo .
11:22:56:915: Classified context: Handshake
11:22:56:915: Handling dialog: NL/Generic/Handshake
11:22:56:915:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:56:915:     Found matches for sequence: 2
11:22:56:915:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
11:22:56:915:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
11:22:56:915:     Prompt variable: firstName
11:22:56:915:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
11:22:56:916:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 2 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
11:22:56:916: Classify language for sequence: Wie ben jij?
11:22:56:916: Classified language: NL
11:22:56:916: Translate sequence: Wie ben jij?
11:22:56:917: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
11:22:56:917: Correction time limit: 400
11:22:56:917: Correcting sequence: Wie ben jij ?
11:22:56:917: Corrected sequence: Wie ben jij?
11:22:56:917: Translate corrected sequence: Wie ben jij?
11:22:56:918: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
11:22:56:918: Classify master context for sequence: Wie ben jij ?
11:22:56:918: Classified master context: Generic
11:22:56:918: Checking profanity for sequence: Wie ben jij ?
11:22:56:918: Classify context for sequence: Wie ben jij ?
11:22:56:918: Classified context: Handshake
11:22:56:918: Handling dialog: NL/Generic/Handshake
11:22:56:918:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:56:918:     Found matches for sequence: 2
11:22:56:918:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
11:22:56:918:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
11:22:56:918:     Prompt variable: firstName
11:22:56:918:     Set dialog output: Mijn naam is Dyz Lecticus.
11:22:56:918:     Set dialog prompt: Wat is jouw naam?
11:22:56:918: Handling dialog: NL/Generic/QuestionAndAnswer
11:22:56:918:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
11:22:56:919:     Found matches for sequence: 15
11:22:56:919:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.3781278962001854 / 0.6476190476190478)
11:22:56:919:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18999073215940687 / 0.32539682539682546)
11:22:56:919:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18999073215940687 / 0.32539682539682546)
11:22:56:919:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.18813716404077852 / 0.32222222222222224)
11:22:56:919:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers en vragen over kamer boekingen. (0.18813716404077852 / 0.32222222222222224)
11:22:56:919:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.18813716404077852 / 0.32222222222222224)
11:22:56:919:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.1857780773443424 / 0.3181818181818182)
11:22:56:919:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.1857780773443424 / 0.3181818181818182)
11:22:56:919:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.1857780773443424 / 0.3181818181818182)
11:22:56:919:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.1857780773443424 / 0.3181818181818182)
11:22:56:919:     - [... 5]
11:22:56:919:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
11:22:56:919: Handling dialog: NL/Generic/Language
11:22:56:919:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
11:22:56:919:     Updated variable languages: Engels en Nederlands
11:22:56:920:     Found matches for sequence: 2
11:22:56:920:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
11:22:56:920:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
11:22:56:920:     Prompt variable: language
11:22:56:920:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 98 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
11:22:57:014: Classify language for sequence: Albert einstein.
11:22:57:014: Classify language for sequence: What is your name? Albert einstein.
11:22:57:014: Classified language: EN
11:22:57:015: Translate sequence: Albert einstein.
11:22:57:016: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
11:22:57:016: Correction time limit: 300
11:22:57:016: Correcting sequence: [] [] .
11:22:57:016: Corrected sequence: Albert einstein.
11:22:57:016: Translate corrected sequence: Albert einstein.
11:22:57:017: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
11:22:57:018: Classify master context for sequence: [ABC] [ABC] .
11:22:57:018: Classify master context for sequence: What is your name? [ABC] [ABC] .
11:22:57:018: Classified master context: Generic
11:22:57:018: Checking profanity for sequence: [ABC] [ABC] .
11:22:57:018: Classify context for sequence: [ABC] [ABC] .
11:22:57:018: Classified context: Handshake
11:22:57:018: Handling dialog: EN/Generic/Handshake
11:22:57:018:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:57:018:     Updated variable firstName: UN_ABC:Albert = Albert
11:22:57:018:     Updated variable lastName: UN_ABC:Einstein = Einstein
11:22:57:018:     Failed to find matches for sequence: [ABC] [ABC] .
11:22:57:018:     Updated variable fullName: Albert Einstein
11:22:57:018:     Prompt variable: nextDialog
11:22:57:018:     Set dialog output: 
11:22:57:018:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 4 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
11:22:57:019: Classify language for sequence: Mijn naam si gekste der henkies.
11:22:57:019: Classified language: NL
11:22:57:019: Translate sequence: Mijn naam si gekste der henkies.
11:22:57:020: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
11:22:57:020: Correction time limit: 700
11:22:57:020: Correcting sequence: Mijn naam si [] [] [] .
11:22:57:020: Corrected sequence: Mijn naam is gekste der henkies.
11:22:57:020: Translate corrected sequence: Mijn naam is gekste der henkies.
11:22:57:022: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
11:22:57:022: Classify master context for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:57:023: Classified master context: Generic
11:22:57:023: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:57:023: Classify context for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:57:023: Classified context: Handshake
11:22:57:023: Handling dialog: NL/Generic/Handshake
11:22:57:023:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:57:023:     Updated variable firstName: UN_ABC:Gekste = Gekste
11:22:57:023:     Updated variable lastName: UN_ABC:Henkies = Henkies
11:22:57:023:     Updated variable preposition: NL_PRE:6 = der
11:22:57:024:     Found matches for sequence: 18
11:22:57:024:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:024:     - [... 8]
11:22:57:024:     Updated variable fullName: Gekste der Henkies
11:22:57:024:     Prompt variable: nextDialog
11:22:57:024:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
11:22:57:024:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 96 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
11:22:57:114: Classify language for sequence: Mijn naam is jan de lange.
11:22:57:114: Classified language: NL
11:22:57:116: Translate sequence: Mijn naam is jan de lange.
11:22:57:117: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
11:22:57:118: Correction time limit: 700
11:22:57:118: Correcting sequence: Mijn naam is [] [] [] .
11:22:57:118: Corrected sequence: Mijn naam is jan de lange.
11:22:57:118: Translate corrected sequence: Mijn naam is jan de lange.
11:22:57:120: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
11:22:57:120: Classify master context for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:57:121: Classified master context: Generic
11:22:57:121: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:57:121: Classify context for sequence: Mijn naam is [ABC] [PRE] [ABC] .
11:22:57:121: Classified context: Handshake
11:22:57:121: Handling dialog: NL/Generic/Handshake
11:22:57:121:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:57:121:     Updated variable firstName: UN_ABC:Jan = Jan
11:22:57:121:     Updated variable lastName: UN_ABC:Lange = Lange
11:22:57:122:     Updated variable preposition: NL_PRE:5 = de
11:22:57:122:     Found matches for sequence: 18
11:22:57:122:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
11:22:57:122:     - [... 8]
11:22:57:122:     Updated variable fullName: Jan de Lange
11:22:57:122:     Prompt variable: nextDialog
11:22:57:122:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
11:22:57:122:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 8 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
11:22:57:123: Classify language for sequence: Who created you?
11:22:57:123: Classified language: EN
11:22:57:123: Translate sequence: Who created you?
11:22:57:124: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
11:22:57:124: Correction time limit: 400
11:22:57:124: Correcting sequence: Who created you ?
11:22:57:124: Corrected sequence: Who created you?
11:22:57:124: Translate corrected sequence: Who created you?
11:22:57:125: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
11:22:57:125: Classify master context for sequence: Who created you ?
11:22:57:125: Classified master context: Generic
11:22:57:125: Checking profanity for sequence: Who created you ?
11:22:57:125: Classify context for sequence: Who created you ?
11:22:57:125: Classified context: QuestionAndAnswer
11:22:57:125: Handling dialog: EN/Generic/QuestionAndAnswer
11:22:57:125:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
11:22:57:125:     Found matches for sequence: 5
11:22:57:125:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.19799498746867167 / 0.8777777777777778)
11:22:57:125:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.05263157894736842 / 0.23333333333333334)
11:22:57:125:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:57:125:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:57:125:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.05263157894736842 / 0.23333333333333334)
11:22:57:125:     Set dialog output: My software was written by André van der Zee.
11:22:57:125: Handling dialog: EN/Generic/Math
11:22:57:125:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
11:22:57:126:     Failed to find matches for sequence: Who created you ?
11:22:57:126: Handling dialog: EN/Generic/Handshake
11:22:57:126:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
11:22:57:126:     Found matches for sequence: 1
11:22:57:127:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
11:22:57:127:     Prompt variable: firstName
11:22:57:127:     Set dialog output: My name is Dyz Lecticus.
11:22:57:127:     Set dialog prompt: What is your name?
Handling the request took: 92 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
11:22:57:214: Classify language for sequence: You asshole!
11:22:57:214: Classified language: EN
11:22:57:214: Translate sequence: You asshole!
11:22:57:215: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
11:22:57:216: Correction time limit: 300
11:22:57:216: Correcting sequence: You [] !
11:22:57:216: Corrected sequence: You asshole!
11:22:57:216: Translate corrected sequence: You asshole!
11:22:57:219: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
11:22:57:219: Classify master context for sequence: You [PRF] !
11:22:57:219: Classified master context: Support
11:22:57:220: Checking profanity for sequence: You [PRF] !
11:22:57:220: Handling dialog: EN/Generic/Profanity
11:22:57:220:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
11:22:57:220:     Found matches for sequence: 2
11:22:57:220:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
11:22:57:221:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
11:22:57:221:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 7 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
11:22:57:222: Classify language for sequence: Jij klootzak!
11:22:57:223: Classified language: NL
11:22:57:223: Translate sequence: Jij klootzak!
11:22:57:226: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
11:22:57:228: Correction time limit: 300
11:22:57:228: Correcting sequence: Jij [] !
11:22:57:228: Corrected sequence: Jij klootzak!
11:22:57:228: Translate corrected sequence: Jij klootzak!
11:22:57:231: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
11:22:57:232: Classify master context for sequence: Jij [PRF] !
11:22:57:232: Classified master context: Support
11:22:57:232: Checking profanity for sequence: Jij [PRF] !
11:22:57:233: Handling dialog: NL/Generic/Profanity
11:22:57:233:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
11:22:57:233:     Found matches for sequence: 2
11:22:57:233:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
11:22:57:233:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
11:22:57:233:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 11 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
11:22:57:235: Classify language for sequence: Can I book a room for 5 people?
11:22:57:235: Classified language: EN
11:22:57:235: Translate sequence: Can I book a room for 5 people?
11:22:57:239: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
11:22:57:240: Correction time limit: 900
11:22:57:240: Correcting sequence: Can I book a room for [] people ?
11:22:57:241: Corrected sequence: Can I book a room for 5 people?
11:22:57:241: Translate corrected sequence: Can I book a room for 5 people?
11:22:57:244: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
11:22:57:245: Classify master context for sequence: Can I book a room for [NUM] people ?
11:22:57:245: Classified master context: Room
11:22:57:245: Checking profanity for sequence: Can I book a room for [NUM] people ?
11:22:57:245: Classify context for sequence: Can I book a room for [NUM] people ?
11:22:57:245: Classified context: Booking
11:22:57:245: Handling dialog: EN/Room/Booking
11:22:57:246:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
11:22:57:246:     Updated variable bookPeople: UN_NUM:5 = 5
11:22:57:248:     Found matches for sequence: 326
11:22:57:248:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.3032375740994073 / 1.0)
11:22:57:248:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.0851573187414503 / 0.9053157790536528)
11:22:57:248:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.000911992704059 / 0.8687388635913682)
11:22:57:249:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9808481532147746 / 0.8600277172837063)
11:22:57:249:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.9808481532147746 / 0.8600277172837063)
11:22:57:249:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9808481532147746 / 0.8600277172837063)
11:22:57:249:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.7828317373461013 / 0.7740546426450208)
11:22:57:249:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.6785225718194257 / 0.7287665808750743)
11:22:57:249:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.6785225718194257 / 0.7287665808750743)
11:22:57:249:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.6785225718194257 / 0.7287665808750743)
11:22:57:249:     - [... 316]
11:22:57:249:     Prompt variable: bookDate
11:22:57:249:     Set dialog output: Okay.
11:22:57:249:     Set dialog prompt: On what date?
Handling the request took: 80 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
11:22:57:315: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
11:22:57:317: Classified language: EN
11:22:57:317: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
11:22:57:320: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
11:22:57:322: Correction time limit: 1332
11:22:57:322: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
11:22:57:323: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
11:22:57:323: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
11:22:57:326: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
11:22:57:328: Classify master context for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR] ?
11:22:57:329: Classified master context: Room
11:22:57:329: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR] ?
11:22:57:329: Classify context for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR] ?
11:22:57:330: Classified context: Booking
11:22:57:330: Handling dialog: EN/Room/Booking
11:22:57:330:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
11:22:57:331:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
11:22:57:331:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
11:22:57:332:     Updated variable bookDuration: EN_DUR:02:00 = two hours
11:22:57:332:     Updated variable bookPeople: UN_NUM:12 = 12
11:22:57:339:     Found matches for sequence: 262
11:22:57:339:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.5882352941176476 / 0.6836495031616985)
11:22:57:343:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.2859097127222987 / 0.6037940379403796)
11:22:57:343:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.1823415412676703 / 0.576437819933755)
11:22:57:343:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (1.9835841313269498 / 0.5239385727190606)
11:22:57:343:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (1.9835841313269498 / 0.5239385727190606)
11:22:57:343:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (1.9808481532147746 / 0.523215898825655)
11:22:57:343:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.9808481532147746 / 0.523215898825655)
11:22:57:343:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.8813269493844051 / 0.4969286359530263)
11:22:57:343:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.8800159598723212 / 0.49658235471243606)
11:22:57:343:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8433652530779754 / 0.4869015356820235)
11:22:57:343:     - [... 252]
11:22:57:343:     Prompt variable: bookConfirmation
11:22:57:344:     Set dialog output: Okay.
11:22:57:344:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 30 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
11:22:57:345: Classify language for sequence: What does it cost to book a room?
11:22:57:347: Classified language: EN
11:22:57:347: Translate sequence: What does it cost to book a room?
11:22:57:350: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book|EN_NAM:firstName:UN_ABC:Book UN_ABC:a|EN_NAM:lastName:UN_ABC:A UN_ABC:room ?
11:22:57:351: Correction time limit: 900
11:22:57:351: Correcting sequence: What does it cost to [] [] room ?
11:22:57:352: Corrected sequence: What does it cost to book a room?
11:22:57:352: Translate corrected sequence: What does it cost to book a room?
11:22:57:354: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book|EN_NAM:firstName:UN_ABC:Book UN_ABC:a|EN_NAM:lastName:UN_ABC:A UN_ABC:room ?
11:22:57:355: Classify master context for sequence: What does it cost to [ABC] [ABC] room ?
11:22:57:355: Classified master context: Room
11:22:57:355: Checking profanity for sequence: What does it cost to [ABC] [ABC] room ?
11:22:57:356: Classify context for sequence: What does it cost to [ABC] [ABC] room ?
11:22:57:356: Classified context: QuestionAndAnswer
11:22:57:356: Handling dialog: EN/Room/QuestionAndAnswer
11:22:57:356:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
11:22:57:357:     Found matches for sequence: 6
11:22:57:357:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (1.2864583333333333 / 0.5966183574879227)
11:22:57:357:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.2864583333333333 / 0.5966183574879227)
11:22:57:357:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5024154589371981)
11:22:57:357:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.0833333333333333 / 0.5024154589371981)
11:22:57:357:     - Are there any costs attached to booking van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.12560386473429952)
11:22:57:357:     - Are there any costs attached to reserving van een room? [OUTPUT] There are no costs attached to booking a room. (0.2708333333333333 / 0.12560386473429952)
11:22:57:357:     Set dialog output: There are no costs attached to booking a room.
Handling the request took: 71 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
11:22:57:415: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
11:22:57:416: Classified language: EN
11:22:57:416: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
11:22:57:417: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
11:22:57:417: Correction time limit: 1300
11:22:57:417: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
11:22:57:417: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
11:22:57:418: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
11:22:57:418: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
11:22:57:419: Classify master context for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:419: Classified master context: Generic
11:22:57:419: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:419: Classify context for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:419: Classified context: Math
11:22:57:419: Handling dialog: EN/Generic/Math
11:22:57:419:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
11:22:57:419:     Updated variable number1: EN_NUM:10 = ten
11:22:57:419:     Updated variable number2: EN_NUM:40 = fourty
11:22:57:419:     Updated variable number3: EN_NUM:20 = twenty
11:22:57:419:     Updated variable number4: EN_NUM:3 = three
11:22:57:419:     Updated variable number5: EN_NUM:6 = six
11:22:57:419:     Updated variable operator1: EN_MTH:M = multiplied by
11:22:57:419:     Updated variable operator2: EN_MTH:D = divided by
11:22:57:419:     Updated variable operator3: EN_MTH:A = plus
11:22:57:419:     Updated variable operator4: EN_MTH:M = multiplied by
11:22:57:419:     Failed to find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:420:     Calculate expression: 10 M 40 D 20 A 3 M 6
11:22:57:420:     Calculated expression: 38.0
11:22:57:420:     Updated variable exact: exactly
11:22:57:420:     Updated variable result: thirtyeight
11:22:57:420:     Prompt variable: nextDialog
11:22:57:420:     Set dialog output: Exactly thirtyeight.
11:22:57:420:     Set dialog prompt: What else can I do for you?
Handling the request took: 5 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
11:22:57:420: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
11:22:57:421: Classified language: NL
11:22:57:421: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
11:22:57:424: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie|NL_NAM:firstName:UN_ABC:Drie NL_MTH:M|UN_ABC:keer|NL_NAM:lastName:UN_ABC:Keer NL_NUM:6|UN_ABC:zes ?
11:22:57:425: Correction time limit: 1200
11:22:57:425: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
11:22:57:425: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
11:22:57:425: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
11:22:57:429: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie|NL_NAM:firstName:UN_ABC:Drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
11:22:57:430: Classify master context for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:430: Classified master context: Generic
11:22:57:430: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:430: Classify context for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:430: Classified context: Math
11:22:57:430: Handling dialog: NL/Generic/Math
11:22:57:430:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
11:22:57:431:     Updated variable number1: NL_NUM:10 = tien
11:22:57:431:     Updated variable number2: NL_NUM:40 = veertig
11:22:57:431:     Updated variable number3: NL_NUM:20 = twintig
11:22:57:431:     Updated variable number4: NL_NUM:3 = drie
11:22:57:431:     Updated variable number5: NL_NUM:6 = zes
11:22:57:431:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
11:22:57:431:     Updated variable operator2: NL_MTH:D = gedeeld door
11:22:57:431:     Updated variable operator3: NL_MTH:A = plus
11:22:57:431:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
11:22:57:431:     Failed to find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] ?
11:22:57:431:     Calculate expression: 10 M 40 D 20 A 3 M 6
11:22:57:431:     Calculated expression: 38.0
11:22:57:431:     Updated variable exact: precies
11:22:57:431:     Updated variable result: achtendertig
11:22:57:431:     Prompt variable: nextDialog
11:22:57:432:     Set dialog output: Precies achtendertig.
11:22:57:432:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 95 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
11:22:57:515: Classify language for sequence: What languages do you speak?
11:22:57:515: Classified language: EN
11:22:57:515: Translate sequence: What languages do you speak?
11:22:57:517: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
11:22:57:517: Correction time limit: 600
11:22:57:517: Correcting sequence: What languages do you speak ?
11:22:57:518: Corrected sequence: What languages do you speak?
11:22:57:518: Translate corrected sequence: What languages do you speak?
11:22:57:519: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
11:22:57:519: Classify master context for sequence: What languages do you speak ?
11:22:57:520: Classified master context: Generic
11:22:57:520: Checking profanity for sequence: What languages do you speak ?
11:22:57:520: Classify context for sequence: What languages do you speak ?
11:22:57:520: Classified context: Language
11:22:57:520: Handling dialog: EN/Generic/Language
11:22:57:520:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
11:22:57:520:     Updated variable languages: English and Dutch
11:22:57:520:     Found matches for sequence: 6
11:22:57:521:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.5959595959595958)
11:22:57:521:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.3838383838383838)
11:22:57:521:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
11:22:57:521:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
11:22:57:521:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.3194444444444444)
11:22:57:521:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.2884615384615385 / 0.1515151515151515)
11:22:57:521:     Prompt variable: language
11:22:57:521:     Set dialog output: I speak English and Dutch.
Handling the request took: 6 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
11:22:57:521: Classify language for sequence: Spreek je duits?
11:22:57:521: Classified language: NL
11:22:57:521: Translate sequence: Spreek je duits?
11:22:57:523: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
11:22:57:523: Correction time limit: 400
11:22:57:523: Correcting sequence: Spreek je [] ?
11:22:57:523: Corrected sequence: Spreek je Duits?
11:22:57:523: Translate corrected sequence: Spreek je Duits?
11:22:57:525: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
11:22:57:525: Classify master context for sequence: Spreek je [LNG] ?
11:22:57:525: Classified master context: Generic
11:22:57:526: Checking profanity for sequence: Spreek je [LNG] ?
11:22:57:526: Classify context for sequence: Spreek je [LNG] ?
11:22:57:526: Classified context: Language
11:22:57:526: Handling dialog: NL/Generic/Language
11:22:57:526:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
11:22:57:526:     Updated variable language: NL_LNG:DE = Duits
11:22:57:526:     Updated variable languages: Engels en Nederlands
11:22:57:526:     Updated variable confirmation: Nee
11:22:57:526:     Found matches for sequence: 5
11:22:57:526:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
11:22:57:526:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
11:22:57:526:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
11:22:57:526:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
11:22:57:527:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
11:22:57:527:     Prompt variable: 
11:22:57:527:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 95 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
11:22:57:616: Classify language for sequence: Spreek je engels?
11:22:57:616: Classified language: NL
11:22:57:616: Translate sequence: Spreek je engels?
11:22:57:618: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
11:22:57:618: Correction time limit: 400
11:22:57:618: Correcting sequence: Spreek je [] ?
11:22:57:618: Corrected sequence: Spreek je Engels?
11:22:57:618: Translate corrected sequence: Spreek je Engels?
11:22:57:619: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
11:22:57:620: Classify master context for sequence: Spreek je [LNG] ?
11:22:57:620: Classified master context: Generic
11:22:57:620: Checking profanity for sequence: Spreek je [LNG] ?
11:22:57:620: Classify context for sequence: Spreek je [LNG] ?
11:22:57:620: Classified context: Language
11:22:57:620: Handling dialog: NL/Generic/Language
11:22:57:620:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
11:22:57:620:     Updated variable language: NL_LNG:EN = Engels
11:22:57:620:     Updated variable languages: Engels en Nederlands
11:22:57:620:     Updated variable confirmation: Ja
11:22:57:620:     Found matches for sequence: 5
11:22:57:620:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
11:22:57:620:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
11:22:57:620:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
11:22:57:620:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
11:22:57:620:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
11:22:57:620:     Prompt variable: 
11:22:57:620:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 4 ms
~~~~

Test results
------------
All 19 tests have been executed successfully (245 assertions).  
Total test duration: 61054 ms (total sleep duration: 28100 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 613 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 424 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 456 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 463 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 472 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117520 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117532 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117530 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 117739 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117593 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117626 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117604 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 546399 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 546400 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 546401 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 546401 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 546408 Kb / 533 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 551278 Kb / 538 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 551300 Kb / 538 Mb
