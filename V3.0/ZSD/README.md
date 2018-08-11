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
Initializing the SymbolCorrector took: 10 ms
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
Average correction time: 248 ms
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
Initializing the SequenceClassifier took: 3394 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 2 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 1 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 2 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 12 ms

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
Initializing the SequenceMatcher took: 6822 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 27 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 45 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 1 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 40 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 56 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 38 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 10 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 38 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 31 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 14 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 58 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 381 ms

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
Initializing the EntityValueTranslator took: 2017 ms

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
Translating the sequence took: 1 ms
Translation: 'NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 3 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 7 ms
Retranslation: 'My name is Andrew from the Sea'

Sequence: 'Mijn naam is Andre van der Zee'
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:Andre|NL_NAM:firstName:UN_ABC:Andre NL_PRE:3|NL_NAM:preposition:NL_PRE:3 UN_ABC:Zee|NL_NAM:lastName:UN_ABC:Zee'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .'
Retranslating the sequence took: 1 ms
Retranslation: 'Mijn naam si gekste der henkies.'

Sequence: 'to Germany or France'
Translating the sequence took: 0 ms
Translation: 'UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France'
Retranslating the sequence took: 0 ms
Retranslation: 'to Germany or France'

Sequence: 'naar Duitsland of Frankrijk'
Translating the sequence took: 0 ms
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
Translating the sequence took: 1 ms
Translation: 'EN_NUM:15|UN_ABC:fifteen EN_CUR:GBP'
Retranslating the sequence took: 0 ms
Retranslation: 'fifteen british pound sterling'

Sequence: 'vijftien euro'
Translating the sequence took: 0 ms
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
Converting 333841 entity values took: 419 ms

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
Converting 1574 dialog examples took: 213 ms

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
Initializing Translator took 2370 ms
Initializing Classifier took 4768 ms
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

Initializing LanguagePreprocessor took 15 ms
Initializing LanguageContextNLForeignTransfer took 39 ms
Initializing LanguageContextENSupport took 66 ms
Initializing LanguageContextENForeignTransfer took 136 ms
Initializing LanguageContextENGeneric took 182 ms
Initializing LanguageContextNLSupport took 224 ms
Initializing LanguageContextNLGeneric took 400 ms
Initializing LanguageContextENRoom took 432 ms
Initializing LanguageContextNLRoom took 439 ms
Initializing LanguageMasterContextEN took 469 ms
Initializing LanguageMasterContextNL took 504 ms
Initializing EntityValueTranslator took 1688 ms
Initializing LanguageClassifier took 33372 ms
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
Classifying the input sequence took: 0 ms

Context probabilities for 'wat is your name?', threshold: 0.1
'EN': 0.23060649760430146 / 0.9932631640367013
'NL': 0.07897828519850139 / 0.3401735088187438

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.23060649760430146 / 0.9932631640367013
~~~~

nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier
-------------------------------------------------------
This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as master context classifiers.
~~~~
Classified sequence: 'Who are you?' -> Generic
Classifying the input sequence took: 2 ms
Classified sequence (case insensitive): 'Who are you?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Who are you?', threshold: 0.0
'Generic': 0.2567506098766168 / 0.8619036365784715

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.32718757689107114 / 1.0
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
'QuestionAndAnswer': 0.2979952203929899 / 0.739945054945055
'Handshake': 0.1732604610321935 / 0.4302190524707082

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.31593200214633926 / 0.5505162432609864
'Handshake': 0.2697062097671942 / 0.4699671080374462

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.3995010545147182 / 0.9919918492103925
'QuestionAndAnswer': 0.12221189591078066 / 0.30346153846153845

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5788490392526984 / 1.0
'QuestionAndAnswer': 0.14467696452831913 / 0.2499390250610055

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 1 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.3039476013453708 / 0.9434065934065932
'Handshake': 0.232863110038816 / 0.7227712684666328
'Thanks': 0.05700901629569717 / 0.17694721596395238

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.32589877955497715 / 0.7098543776234906
'Handshake': 0.30491747737282804 / 0.6641540861938096
'Language': 0.11799437675726336 / 0.2570087098530212
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
21:58:32:613: Classify language for sequence: Hallo.
21:58:32:614: Classified language: NL
21:58:32:614: Translate sequence: Hallo.
21:58:32:616: Translated sequence: UN_ABC:Hallo .
21:58:32:616: Correction time limit: 200
21:58:32:616: Correcting sequence: Hallo .
21:58:32:616: Corrected sequence: Hallo.
21:58:32:616: Translate corrected sequence: Hallo.
21:58:32:618: Translated corrected sequence: UN_ABC:Hallo .
21:58:32:618: Checking profanity for sequence: Hallo.
21:58:32:619: Classify master context for input sequence: Hallo.
21:58:32:619: Classified master context: Generic
21:58:32:619: Classify context for input sequence: Hallo.
21:58:32:619: Classified context: Handshake
Interpreting the request took: 7 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
21:58:32:620: Classify language for sequence: Mijn naam si gekste der henkies.
21:58:32:620: Classified language: NL
21:58:32:620: Translate sequence: Mijn naam si gekste der henkies.
21:58:32:629: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
21:58:32:630: Correction time limit: 700
21:58:32:630: Correcting sequence: Mijn naam si gekste [] henkies .
21:58:33:183: Corrected sequence: Mijn naam is gekste der henkies.
21:58:33:183: Translate corrected sequence: Mijn naam is gekste der henkies.
21:58:33:186: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:58:33:186: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:33:187: Classify master context for input sequence: Mijn naam is gekste der henkies.
21:58:33:187: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:33:187: Classified master context: Generic
21:58:33:187: Classify context for input sequence: Mijn naam is gekste der henkies.
21:58:33:189: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:33:190: Classified context: Handshake
Interpreting the request took: 571 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
21:58:33:190: Classify language for sequence: Albert einstein.
21:58:33:191: Classify language for sequence: What is your name? Albert einstein.
21:58:33:191: Classified language: EN
21:58:33:193: Translate sequence: Albert einstein.
21:58:33:195: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:58:33:195: Correction time limit: 300
21:58:33:195: Correcting sequence: [] [] .
21:58:33:195: Corrected sequence: Albert einstein.
21:58:33:195: Translate corrected sequence: Albert einstein.
21:58:33:198: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:58:33:198: Checking profanity for sequence: [ABC] [ABC].
21:58:33:198: Classify master context for input sequence: Albert einstein.
21:58:33:198: Classify master context for classification sequence: What is your name? [ABC] [ABC].
21:58:33:199: Classified master context: Generic
21:58:33:199: Classify context for input sequence: Albert einstein.
21:58:33:199: Classify context for classification sequence: What is your name? [ABC] [ABC].
21:58:33:199: Classified context: Handshake
Interpreting the request took: 9 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
21:58:33:199: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:58:33:200: Selected primary language: EN
21:58:33:200: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:58:33:202: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
21:58:33:203: Correction time limit: 1100
21:58:33:203: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
21:58:34:303: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:58:34:303: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:58:34:304: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
21:58:34:304: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
21:58:34:305: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
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

