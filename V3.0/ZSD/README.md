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
Average correction time: 341 ms
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
Initializing the SequenceClassifier took: 5763 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 3 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 7 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 3 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 7 ms

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
Initializing the SequenceMatcher took: 6496 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 19 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 134 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 3 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 84 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 50 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 28 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 33 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 54 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 31 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 44 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 115 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 295 ms

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
Initializing the EntityValueTranslator took: 1390 ms

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
Retranslating the sequence took: 1 ms
Retranslation: 'I finished twohundredandtwentyfourth or 225th'

Sequence: 'Ik ben tweehonderdvierentwintigste geworden'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 1 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 1 ms
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
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 1 ms
Translation: 'UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies UN_ABC:is UN_ABC:mijn UN_ABC:naam'
Retranslating the sequence took: 0 ms
Retranslation: 'gekste der henkies is mijn naam'

Sequence: 'Mijn naam si gekste der henkies.'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .'
Retranslating the sequence took: 0 ms
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
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak'
Retranslating the sequence took: 0 ms
Retranslation: 'Jij klootzak'

Sequence: 'Can I book a room for 5 people?'
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?'
Retranslating the sequence took: 1 ms
Retranslation: 'Can I book a room for 5 people?'

Sequence: 'ten times five'
Translating the sequence took: 0 ms
Translation: 'EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five'
Retranslating the sequence took: 1 ms
Retranslation: 'ten multiplied by five'

Sequence: 'tien keer vijf'
Translating the sequence took: 0 ms
Translation: 'NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf'
Retranslating the sequence took: 1 ms
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
Converting 333841 entity values took: 278 ms

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
Converting 1530 dialog examples took: 173 ms

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
Converting 14 dialogs took: 1 ms

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
Initializing Translator took 1717 ms
Initializing Classifier took 5364 ms
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
Initializing LanguageContextENGeneric took 41 ms
Initializing LanguageContextNLSupport took 46 ms
Initializing LanguageContextENForeignTransfer took 80 ms
Initializing LanguageContextENSupport took 85 ms
Initializing LanguageContextNLForeignTransfer took 91 ms
Initializing LanguageContextNLGeneric took 128 ms
Initializing LanguageContextENRoom took 254 ms
Initializing LanguageContextNLRoom took 260 ms
Initializing LanguageMasterContextEN took 323 ms
Initializing LanguageMasterContextNL took 356 ms
Initializing EntityValueTranslator took 1177 ms
Initializing LanguageClassifier took 27322 ms
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
'EN': 0.23069217694331634 / 0.9933012214420261
'NL': 0.07900888202051941 / 0.340191939126892

Context probabilities for 'wat is your name?', threshold: 0.6
'EN': 0.23069217694331634 / 0.9933012214420261
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
'Generic': 0.25889635942441624 / 0.8668913550898241

Classified sequence: 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wie ben jij?' -> Generic
Classifying the input sequence took: 0 ms
Context probabilities for 'Wie ben jij?', threshold: 0.0
'Generic': 0.32890639548462614 / 1.0
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
'QuestionAndAnswer': 0.3006327216024949 / 0.7344688644688644
'Handshake': 0.17457921163694595 / 0.4265104431991849

Classified sequence: 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw doel?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw doel?', threshold: 0.0
'QuestionAndAnswer': 0.3176806765997 / 0.5493783197724152
'Handshake': 0.2705805469938746 / 0.4679261195918023

Classified sequence: 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What is your name?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'What is your name?', threshold: 0.0
'Handshake': 0.4021385557242231 / 0.9824554253693327
'QuestionAndAnswer': 0.12353064651553317 / 0.3017948717948718

Classified sequence: 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat is jouw naam?' -> Handshake
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat is jouw naam?', threshold: 0.0
'Handshake': 0.5805977137060591 / 1.0
'QuestionAndAnswer': 0.1455513017549995 / 0.25069217173784497

Classified sequence: 'What are you?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'What are you?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'What are you?', threshold: 0.0
'QuestionAndAnswer': 0.3065851025548758 / 0.9362637362637362
'Handshake': 0.2341818606435685 / 0.7151553744268977
'Thanks': 0.05766839159807341 / 0.17611039588027033

Classified sequence: 'Wat ben jij?' -> Handshake
Classifying the input sequence took: 0 ms
Classified sequence (case insensitive): 'Wat ben jij?' -> QuestionAndAnswer
Classifying the input sequence took: 0 ms
Context probabilities for 'Wat ben jij?', threshold: 0.0
'QuestionAndAnswer': 0.32764745400833795 / 0.7082678496826945
'Handshake': 0.30579181459950844 / 0.6610230243737877
'Language': 0.11843154537060355 / 0.25601070586100527
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
01:19:54:391: Classify language for sequence: Hallo.
01:19:54:391: Classified language: NL
01:19:54:391: Translate sequence: Hallo.
01:19:54:391: Translated sequence: UN_ABC:Hallo .
01:19:54:391: Correction time limit: 200
01:19:54:391: Correcting sequence: Hallo .
01:19:54:391: Corrected sequence: Hallo.
01:19:54:391: Translate corrected sequence: Hallo.
01:19:54:392: Translated corrected sequence: UN_ABC:Hallo .
01:19:54:392: Checking profanity for sequence: Hallo.
01:19:54:392: Classify master context for input sequence: Hallo.
01:19:54:392: Classified master context: Generic
01:19:54:392: Classify context for input sequence: Hallo.
01:19:54:392: Classified context: Handshake
Interpreting the request took: 2 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
01:19:54:392: Classify language for sequence: Mijn naam si gekste der henkies.
01:19:54:392: Classified language: NL
01:19:54:392: Translate sequence: Mijn naam si gekste der henkies.
01:19:54:393: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
01:19:54:394: Correction time limit: 700
01:19:54:394: Correcting sequence: Mijn naam si gekste [] henkies .
01:19:54:796: Corrected sequence: Mijn naam is gekste der henkies.
01:19:54:796: Translate corrected sequence: Mijn naam is gekste der henkies.
01:19:54:797: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
01:19:54:798: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:54:798: Classify master context for input sequence: Mijn naam is gekste der henkies.
01:19:54:798: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:54:798: Classified master context: Generic
01:19:54:798: Classify context for input sequence: Mijn naam is gekste der henkies.
01:19:54:798: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:54:798: Classified context: Handshake
Interpreting the request took: 407 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
01:19:54:799: Classify language for sequence: Albert einstein.
01:19:54:799: Classify language for sequence: What is your name? Albert einstein.
01:19:54:799: Classified language: EN
01:19:54:800: Translate sequence: Albert einstein.
01:19:54:802: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:19:54:802: Correction time limit: 300
01:19:54:802: Correcting sequence: [] [] .
01:19:54:802: Corrected sequence: Albert einstein.
01:19:54:802: Translate corrected sequence: Albert einstein.
01:19:54:804: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:19:54:804: Checking profanity for sequence: [ABC] [ABC].
01:19:54:804: Classify master context for input sequence: Albert einstein.
01:19:54:804: Classify master context for classification sequence: What is your name? [ABC] [ABC].
01:19:54:805: Classified master context: Generic
01:19:54:805: Classify context for input sequence: Albert einstein.
01:19:54:805: Classify context for classification sequence: What is your name? [ABC] [ABC].
01:19:54:805: Classified context: Handshake
Interpreting the request took: 6 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
01:19:54:806: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:19:54:807: Selected primary language: EN
01:19:54:807: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:19:54:809: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
01:19:54:809: Correction time limit: 1100
01:19:54:809: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
01:19:55:909: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:19:55:909: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:19:55:910: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
01:19:55:911: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
01:19:55:911: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
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

Initializing DialogSet took 384 ms
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
01:19:58:288: Classify language for sequence: What are you?
01:19:58:289: Classified language: EN
01:19:58:289: Translate sequence: What are you?
01:19:58:290: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
01:19:58:290: Correction time limit: 400
01:19:58:290: Correcting sequence: What are you ?
01:19:58:290: Corrected sequence: What are you?
01:19:58:290: Translate corrected sequence: What are you?
01:19:58:291: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
01:19:58:291: Checking profanity for sequence: What are you?
01:19:58:291: Classify master context for input sequence: What are you?
01:19:58:291: Classified master context: Generic
01:19:58:291: Classify context for input sequence: What are you?
01:19:58:291: Classified context: QuestionAndAnswer
01:19:58:291: Handling dialog: EN/Generic/QuestionAndAnswer
01:19:58:292:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:58:292:     Find matches for sequence: What are you?
01:19:58:292:     Found matches for sequence: 10
01:19:58:292:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.21428571428571425 / 0.8809523809523809)
01:19:58:292:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
01:19:58:294:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
01:19:58:294:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
01:19:58:294:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
01:19:58:294:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
01:19:58:294:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:58:294:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:58:294:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:58:294:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:58:295:     Set dialog output: I am an artificially intelligent personal assistant.
01:19:58:295: Handling dialog: EN/Generic/Handshake
01:19:58:295:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:58:295:     Find matches for sequence: What are you?
01:19:58:295:     Found matches for sequence: 2
01:19:58:295:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
01:19:58:295:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
01:19:58:295:     Prompt variable: firstName
01:19:58:296:     Set dialog output: My name is Dyz Lecticus.
01:19:58:296:     Set dialog prompt: What is your name?
Handling the request took: 86 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
01:19:58:374: Classify language for sequence: Wat ben jij?
01:19:58:374: Classified language: NL
01:19:58:374: Translate sequence: Wat ben jij?
01:19:58:376: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
01:19:58:376: Correction time limit: 400
01:19:58:376: Correcting sequence: Wat ben jij ?
01:19:58:376: Corrected sequence: Wat ben jij?
01:19:58:376: Translate corrected sequence: Wat ben jij?
01:19:58:377: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
01:19:58:377: Checking profanity for sequence: Wat ben jij?
01:19:58:377: Classify master context for input sequence: Wat ben jij?
01:19:58:377: Classified master context: Generic
01:19:58:377: Classify context for input sequence: Wat ben jij?
01:19:58:377: Classified context: QuestionAndAnswer
01:19:58:377: Handling dialog: NL/Generic/QuestionAndAnswer
01:19:58:377:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:58:377:     Find matches for sequence: Wat ben jij?
01:19:58:378:     Found matches for sequence: 9
01:19:58:378:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.46601208459214505 / 0.9793650793650793)
01:19:58:378:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:19:58:378:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:19:58:378:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:19:58:378:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:19:58:378:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
01:19:58:378:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
01:19:58:378:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
01:19:58:378:     - Ben jij een menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
01:19:58:379:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
01:19:58:379: Handling dialog: NL/Generic/Handshake
01:19:58:379:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:58:379:     Find matches for sequence: Wat ben jij?
01:19:58:379:     Found matches for sequence: 5
01:19:58:379:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
01:19:58:379:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
01:19:58:379:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
01:19:58:379:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
01:19:58:379:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
01:19:58:379:     Prompt variable: firstName
01:19:58:379:     Set dialog output: Mijn naam is Dyz Lecticus.
01:19:58:379:     Set dialog prompt: Wat is jouw naam?
01:19:58:379: Handling dialog: NL/Generic/Language
01:19:58:380:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:19:58:380:     Updated variable languages: Engels en Nederlands
01:19:58:380:     Find matches for sequence: Wat ben jij?
01:19:58:380:     Found matches for sequence: 2
01:19:58:380:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:19:58:380:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:19:58:380:     Prompt variable: language
01:19:58:380:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 100 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
01:19:58:474: Classify language for sequence: What is the answer?
01:19:58:475: Classified language: EN
01:19:58:475: Translate sequence: What is the answer?
01:19:58:476: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
01:19:58:477: Correction time limit: 500
01:19:58:477: Correcting sequence: What is [] answer ?
01:19:58:478: Corrected sequence: What is the answer?
01:19:58:478: Translate corrected sequence: What is the answer?
01:19:58:480: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
01:19:58:480: Checking profanity for sequence: What is [PRE] answer?
01:19:58:480: Classify master context for input sequence: What is the answer?
01:19:58:480: Classify master context for classification sequence: What is [PRE] answer?
01:19:58:481: Selected input sequence master context classification.
01:19:58:481: Classified master context: Generic
01:19:58:481: Classify context for input sequence: What is the answer?
01:19:58:481: Classified context: QuestionAndAnswer
01:19:58:481: Handling dialog: EN/Generic/QuestionAndAnswer
01:19:58:481:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:58:481:     Find matches for sequence: What is the answer?
01:19:58:482:     Found matches for sequence: 9
01:19:58:482:     - What is the answer? [OUTPUT] Fourtytwo. (0.2548262548262548 / 0.8380952380952382)
01:19:58:482:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
01:19:58:482:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
01:19:58:482:     - What is the question? [OUTPUT] I do not know. (0.10424710424710423 / 0.34285714285714286)
01:19:58:482:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.10424710424710423 / 0.34285714285714286)
01:19:58:482:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.10424710424710423 / 0.34285714285714286)
01:19:58:483:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.10424710424710423 / 0.34285714285714286)
01:19:58:483:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
01:19:58:483:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
01:19:58:483:     Set dialog output: Fourtytwo.
Handling the request took: 9 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
01:19:58:484: Classify language for sequence: Wat is het antwoord?
01:19:58:484: Classified language: NL
01:19:58:484: Translate sequence: Wat is het antwoord?
01:19:58:485: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
01:19:58:486: Correction time limit: 500
01:19:58:486: Correcting sequence: Wat is het antwoord ?
01:19:58:486: Corrected sequence: Wat is het antwoord?
01:19:58:486: Translate corrected sequence: Wat is het antwoord?
01:19:58:487: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
01:19:58:488: Checking profanity for sequence: Wat is het antwoord?
01:19:58:488: Classify master context for input sequence: Wat is het antwoord?
01:19:58:488: Classified master context: Generic
01:19:58:488: Classify context for input sequence: Wat is het antwoord?
01:19:58:488: Classified context: QuestionAndAnswer
01:19:58:488: Handling dialog: NL/Generic/QuestionAndAnswer
01:19:58:488:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:58:489:     Find matches for sequence: Wat is het antwoord?
01:19:58:489:     Found matches for sequence: 10
01:19:58:489:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.6102719033232629 / 1.0)
01:19:58:490:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
01:19:58:490:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
01:19:58:490:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.297583081570997 / 0.4876237623762376)
01:19:58:490:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.14425981873111782 / 0.23638613861386137)
01:19:58:490:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
01:19:58:490:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
01:19:58:490:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.14425981873111782 / 0.23638613861386137)
01:19:58:490:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
01:19:58:490:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
01:19:58:490:     Set dialog output: Tweeenveertig.
Handling the request took: 91 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
01:19:58:574: Classify language for sequence: Hallo.
01:19:58:574: Classified language: NL
01:19:58:574: Translate sequence: Hallo.
01:19:58:575: Translated sequence: UN_ABC:Hallo .
01:19:58:575: Correction time limit: 200
01:19:58:575: Correcting sequence: Hallo .
01:19:58:575: Corrected sequence: Hallo.
01:19:58:576: Translate corrected sequence: Hallo.
01:19:58:576: Translated corrected sequence: UN_ABC:Hallo .
01:19:58:576: Checking profanity for sequence: Hallo.
01:19:58:576: Classify master context for input sequence: Hallo.
01:19:58:576: Classified master context: Generic
01:19:58:576: Classify context for input sequence: Hallo.
01:19:58:577: Classified context: Handshake
01:19:58:577: Handling dialog: NL/Generic/Handshake
01:19:58:577:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:58:577:     Find matches for sequence: Hallo.
01:19:58:577:     Found matches for sequence: 2
01:19:58:577:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
01:19:58:577:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
01:19:58:577:     Prompt variable: firstName
01:19:58:577:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
01:19:58:577:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 4 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
01:19:58:578: Classify language for sequence: Wie ben jij?
01:19:58:578: Classified language: NL
01:19:58:578: Translate sequence: Wie ben jij?
01:19:58:580: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
01:19:58:580: Correction time limit: 400
01:19:58:580: Correcting sequence: Wie ben jij ?
01:19:58:580: Corrected sequence: Wie ben jij?
01:19:58:580: Translate corrected sequence: Wie ben jij?
01:19:58:582: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
01:19:58:582: Checking profanity for sequence: Wie ben jij?
01:19:58:582: Classify master context for input sequence: Wie ben jij?
01:19:58:582: Classified master context: Generic
01:19:58:582: Classify context for input sequence: Wie ben jij?
01:19:58:582: Classified context: Handshake
01:19:58:583: Handling dialog: NL/Generic/Handshake
01:19:58:583:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:58:583:     Find matches for sequence: Wie ben jij?
01:19:58:583:     Found matches for sequence: 2
01:19:58:583:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
01:19:58:583:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
01:19:58:583:     Prompt variable: firstName
01:19:58:583:     Set dialog output: Mijn naam is Dyz Lecticus.
01:19:58:584:     Set dialog prompt: Wat is jouw naam?
01:19:58:584: Handling dialog: NL/Generic/QuestionAndAnswer
01:19:58:584:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:58:584:     Find matches for sequence: Wie ben jij?
01:19:58:584:     Found matches for sequence: 15
01:19:58:584:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.3081570996978852 / 0.6476190476190475)
01:19:58:585:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:19:58:585:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
01:19:58:585:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:19:58:585:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
01:19:58:585:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
01:19:58:585:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.15312375306390014 / 0.3218029350104822)
01:19:58:585:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
01:19:58:585:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
01:19:58:585:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
01:19:58:585:     - [... 5]
01:19:58:585:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
01:19:58:585: Handling dialog: NL/Generic/Language
01:19:58:585:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:19:58:586:     Updated variable languages: Engels en Nederlands
01:19:58:586:     Find matches for sequence: Wie ben jij?
01:19:58:586:     Found matches for sequence: 2
01:19:58:586:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:19:58:586:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
01:19:58:586:     Prompt variable: language
01:19:58:586:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 98 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
01:19:58:676: Classify language for sequence: Albert einstein.
01:19:58:677: Classify language for sequence: What is your name? Albert einstein.
01:19:58:677: Classified language: EN
01:19:58:679: Translate sequence: Albert einstein.
01:19:58:683: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:19:58:684: Correction time limit: 300
01:19:58:684: Correcting sequence: [] [] .
01:19:58:684: Corrected sequence: Albert einstein.
01:19:58:684: Translate corrected sequence: Albert einstein.
01:19:58:687: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
01:19:58:688: Checking profanity for sequence: [ABC] [ABC].
01:19:58:688: Classify master context for input sequence: Albert einstein.
01:19:58:688: Classify master context for classification sequence: What is your name? [ABC] [ABC].
01:19:58:688: Classified master context: Generic
01:19:58:688: Classify context for input sequence: Albert einstein.
01:19:58:688: Classify context for classification sequence: What is your name? [ABC] [ABC].
01:19:58:689: Classified context: Handshake
01:19:58:689: Handling dialog: EN/Generic/Handshake
01:19:58:689:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:58:689:     Updated variable firstName: UN_ABC:Albert = Albert
01:19:58:689:     Updated variable lastName: UN_ABC:Einstein = Einstein
01:19:58:689:     Find matches for sequence: [ABC] [ABC].
01:19:58:689:     Found matches for sequence: 0
01:19:58:689:     Updated variable fullName: Albert Einstein
01:19:58:689:     Prompt variable: nextDialog
01:19:58:689:     Set dialog output: 
01:19:58:689:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 13 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
01:19:58:690: Classify language for sequence: Mijn naam si gekste der henkies.
01:19:58:690: Classified language: NL
01:19:58:690: Translate sequence: Mijn naam si gekste der henkies.
01:19:58:691: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
01:19:58:692: Correction time limit: 700
01:19:58:692: Correcting sequence: Mijn naam si gekste [] henkies .
01:19:59:070: Corrected sequence: Mijn naam is gekste der henkies.
01:19:59:070: Translate corrected sequence: Mijn naam is gekste der henkies.
01:19:59:071: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
01:19:59:071: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:59:071: Classify master context for input sequence: Mijn naam is gekste der henkies.
01:19:59:071: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:59:071: Classified master context: Generic
01:19:59:071: Classify context for input sequence: Mijn naam is gekste der henkies.
01:19:59:071: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:59:071: Classified context: Handshake
01:19:59:071: Handling dialog: NL/Generic/Handshake
01:19:59:071:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:59:071:     Updated variable firstName: UN_ABC:Gekste = Gekste
01:19:59:071:     Updated variable lastName: UN_ABC:Henkies = Henkies
01:19:59:072:     Updated variable preposition: NL_PRE:6 = der
01:19:59:072:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:59:072:     Found matches for sequence: 18
01:19:59:072:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:072:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:073:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:073:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:073:     - [... 8]
01:19:59:073:     Updated variable fullName: Gekste der Henkies
01:19:59:073:     Prompt variable: nextDialog
01:19:59:073:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
01:19:59:073:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 384 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
01:19:59:073: Classify language for sequence: Mijn naam is jan de lange.
01:19:59:073: Classified language: NL
01:19:59:074: Translate sequence: Mijn naam is jan de lange.
01:19:59:076: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
01:19:59:076: Correction time limit: 700
01:19:59:076: Correcting sequence: Mijn naam is [] [] [] .
01:19:59:076: Corrected sequence: Mijn naam is jan de lange.
01:19:59:076: Translate corrected sequence: Mijn naam is jan de lange.
01:19:59:079: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
01:19:59:079: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:59:079: Classify master context for input sequence: Mijn naam is jan de lange.
01:19:59:080: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
01:19:59:080: Classified master context: Generic
01:19:59:080: Classify context for input sequence: Mijn naam is jan de lange.
01:19:59:080: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
01:19:59:080: Classified context: Handshake
01:19:59:080: Handling dialog: NL/Generic/Handshake
01:19:59:080:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:59:081:     Updated variable firstName: UN_ABC:Jan = Jan
01:19:59:081:     Updated variable lastName: UN_ABC:Lange = Lange
01:19:59:081:     Updated variable preposition: NL_PRE:5 = de
01:19:59:081:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
01:19:59:081:     Found matches for sequence: 18
01:19:59:081:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
01:19:59:081:     - [... 8]
01:19:59:081:     Updated variable fullName: Jan de Lange
01:19:59:081:     Prompt variable: nextDialog
01:19:59:081:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
01:19:59:081:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 8 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
01:19:59:082: Classify language for sequence: Who created you?
01:19:59:082: Classified language: EN
01:19:59:082: Translate sequence: Who created you?
01:19:59:082: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
01:19:59:083: Correction time limit: 400
01:19:59:083: Correcting sequence: Who created you ?
01:19:59:083: Corrected sequence: Who created you?
01:19:59:083: Translate corrected sequence: Who created you?
01:19:59:083: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
01:19:59:083: Checking profanity for sequence: Who created you?
01:19:59:084: Classify master context for input sequence: Who created you?
01:19:59:084: Classified master context: Generic
01:19:59:084: Classify context for input sequence: Who created you?
01:19:59:084: Classified context: QuestionAndAnswer
01:19:59:084: Handling dialog: EN/Generic/QuestionAndAnswer
01:19:59:084:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:59:084:     Find matches for sequence: Who created you?
01:19:59:084:     Found matches for sequence: 5
01:19:59:084:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.222007722007722 / 0.9126984126984129)
01:19:59:084:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.0637065637065637 / 0.2619047619047619)
01:19:59:084:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:59:084:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:59:084:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
01:19:59:084:     Set dialog output: My software was written by André van der Zee.
01:19:59:084: Handling dialog: EN/Generic/Handshake
01:19:59:084:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
01:19:59:084:     Find matches for sequence: Who created you?
01:19:59:084:     Found matches for sequence: 1
01:19:59:084:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
01:19:59:084:     Prompt variable: firstName
01:19:59:085:     Set dialog output: My name is Dyz Lecticus.
01:19:59:085:     Set dialog prompt: What is your name?
Handling the request took: 96 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
01:19:59:177: Classify language for sequence: You asshole!
01:19:59:178: Classified language: EN
01:19:59:178: Translate sequence: You asshole!
01:19:59:179: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
01:19:59:179: Correction time limit: 300
01:19:59:179: Correcting sequence: You [] !
01:19:59:179: Corrected sequence: You asshole!
01:19:59:179: Translate corrected sequence: You asshole!
01:19:59:180: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
01:19:59:180: Checking profanity for sequence: You [PRF]!
01:19:59:180: Handling dialog: EN/Generic/Profanity
01:19:59:180:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:59:180:     Find matches for sequence: You [PRF]!
01:19:59:181:     Found matches for sequence: 2
01:19:59:181:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
01:19:59:181:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
01:19:59:181:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 4 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
01:19:59:181: Classify language for sequence: Jij klootzak!
01:19:59:182: Classified language: NL
01:19:59:182: Translate sequence: Jij klootzak!
01:19:59:182: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
01:19:59:183: Correction time limit: 300
01:19:59:183: Correcting sequence: Jij [] !
01:19:59:183: Corrected sequence: Jij klootzak!
01:19:59:183: Translate corrected sequence: Jij klootzak!
01:19:59:183: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
01:19:59:184: Checking profanity for sequence: Jij [PRF]!
01:19:59:184: Handling dialog: NL/Generic/Profanity
01:19:59:184:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:59:184:     Find matches for sequence: Jij [PRF]!
01:19:59:184:     Found matches for sequence: 2
01:19:59:184:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
01:19:59:184:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
01:19:59:184:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 3 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
01:19:59:185: Classify language for sequence: Can I book a room for 5 people?
01:19:59:186: Classified language: EN
01:19:59:186: Translate sequence: Can I book a room for 5 people?
01:19:59:187: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
01:19:59:188: Correction time limit: 900
01:19:59:188: Correcting sequence: Can I book a room for [] people ?
01:19:59:188: Corrected sequence: Can I book a room for 5 people?
01:19:59:188: Translate corrected sequence: Can I book a room for 5 people?
01:19:59:190: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
01:19:59:190: Checking profanity for sequence: Can I book a room for [NUM] people?
01:19:59:191: Classify master context for input sequence: Can I book a room for 5 people?
01:19:59:191: Classify master context for classification sequence: Can I book a room for [NUM] people?
01:19:59:191: Classified master context: Room
01:19:59:191: Classify context for input sequence: Can I book a room for 5 people?
01:19:59:192: Classify context for classification sequence: Can I book a room for [NUM] people?
01:19:59:192: Classified context: Booking
01:19:59:192: Handling dialog: EN/Room/Booking
01:19:59:192:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
01:19:59:193:     Updated variable bookPeople: UN_NUM:5 = 5
01:19:59:193:     Find matches for sequence: Can I book a room for [NUM] people?
01:19:59:195:     Found matches for sequence: 329
01:19:59:195:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
01:19:59:195:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
01:19:59:195:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
01:19:59:195:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
01:19:59:195:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
01:19:59:195:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
01:19:59:195:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
01:19:59:195:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
01:19:59:195:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
01:19:59:196:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
01:19:59:196:     - [... 319]
01:19:59:196:     Prompt variable: bookDate
01:19:59:196:     Set dialog output: Okay.
01:19:59:196:     Set dialog prompt: On what date?
Handling the request took: 93 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
01:19:59:280: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
01:19:59:281: Classified language: EN
01:19:59:281: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
01:19:59:284: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
01:19:59:285: Correction time limit: 1332
01:19:59:285: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
01:19:59:285: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:19:59:285: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:19:59:288: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
01:19:59:289: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:19:59:289: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:19:59:289: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:19:59:290: Classified master context: Room
01:19:59:290: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
01:19:59:290: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:19:59:290: Classified context: Booking
01:19:59:290: Handling dialog: EN/Room/Booking
01:19:59:290:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
01:19:59:291:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
01:19:59:291:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
01:19:59:291:     Updated variable bookDuration: EN_DUR:02:00 = two hours
01:19:59:291:     Updated variable bookPeople: UN_NUM:12 = 12
01:19:59:291:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
01:19:59:293:     Found matches for sequence: 263
01:19:59:293:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
01:19:59:293:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
01:19:59:293:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
01:19:59:293:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
01:19:59:293:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
01:19:59:293:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
01:19:59:293:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
01:19:59:293:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
01:19:59:293:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
01:19:59:293:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
01:19:59:293:     - [... 253]
01:19:59:294:     Prompt variable: bookConfirmation
01:19:59:294:     Set dialog output: Okay.
01:19:59:294:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 101 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
01:19:59:379: Classify language for sequence: What does it cost to book a room?
01:19:59:380: Classified language: EN
01:19:59:380: Translate sequence: What does it cost to book a room?
01:19:59:380: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
01:19:59:381: Correction time limit: 900
01:19:59:381: Correcting sequence: What does it cost to book a room ?
01:19:59:381: Corrected sequence: What does it cost to book a room?
01:19:59:381: Translate corrected sequence: What does it cost to book a room?
01:19:59:382: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
01:19:59:382: Checking profanity for sequence: What does it cost to book a room?
01:19:59:382: Classify master context for input sequence: What does it cost to book a room?
01:19:59:382: Classified master context: Room
01:19:59:382: Classify context for input sequence: What does it cost to book a room?
01:19:59:382: Classified context: QuestionAndAnswer
01:19:59:382: Handling dialog: EN/Room/QuestionAndAnswer
01:19:59:382:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:59:382:     Find matches for sequence: What does it cost to book a room?
01:19:59:383:     Found matches for sequence: 4
01:19:59:383:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
01:19:59:383:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
01:19:59:383:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
01:19:59:383:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
01:19:59:383:     Set dialog output: There are no costs attached to booking a room.
01:19:59:383: Handling dialog: EN/Room/Booking
01:19:59:383:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
01:19:59:384:     Find matches for sequence: What does it cost to book a room?
01:19:59:386:     Found matches for sequence: 276
01:19:59:386:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
01:19:59:386:     - [... 266]
01:19:59:386:     Prompt variable: bookDate
01:19:59:386:     Set dialog output: Okay.
01:19:59:386:     Set dialog prompt: On what date?
Handling the request took: 8 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
01:19:59:388: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
01:19:59:389: Classified language: EN
01:19:59:389: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
01:19:59:391: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
01:19:59:392: Correction time limit: 1300
01:19:59:392: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
01:19:59:392: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:19:59:393: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:19:59:395: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
01:19:59:396: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:397: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:19:59:397: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:397: Classified master context: Generic
01:19:59:397: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
01:19:59:398: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:398: Classified context: Math
01:19:59:398: Handling dialog: EN/Generic/Math
01:19:59:398:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
01:19:59:399:     Updated variable number1: EN_NUM:10 = ten
01:19:59:399:     Updated variable number2: EN_NUM:40 = fourty
01:19:59:399:     Updated variable number3: EN_NUM:20 = twenty
01:19:59:399:     Updated variable number4: EN_NUM:3 = three
01:19:59:399:     Updated variable number5: EN_NUM:6 = six
01:19:59:399:     Updated variable operator1: EN_MTH:M = multiplied by
01:19:59:399:     Updated variable operator2: EN_MTH:D = divided by
01:19:59:399:     Updated variable operator3: EN_MTH:A = plus
01:19:59:399:     Updated variable operator4: EN_MTH:M = multiplied by
01:19:59:399:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:399:     Found matches for sequence: 0
01:19:59:399:     Calculate expression: 10 M 40 D 20 A 3 M 6
01:19:59:400:     Calculated expression: 38.0
01:19:59:400:     Updated variable exact: exactly
01:19:59:400:     Updated variable result: thirtyeight
01:19:59:400:     Prompt variable: nextDialog
01:19:59:400:     Set dialog output: Exactly thirtyeight.
01:19:59:400:     Set dialog prompt: What else can I do for you?
Handling the request took: 92 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
01:19:59:480: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
01:19:59:481: Classified language: NL
01:19:59:481: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
01:19:59:485: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
01:19:59:496: Correction time limit: 1200
01:19:59:496: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
01:19:59:496: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:19:59:496: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:19:59:498: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
01:19:59:498: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:498: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:19:59:499: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:499: Classified master context: Generic
01:19:59:499: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
01:19:59:499: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:499: Classified context: Math
01:19:59:499: Handling dialog: NL/Generic/Math
01:19:59:499:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
01:19:59:499:     Updated variable number1: NL_NUM:10 = tien
01:19:59:499:     Updated variable number2: NL_NUM:40 = veertig
01:19:59:499:     Updated variable number3: NL_NUM:20 = twintig
01:19:59:499:     Updated variable number4: NL_NUM:3 = drie
01:19:59:499:     Updated variable number5: NL_NUM:6 = zes
01:19:59:499:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
01:19:59:499:     Updated variable operator2: NL_MTH:D = gedeeld door
01:19:59:499:     Updated variable operator3: NL_MTH:A = plus
01:19:59:499:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
01:19:59:499:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
01:19:59:499:     Found matches for sequence: 0
01:19:59:499:     Calculate expression: 10 M 40 D 20 A 3 M 6
01:19:59:500:     Calculated expression: 38.0
01:19:59:500:     Updated variable exact: precies
01:19:59:500:     Updated variable result: achtendertig
01:19:59:500:     Prompt variable: nextDialog
01:19:59:500:     Set dialog output: Precies achtendertig.
01:19:59:500:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 21 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
01:19:59:500: Classify language for sequence: What languages do you speak?
01:19:59:500: Classified language: EN
01:19:59:500: Translate sequence: What languages do you speak?
01:19:59:501: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
01:19:59:501: Correction time limit: 600
01:19:59:501: Correcting sequence: What languages do you speak ?
01:19:59:502: Corrected sequence: What languages do you speak?
01:19:59:502: Translate corrected sequence: What languages do you speak?
01:19:59:502: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
01:19:59:502: Checking profanity for sequence: What languages do you speak?
01:19:59:502: Classify master context for input sequence: What languages do you speak?
01:19:59:502: Classified master context: Generic
01:19:59:502: Classify context for input sequence: What languages do you speak?
01:19:59:502: Classified context: Language
01:19:59:502: Handling dialog: EN/Generic/Language
01:19:59:502:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
01:19:59:502:     Updated variable languages: English and Dutch
01:19:59:502:     Find matches for sequence: What languages do you speak?
01:19:59:502:     Found matches for sequence: 6
01:19:59:503:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.5959595959595958)
01:19:59:503:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.3838383838383838)
01:19:59:503:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
01:19:59:503:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
01:19:59:503:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.3194444444444444)
01:19:59:503:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.2884615384615385 / 0.1515151515151515)
01:19:59:503:     Prompt variable: language
01:19:59:503:     Set dialog output: I speak English and Dutch.
01:19:59:503: Handling dialog: EN/Generic/QuestionAndAnswer
01:19:59:503:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
01:19:59:503:     Find matches for sequence: What languages do you speak?
01:19:59:503:     Found matches for sequence: 17
01:19:59:503:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07915057915057915 / 0.21693121693121695)
01:19:59:503:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06925675675675676 / 0.18981481481481485)
01:19:59:503:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
01:19:59:503:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
01:19:59:503:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
01:19:59:503:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
01:19:59:503:     - What is the answer? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
01:19:59:503:     - What is the question? [OUTPUT] I do not know. (0.06887007489417127 / 0.18875502008032127)
01:19:59:503:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06887007489417127 / 0.18875502008032127)
01:19:59:503:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06887007489417127 / 0.18875502008032127)
01:19:59:503:     - [... 7]
01:19:59:503:     Set dialog output: My goal is to understand and help people.
Handling the request took: 80 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
01:19:59:580: Classify language for sequence: Spreek je duits?
01:19:59:581: Classified language: NL
01:19:59:581: Translate sequence: Spreek je duits?
01:19:59:582: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
01:19:59:582: Correction time limit: 400
01:19:59:582: Correcting sequence: Spreek je [] ?
01:19:59:582: Corrected sequence: Spreek je Duits?
01:19:59:582: Translate corrected sequence: Spreek je Duits?
01:19:59:583: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
01:19:59:583: Checking profanity for sequence: Spreek je [LNG]?
01:19:59:583: Classify master context for input sequence: Spreek je Duits?
01:19:59:583: Classify master context for classification sequence: Spreek je [LNG]?
01:19:59:583: Classified master context: Generic
01:19:59:583: Classify context for input sequence: Spreek je Duits?
01:19:59:584: Classify context for classification sequence: Spreek je [LNG]?
01:19:59:584: Classified context: Language
01:19:59:584: Handling dialog: NL/Generic/Language
01:19:59:584:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:19:59:584:     Updated variable language: NL_LNG:DE = Duits
01:19:59:584:     Updated variable languages: Engels en Nederlands
01:19:59:584:     Updated variable confirmation: Nee
01:19:59:584:     Find matches for sequence: Spreek je [LNG]?
01:19:59:584:     Found matches for sequence: 5
01:19:59:584:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
01:19:59:585:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:19:59:585:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:19:59:585:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:19:59:585:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:19:59:585:     Prompt variable: 
01:19:59:585:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 5 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
01:19:59:585: Classify language for sequence: Spreek je engels?
01:19:59:585: Classified language: NL
01:19:59:586: Translate sequence: Spreek je engels?
01:19:59:586: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
01:19:59:587: Correction time limit: 400
01:19:59:587: Correcting sequence: Spreek je [] ?
01:19:59:587: Corrected sequence: Spreek je Engels?
01:19:59:587: Translate corrected sequence: Spreek je Engels?
01:19:59:588: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
01:19:59:588: Checking profanity for sequence: Spreek je [LNG]?
01:19:59:588: Classify master context for input sequence: Spreek je Engels?
01:19:59:588: Classify master context for classification sequence: Spreek je [LNG]?
01:19:59:588: Classified master context: Generic
01:19:59:588: Classify context for input sequence: Spreek je Engels?
01:19:59:588: Classify context for classification sequence: Spreek je [LNG]?
01:19:59:588: Classified context: Language
01:19:59:589: Handling dialog: NL/Generic/Language
01:19:59:589:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
01:19:59:589:     Updated variable language: NL_LNG:EN = Engels
01:19:59:589:     Updated variable languages: Engels en Nederlands
01:19:59:589:     Updated variable confirmation: Ja
01:19:59:589:     Find matches for sequence: Spreek je [LNG]?
01:19:59:589:     Found matches for sequence: 5
01:19:59:589:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
01:19:59:589:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:19:59:589:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
01:19:59:589:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:19:59:589:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
01:19:59:589:     Prompt variable: 
01:19:59:589:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 97 ms