Initializing DialogSet took 560 ms
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
21:58:37:161: Classify language for sequence: What are you?
21:58:37:162: Classified language: EN
21:58:37:162: Translate sequence: What are you?
21:58:37:163: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
21:58:37:163: Correction time limit: 400
21:58:37:163: Correcting sequence: What are you ?
21:58:37:164: Corrected sequence: What are you?
21:58:37:164: Translate corrected sequence: What are you?
21:58:37:164: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
21:58:37:165: Checking profanity for sequence: What are you?
21:58:37:165: Classify master context for input sequence: What are you?
21:58:37:165: Classified master context: Generic
21:58:37:165: Classify context for input sequence: What are you?
21:58:37:165: Classified context: QuestionAndAnswer
21:58:37:165: Handling dialog: EN/Generic/QuestionAndAnswer
21:58:37:165:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:37:165:     Find matches for sequence: What are you?
21:58:37:166:     Found matches for sequence: 10
21:58:37:166:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.21428571428571425 / 0.8809523809523809)
21:58:37:166:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
21:58:37:167:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
21:58:37:167:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
21:58:37:167:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
21:58:37:167:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
21:58:37:167:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:167:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:167:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:167:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:168:     Set dialog output: I am an artificially intelligent personal assistant.
21:58:37:168: Handling dialog: EN/Generic/Handshake
21:58:37:169:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:169:     Find matches for sequence: What are you?
21:58:37:169:     Found matches for sequence: 2
21:58:37:169:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
21:58:37:169:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
21:58:37:169:     Prompt variable: firstName
21:58:37:169:     Set dialog output: My name is Dyz Lecticus.
21:58:37:169:     Set dialog prompt: What is your name?
Handling the request took: 27 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
21:58:37:188: Classify language for sequence: Wat ben jij?
21:58:37:188: Classified language: NL
21:58:37:188: Translate sequence: Wat ben jij?
21:58:37:189: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
21:58:37:189: Correction time limit: 400
21:58:37:189: Correcting sequence: Wat ben jij ?
21:58:37:189: Corrected sequence: Wat ben jij?
21:58:37:189: Translate corrected sequence: Wat ben jij?
21:58:37:190: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
21:58:37:190: Checking profanity for sequence: Wat ben jij?
21:58:37:190: Classify master context for input sequence: Wat ben jij?
21:58:37:190: Classified master context: Generic
21:58:37:190: Classify context for input sequence: Wat ben jij?
21:58:37:190: Classified context: QuestionAndAnswer
21:58:37:190: Handling dialog: NL/Generic/QuestionAndAnswer
21:58:37:190:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:37:190:     Find matches for sequence: Wat ben jij?
21:58:37:190:     Found matches for sequence: 9
21:58:37:190:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.46601208459214505 / 0.9793650793650793)
21:58:37:190:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
21:58:37:191:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
21:58:37:191:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
21:58:37:191:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
21:58:37:191:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
21:58:37:191:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
21:58:37:191:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
21:58:37:191:     - Ben jij een menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
21:58:37:191:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
21:58:37:191: Handling dialog: NL/Generic/Handshake
21:58:37:192:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:192:     Find matches for sequence: Wat ben jij?
21:58:37:193:     Found matches for sequence: 5
21:58:37:193:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
21:58:37:193:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
21:58:37:193:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
21:58:37:193:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
21:58:37:193:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
21:58:37:193:     Prompt variable: firstName
21:58:37:193:     Set dialog output: Mijn naam is Dyz Lecticus.
21:58:37:193:     Set dialog prompt: Wat is jouw naam?
21:58:37:193: Handling dialog: NL/Generic/Language
21:58:37:194:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:58:37:194:     Updated variable languages: Engels en Nederlands
21:58:37:195:     Find matches for sequence: Wat ben jij?
21:58:37:195:     Found matches for sequence: 2
21:58:37:195:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
21:58:37:195:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
21:58:37:195:     Prompt variable: language
21:58:37:195:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 100 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
21:58:37:288: Classify language for sequence: What is the answer?
21:58:37:288: Classified language: EN
21:58:37:288: Translate sequence: What is the answer?
21:58:37:289: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
21:58:37:289: Correction time limit: 500
21:58:37:289: Correcting sequence: What is [] answer ?
21:58:37:289: Corrected sequence: What is the answer?
21:58:37:289: Translate corrected sequence: What is the answer?
21:58:37:290: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
21:58:37:290: Checking profanity for sequence: What is [PRE] answer?
21:58:37:290: Classify master context for input sequence: What is the answer?
21:58:37:290: Classify master context for classification sequence: What is [PRE] answer?
21:58:37:290: Selected input sequence master context classification.
21:58:37:290: Classified master context: Generic
21:58:37:290: Classify context for input sequence: What is the answer?
21:58:37:290: Classified context: QuestionAndAnswer
21:58:37:290: Handling dialog: EN/Generic/QuestionAndAnswer
21:58:37:290:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:37:290:     Find matches for sequence: What is the answer?
21:58:37:290:     Found matches for sequence: 9
21:58:37:290:     - What is the answer? [OUTPUT] Fourtytwo. (0.2548262548262548 / 0.8380952380952382)
21:58:37:290:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
21:58:37:290:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
21:58:37:290:     - What is the question? [OUTPUT] I do not know. (0.10424710424710423 / 0.34285714285714286)
21:58:37:290:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.10424710424710423 / 0.34285714285714286)
21:58:37:290:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.10424710424710423 / 0.34285714285714286)
21:58:37:290:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.10424710424710423 / 0.34285714285714286)
21:58:37:290:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
21:58:37:290:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
21:58:37:290:     Set dialog output: Fourtytwo.
Handling the request took: 3 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
21:58:37:291: Classify language for sequence: Wat is het antwoord?
21:58:37:291: Classified language: NL
21:58:37:291: Translate sequence: Wat is het antwoord?
21:58:37:292: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
21:58:37:292: Correction time limit: 500
21:58:37:292: Correcting sequence: Wat is het antwoord ?
21:58:37:292: Corrected sequence: Wat is het antwoord?
21:58:37:292: Translate corrected sequence: Wat is het antwoord?
21:58:37:292: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
21:58:37:292: Checking profanity for sequence: Wat is het antwoord?
21:58:37:293: Classify master context for input sequence: Wat is het antwoord?
21:58:37:293: Classified master context: Generic
21:58:37:293: Classify context for input sequence: Wat is het antwoord?
21:58:37:293: Classified context: QuestionAndAnswer
21:58:37:293: Handling dialog: NL/Generic/QuestionAndAnswer
21:58:37:293:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:37:293:     Find matches for sequence: Wat is het antwoord?
21:58:37:293:     Found matches for sequence: 10
21:58:37:293:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.6102719033232629 / 1.0)
21:58:37:293:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
21:58:37:293:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
21:58:37:293:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.297583081570997 / 0.4876237623762376)
21:58:37:293:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.14425981873111782 / 0.23638613861386137)
21:58:37:293:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
21:58:37:293:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
21:58:37:293:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.14425981873111782 / 0.23638613861386137)
21:58:37:293:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
21:58:37:293:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
21:58:37:293:     Set dialog output: Tweeenveertig.
Handling the request took: 97 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
21:58:37:388: Classify language for sequence: Hallo.
21:58:37:388: Classified language: NL
21:58:37:388: Translate sequence: Hallo.
21:58:37:388: Translated sequence: UN_ABC:Hallo .
21:58:37:388: Correction time limit: 200
21:58:37:388: Correcting sequence: Hallo .
21:58:37:388: Corrected sequence: Hallo.
21:58:37:388: Translate corrected sequence: Hallo.
21:58:37:388: Translated corrected sequence: UN_ABC:Hallo .
21:58:37:389: Checking profanity for sequence: Hallo.
21:58:37:389: Classify master context for input sequence: Hallo.
21:58:37:389: Classified master context: Generic
21:58:37:389: Classify context for input sequence: Hallo.
21:58:37:389: Classified context: Handshake
21:58:37:389: Handling dialog: NL/Generic/Handshake
21:58:37:389:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:389:     Find matches for sequence: Hallo.
21:58:37:389:     Found matches for sequence: 2
21:58:37:389:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
21:58:37:389:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
21:58:37:389:     Prompt variable: firstName
21:58:37:389:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
21:58:37:389:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 1 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
21:58:37:389: Classify language for sequence: Wie ben jij?
21:58:37:390: Classified language: NL
21:58:37:390: Translate sequence: Wie ben jij?
21:58:37:390: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
21:58:37:390: Correction time limit: 400
21:58:37:390: Correcting sequence: Wie ben jij ?
21:58:37:391: Corrected sequence: Wie ben jij?
21:58:37:391: Translate corrected sequence: Wie ben jij?
21:58:37:391: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
21:58:37:391: Checking profanity for sequence: Wie ben jij?
21:58:37:391: Classify master context for input sequence: Wie ben jij?
21:58:37:391: Classified master context: Generic
21:58:37:391: Classify context for input sequence: Wie ben jij?
21:58:37:391: Classified context: Handshake
21:58:37:391: Handling dialog: NL/Generic/Handshake
21:58:37:391:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:391:     Find matches for sequence: Wie ben jij?
21:58:37:391:     Found matches for sequence: 2
21:58:37:392:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
21:58:37:392:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
21:58:37:392:     Prompt variable: firstName
21:58:37:392:     Set dialog output: Mijn naam is Dyz Lecticus.
21:58:37:392:     Set dialog prompt: Wat is jouw naam?
21:58:37:392: Handling dialog: NL/Generic/QuestionAndAnswer
21:58:37:392:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:37:392:     Find matches for sequence: Wie ben jij?
21:58:37:392:     Found matches for sequence: 15
21:58:37:392:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.3081570996978852 / 0.6476190476190475)
21:58:37:392:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
21:58:37:392:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
21:58:37:392:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
21:58:37:392:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
21:58:37:392:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
21:58:37:392:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.15312375306390014 / 0.3218029350104822)
21:58:37:392:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
21:58:37:392:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
21:58:37:392:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
21:58:37:392:     - [... 5]
21:58:37:392:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
21:58:37:392: Handling dialog: NL/Generic/Language
21:58:37:392:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:58:37:392:     Updated variable languages: Engels en Nederlands
21:58:37:392:     Find matches for sequence: Wie ben jij?
21:58:37:392:     Found matches for sequence: 2
21:58:37:392:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
21:58:37:392:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
21:58:37:392:     Prompt variable: language
21:58:37:392:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 99 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
21:58:37:488: Classify language for sequence: Albert einstein.
21:58:37:488: Classify language for sequence: What is your name? Albert einstein.
21:58:37:488: Classified language: EN
21:58:37:489: Translate sequence: Albert einstein.
21:58:37:491: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:58:37:491: Correction time limit: 300
21:58:37:491: Correcting sequence: [] [] .
21:58:37:491: Corrected sequence: Albert einstein.
21:58:37:491: Translate corrected sequence: Albert einstein.
21:58:37:493: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
21:58:37:493: Checking profanity for sequence: [ABC] [ABC].
21:58:37:493: Classify master context for input sequence: Albert einstein.
21:58:37:493: Classify master context for classification sequence: What is your name? [ABC] [ABC].
21:58:37:493: Classified master context: Generic
21:58:37:493: Classify context for input sequence: Albert einstein.
21:58:37:493: Classify context for classification sequence: What is your name? [ABC] [ABC].
21:58:37:494: Classified context: Handshake
21:58:37:494: Handling dialog: EN/Generic/Handshake
21:58:37:494:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:494:     Updated variable firstName: UN_ABC:Albert = Albert
21:58:37:494:     Updated variable lastName: UN_ABC:Einstein = Einstein
21:58:37:494:     Find matches for sequence: [ABC] [ABC].
21:58:37:494:     Found matches for sequence: 0
21:58:37:494:     Updated variable fullName: Albert Einstein
21:58:37:494:     Prompt variable: nextDialog
21:58:37:494:     Set dialog output: 
21:58:37:494:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 6 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
21:58:37:495: Classify language for sequence: Mijn naam si gekste der henkies.
21:58:37:495: Classified language: NL
21:58:37:495: Translate sequence: Mijn naam si gekste der henkies.
21:58:37:496: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
21:58:37:496: Correction time limit: 700
21:58:37:496: Correcting sequence: Mijn naam si gekste [] henkies .
21:58:37:977: Corrected sequence: Mijn naam is gekste der henkies.
21:58:37:977: Translate corrected sequence: Mijn naam is gekste der henkies.
21:58:37:978: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
21:58:37:978: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:37:978: Classify master context for input sequence: Mijn naam is gekste der henkies.
21:58:37:978: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:37:978: Classified master context: Generic
21:58:37:978: Classify context for input sequence: Mijn naam is gekste der henkies.
21:58:37:978: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:37:978: Classified context: Handshake
21:58:37:978: Handling dialog: NL/Generic/Handshake
21:58:37:978:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:978:     Updated variable firstName: UN_ABC:Gekste = Gekste
21:58:37:978:     Updated variable lastName: UN_ABC:Henkies = Henkies
21:58:37:978:     Updated variable preposition: NL_PRE:6 = der
21:58:37:978:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:37:979:     Found matches for sequence: 18
21:58:37:979:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:979:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:980:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:980:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:980:     - [... 8]
21:58:37:980:     Updated variable fullName: Gekste der Henkies
21:58:37:980:     Prompt variable: nextDialog
21:58:37:980:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
21:58:37:980:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 486 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
21:58:37:980: Classify language for sequence: Mijn naam is jan de lange.
21:58:37:980: Classified language: NL
21:58:37:981: Translate sequence: Mijn naam is jan de lange.
21:58:37:982: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
21:58:37:987: Correction time limit: 700
21:58:37:987: Correcting sequence: Mijn naam is [] [] [] .
21:58:37:988: Corrected sequence: Mijn naam is jan de lange.
21:58:37:988: Translate corrected sequence: Mijn naam is jan de lange.
21:58:37:990: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
21:58:37:990: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:37:990: Classify master context for input sequence: Mijn naam is jan de lange.
21:58:37:990: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
21:58:37:990: Classified master context: Generic
21:58:37:990: Classify context for input sequence: Mijn naam is jan de lange.
21:58:37:990: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
21:58:37:990: Classified context: Handshake
21:58:37:990: Handling dialog: NL/Generic/Handshake
21:58:37:990:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:991:     Updated variable firstName: UN_ABC:Jan = Jan
21:58:37:991:     Updated variable lastName: UN_ABC:Lange = Lange
21:58:37:991:     Updated variable preposition: NL_PRE:5 = de
21:58:37:991:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
21:58:37:991:     Found matches for sequence: 18
21:58:37:991:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
21:58:37:991:     - [... 8]
21:58:37:991:     Updated variable fullName: Jan de Lange
21:58:37:991:     Prompt variable: nextDialog
21:58:37:991:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
21:58:37:991:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 11 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
21:58:37:991: Classify language for sequence: Who created you?
21:58:37:992: Classified language: EN
21:58:37:992: Translate sequence: Who created you?
21:58:37:992: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
21:58:37:993: Correction time limit: 400
21:58:37:993: Correcting sequence: Who created you ?
21:58:37:993: Corrected sequence: Who created you?
21:58:37:993: Translate corrected sequence: Who created you?
21:58:37:993: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
21:58:37:994: Checking profanity for sequence: Who created you?
21:58:37:994: Classify master context for input sequence: Who created you?
21:58:37:994: Classified master context: Generic
21:58:37:994: Classify context for input sequence: Who created you?
21:58:37:994: Classified context: QuestionAndAnswer
21:58:37:994: Handling dialog: EN/Generic/QuestionAndAnswer
21:58:37:994:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:37:994:     Find matches for sequence: Who created you?
21:58:37:994:     Found matches for sequence: 5
21:58:37:994:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.222007722007722 / 0.9126984126984129)
21:58:37:994:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.0637065637065637 / 0.2619047619047619)
21:58:37:994:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:994:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:994:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
21:58:37:994:     Set dialog output: My software was written by André van der Zee.
21:58:37:994: Handling dialog: EN/Generic/Handshake
21:58:37:994:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
21:58:37:995:     Find matches for sequence: Who created you?
21:58:37:995:     Found matches for sequence: 1
21:58:37:995:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
21:58:37:995:     Prompt variable: firstName
21:58:37:995:     Set dialog output: My name is Dyz Lecticus.
21:58:37:995:     Set dialog prompt: What is your name?
Handling the request took: 98 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
21:58:38:089: Classify language for sequence: You asshole!
21:58:38:089: Classified language: EN
21:58:38:089: Translate sequence: You asshole!
21:58:38:090: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
21:58:38:090: Correction time limit: 300
21:58:38:090: Correcting sequence: You [] !
21:58:38:090: Corrected sequence: You asshole!
21:58:38:090: Translate corrected sequence: You asshole!
21:58:38:090: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
21:58:38:090: Checking profanity for sequence: You [PRF]!
21:58:38:090: Handling dialog: EN/Generic/Profanity
21:58:38:090:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:38:090:     Find matches for sequence: You [PRF]!
21:58:38:090:     Found matches for sequence: 2
21:58:38:090:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
21:58:38:090:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
21:58:38:090:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 1 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
21:58:38:090: Classify language for sequence: Jij klootzak!
21:58:38:091: Classified language: NL
21:58:38:091: Translate sequence: Jij klootzak!
21:58:38:091: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
21:58:38:091: Correction time limit: 300
21:58:38:091: Correcting sequence: Jij [] !
21:58:38:091: Corrected sequence: Jij klootzak!
21:58:38:091: Translate corrected sequence: Jij klootzak!
21:58:38:091: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
21:58:38:091: Checking profanity for sequence: Jij [PRF]!
21:58:38:091: Handling dialog: NL/Generic/Profanity
21:58:38:091:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:38:091:     Find matches for sequence: Jij [PRF]!
21:58:38:091:     Found matches for sequence: 2
21:58:38:091:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
21:58:38:091:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
21:58:38:091:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 2 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
21:58:38:092: Classify language for sequence: Can I book a room for 5 people?
21:58:38:092: Classified language: EN
21:58:38:092: Translate sequence: Can I book a room for 5 people?
21:58:38:092: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
21:58:38:092: Correction time limit: 900
21:58:38:093: Correcting sequence: Can I book a room for [] people ?
21:58:38:093: Corrected sequence: Can I book a room for 5 people?
21:58:38:093: Translate corrected sequence: Can I book a room for 5 people?
21:58:38:093: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
21:58:38:093: Checking profanity for sequence: Can I book a room for [NUM] people?
21:58:38:093: Classify master context for input sequence: Can I book a room for 5 people?
21:58:38:093: Classify master context for classification sequence: Can I book a room for [NUM] people?
21:58:38:093: Classified master context: Room
21:58:38:093: Classify context for input sequence: Can I book a room for 5 people?
21:58:38:094: Classify context for classification sequence: Can I book a room for [NUM] people?
21:58:38:094: Classified context: Booking
21:58:38:094: Handling dialog: EN/Room/Booking
21:58:38:094:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:58:38:094:     Updated variable bookPeople: UN_NUM:5 = 5
21:58:38:094:     Find matches for sequence: Can I book a room for [NUM] people?
21:58:38:095:     Found matches for sequence: 329
21:58:38:095:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
21:58:38:095:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
21:58:38:095:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
21:58:38:095:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
21:58:38:095:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
21:58:38:095:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
21:58:38:095:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
21:58:38:095:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
21:58:38:095:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
21:58:38:095:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
21:58:38:095:     - [... 319]
21:58:38:095:     Prompt variable: bookDate
21:58:38:096:     Set dialog output: Okay.
21:58:38:096:     Set dialog prompt: On what date?
Handling the request took: 97 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
21:58:38:189: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:58:38:190: Classified language: EN
21:58:38:190: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
21:58:38:191: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
21:58:38:191: Correction time limit: 1332
21:58:38:191: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
21:58:38:192: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:58:38:192: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:58:38:194: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
21:58:38:194: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:58:38:195: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:58:38:195: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:58:38:196: Classified master context: Room
21:58:38:196: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
21:58:38:196: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:58:38:196: Classified context: Booking
21:58:38:196: Handling dialog: EN/Room/Booking
21:58:38:196:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:58:38:197:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
21:58:38:197:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
21:58:38:197:     Updated variable bookDuration: EN_DUR:02:00 = two hours
21:58:38:197:     Updated variable bookPeople: UN_NUM:12 = 12
21:58:38:197:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
21:58:38:199:     Found matches for sequence: 263
21:58:38:199:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
21:58:38:199:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
21:58:38:199:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
21:58:38:199:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
21:58:38:199:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
21:58:38:199:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
21:58:38:199:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
21:58:38:199:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
21:58:38:199:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
21:58:38:199:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
21:58:38:199:     - [... 253]
21:58:38:199:     Prompt variable: bookConfirmation
21:58:38:199:     Set dialog output: Okay.
21:58:38:199:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 100 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
21:58:38:290: Classify language for sequence: What does it cost to book a room?
21:58:38:291: Classified language: EN
21:58:38:291: Translate sequence: What does it cost to book a room?
21:58:38:293: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
21:58:38:294: Correction time limit: 900
21:58:38:294: Correcting sequence: What does it cost to book a room ?
21:58:38:294: Corrected sequence: What does it cost to book a room?
21:58:38:294: Translate corrected sequence: What does it cost to book a room?
21:58:38:296: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
21:58:38:296: Checking profanity for sequence: What does it cost to book a room?
21:58:38:296: Classify master context for input sequence: What does it cost to book a room?
21:58:38:296: Classified master context: Room
21:58:38:296: Classify context for input sequence: What does it cost to book a room?
21:58:38:297: Classified context: QuestionAndAnswer
21:58:38:297: Handling dialog: EN/Room/QuestionAndAnswer
21:58:38:297:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:38:297:     Find matches for sequence: What does it cost to book a room?
21:58:38:297:     Found matches for sequence: 4
21:58:38:297:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
21:58:38:297:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
21:58:38:297:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
21:58:38:297:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
21:58:38:297:     Set dialog output: There are no costs attached to booking a room.
21:58:38:297: Handling dialog: EN/Room/Booking
21:58:38:297:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
21:58:38:297:     Find matches for sequence: What does it cost to book a room?
21:58:38:299:     Found matches for sequence: 276
21:58:38:299:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:299:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
21:58:38:300:     - [... 266]
21:58:38:300:     Prompt variable: bookDate
21:58:38:300:     Set dialog output: Okay.
21:58:38:300:     Set dialog prompt: On what date?
Handling the request took: 11 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
21:58:38:301: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
21:58:38:302: Classified language: EN
21:58:38:302: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
21:58:38:304: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
21:58:38:305: Correction time limit: 1300
21:58:38:305: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
21:58:38:305: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:58:38:305: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:58:38:307: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
21:58:38:308: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:309: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:58:38:309: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:309: Classified master context: Generic
21:58:38:309: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
21:58:38:310: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:310: Classified context: Math
21:58:38:310: Handling dialog: EN/Generic/Math
21:58:38:310:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
21:58:38:311:     Updated variable number1: EN_NUM:10 = ten
21:58:38:311:     Updated variable number2: EN_NUM:40 = fourty
21:58:38:311:     Updated variable number3: EN_NUM:20 = twenty
21:58:38:311:     Updated variable number4: EN_NUM:3 = three
21:58:38:311:     Updated variable number5: EN_NUM:6 = six
21:58:38:311:     Updated variable operator1: EN_MTH:M = multiplied by
21:58:38:311:     Updated variable operator2: EN_MTH:D = divided by
21:58:38:311:     Updated variable operator3: EN_MTH:A = plus
21:58:38:311:     Updated variable operator4: EN_MTH:M = multiplied by
21:58:38:311:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:311:     Found matches for sequence: 0
21:58:38:311:     Calculate expression: 10 M 40 D 20 A 3 M 6
21:58:38:312:     Calculated expression: 38.0
21:58:38:312:     Updated variable exact: exactly
21:58:38:312:     Updated variable result: thirtyeight
21:58:38:312:     Prompt variable: nextDialog
21:58:38:312:     Set dialog output: Exactly thirtyeight.
21:58:38:312:     Set dialog prompt: What else can I do for you?
Handling the request took: 89 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
21:58:38:390: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:58:38:391: Classified language: NL
21:58:38:391: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
21:58:38:393: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
21:58:38:394: Correction time limit: 1200
21:58:38:394: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
21:58:38:395: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:58:38:395: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:58:38:397: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
21:58:38:398: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:399: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:58:38:399: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:399: Classified master context: Generic
21:58:38:399: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
21:58:38:400: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:400: Classified context: Math
21:58:38:400: Handling dialog: NL/Generic/Math
21:58:38:401:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
21:58:38:401:     Updated variable number1: NL_NUM:10 = tien
21:58:38:402:     Updated variable number2: NL_NUM:40 = veertig
21:58:38:402:     Updated variable number3: NL_NUM:20 = twintig
21:58:38:403:     Updated variable number4: NL_NUM:3 = drie
21:58:38:403:     Updated variable number5: NL_NUM:6 = zes
21:58:38:404:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
21:58:38:404:     Updated variable operator2: NL_MTH:D = gedeeld door
21:58:38:404:     Updated variable operator3: NL_MTH:A = plus
21:58:38:404:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
21:58:38:404:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
21:58:38:404:     Found matches for sequence: 0
21:58:38:404:     Calculate expression: 10 M 40 D 20 A 3 M 6
21:58:38:405:     Calculated expression: 38.0
21:58:38:405:     Updated variable exact: precies
21:58:38:405:     Updated variable result: achtendertig
21:58:38:405:     Prompt variable: nextDialog
21:58:38:405:     Set dialog output: Precies achtendertig.
21:58:38:405:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 16 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
21:58:38:406: Classify language for sequence: What languages do you speak?
21:58:38:407: Classified language: EN
21:58:38:407: Translate sequence: What languages do you speak?
21:58:38:408: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
21:58:38:409: Correction time limit: 600
21:58:38:409: Correcting sequence: What languages do you speak ?
21:58:38:410: Corrected sequence: What languages do you speak?
21:58:38:410: Translate corrected sequence: What languages do you speak?
21:58:38:410: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
21:58:38:411: Checking profanity for sequence: What languages do you speak?
21:58:38:411: Classify master context for input sequence: What languages do you speak?
21:58:38:411: Classified master context: Generic
21:58:38:411: Classify context for input sequence: What languages do you speak?
21:58:38:411: Classified context: Language
21:58:38:411: Handling dialog: EN/Generic/Language
21:58:38:411:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
21:58:38:412:     Updated variable languages: English and Dutch
21:58:38:412:     Find matches for sequence: What languages do you speak?
21:58:38:412:     Found matches for sequence: 6
21:58:38:412:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.5959595959595958)
21:58:38:412:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.3838383838383838)
21:58:38:412:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
21:58:38:412:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
21:58:38:412:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.3194444444444444)
21:58:38:412:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.2884615384615385 / 0.1515151515151515)
21:58:38:412:     Prompt variable: language
21:58:38:412:     Set dialog output: I speak English and Dutch.
21:58:38:412: Handling dialog: EN/Generic/QuestionAndAnswer
21:58:38:412:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
21:58:38:412:     Find matches for sequence: What languages do you speak?
21:58:38:412:     Found matches for sequence: 17
21:58:38:412:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07915057915057915 / 0.21693121693121695)
21:58:38:412:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06925675675675676 / 0.18981481481481485)
21:58:38:412:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
21:58:38:412:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
21:58:38:412:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
21:58:38:412:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
21:58:38:412:     - What is the answer? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
21:58:38:412:     - What is the question? [OUTPUT] I do not know. (0.06887007489417127 / 0.18875502008032127)
21:58:38:412:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06887007489417127 / 0.18875502008032127)
21:58:38:412:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06887007489417127 / 0.18875502008032127)
21:58:38:412:     - [... 7]
21:58:38:412:     Set dialog output: My goal is to understand and help people.
Handling the request took: 85 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
21:58:38:490: Classify language for sequence: Spreek je duits?
21:58:38:490: Classified language: NL
21:58:38:490: Translate sequence: Spreek je duits?
21:58:38:491: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
21:58:38:491: Correction time limit: 400
21:58:38:491: Correcting sequence: Spreek je [] ?
21:58:38:491: Corrected sequence: Spreek je Duits?
21:58:38:491: Translate corrected sequence: Spreek je Duits?
21:58:38:492: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
21:58:38:492: Checking profanity for sequence: Spreek je [LNG]?
21:58:38:492: Classify master context for input sequence: Spreek je Duits?
21:58:38:492: Classify master context for classification sequence: Spreek je [LNG]?
21:58:38:492: Classified master context: Generic
21:58:38:493: Classify context for input sequence: Spreek je Duits?
21:58:38:493: Classify context for classification sequence: Spreek je [LNG]?
21:58:38:493: Classified context: Language
21:58:38:493: Handling dialog: NL/Generic/Language
21:58:38:493:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:58:38:493:     Updated variable language: NL_LNG:DE = Duits
21:58:38:493:     Updated variable languages: Engels en Nederlands
21:58:38:493:     Updated variable confirmation: Nee
21:58:38:493:     Find matches for sequence: Spreek je [LNG]?
21:58:38:493:     Found matches for sequence: 5
21:58:38:493:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
21:58:38:493:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
21:58:38:493:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
21:58:38:493:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
21:58:38:493:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
21:58:38:493:     Prompt variable: 
21:58:38:493:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 3 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
21:58:38:494: Classify language for sequence: Spreek je engels?
21:58:38:494: Classified language: NL
21:58:38:494: Translate sequence: Spreek je engels?
21:58:38:495: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
21:58:38:495: Correction time limit: 400
21:58:38:495: Correcting sequence: Spreek je [] ?
21:58:38:495: Corrected sequence: Spreek je Engels?
21:58:38:495: Translate corrected sequence: Spreek je Engels?
21:58:38:496: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
21:58:38:496: Checking profanity for sequence: Spreek je [LNG]?
21:58:38:496: Classify master context for input sequence: Spreek je Engels?
21:58:38:496: Classify master context for classification sequence: Spreek je [LNG]?
21:58:38:496: Classified master context: Generic
21:58:38:496: Classify context for input sequence: Spreek je Engels?
21:58:38:496: Classify context for classification sequence: Spreek je [LNG]?
21:58:38:496: Classified context: Language
21:58:38:496: Handling dialog: NL/Generic/Language
21:58:38:497:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
21:58:38:497:     Updated variable language: NL_LNG:EN = Engels
21:58:38:497:     Updated variable languages: Engels en Nederlands
21:58:38:497:     Updated variable confirmation: Ja
21:58:38:497:     Find matches for sequence: Spreek je [LNG]?
21:58:38:497:     Found matches for sequence: 5
21:58:38:497:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
21:58:38:497:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
21:58:38:497:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
21:58:38:497:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
21:58:38:497:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
21:58:38:497:     Prompt variable: 
21:58:38:497:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 96 ms