Request prompt: '', input: 'Wat kost overboeken naar buitenland?'
Response debug log;
01:19:59:683: Classify language for sequence: Wat kost overboeken naar buitenland?
01:19:59:684: Classified language: NL
01:19:59:684: Translate sequence: Wat kost overboeken naar buitenland?
01:19:59:685: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
01:19:59:686: Correction time limit: 600
01:19:59:686: Correcting sequence: Wat kost overboeken naar buitenland ?
01:19:59:686: Corrected sequence: Wat kost overboeken naar buitenland?
01:19:59:686: Translate corrected sequence: Wat kost overboeken naar buitenland?
01:19:59:687: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
01:19:59:688: Checking profanity for sequence: Wat kost overboeken naar buitenland?
01:19:59:688: Classify master context for input sequence: Wat kost overboeken naar buitenland?
01:19:59:688: Classified master context: ForeignTransfer
01:19:59:688: Classify context for input sequence: Wat kost overboeken naar buitenland?
01:19:59:688: Classified context: QuestionAndAnswer
01:19:59:688: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
01:19:59:689:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
01:19:59:689:     Updated variable durationDays: dagen
01:19:59:689:     Updated variable cost: vijf
01:19:59:689:     Updated variable costCurrency: euro
01:19:59:689:     Updated variable duration: drie
01:19:59:689:     Find matches for sequence: Wat kost overboeken naar buitenland?
01:19:59:690:     Found matches for sequence: 22
01:19:59:690:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.9678423236514523 / 0.5032362459546925)
01:19:59:690:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6654564315352697 / 0.34600862998921245)
01:19:59:690:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6654564315352697 / 0.34600862998921245)
01:19:59:690:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6654564315352697 / 0.34600862998921245)
01:19:59:690:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6654564315352697 / 0.34600862998921245)
01:19:59:690:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
01:19:59:690:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
01:19:59:690:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
01:19:59:690:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
01:19:59:690:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
01:19:59:690:     - [... 12]
01:19:59:690:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
01:19:59:690: Handling dialog: NL/ForeignTransfer/Cost
01:19:59:690:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
01:19:59:691:     Updated variable cost: vijf
01:19:59:691:     Updated variable costCurrency: euro
01:19:59:691:     Updated variable duration: drie
01:19:59:691:     Find matches for sequence: Wat kost overboeken naar buitenland?
01:19:59:691:     Found matches for sequence: 7
01:19:59:691:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.49355670103092775 / 0.3431899641577061)
01:19:59:691:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2938144329896907 / 0.20430107526881722)
01:19:59:691:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2938144329896907 / 0.20430107526881722)
01:19:59:691:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
01:19:59:691:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
01:19:59:691:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
01:19:59:691:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
01:19:59:691:     Prompt variable: transferToCountry
01:19:59:691:     Set dialog output: Een overboeking naar  kost vijf euro.
Handling the request took: 101 ms