Request prompt: '', input: 'Wat kost overboeken naar buitenland?'
Response debug log;
21:58:38:591: Classify language for sequence: Wat kost overboeken naar buitenland?
21:58:38:591: Classified language: NL
21:58:38:591: Translate sequence: Wat kost overboeken naar buitenland?
21:58:38:592: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
21:58:38:592: Correction time limit: 600
21:58:38:592: Correcting sequence: Wat kost overboeken naar buitenland ?
21:58:38:592: Corrected sequence: Wat kost overboeken naar buitenland?
21:58:38:592: Translate corrected sequence: Wat kost overboeken naar buitenland?
21:58:38:593: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
21:58:38:593: Checking profanity for sequence: Wat kost overboeken naar buitenland?
21:58:38:594: Classify master context for input sequence: Wat kost overboeken naar buitenland?
21:58:38:594: Classified master context: ForeignTransfer
21:58:38:594: Classify context for input sequence: Wat kost overboeken naar buitenland?
21:58:38:594: Classified context: QuestionAndAnswer
21:58:38:594: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
21:58:38:594:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
21:58:38:594:     Updated variable durationDays: dagen
21:58:38:594:     Updated variable cost: vijf
21:58:38:594:     Updated variable costCurrency: euro
21:58:38:594:     Updated variable duration: drie
21:58:38:594:     Find matches for sequence: Wat kost overboeken naar buitenland?
21:58:38:595:     Found matches for sequence: 22
21:58:38:595:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.9678423236514523 / 0.5032362459546925)
21:58:38:595:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6654564315352697 / 0.34600862998921245)
21:58:38:595:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6654564315352697 / 0.34600862998921245)
21:58:38:595:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6654564315352697 / 0.34600862998921245)
21:58:38:595:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6654564315352697 / 0.34600862998921245)
21:58:38:595:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
21:58:38:595:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
21:58:38:595:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
21:58:38:595:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
21:58:38:595:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
21:58:38:595:     - [... 12]
21:58:38:595:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
21:58:38:595: Handling dialog: NL/ForeignTransfer/Cost
21:58:38:595:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
Handling the request took: 5 ms

Request prompt: '', input: 'Wat kost overboeken naar italie?'
Response debug log;
21:58:38:596: Classify language for sequence: Wat kost overboeken naar italie?
21:58:38:596: Classified language: NL
21:58:38:596: Translate sequence: Wat kost overboeken naar italie?
21:58:38:597: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:italie ?
21:58:38:597: Correction time limit: 600
21:58:38:597: Correcting sequence: Wat kost overboeken naar italie ?
21:58:38:598: Corrected sequence: Wat kost overboeken naar italië?
21:58:38:598: Translate corrected sequence: Wat kost overboeken naar italië?
21:58:38:599: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italië ?
21:58:38:600: Checking profanity for sequence: Wat kost overboeken naar [CNT]?
21:58:38:600: Classify master context for input sequence: Wat kost overboeken naar italië?
21:58:38:600: Classify master context for classification sequence: Wat kost overboeken naar [CNT]?
21:58:38:600: Classified master context: ForeignTransfer
21:58:38:601: Classify context for input sequence: Wat kost overboeken naar italië?
21:58:38:601: Classify context for classification sequence: Wat kost overboeken naar [CNT]?
21:58:38:601: Classified context: Cost
21:58:38:601: Handling dialog: NL/ForeignTransfer/Cost
21:58:38:601:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
21:58:38:601:     Updated variable transferToCountry: NL_CNT:IT = Italië
21:58:38:601:     Updated variable cost: vijf
21:58:38:601:     Updated variable costCurrency: euro
21:58:38:601:     Updated variable duration: drie
21:58:38:601:     Find matches for sequence: Wat kost overboeken naar [CNT]?
21:58:38:602:     Found matches for sequence: 13
21:58:38:602:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7796391752577319 / 0.5421146953405018)
21:58:38:602:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.6752577319587628 / 0.4695340501792115)
21:58:38:602:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.6752577319587628 / 0.4695340501792115)
21:58:38:602:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
21:58:38:602:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
21:58:38:602:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
21:58:38:602:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
21:58:38:602:     - Wat zijn de kosten van een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
21:58:38:602:     - Zijn er kosten verbonden aan geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
21:58:38:602:     - Hoeveel kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
21:58:38:602:     - [... 3]
21:58:38:602:     Prompt variable: 
21:58:38:602:     Set dialog output: Een overboeking naar Italië kost vijf euro.
21:58:38:602: Handling dialog: NL/ForeignTransfer/Duration
21:58:38:602:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
21:58:38:603:     Updated variable transferToCountry: NL_CNT:IT = Italië
21:58:38:603:     Updated variable durationDays: dagen
21:58:38:603:     Updated variable cost: vijf
21:58:38:603:     Updated variable costCurrency: euro
21:58:38:603:     Updated variable duration: drie
21:58:38:603:     Find matches for sequence: Wat kost overboeken naar [CNT]?
21:58:38:603:     Found matches for sequence: 8
21:58:38:603:     - Hoeveel tijd kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.77683654106804 / 0.6042061986084756)
21:58:38:603:     - Hoelang duurt geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5798319327731092 / 0.45098039215686275)
21:58:38:603:     - Hoeveel tijd kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5163323393873678 / 0.4015918195235083)
21:58:38:603:     - Hoeveel tijd kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5163323393873678 / 0.4015918195235083)
21:58:38:603:     - Hoelang duurt een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
21:58:38:603:     - Hoelang duurt het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
21:58:38:603:     - Hoelang duurt geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
21:58:38:603:     - Hoelang duurt het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
21:58:38:603:     Prompt variable: 
21:58:38:603:     Set dialog output: Een overboeking naar Italië duurt drie dagen.
21:58:38:604: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
21:58:38:604:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferQnAHandler
21:58:38:604:     Updated variable durationDays: dagen
21:58:38:604:     Updated variable cost: vijf
21:58:38:604:     Updated variable costCurrency: euro
21:58:38:604:     Updated variable duration: drie
21:58:38:604:     Find matches for sequence: Wat kost overboeken naar [CNT]?
21:58:38:604:     Found matches for sequence: 10
21:58:38:604:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.7110995850622407 / 0.36974110032362456)
21:58:38:604:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4087136929460581 / 0.21251348435814452)
21:58:38:604:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4087136929460581 / 0.21251348435814452)
21:58:38:604:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.4087136929460581 / 0.21251348435814452)
21:58:38:604:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.4087136929460581 / 0.21251348435814452)
21:58:38:604:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
21:58:38:604:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
21:58:38:604:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
21:58:38:604:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
21:58:38:604:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
21:58:38:604:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
Handling the request took: 95 ms
~~~~