Request prompt: '', input: 'Wat kost overboeken naar italie?'
Response debug log;
01:19:59:784: Classify language for sequence: Wat kost overboeken naar italie?
01:19:59:784: Classified language: NL
01:19:59:784: Translate sequence: Wat kost overboeken naar italie?
01:19:59:785: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:italie ?
01:19:59:786: Correction time limit: 600
01:19:59:786: Correcting sequence: Wat kost overboeken naar italie ?
01:19:59:787: Corrected sequence: Wat kost overboeken naar italië?
01:19:59:787: Translate corrected sequence: Wat kost overboeken naar italië?
01:19:59:788: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italië ?
01:19:59:789: Checking profanity for sequence: Wat kost overboeken naar [CNT]?
01:19:59:789: Classify master context for input sequence: Wat kost overboeken naar italië?
01:19:59:789: Classify master context for classification sequence: Wat kost overboeken naar [CNT]?
01:19:59:790: Classified master context: ForeignTransfer
01:19:59:790: Classify context for input sequence: Wat kost overboeken naar italië?
01:19:59:790: Classify context for classification sequence: Wat kost overboeken naar [CNT]?
01:19:59:790: Classified context: Cost
01:19:59:790: Handling dialog: NL/ForeignTransfer/Cost
01:19:59:790:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
01:19:59:790:     Updated variable transferToCountry: NL_CNT:IT = Italië
01:19:59:790:     Updated variable cost: vijf
01:19:59:790:     Updated variable costCurrency: euro
01:19:59:790:     Updated variable duration: drie
01:19:59:790:     Find matches for sequence: Wat kost overboeken naar [CNT]?
01:19:59:791:     Found matches for sequence: 13
01:19:59:791:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7796391752577319 / 0.5421146953405018)
01:19:59:791:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.6752577319587628 / 0.4695340501792115)
01:19:59:791:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.6752577319587628 / 0.4695340501792115)
01:19:59:791:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
01:19:59:791:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
01:19:59:791:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
01:19:59:791:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
01:19:59:791:     - Wat zijn de kosten van een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
01:19:59:791:     - Zijn er kosten verbonden aan geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
01:19:59:791:     - Hoeveel kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
01:19:59:791:     - [... 3]
01:19:59:791:     Prompt variable: 
01:19:59:791:     Set dialog output: Een overboeking naar Italië kost vijf euro.
01:19:59:791: Handling dialog: NL/ForeignTransfer/Duration
01:19:59:791:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
01:19:59:791:     Updated variable transferToCountry: NL_CNT:IT = Italië
01:19:59:791:     Updated variable durationDays: dagen
01:19:59:791:     Updated variable cost: vijf
01:19:59:791:     Updated variable costCurrency: euro
01:19:59:791:     Updated variable duration: drie
01:19:59:791:     Find matches for sequence: Wat kost overboeken naar [CNT]?
01:19:59:792:     Found matches for sequence: 8
01:19:59:792:     - Hoeveel tijd kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.77683654106804 / 0.6042061986084756)
01:19:59:792:     - Hoelang duurt geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5798319327731092 / 0.45098039215686275)
01:19:59:792:     - Hoeveel tijd kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5163323393873678 / 0.4015918195235083)
01:19:59:792:     - Hoeveel tijd kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5163323393873678 / 0.4015918195235083)
01:19:59:792:     - Hoelang duurt een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
01:19:59:792:     - Hoelang duurt het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
01:19:59:792:     - Hoelang duurt geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
01:19:59:792:     - Hoelang duurt het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
01:19:59:792:     Prompt variable: 
01:19:59:792:     Set dialog output: Een overboeking naar Italië duurt drie dagen.
01:19:59:792: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
01:19:59:792:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
01:19:59:792:     Updated variable durationDays: dagen
01:19:59:792:     Updated variable cost: vijf
01:19:59:792:     Updated variable costCurrency: euro
01:19:59:792:     Updated variable duration: drie
01:19:59:792:     Find matches for sequence: Wat kost overboeken naar [CNT]?
01:19:59:793:     Found matches for sequence: 10
01:19:59:793:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.7110995850622407 / 0.36974110032362456)
01:19:59:793:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4087136929460581 / 0.21251348435814452)
01:19:59:793:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4087136929460581 / 0.21251348435814452)
01:19:59:793:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.4087136929460581 / 0.21251348435814452)
01:19:59:793:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.4087136929460581 / 0.21251348435814452)
01:19:59:793:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
01:19:59:793:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
01:19:59:793:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
01:19:59:793:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
01:19:59:793:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
01:19:59:793:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
Handling the request took: 101 ms
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
  "timeStamp": "2018-08-11 01:20:16:239",
  "totals": {
    "tests": 1480,
    "durationMs": 15199,
    "successful": 1466,
    "successPercentage": 99,
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
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.9697446021331779)"
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
      "error": "Response context does not match expected context: Handshake (0.679290714345956) <> QuestionAndAnswer (0.5545412497264607)"
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
All 20 tests have been executed successfully (263 assertions).  
Total test duration: 81799 ms (total sleep duration: 41500 ms).  

Memory usage per test;  
 * nl.zeesoft.zsd.test.TestTsvToJson: 619 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequencePreprocessor: 425 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSymbolCorrector: 465 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceClassifier: 463 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestSequenceMatcher: 472 Kb / 0 Mb
 * nl.zeesoft.zsd.test.TestEntityValueTranslator: 117720 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestBaseConfigurationToJson: 117733 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestEntityToJson: 117731 Kb / 114 Mb
 * nl.zeesoft.zsd.test.TestDialogToJson: 118023 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestDialogSetToJson: 117825 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestRequestResponseToJson: 117859 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInitializer: 117835 Kb / 115 Mb
 * nl.zeesoft.zsd.test.TestInterpreterConfiguration: 547604 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 547605 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 547605 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 547606 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 547613 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 552418 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 552435 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreterTester: 552439 Kb / 539 Mb