nl.zeesoft.zsd.test.TestSequenceInterpreterTester
-------------------------------------------------
This test show how to use a *SequenceInterpreterTester* to test sequence classification for a *DialogHandlerConfiguration*.

**Example implementation**  
~~~~
// Create the DialogHandlerConfiguration
DialogHandlerConfiguration config = new DialogHandlerConfiguration();
// Start the initialization (and wait until it's done)
config.initialize();
// Create the tester
SequenceInterpreterTester tester = new SequenceInterpreterTester(config);
// Initialize the tester
tester.initialize();
// Start the tester (and wait until it's done)
tester.start();
// Get the test results summary
JsFile summary = tester.getSummary();
~~~~

Class references;  
 * [TestSequenceInterpreterTester](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/test/TestSequenceInterpreterTester.java)
 * [SequenceInterpreterTester](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/interpret/SequenceInterpreterTester.java)
 * [DialogHandlerConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/src/nl/zeesoft/zsd/dialog/DialogHandlerConfiguration.java)

**Test output**  
The output of this test shows the test results summary.  
~~~~
{
  "timeStamp": "2018-08-11 21:58:54:086",
  "totals": {
    "tests": 1362,
    "successful": 1348,
    "successPercentage": 98.9721,
    "durationMs": 13960,
    "averageRequestMs": 0,
    "errorsPerDialog": [
      {
        "id": "NL/ForeignTransfer/QuestionAndAnswer",
        "errors": 8
      },
      {
        "id": "EN/ForeignTransfer/QuestionAndAnswer",
        "errors": 4
      },
      {
        "id": "NL/Generic/QuestionAndAnswer",
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
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.9697600716946488)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much does it cost to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6169040835707503) <> QuestionAndAnswer (0.599386368617138)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "Are there any costs attached to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6590693257359924) <> QuestionAndAnswer (0.656147271531887)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "What does it cost to transfer money abroad?",
      "error": "Response context does not match expected context: Cost (0.6581618655692729) <> QuestionAndAnswer (0.5967078189300413)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much does it cost to transfer abroad?",
      "error": "Response context does not match expected context: Cost (0.7190672153635117) <> QuestionAndAnswer (0.6201329534662868)"
    },
    {
      "dialog": "NL/Generic/QuestionAndAnswer",
      "input": "Wat bent u?",
      "error": "Response context does not match expected context: Handshake (0.6825609344690899) <> QuestionAndAnswer (0.5559089462271469)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat zijn de kosten van een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Cost (1.0) <> QuestionAndAnswer (0.9368822589074617)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8856837606837608)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8787349732471683)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Zijn er kosten verbonden aan geld overmaken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.963995866790089) <> QuestionAndAnswer (0.9055849299751738)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Zijn er kosten verbonden aan geld overboeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.963995866790089) <> QuestionAndAnswer (0.9055849299751738)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8856837606837608)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8787349732471683)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel tijd kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.9298519908276006) <> QuestionAndAnswer (0.907329901701384)"
    }
  ]
}
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
  "timeStamp": "2018-08-11 21:59:09:876",
  "totals": {
    "tests": 1366,
    "successful": 1348,
    "successPercentage": 98.68228,
    "durationMs": 13895,
    "averageRequestMs": 2,
    "outputTests": 153,
    "succesfulOutputTests": 147,
    "successPercentageOutputTests": 96.07843,
    "averageOutputTestRequestMs": 2,
    "errorsPerDialog": [
      {
        "id": "NL/ForeignTransfer/QuestionAndAnswer",
        "errors": 8
      },
      {
        "id": "EN/ForeignTransfer/QuestionAndAnswer",
        "errors": 4
      },
      {
        "id": "EN/Generic/Goodbye",
        "errors": 3
      },
      {
        "id": "NL/Generic/QuestionAndAnswer",
        "errors": 1
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
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.9697600716946488)"
    },
    {
      "dialog": "EN/Generic/Goodbye",
      "input": "See you later.",
      "error": "Response output does not match expected output",
      "output": "Talk to you later.",
      "expectedOutput": "Goodbye."
    },
    {
      "dialog": "EN/Generic/Goodbye",
      "input": "Talk to you later.",
      "error": "Response output does not match expected output",
      "output": "Talk to you later.",
      "expectedOutput": "Goodbye."
    },
    {
      "dialog": "EN/Generic/Goodbye",
      "input": "Later.",
      "error": "Response output does not match expected output",
      "output": "Talk to you later.",
      "expectedOutput": "Goodbye."
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much does it cost to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6169040835707503) <> QuestionAndAnswer (0.599386368617138)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "Are there any costs attached to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6590693257359924) <> QuestionAndAnswer (0.656147271531887)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "What does it cost to transfer money abroad?",
      "error": "Response context does not match expected context: Cost (0.6581618655692729) <> QuestionAndAnswer (0.5967078189300413)"
    },
    {
      "dialog": "EN/ForeignTransfer/QuestionAndAnswer",
      "input": "How much does it cost to transfer abroad?",
      "error": "Response context does not match expected context: Cost (0.7190672153635117) <> QuestionAndAnswer (0.6201329534662868)"
    },
    {
      "dialog": "NL/Generic/QuestionAndAnswer",
      "input": "Wat bent u?",
      "error": "Response context does not match expected context: Handshake (0.6825609344690899) <> QuestionAndAnswer (0.5559089462271469)"
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
      "input": "Wat zijn de kosten van een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Cost (1.0) <> QuestionAndAnswer (0.9368822589074617)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8856837606837608)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Wat kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8787349732471683)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Zijn er kosten verbonden aan geld overmaken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.963995866790089) <> QuestionAndAnswer (0.9055849299751738)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Zijn er kosten verbonden aan geld overboeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.963995866790089) <> QuestionAndAnswer (0.9055849299751738)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8856837606837608)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8787349732471683)"
    },
    {
      "dialog": "NL/ForeignTransfer/QuestionAndAnswer",
      "input": "Hoeveel tijd kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.9298519908276006) <> QuestionAndAnswer (0.907329901701384)"
    }
  ]
}
~~~~

Test results
------------
All 21 tests have been executed successfully (264 assertions).  
Total test duration: 102197 ms (total sleep duration: 58300 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 649 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 428 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 460 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 466 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 475 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117722 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117735 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117733 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 118045 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117831 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117865 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117840 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 547659 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 547660 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 547661 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 547661 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 547668 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 552603 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 552621 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreterTester: 552626 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerTester: 552652 Kb / 539 Mb
