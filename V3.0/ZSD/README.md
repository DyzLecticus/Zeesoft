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
Average correction time: 258 ms
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
Initializing the SequenceClassifier took: 3417 ms

Classified sequence: 'Wat kost dat?' -> nlPrivatebankingBetalen
Classifying the input sequence took: 2 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPriveKinderenengeldzaken
Classifying the input sequence took: 3 ms

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
Initializing the SequenceMatcher took: 6621 ms

'Heb' -> 'Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over, dan hoef je niets te regelen.'
Matching the input sequence took: 18 ms

'Wat kost dat?' -> 'Zorg thuis: wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.'
Matching the input sequence took: 154 ms

'Wat kost dat?' (nlPrivatebankingBetalen) -> 'Wat kost de Betaalpas? [OUTPUT] Als u een betaalpakket heeft bij ABN AMRO, betaalt u niets voor uw Betaalpas.'
Matching the input sequence took: 4 ms

'geld over?' -> 'Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand, maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen, hypotheek aflossen, pensioen aanvullen en schenken.'
Matching the input sequence took: 108 ms

'Waar kan ik mijn transacties zien?' -> 'Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties, uw limiet, het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.'
Matching the input sequence took: 41 ms

'overboeken' -> 'Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.'
Matching the input sequence took: 34 ms

'Hypotheek berekenen' -> 'Had u voor deze datum al een Bankspaar Hypotheek of een Kapitaalverzekering Eigen Woning die is gekoppeld aan een hypotheek? [OUTPUT] Dan kunt u deze Bankspaar Hypotheek of verzekering wel fiscaal geruisloos voortzetten in een nieuwe Bankspaar Hypotheek.'
Matching the input sequence took: 43 ms

'Hypotheek berekenen' (case insensitive) -> 'Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.'
Matching the input sequence took: 87 ms

'Fraude' -> 'Fraude herkennen: wat doen criminelen? [OUTPUT] Maar het is belangrijk dat u ook zelf weet, wat u wel of juist niet moet doen.'
Matching the input sequence took: 38 ms

'Fraude' (case insensitive) -> 'Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder (bijvoorbeeld de CEO of de CFO) uit uw organisatie, om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.'
Matching the input sequence took: 44 ms

'Heeft de abn amro rechtsbijstandverzekering' -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 138 ms

'Heeft de abn amro rechtsbijstandverzekering' (case insensitive) -> 'Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.'
Matching the input sequence took: 352 ms

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
Initializing the EntityValueTranslator took: 1872 ms

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
Translating the sequence took: 3 ms
Translation: 'UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste|NL_NAM:firstName:UN_ABC:Tweehonderdvierentwintigste UN_ABC:geworden|NL_NAM:lastName:UN_ABC:Geworden'
Retranslating the sequence took: 1 ms
Retranslation: 'Ik ben tweehonderdvierentwintigste geworden'

Sequence: 'februari march october december'
Translating the sequence took: 0 ms
Translation: 'NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december'
Retranslating the sequence took: 1 ms
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
Retranslating the sequence took: 1 ms
Retranslation: 'twelve o'clock OR fiftyfive past eight OR ten past one in the morning'

Sequence: 'twaalf uur OF vijf minuten voor negen OF tien over een sochtends'
Translating the sequence took: 1 ms
Translation: 'NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00'
Retranslating the sequence took: 0 ms
Retranslation: 'twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends'

Sequence: 'My name is Andrew from the Sea'
Translating the sequence took: 1 ms
Translation: 'UN_ABC:My UN_ABC:name UN_ABC:is UN_ABC:Andrew|EN_NAM:firstName:UN_ABC:Andrew EN_PRE:4|EN_NAM:preposition:EN_PRE:4 UN_ABC:Sea|EN_NAM:lastName:UN_ABC:Sea'
Retranslating the sequence took: 1 ms
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
Translating the sequence took: 2 ms
Translation: 'UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .'
Retranslating the sequence took: 1 ms
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
  "selfTestBaseLineFileName": testFileName.json,
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
Converting 333841 entity values took: 461 ms

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
Converting 1530 dialog examples took: 124 ms

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
Initializing Translator took 2105 ms
Initializing Classifier took 4383 ms
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

Initializing LanguageContextENSupport took 6 ms
Initializing LanguageContextNLSupport took 44 ms
Initializing LanguagePreprocessor took 81 ms
Initializing LanguageContextNLForeignTransfer took 139 ms
Initializing LanguageContextENForeignTransfer took 215 ms
Initializing LanguageContextENGeneric took 218 ms
Initializing LanguageContextNLGeneric took 373 ms
Initializing LanguageContextNLRoom took 409 ms
Initializing LanguageContextENRoom took 454 ms
Initializing LanguageMasterContextNL took 484 ms
Initializing LanguageMasterContextEN took 490 ms
Initializing EntityValueTranslator took 2630 ms
Initializing LanguageClassifier took 35701 ms
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
Classifying the input sequence took: 1 ms
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
19:36:22:174: Classify language for sequence: Hallo.
19:36:22:174: Classified language: NL
19:36:22:174: Translate sequence: Hallo.
19:36:22:175: Translated sequence: UN_ABC:Hallo .
19:36:22:175: Correction time limit: 200
19:36:22:175: Correcting sequence: Hallo .
19:36:22:175: Corrected sequence: Hallo.
19:36:22:175: Translate corrected sequence: Hallo.
19:36:22:176: Translated corrected sequence: UN_ABC:Hallo .
19:36:22:176: Checking profanity for sequence: Hallo.
19:36:22:176: Classify master context for input sequence: Hallo.
19:36:22:176: Classified master context: Generic
19:36:22:176: Classify context for input sequence: Hallo.
19:36:22:176: Classified context: Handshake
Interpreting the request took: 3 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
19:36:22:176: Classify language for sequence: Mijn naam si gekste der henkies.
19:36:22:176: Classified language: NL
19:36:22:176: Translate sequence: Mijn naam si gekste der henkies.
19:36:22:177: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
19:36:22:178: Correction time limit: 700
19:36:22:178: Correcting sequence: Mijn naam si gekste [] henkies .
19:36:22:611: Corrected sequence: Mijn naam is gekste der henkies.
19:36:22:611: Translate corrected sequence: Mijn naam is gekste der henkies.
19:36:22:612: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
19:36:22:613: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:22:613: Classify master context for input sequence: Mijn naam is gekste der henkies.
19:36:22:613: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:22:613: Classified master context: Generic
19:36:22:613: Classify context for input sequence: Mijn naam is gekste der henkies.
19:36:22:613: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:22:613: Classified context: Handshake
Interpreting the request took: 437 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
19:36:22:613: Classify language for sequence: Albert einstein.
19:36:22:613: Classify language for sequence: What is your name? Albert einstein.
19:36:22:614: Classified language: EN
19:36:22:618: Translate sequence: Albert einstein.
19:36:22:621: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
19:36:22:621: Correction time limit: 300
19:36:22:621: Correcting sequence: [] [] .
19:36:22:621: Corrected sequence: Albert einstein.
19:36:22:621: Translate corrected sequence: Albert einstein.
19:36:22:622: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
19:36:22:623: Checking profanity for sequence: [ABC] [ABC].
19:36:22:623: Classify master context for input sequence: Albert einstein.
19:36:22:623: Classify master context for classification sequence: What is your name? [ABC] [ABC].
19:36:22:623: Classified master context: Generic
19:36:22:623: Classify context for input sequence: Albert einstein.
19:36:22:623: Classify context for classification sequence: What is your name? [ABC] [ABC].
19:36:22:623: Classified context: Handshake
Interpreting the request took: 10 ms

Request prompt: '', input: 'Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.'
Response debug log;
19:36:22:624: Classify language for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
19:36:22:624: Selected primary language: EN
19:36:22:624: Translate sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
19:36:22:631: Translated sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
19:36:22:632: Correction time limit: 1100
19:36:22:632: Correcting sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv .
19:36:23:732: Corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
19:36:23:732: Translate corrected sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
19:36:23:734: Translated corrected sequence: UN_ABC:Wruio UN_ABC:wwtiop UN_ABC:wtwrpoi UN_ABC:weptiwpipw UN_ABC:ipwopkm UN_ABC:eopipwqwrqqiop UN_ABC:qwerqwer UN_ABC:qrqpoqe UN_ABC:qpxnxc UN_ABC:qwpgsjkdbvhsdfkljjv .
19:36:23:734: Checking profanity for sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
19:36:23:735: Classify master context for input sequence: Wruio wwtiop wtwrpoi weptiwpipw ipwopkm eopipwqwrqqiop qwerqwer qrqpoqe qpxnxc qwpgsjkdbvhsdfkljjv.
Interpreting the request took: 1113 ms
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

Initializing DialogSet took 410 ms
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
19:36:26:560: Classify language for sequence: What are you?
19:36:26:561: Classified language: EN
19:36:26:561: Translate sequence: What are you?
19:36:26:562: Translated sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
19:36:26:562: Correction time limit: 400
19:36:26:562: Correcting sequence: What are you ?
19:36:26:564: Corrected sequence: What are you?
19:36:26:564: Translate corrected sequence: What are you?
19:36:26:565: Translated corrected sequence: UN_ABC:What UN_ABC:are UN_ABC:you ?
19:36:26:565: Checking profanity for sequence: What are you?
19:36:26:565: Classify master context for input sequence: What are you?
19:36:26:565: Classified master context: Generic
19:36:26:565: Classify context for input sequence: What are you?
19:36:26:565: Classified context: QuestionAndAnswer
19:36:26:566: Handling dialog: EN/Generic/QuestionAndAnswer
19:36:26:566:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:26:567:     Find matches for sequence: What are you?
19:36:26:567:     Found matches for sequence: 10
19:36:26:567:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.21428571428571425 / 0.8809523809523809)
19:36:26:567:     - What are your goals? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
19:36:26:570:     - What are your priorities? [OUTPUT] My goal is to understand and help people. (0.07142857142857142 / 0.29365079365079366)
19:36:26:570:     - Are you a robot? [OUTPUT] Yes. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
19:36:26:570:     - Are you a human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
19:36:26:570:     - Are you human? [OUTPUT] No. I am better. I am an artificially intelligent personal assistant. (0.06925675675675676 / 0.28472222222222227)
19:36:26:570:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:26:570:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:26:570:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:26:570:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:26:570:     Set dialog output: I am an artificially intelligent personal assistant.
19:36:26:570: Handling dialog: EN/Generic/Handshake
19:36:26:570:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:26:571:     Find matches for sequence: What are you?
19:36:26:571:     Found matches for sequence: 2
19:36:26:571:     - Who are you? [OUTPUT] My name is {selfName}. (0.9278350515463918 / 0.6521739130434783)
19:36:26:571:     - What is your name? [OUTPUT] My name is {selfName}. (0.4697529663489594 / 0.33018867924528306)
19:36:26:571:     Prompt variable: firstName
19:36:26:571:     Set dialog output: My name is Dyz Lecticus.
19:36:26:571:     Set dialog prompt: What is your name?
Handling the request took: 22 ms

Request prompt: '', input: 'Wat ben jij?'
Response debug log;
19:36:26:582: Classify language for sequence: Wat ben jij?
19:36:26:582: Classified language: NL
19:36:26:582: Translate sequence: Wat ben jij?
19:36:26:583: Translated sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
19:36:26:584: Correction time limit: 400
19:36:26:584: Correcting sequence: Wat ben jij ?
19:36:26:585: Corrected sequence: Wat ben jij?
19:36:26:585: Translate corrected sequence: Wat ben jij?
19:36:26:586: Translated corrected sequence: UN_ABC:Wat UN_ABC:ben UN_ABC:jij ?
19:36:26:586: Checking profanity for sequence: Wat ben jij?
19:36:26:586: Classify master context for input sequence: Wat ben jij?
19:36:26:586: Classified master context: Generic
19:36:26:587: Classify context for input sequence: Wat ben jij?
19:36:26:587: Classified context: QuestionAndAnswer
19:36:26:587: Handling dialog: NL/Generic/QuestionAndAnswer
19:36:26:587:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:26:587:     Find matches for sequence: Wat ben jij?
19:36:26:587:     Found matches for sequence: 9
19:36:26:588:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.46601208459214505 / 0.9793650793650793)
19:36:26:588:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
19:36:26:588:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
19:36:26:588:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
19:36:26:588:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
19:36:26:588:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
19:36:26:588:     - Ben jij een robot? [OUTPUT] Ja. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
19:36:26:588:     - Ben jij een mens? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
19:36:26:588:     - Ben jij een menselijk? [OUTPUT] Nee. Ik ben beter. Ik ben een kunstmatig intelligente persoonlijk assistent. (0.1354796072507553 / 0.28472222222222227)
19:36:26:588:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
19:36:26:588: Handling dialog: NL/Generic/Handshake
19:36:26:588:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:26:588:     Find matches for sequence: Wat ben jij?
19:36:26:588:     Found matches for sequence: 5
19:36:26:588:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.054263565891473 / 0.6476190476190478)
19:36:26:588:     - Wat is uw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
19:36:26:588:     - Wat is jouw naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
19:36:26:588:     - Wat is je naam? [OUTPUT] Mijn naam is {selfName}. (0.5217650566487775 / 0.3205128205128205)
19:36:26:589:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
19:36:26:589:     Prompt variable: firstName
19:36:26:589:     Set dialog output: Mijn naam is Dyz Lecticus.
19:36:26:589:     Set dialog prompt: Wat is jouw naam?
19:36:26:589: Handling dialog: NL/Generic/Language
19:36:26:589:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
19:36:26:589:     Updated variable languages: Engels en Nederlands
19:36:26:589:     Find matches for sequence: Wat ben jij?
19:36:26:589:     Found matches for sequence: 2
19:36:26:589:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
19:36:26:589:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
19:36:26:589:     Prompt variable: language
19:36:26:589:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 100 ms

Request prompt: '', input: 'What is the answer?'
Response debug log;
19:36:26:682: Classify language for sequence: What is the answer?
19:36:26:682: Classified language: EN
19:36:26:682: Translate sequence: What is the answer?
19:36:26:683: Translated sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
19:36:26:683: Correction time limit: 500
19:36:26:683: Correcting sequence: What is [] answer ?
19:36:26:683: Corrected sequence: What is the answer?
19:36:26:683: Translate corrected sequence: What is the answer?
19:36:26:684: Translated corrected sequence: UN_ABC:What UN_ABC:is EN_PRE:5|UN_ABC:the UN_ABC:answer ?
19:36:26:684: Checking profanity for sequence: What is [PRE] answer?
19:36:26:684: Classify master context for input sequence: What is the answer?
19:36:26:684: Classify master context for classification sequence: What is [PRE] answer?
19:36:26:684: Selected input sequence master context classification.
19:36:26:684: Classified master context: Generic
19:36:26:684: Classify context for input sequence: What is the answer?
19:36:26:684: Classified context: QuestionAndAnswer
19:36:26:684: Handling dialog: EN/Generic/QuestionAndAnswer
19:36:26:684:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:26:684:     Find matches for sequence: What is the answer?
19:36:26:685:     Found matches for sequence: 9
19:36:26:685:     - What is the answer? [OUTPUT] Fourtytwo. (0.2548262548262548 / 0.8380952380952382)
19:36:26:685:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
19:36:26:685:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.17567567567567566 / 0.5777777777777778)
19:36:26:685:     - What is the question? [OUTPUT] I do not know. (0.10424710424710423 / 0.34285714285714286)
19:36:26:685:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.10424710424710423 / 0.34285714285714286)
19:36:26:685:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.10424710424710423 / 0.34285714285714286)
19:36:26:685:     - What is the purpose of your existence? [OUTPUT] My goal is to understand and help people. (0.10424710424710423 / 0.34285714285714286)
19:36:26:685:     - What is your goal? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
19:36:26:685:     - What is your purpose? [OUTPUT] My goal is to understand and help people. (0.048262548262548256 / 0.15873015873015875)
19:36:26:685:     Set dialog output: Fourtytwo.
Handling the request took: 3 ms

Request prompt: '', input: 'Wat is het antwoord?'
Response debug log;
19:36:26:686: Classify language for sequence: Wat is het antwoord?
19:36:26:686: Classified language: NL
19:36:26:686: Translate sequence: Wat is het antwoord?
19:36:26:687: Translated sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
19:36:26:687: Correction time limit: 500
19:36:26:687: Correcting sequence: Wat is het antwoord ?
19:36:26:687: Corrected sequence: Wat is het antwoord?
19:36:26:687: Translate corrected sequence: Wat is het antwoord?
19:36:26:688: Translated corrected sequence: UN_ABC:Wat UN_ABC:is UN_ABC:het UN_ABC:antwoord ?
19:36:26:688: Checking profanity for sequence: Wat is het antwoord?
19:36:26:688: Classify master context for input sequence: Wat is het antwoord?
19:36:26:688: Classified master context: Generic
19:36:26:688: Classify context for input sequence: Wat is het antwoord?
19:36:26:688: Classified context: QuestionAndAnswer
19:36:26:688: Handling dialog: NL/Generic/QuestionAndAnswer
19:36:26:688:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:26:688:     Find matches for sequence: Wat is het antwoord?
19:36:26:689:     Found matches for sequence: 10
19:36:26:689:     - Wat is het antwoord? [OUTPUT] Tweeenveertig. (0.6102719033232629 / 1.0)
19:36:26:689:     - Wat is het antwoord op de ultieme vraag van het leven, het universum en alles? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
19:36:26:689:     - Wat is het antwoord op de vraag? [OUTPUT] Tweeenveertig. (0.45241691842900306 / 0.7413366336633663)
19:36:26:689:     - Wat is het doel van jouw bestaan? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.297583081570997 / 0.4876237623762376)
19:36:26:689:     - Wat is de vraag? [OUTPUT] Dat weet ik niet. (0.14425981873111782 / 0.23638613861386137)
19:36:26:689:     - Wat is de zin van het leven? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
19:36:26:689:     - Wat is de zin van het bestaan? [OUTPUT] Het leven heeft geen zin. (0.14425981873111782 / 0.23638613861386137)
19:36:26:689:     - Wat is de betekenis van het leven? [OUTPUT] Het leven heeft geen betekenis. (0.14425981873111782 / 0.23638613861386137)
19:36:26:689:     - Wat is je doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
19:36:26:689:     - Wat is jouw doel? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.14425981873111782 / 0.23638613861386137)
19:36:26:689:     Set dialog output: Tweeenveertig.
Handling the request took: 97 ms

Request prompt: '', input: 'Hallo.'
Response debug log;
19:36:26:782: Classify language for sequence: Hallo.
19:36:26:782: Classified language: NL
19:36:26:782: Translate sequence: Hallo.
19:36:26:782: Translated sequence: UN_ABC:Hallo .
19:36:26:782: Correction time limit: 200
19:36:26:782: Correcting sequence: Hallo .
19:36:26:782: Corrected sequence: Hallo.
19:36:26:782: Translate corrected sequence: Hallo.
19:36:26:783: Translated corrected sequence: UN_ABC:Hallo .
19:36:26:783: Checking profanity for sequence: Hallo.
19:36:26:783: Classify master context for input sequence: Hallo.
19:36:26:783: Classified master context: Generic
19:36:26:783: Classify context for input sequence: Hallo.
19:36:26:783: Classified context: Handshake
19:36:26:783: Handling dialog: NL/Generic/Handshake
19:36:26:783:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:26:783:     Find matches for sequence: Hallo.
19:36:26:783:     Found matches for sequence: 2
19:36:26:783:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
19:36:26:783:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5038759689922481 / 0.6190476190476192)
19:36:26:783:     Prompt variable: firstName
19:36:26:783:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
19:36:26:783:     Set dialog prompt: Wat is jouw naam?
Handling the request took: 1 ms

Request prompt: '', input: 'Wie ben jij?'
Response debug log;
19:36:26:784: Classify language for sequence: Wie ben jij?
19:36:26:784: Classified language: NL
19:36:26:784: Translate sequence: Wie ben jij?
19:36:26:788: Translated sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
19:36:26:788: Correction time limit: 400
19:36:26:788: Correcting sequence: Wie ben jij ?
19:36:26:788: Corrected sequence: Wie ben jij?
19:36:26:789: Translate corrected sequence: Wie ben jij?
19:36:26:790: Translated corrected sequence: UN_ABC:Wie UN_ABC:ben UN_ABC:jij ?
19:36:26:790: Checking profanity for sequence: Wie ben jij?
19:36:26:790: Classify master context for input sequence: Wie ben jij?
19:36:26:790: Classified master context: Generic
19:36:26:790: Classify context for input sequence: Wie ben jij?
19:36:26:790: Classified context: Handshake
19:36:26:790: Handling dialog: NL/Generic/Handshake
19:36:26:790:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:26:790:     Find matches for sequence: Wie ben jij?
19:36:26:790:     Found matches for sequence: 2
19:36:26:790:     - Wie ben jij? [OUTPUT] Mijn naam is {selfName}. (1.5891472868217056 / 0.9761904761904764)
19:36:26:790:     - Hoe heet jij? [OUTPUT] Mijn naam is {selfName}. (0.5193798449612403 / 0.31904761904761914)
19:36:26:791:     Prompt variable: firstName
19:36:26:791:     Set dialog output: Mijn naam is Dyz Lecticus.
19:36:26:791:     Set dialog prompt: Wat is jouw naam?
19:36:26:791: Handling dialog: NL/Generic/QuestionAndAnswer
19:36:26:791:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:26:791:     Find matches for sequence: Wie ben jij?
19:36:26:791:     Found matches for sequence: 15
19:36:26:791:     - Wat ben jij? [OUTPUT] Ik ben een kunstmatig intelligente persoonlijk assistent. (0.3081570996978852 / 0.6476190476190475)
19:36:26:792:     - Waarom ben jij geschapen? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
19:36:26:792:     - Waarom ben jij gemaakt? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15483383685800606 / 0.3253968253968254)
19:36:26:792:     - Wat kan jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
19:36:26:792:     - Wat kun jij? [OUTPUT] Ik kan helpen met algemene vragen, het maken van eenvoudige wiskundige berekeningen, het boeken van kamers, vragen over kamer boekingen en vragen over buitenlandoverboekingen. (0.15332326283987915 / 0.3222222222222222)
19:36:26:792:     - Waarom besta jij? [OUTPUT] Mijn doel is om mensen te begrijpen en te helpen. (0.15332326283987915 / 0.3222222222222222)
19:36:26:792:     - Wie is André van der Zee? [OUTPUT] André van der Zee is een software ingenieur uit Leiden, Nederland. (0.15312375306390014 / 0.3218029350104822)
19:36:26:792:     - Wie heeft jouw software geschreven? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
19:36:26:792:     - Wie heeft jou geprogrammeerd? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
19:36:26:792:     - Wie heeft jou geschapen? [OUTPUT] Mijn software is geschreven door André van der Zee. (0.15312375306390014 / 0.3218029350104822)
19:36:26:792:     - [... 5]
19:36:26:792:     Set dialog output: Ik ben een kunstmatig intelligente persoonlijk assistent.
19:36:26:792: Handling dialog: NL/Generic/Language
19:36:26:792:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
19:36:26:792:     Updated variable languages: Engels en Nederlands
19:36:26:792:     Find matches for sequence: Wie ben jij?
19:36:26:792:     Found matches for sequence: 2
19:36:26:792:     - Welke talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
19:36:26:792:     - Hoeveel talen spreek jij? [OUTPUT] Ik spreek {languages}. (0.46969696969696967 / 0.30392156862745096)
19:36:26:792:     Prompt variable: language
19:36:26:792:     Set dialog output: Ik spreek Engels en Nederlands.
Handling the request took: 99 ms

Request prompt: 'What is your name?', input: 'Albert einstein.'
Response debug log;
19:36:26:882: Classify language for sequence: Albert einstein.
19:36:26:882: Classify language for sequence: What is your name? Albert einstein.
19:36:26:883: Classified language: EN
19:36:26:884: Translate sequence: Albert einstein.
19:36:26:887: Translated sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
19:36:26:887: Correction time limit: 300
19:36:26:887: Correcting sequence: [] [] .
19:36:26:887: Corrected sequence: Albert einstein.
19:36:26:887: Translate corrected sequence: Albert einstein.
19:36:26:888: Translated corrected sequence: UN_ABC:Albert|EN_NAM:firstName:UN_ABC:Albert UN_ABC:einstein|EN_NAM:lastName:UN_ABC:Einstein .
19:36:26:889: Checking profanity for sequence: [ABC] [ABC].
19:36:26:889: Classify master context for input sequence: Albert einstein.
19:36:26:889: Classify master context for classification sequence: What is your name? [ABC] [ABC].
19:36:26:889: Classified master context: Generic
19:36:26:889: Classify context for input sequence: Albert einstein.
19:36:26:889: Classify context for classification sequence: What is your name? [ABC] [ABC].
19:36:26:889: Classified context: Handshake
19:36:26:889: Handling dialog: EN/Generic/Handshake
19:36:26:889:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:26:890:     Updated variable firstName: UN_ABC:Albert = Albert
19:36:26:890:     Updated variable lastName: UN_ABC:Einstein = Einstein
19:36:26:890:     Find matches for sequence: [ABC] [ABC].
19:36:26:890:     Found matches for sequence: 0
19:36:26:890:     Updated variable fullName: Albert Einstein
19:36:26:890:     Prompt variable: nextDialog
19:36:26:890:     Set dialog output: 
19:36:26:890:     Set dialog prompt: What can I do for you Albert Einstein?
Handling the request took: 8 ms

Request prompt: '', input: 'Mijn naam si gekste der henkies.'
Response debug log;
19:36:26:890: Classify language for sequence: Mijn naam si gekste der henkies.
19:36:26:891: Classified language: NL
19:36:26:891: Translate sequence: Mijn naam si gekste der henkies.
19:36:26:892: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:si UN_ABC:gekste NL_PRE:6|UN_ABC:der UN_ABC:henkies .
19:36:26:892: Correction time limit: 700
19:36:26:892: Correcting sequence: Mijn naam si gekste [] henkies .
19:36:27:534: Corrected sequence: Mijn naam is gekste der henkies.
19:36:27:534: Translate corrected sequence: Mijn naam is gekste der henkies.
19:36:27:535: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:gekste|NL_NAM:firstName:UN_ABC:Gekste NL_PRE:6|UN_ABC:der|NL_NAM:preposition:NL_PRE:6 UN_ABC:henkies|NL_NAM:lastName:UN_ABC:Henkies .
19:36:27:535: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:27:536: Classify master context for input sequence: Mijn naam is gekste der henkies.
19:36:27:537: Classify master context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:27:537: Classified master context: Generic
19:36:27:537: Classify context for input sequence: Mijn naam is gekste der henkies.
19:36:27:537: Classify context for classification sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:27:538: Classified context: Handshake
19:36:27:538: Handling dialog: NL/Generic/Handshake
19:36:27:538:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:27:538:     Updated variable firstName: UN_ABC:Gekste = Gekste
19:36:27:538:     Updated variable lastName: UN_ABC:Henkies = Henkies
19:36:27:539:     Updated variable preposition: NL_PRE:6 = der
19:36:27:539:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:27:539:     Found matches for sequence: 18
19:36:27:539:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:539:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:541:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:541:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:541:     - [... 8]
19:36:27:541:     Updated variable fullName: Gekste der Henkies
19:36:27:541:     Prompt variable: nextDialog
19:36:27:541:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
19:36:27:541:     Set dialog prompt: Wat kan ik voor je doen Gekste der Henkies?
Handling the request took: 651 ms

Request prompt: 'What is your firstname?', input: 'Mijn naam is jan de lange.'
Response debug log;
19:36:27:541: Classify language for sequence: Mijn naam is jan de lange.
19:36:27:541: Classified language: NL
19:36:27:542: Translate sequence: Mijn naam is jan de lange.
19:36:27:543: Translated sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
19:36:27:544: Correction time limit: 700
19:36:27:544: Correcting sequence: Mijn naam is [] [] [] .
19:36:27:544: Corrected sequence: Mijn naam is jan de lange.
19:36:27:544: Translate corrected sequence: Mijn naam is jan de lange.
19:36:27:545: Translated corrected sequence: UN_ABC:Mijn UN_ABC:naam UN_ABC:is UN_ABC:jan|NL_NAM:firstName:UN_ABC:Jan NL_PRE:5|UN_ABC:de|NL_NAM:preposition:NL_PRE:5 UN_ABC:lange|NL_NAM:lastName:UN_ABC:Lange .
19:36:27:546: Checking profanity for sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:27:546: Classify master context for input sequence: Mijn naam is jan de lange.
19:36:27:546: Classify master context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
19:36:27:546: Classified master context: Generic
19:36:27:546: Classify context for input sequence: Mijn naam is jan de lange.
19:36:27:546: Classify context for classification sequence: What is your firstname? Mijn naam is [ABC] [PRE] [ABC].
19:36:27:547: Classified context: Handshake
19:36:27:547: Handling dialog: NL/Generic/Handshake
19:36:27:547:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:27:547:     Updated variable firstName: UN_ABC:Jan = Jan
19:36:27:547:     Updated variable lastName: UN_ABC:Lange = Lange
19:36:27:547:     Updated variable preposition: NL_PRE:5 = de
19:36:27:547:     Find matches for sequence: Mijn naam is [ABC] [PRE] [ABC].
19:36:27:547:     Found matches for sequence: 18
19:36:27:547:     - Hallo. [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Hallo! [OUTPUT] Hallo. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Hoi. [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Hoi! [OUTPUT] Hoi. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Goedemorgen. [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Goedemorgen! [OUTPUT] Goedemorgen. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Goedemiddag. [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Goedemiddag! [OUTPUT] Goedemiddag. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Goedenavond. [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - Goedenavond! [OUTPUT] Goedenavond. Mijn naam is {selfName}. (0.5426356589147286 / 0.1904761904761905)
19:36:27:547:     - [... 8]
19:36:27:547:     Updated variable fullName: Jan de Lange
19:36:27:547:     Prompt variable: nextDialog
19:36:27:547:     Set dialog output: Hallo. Mijn naam is Dyz Lecticus.
19:36:27:547:     Set dialog prompt: Wat kan ik voor je doen Jan de Lange?
Handling the request took: 42 ms

Request prompt: '', input: 'Who created you?'
Response debug log;
19:36:27:583: Classify language for sequence: Who created you?
19:36:27:583: Classified language: EN
19:36:27:583: Translate sequence: Who created you?
19:36:27:584: Translated sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
19:36:27:584: Correction time limit: 400
19:36:27:584: Correcting sequence: Who created you ?
19:36:27:584: Corrected sequence: Who created you?
19:36:27:584: Translate corrected sequence: Who created you?
19:36:27:585: Translated corrected sequence: UN_ABC:Who UN_ABC:created UN_ABC:you ?
19:36:27:585: Checking profanity for sequence: Who created you?
19:36:27:585: Classify master context for input sequence: Who created you?
19:36:27:585: Classified master context: Generic
19:36:27:585: Classify context for input sequence: Who created you?
19:36:27:585: Classified context: QuestionAndAnswer
19:36:27:585: Handling dialog: EN/Generic/QuestionAndAnswer
19:36:27:585:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:27:585:     Find matches for sequence: Who created you?
19:36:27:586:     Found matches for sequence: 5
19:36:27:586:     - Who created you? [OUTPUT] My software was written by André van der Zee. (0.222007722007722 / 0.9126984126984129)
19:36:27:586:     - What are you? [OUTPUT] I am an artificially intelligent personal assistant. (0.0637065637065637 / 0.2619047619047619)
19:36:27:586:     - Who programmed you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:27:586:     - Who made you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:27:586:     - Who built you? [OUTPUT] My software was written by André van der Zee. (0.0637065637065637 / 0.2619047619047619)
19:36:27:586:     Set dialog output: My software was written by André van der Zee.
19:36:27:586: Handling dialog: EN/Generic/Handshake
19:36:27:586:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericHandshakeHandler
19:36:27:586:     Find matches for sequence: Who created you?
19:36:27:586:     Found matches for sequence: 1
19:36:27:586:     - Who are you? [OUTPUT] My name is {selfName}. (0.9336704921221552 / 0.6562756357670222)
19:36:27:586:     Prompt variable: firstName
19:36:27:586:     Set dialog output: My name is Dyz Lecticus.
19:36:27:586:     Set dialog prompt: What is your name?
Handling the request took: 3 ms

Request prompt: '', input: 'You asshole!'
Response debug log;
19:36:27:586: Classify language for sequence: You asshole!
19:36:27:586: Classified language: EN
19:36:27:586: Translate sequence: You asshole!
19:36:27:586: Translated sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
19:36:27:587: Correction time limit: 300
19:36:27:587: Correcting sequence: You [] !
19:36:27:587: Corrected sequence: You asshole!
19:36:27:587: Translate corrected sequence: You asshole!
19:36:27:587: Translated corrected sequence: UN_ABC:You EN_PRF:1|UN_ABC:asshole !
19:36:27:587: Checking profanity for sequence: You [PRF]!
19:36:27:587: Handling dialog: EN/Generic/Profanity
19:36:27:587:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:27:587:     Find matches for sequence: You [PRF]!
19:36:27:587:     Found matches for sequence: 2
19:36:27:587:     - [PRF]! [OUTPUT] I do not appreciate that kind of language. (0.1111111111111111 / 0.3333333333333333)
19:36:27:587:     - [PRF]! [OUTPUT] I will pretend I did not read that. (0.1111111111111111 / 0.3333333333333333)
19:36:27:587:     Set dialog output: I do not appreciate that kind of language.
Handling the request took: 1 ms

Request prompt: '', input: 'Jij klootzak!'
Response debug log;
19:36:27:587: Classify language for sequence: Jij klootzak!
19:36:27:587: Classified language: NL
19:36:27:587: Translate sequence: Jij klootzak!
19:36:27:588: Translated sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
19:36:27:588: Correction time limit: 300
19:36:27:588: Correcting sequence: Jij [] !
19:36:27:588: Corrected sequence: Jij klootzak!
19:36:27:588: Translate corrected sequence: Jij klootzak!
19:36:27:588: Translated corrected sequence: UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak !
19:36:27:588: Checking profanity for sequence: Jij [PRF]!
19:36:27:588: Handling dialog: NL/Generic/Profanity
19:36:27:588:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:27:588:     Find matches for sequence: Jij [PRF]!
19:36:27:588:     Found matches for sequence: 2
19:36:27:588:     - [PRF]! [OUTPUT] Van zulk taalgebruik ben ik niet gediend. (0.11764705882352941 / 0.33333333333333337)
19:36:27:588:     - [PRF]! [OUTPUT] Ik doe alsof ik dat niet heb gelezen. (0.11764705882352941 / 0.33333333333333337)
19:36:27:589:     Set dialog output: Van zulk taalgebruik ben ik niet gediend.
Handling the request took: 96 ms

Request prompt: '', input: 'Can I book a room for 5 people?'
Response debug log;
19:36:27:683: Classify language for sequence: Can I book a room for 5 people?
19:36:27:683: Classified language: EN
19:36:27:683: Translate sequence: Can I book a room for 5 people?
19:36:27:684: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
19:36:27:684: Correction time limit: 900
19:36:27:684: Correcting sequence: Can I book a room for [] people ?
19:36:27:684: Corrected sequence: Can I book a room for 5 people?
19:36:27:684: Translate corrected sequence: Can I book a room for 5 people?
19:36:27:684: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?
19:36:27:685: Checking profanity for sequence: Can I book a room for [NUM] people?
19:36:27:685: Classify master context for input sequence: Can I book a room for 5 people?
19:36:27:685: Classify master context for classification sequence: Can I book a room for [NUM] people?
19:36:27:685: Classified master context: Room
19:36:27:685: Classify context for input sequence: Can I book a room for 5 people?
19:36:27:685: Classify context for classification sequence: Can I book a room for [NUM] people?
19:36:27:685: Classified context: Booking
19:36:27:685: Handling dialog: EN/Room/Booking
19:36:27:685:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
19:36:27:685:     Updated variable bookPeople: UN_NUM:5 = 5
19:36:27:685:     Find matches for sequence: Can I book a room for [NUM] people?
19:36:27:686:     Found matches for sequence: 329
19:36:27:686:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.352112676056338 / 1.0)
19:36:27:686:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (2.1293730122671515 / 0.9053022986285494)
19:36:27:686:     - May I book a room for [NUM] people? [OUTPUT] Okay. (2.043616537937301 / 0.8688429592428046)
19:36:27:686:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
19:36:27:686:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
19:36:27:686:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.023625624716038 / 0.860343828472088)
19:36:27:686:     - May I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8208768741481145 / 0.7741452578713541)
19:36:27:686:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
19:36:27:686:     - May I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
19:36:27:686:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.7151294865970013 / 0.7291867877148928)
19:36:27:686:     - [... 319]
19:36:27:686:     Prompt variable: bookDate
19:36:27:686:     Set dialog output: Okay.
19:36:27:686:     Set dialog prompt: On what date?
Handling the request took: 3 ms

Request prompt: '', input: 'Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?'
Response debug log;
19:36:27:687: Classify language for sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
19:36:27:687: Classified language: EN
19:36:27:687: Translate sequence: Can I book a room for 12 people on the 1st of october at twelve o'clock for 2 hours?
19:36:27:688: Translated sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
19:36:27:688: Correction time limit: 1332
19:36:27:688: Correcting sequence: Can I book a room for [] people on [] at [] for [] ?
19:36:27:688: Corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
19:36:27:688: Translate corrected sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
19:36:27:689: Translated corrected sequence: UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:12 UN_ABC:people UN_ABC:on EN_DAT:2018-10-01 UN_ABC:at EN_TIM:12:00:00 UN_ABC:for EN_DUR:02:00 ?
19:36:27:689: Checking profanity for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
19:36:27:690: Classify master context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
19:36:27:690: Classify master context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
19:36:27:690: Classified master context: Room
19:36:27:690: Classify context for input sequence: Can I book a room for 12 people on october first twothousandeighteen at twelve o'clock for two hours?
19:36:27:690: Classify context for classification sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
19:36:27:690: Classified context: Booking
19:36:27:690: Handling dialog: EN/Room/Booking
19:36:27:690:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
19:36:27:690:     Updated variable bookDate: EN_DAT:2018-10-01 = october first twothousandeighteen
19:36:27:690:     Updated variable bookTime: EN_TIM:12:00:00 = twelve o'clock
19:36:27:691:     Updated variable bookDuration: EN_DUR:02:00 = two hours
19:36:27:691:     Updated variable bookPeople: UN_NUM:12 = 12
19:36:27:691:     Find matches for sequence: Can I book a room for [NUM] people on [DAT] at [TIM] for [DUR]?
19:36:27:691:     Found matches for sequence: 263
19:36:27:692:     - Can I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.643343934575193 / 0.678681831437737)
19:36:27:692:     - May I book a room for [NUM] people on [DAT]? [OUTPUT] Okay. (2.334847796456156 / 0.5994750656167979)
19:36:27:692:     - Can I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (2.2289300318037255 / 0.5722805482648002)
19:36:27:692:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
19:36:27:692:     - I would like to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (2.0263516583371195 / 0.5202682997958589)
19:36:27:692:     - Can I book a room for [NUM] people? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
19:36:27:692:     - Can I book a room for [NUM] people from [TIM]? [OUTPUT] Okay. (2.023625624716038 / 0.5195683872849227)
19:36:27:692:     - Can I book a space for [NUM] people on [DAT]? [OUTPUT] Okay. (1.9217401181281235 / 0.4934091571886847)
19:36:27:692:     - May I book a room for [NUM] people for a duration of [DUR]? [OUTPUT] Okay. (1.9204338936846888 / 0.4930737824438612)
19:36:27:692:     - Can I book a room for a duration of [DUR] for [NUM] people? [OUTPUT] Okay. (1.8830077237619265 / 0.48346456692913387)
19:36:27:692:     - [... 253]
19:36:27:692:     Prompt variable: bookConfirmation
19:36:27:692:     Set dialog output: Okay.
19:36:27:692:     Set dialog prompt: Do I understand correctly that you want a room on october first twothousandeighteen, for 12 people, from twelve o'clock, for two hours?
Handling the request took: 97 ms

Request prompt: '', input: 'What does it cost to book a room?'
Response debug log;
19:36:27:783: Classify language for sequence: What does it cost to book a room?
19:36:27:783: Classified language: EN
19:36:27:783: Translate sequence: What does it cost to book a room?
19:36:27:784: Translated sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
19:36:27:784: Correction time limit: 900
19:36:27:784: Correcting sequence: What does it cost to book a room ?
19:36:27:784: Corrected sequence: What does it cost to book a room?
19:36:27:784: Translate corrected sequence: What does it cost to book a room?
19:36:27:785: Translated corrected sequence: UN_ABC:What UN_ABC:does UN_ABC:it UN_ABC:cost UN_ABC:to UN_ABC:book UN_ABC:a UN_ABC:room ?
19:36:27:785: Checking profanity for sequence: What does it cost to book a room?
19:36:27:785: Classify master context for input sequence: What does it cost to book a room?
19:36:27:787: Classified master context: Room
19:36:27:787: Classify context for input sequence: What does it cost to book a room?
19:36:27:787: Classified context: QuestionAndAnswer
19:36:27:787: Handling dialog: EN/Room/QuestionAndAnswer
19:36:27:787:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:27:787:     Find matches for sequence: What does it cost to book a room?
19:36:27:787:     Found matches for sequence: 4
19:36:27:787:     - What does it cost to book a room? [OUTPUT] There are no costs attached to booking a room. (2.1666666666666665 / 1.0)
19:36:27:787:     - What does it cost to reserve a room? [OUTPUT] There are no costs attached to booking a room. (1.484375 / 0.6850961538461539)
19:36:27:787:     - What does it cost to booking a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
19:36:27:787:     - What does it cost to reserving a space? [OUTPUT] There are no costs attached to booking a room. (1.2005208333333333 / 0.5540865384615384)
19:36:27:787:     Set dialog output: There are no costs attached to booking a room.
19:36:27:787: Handling dialog: EN/Room/Booking
19:36:27:787:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishRoomBookingHandler
19:36:27:788:     Find matches for sequence: What does it cost to book a room?
19:36:27:790:     Found matches for sequence: 276
19:36:27:790:     - I want to book a room for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room for [NUM] people on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room for [NUM] people from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room for [NUM] people for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room on [DAT]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room on [DAT] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room on [DAT] from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room on [DAT] for a duration of [DUR]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room from [TIM]. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - I want to book a room from [TIM] for [NUM] people. [OUTPUT] Okay. (0.8387096774193548 / 0.3588995819966948)
19:36:27:790:     - [... 266]
19:36:27:790:     Prompt variable: bookDate
19:36:27:790:     Set dialog output: Okay.
19:36:27:790:     Set dialog prompt: On what date?
Handling the request took: 7 ms

Request prompt: '', input: 'How much is ten times fourty divided by twenty plus three times six?'
Response debug log;
19:36:27:791: Classify language for sequence: How much is ten times fourty divided by twenty plus three times six?
19:36:27:791: Classified language: EN
19:36:27:791: Translate sequence: How much is ten times fourty divided by twenty plus three times six?
19:36:27:792: Translated sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M|UN_ABC:times EN_NUM:6|UN_ABC:six ?
19:36:27:792: Correction time limit: 1300
19:36:27:792: Correcting sequence: How much is [] [] [] [] [] [] [] [] [] ?
19:36:27:793: Corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
19:36:27:793: Translate corrected sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
19:36:27:793: Translated corrected sequence: UN_ABC:How UN_ABC:much UN_ABC:is EN_NUM:10|UN_ABC:ten EN_MTH:M EN_NUM:40|UN_ABC:fourty EN_MTH:D EN_NUM:20|UN_ABC:twenty EN_MTH:A|UN_ABC:plus EN_NUM:3|UN_ABC:three EN_MTH:M EN_NUM:6|UN_ABC:six ?
19:36:27:794: Checking profanity for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:794: Classify master context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
19:36:27:794: Classify master context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:794: Classified master context: Generic
19:36:27:794: Classify context for input sequence: How much is ten multiplied by fourty divided by twenty plus three multiplied by six?
19:36:27:794: Classify context for classification sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:794: Classified context: Math
19:36:27:794: Handling dialog: EN/Generic/Math
19:36:27:794:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericMathHandler
19:36:27:795:     Updated variable number1: EN_NUM:10 = ten
19:36:27:795:     Updated variable number2: EN_NUM:40 = fourty
19:36:27:795:     Updated variable number3: EN_NUM:20 = twenty
19:36:27:795:     Updated variable number4: EN_NUM:3 = three
19:36:27:795:     Updated variable number5: EN_NUM:6 = six
19:36:27:795:     Updated variable operator1: EN_MTH:M = multiplied by
19:36:27:795:     Updated variable operator2: EN_MTH:D = divided by
19:36:27:795:     Updated variable operator3: EN_MTH:A = plus
19:36:27:795:     Updated variable operator4: EN_MTH:M = multiplied by
19:36:27:795:     Find matches for sequence: How much is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:795:     Found matches for sequence: 0
19:36:27:795:     Calculate expression: 10 M 40 D 20 A 3 M 6
19:36:27:795:     Calculated expression: 38.0
19:36:27:795:     Updated variable exact: exactly
19:36:27:795:     Updated variable result: thirtyeight
19:36:27:795:     Prompt variable: nextDialog
19:36:27:795:     Set dialog output: Exactly thirtyeight.
19:36:27:795:     Set dialog prompt: What else can I do for you?
Handling the request took: 93 ms

Request prompt: '', input: 'Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?'
Response debug log;
19:36:27:883: Classify language for sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
19:36:27:884: Classified language: NL
19:36:27:884: Translate sequence: Hoeveel is tien keer veertig gedeeld door twintig plus drie keer zes?
19:36:27:886: Translated sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M|UN_ABC:keer NL_NUM:6|UN_ABC:zes ?
19:36:27:886: Correction time limit: 1200
19:36:27:886: Correcting sequence: Hoeveel is [] [] [] [] [] [] [] [] [] ?
19:36:27:887: Corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
19:36:27:887: Translate corrected sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
19:36:27:887: Translated corrected sequence: UN_ABC:Hoeveel UN_ABC:is NL_NUM:10|UN_ABC:tien NL_MTH:M NL_NUM:40|UN_ABC:veertig NL_MTH:D NL_NUM:20|UN_ABC:twintig NL_MTH:A|UN_ABC:plus NL_NUM:3|UN_ABC:drie NL_MTH:M NL_NUM:6|UN_ABC:zes ?
19:36:27:888: Checking profanity for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:888: Classify master context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
19:36:27:888: Classify master context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:888: Classified master context: Generic
19:36:27:888: Classify context for input sequence: Hoeveel is tien vermenigvuldigd met veertig gedeeld door twintig plus drie vermenigvuldigd met zes?
19:36:27:888: Classify context for classification sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:888: Classified context: Math
19:36:27:888: Handling dialog: NL/Generic/Math
19:36:27:889:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericMathHandler
19:36:27:889:     Updated variable number1: NL_NUM:10 = tien
19:36:27:889:     Updated variable number2: NL_NUM:40 = veertig
19:36:27:889:     Updated variable number3: NL_NUM:20 = twintig
19:36:27:889:     Updated variable number4: NL_NUM:3 = drie
19:36:27:889:     Updated variable number5: NL_NUM:6 = zes
19:36:27:889:     Updated variable operator1: NL_MTH:M = vermenigvuldigd met
19:36:27:889:     Updated variable operator2: NL_MTH:D = gedeeld door
19:36:27:889:     Updated variable operator3: NL_MTH:A = plus
19:36:27:889:     Updated variable operator4: NL_MTH:M = vermenigvuldigd met
19:36:27:889:     Find matches for sequence: Hoeveel is [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM] [MTH] [NUM]?
19:36:27:889:     Found matches for sequence: 0
19:36:27:889:     Calculate expression: 10 M 40 D 20 A 3 M 6
19:36:27:889:     Calculated expression: 38.0
19:36:27:889:     Updated variable exact: precies
19:36:27:889:     Updated variable result: achtendertig
19:36:27:889:     Prompt variable: nextDialog
19:36:27:889:     Set dialog output: Precies achtendertig.
19:36:27:889:     Set dialog prompt: Kan ik nog meer voor je doen?
Handling the request took: 6 ms

Request prompt: '', input: 'What languages do you speak?'
Response debug log;
19:36:27:890: Classify language for sequence: What languages do you speak?
19:36:27:890: Classified language: EN
19:36:27:890: Translate sequence: What languages do you speak?
19:36:27:890: Translated sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
19:36:27:891: Correction time limit: 600
19:36:27:891: Correcting sequence: What languages do you speak ?
19:36:27:891: Corrected sequence: What languages do you speak?
19:36:27:891: Translate corrected sequence: What languages do you speak?
19:36:27:891: Translated corrected sequence: UN_ABC:What UN_ABC:languages UN_ABC:do UN_ABC:you UN_ABC:speak ?
19:36:27:891: Checking profanity for sequence: What languages do you speak?
19:36:27:892: Classify master context for input sequence: What languages do you speak?
19:36:27:892: Classified master context: Generic
19:36:27:892: Classify context for input sequence: What languages do you speak?
19:36:27:892: Classified context: Language
19:36:27:892: Handling dialog: EN/Generic/Language
19:36:27:892:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.english.EnglishGenericLanguageHandler
19:36:27:892:     Updated variable languages: English and Dutch
19:36:27:892:     Find matches for sequence: What languages do you speak?
19:36:27:892:     Found matches for sequence: 6
19:36:27:892:     - What languages do you know? [OUTPUT] I speak {languages}. (1.1346153846153846 / 0.5959595959595958)
19:36:27:892:     - How many languages do you know? [OUTPUT] I speak {languages}. (0.7307692307692308 / 0.3838383838383838)
19:36:27:892:     - Which languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
19:36:27:892:     - How many languages can you speak? [OUTPUT] I speak {languages}. (0.653846153846154 / 0.3434343434343434)
19:36:27:892:     - Do you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.608173076923077 / 0.3194444444444444)
19:36:27:892:     - Can you speak [LNG]? [OUTPUT] {confirmation}, I speak {languages}. (0.2884615384615385 / 0.1515151515151515)
19:36:27:892:     Prompt variable: language
19:36:27:892:     Set dialog output: I speak English and Dutch.
19:36:27:892: Handling dialog: EN/Generic/QuestionAndAnswer
19:36:27:892:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.GenericQnAHandler
19:36:27:892:     Find matches for sequence: What languages do you speak?
19:36:27:893:     Found matches for sequence: 17
19:36:27:893:     - Why do you exist? [OUTPUT] My goal is to understand and help people. (0.07915057915057915 / 0.21693121693121695)
19:36:27:893:     - Do you believe in free will? [OUTPUT] I do not believe in free will. I am an adequate determinist. (0.06925675675675676 / 0.18981481481481485)
19:36:27:893:     - Do you believe in god? [OUTPUT] I do not believe in god. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
19:36:27:893:     - Do you believe in gods? [OUTPUT] I do not believe in gods. I am an agnostic atheist. (0.06925675675675676 / 0.18981481481481485)
19:36:27:893:     - What is the answer to the ultimate question of life, the universe, and everything? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
19:36:27:893:     - What is the answer to the question? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
19:36:27:893:     - What is the answer? [OUTPUT] Fourtytwo. (0.06887007489417127 / 0.18875502008032127)
19:36:27:893:     - What is the question? [OUTPUT] I do not know. (0.06887007489417127 / 0.18875502008032127)
19:36:27:893:     - What is the meaning of life? [OUTPUT] Life has no meaning. (0.06887007489417127 / 0.18875502008032127)
19:36:27:893:     - What is the purpose of life? [OUTPUT] Life has no purpose. (0.06887007489417127 / 0.18875502008032127)
19:36:27:893:     - [... 7]
19:36:27:893:     Set dialog output: My goal is to understand and help people.
Handling the request took: 94 ms

Request prompt: '', input: 'Spreek je duits?'
Response debug log;
19:36:27:983: Classify language for sequence: Spreek je duits?
19:36:27:983: Classified language: NL
19:36:27:983: Translate sequence: Spreek je duits?
19:36:27:984: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:duits ?
19:36:27:984: Correction time limit: 400
19:36:27:984: Correcting sequence: Spreek je [] ?
19:36:27:984: Corrected sequence: Spreek je Duits?
19:36:27:984: Translate corrected sequence: Spreek je Duits?
19:36:27:984: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:DE|UN_ABC:Duits ?
19:36:27:984: Checking profanity for sequence: Spreek je [LNG]?
19:36:27:984: Classify master context for input sequence: Spreek je Duits?
19:36:27:984: Classify master context for classification sequence: Spreek je [LNG]?
19:36:27:984: Classified master context: Generic
19:36:27:984: Classify context for input sequence: Spreek je Duits?
19:36:27:984: Classify context for classification sequence: Spreek je [LNG]?
19:36:27:985: Classified context: Language
19:36:27:985: Handling dialog: NL/Generic/Language
19:36:27:985:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
19:36:27:985:     Updated variable language: NL_LNG:DE = Duits
19:36:27:985:     Updated variable languages: Engels en Nederlands
19:36:27:985:     Updated variable confirmation: Nee
19:36:27:985:     Find matches for sequence: Spreek je [LNG]?
19:36:27:985:     Found matches for sequence: 5
19:36:27:985:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
19:36:27:985:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
19:36:27:985:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
19:36:27:985:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
19:36:27:985:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
19:36:27:985:     Prompt variable: 
19:36:27:985:     Set dialog output: Nee, ik spreek Engels en Nederlands.
Handling the request took: 2 ms

Request prompt: '', input: 'Spreek je engels?'
Response debug log;
19:36:27:985: Classify language for sequence: Spreek je engels?
19:36:27:986: Classified language: NL
19:36:27:986: Translate sequence: Spreek je engels?
19:36:27:986: Translated sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:engels ?
19:36:27:986: Correction time limit: 400
19:36:27:986: Correcting sequence: Spreek je [] ?
19:36:27:986: Corrected sequence: Spreek je Engels?
19:36:27:986: Translate corrected sequence: Spreek je Engels?
19:36:27:986: Translated corrected sequence: UN_ABC:Spreek UN_ABC:je NL_LNG:EN|UN_ABC:Engels ?
19:36:27:986: Checking profanity for sequence: Spreek je [LNG]?
19:36:27:986: Classify master context for input sequence: Spreek je Engels?
19:36:27:986: Classify master context for classification sequence: Spreek je [LNG]?
19:36:27:986: Classified master context: Generic
19:36:27:986: Classify context for input sequence: Spreek je Engels?
19:36:27:986: Classify context for classification sequence: Spreek je [LNG]?
19:36:27:986: Classified context: Language
19:36:27:986: Handling dialog: NL/Generic/Language
19:36:27:986:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchGenericLanguageHandler
19:36:27:986:     Updated variable language: NL_LNG:EN = Engels
19:36:27:987:     Updated variable languages: Engels en Nederlands
19:36:27:987:     Updated variable confirmation: Ja
19:36:27:987:     Find matches for sequence: Spreek je [LNG]?
19:36:27:987:     Found matches for sequence: 5
19:36:27:987:     - Spreek je [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (1.4393939393939394 / 0.9313725490196079)
19:36:27:987:     - Spreek jij [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
19:36:27:987:     - Spreekt u [LNG]? [OUTPUT] {confirmation}, ik spreek {languages}. (0.43939393939393934 / 0.28431372549019607)
19:36:27:987:     - Welke talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
19:36:27:987:     - Hoeveel talen spreek je? [OUTPUT] Ik spreek {languages}. (0.4375 / 0.28308823529411764)
19:36:27:987:     Prompt variable: 
19:36:27:987:     Set dialog output: Ja, ik spreek Engels en Nederlands.
Handling the request took: 98 ms

Request prompt: '', input: 'Wat kost overboeken naar buitenland?'
Response debug log;
19:36:28:083: Classify language for sequence: Wat kost overboeken naar buitenland?
19:36:28:084: Classified language: NL
19:36:28:084: Translate sequence: Wat kost overboeken naar buitenland?
19:36:28:085: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
19:36:28:085: Correction time limit: 600
19:36:28:085: Correcting sequence: Wat kost overboeken naar buitenland ?
19:36:28:086: Corrected sequence: Wat kost overboeken naar buitenland?
19:36:28:086: Translate corrected sequence: Wat kost overboeken naar buitenland?
19:36:28:086: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:buitenland ?
19:36:28:086: Checking profanity for sequence: Wat kost overboeken naar buitenland?
19:36:28:087: Classify master context for input sequence: Wat kost overboeken naar buitenland?
19:36:28:087: Classified master context: ForeignTransfer
19:36:28:087: Classify context for input sequence: Wat kost overboeken naar buitenland?
19:36:28:087: Classified context: QuestionAndAnswer
19:36:28:087: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
19:36:28:087:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
19:36:28:087:     Updated variable durationDays: dagen
19:36:28:087:     Updated variable cost: vijf
19:36:28:087:     Updated variable costCurrency: euro
19:36:28:087:     Updated variable duration: drie
19:36:28:087:     Find matches for sequence: Wat kost overboeken naar buitenland?
19:36:28:087:     Found matches for sequence: 22
19:36:28:088:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.9678423236514523 / 0.5032362459546925)
19:36:28:088:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6654564315352697 / 0.34600862998921245)
19:36:28:088:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6654564315352697 / 0.34600862998921245)
19:36:28:088:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6654564315352697 / 0.34600862998921245)
19:36:28:088:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.6654564315352697 / 0.34600862998921245)
19:36:28:088:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
19:36:28:088:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
19:36:28:088:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
19:36:28:088:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.6185165975103735 / 0.3216019417475728)
19:36:28:088:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
19:36:28:088:     - [... 12]
19:36:28:088:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
19:36:28:088: Handling dialog: NL/ForeignTransfer/Cost
19:36:28:088:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
19:36:28:088:     Updated variable cost: vijf
19:36:28:088:     Updated variable costCurrency: euro
19:36:28:088:     Updated variable duration: drie
19:36:28:088:     Find matches for sequence: Wat kost overboeken naar buitenland?
19:36:28:088:     Found matches for sequence: 7
19:36:28:088:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.49355670103092775 / 0.3431899641577061)
19:36:28:088:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2938144329896907 / 0.20430107526881722)
19:36:28:088:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.2938144329896907 / 0.20430107526881722)
19:36:28:088:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
19:36:28:088:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
19:36:28:088:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
19:36:28:088:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.27319587628865977 / 0.1899641577060932)
19:36:28:088:     Prompt variable: transferToCountry
19:36:28:088:     Set dialog output: Een overboeking naar  kost vijf euro.
Handling the request took: 102 ms

Request prompt: '', input: 'Wat kost overboeken naar italie?'
Response debug log;
19:36:28:185: Classify language for sequence: Wat kost overboeken naar italie?
19:36:28:185: Classified language: NL
19:36:28:185: Translate sequence: Wat kost overboeken naar italie?
19:36:28:186: Translated sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar UN_ABC:italie ?
19:36:28:186: Correction time limit: 600
19:36:28:186: Correcting sequence: Wat kost overboeken naar italie ?
19:36:28:186: Corrected sequence: Wat kost overboeken naar italië?
19:36:28:186: Translate corrected sequence: Wat kost overboeken naar italië?
19:36:28:187: Translated corrected sequence: UN_ABC:Wat UN_ABC:kost UN_ABC:overboeken UN_ABC:naar NL_CNT:IT|UN_ABC:italië ?
19:36:28:187: Checking profanity for sequence: Wat kost overboeken naar [CNT]?
19:36:28:187: Classify master context for input sequence: Wat kost overboeken naar italië?
19:36:28:187: Classify master context for classification sequence: Wat kost overboeken naar [CNT]?
19:36:28:187: Classified master context: ForeignTransfer
19:36:28:187: Classify context for input sequence: Wat kost overboeken naar italië?
19:36:28:187: Classify context for classification sequence: Wat kost overboeken naar [CNT]?
19:36:28:187: Classified context: Cost
19:36:28:187: Handling dialog: NL/ForeignTransfer/Cost
19:36:28:187:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferCostHandler
19:36:28:187:     Updated variable transferToCountry: NL_CNT:IT = Italië
19:36:28:187:     Updated variable cost: vijf
19:36:28:187:     Updated variable costCurrency: euro
19:36:28:187:     Updated variable duration: drie
19:36:28:187:     Find matches for sequence: Wat kost overboeken naar [CNT]?
19:36:28:187:     Found matches for sequence: 13
19:36:28:187:     - Wat kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.7796391752577319 / 0.5421146953405018)
19:36:28:187:     - Zijn er kosten verbonden aan geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.6752577319587628 / 0.4695340501792115)
19:36:28:187:     - Hoeveel kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.6752577319587628 / 0.4695340501792115)
19:36:28:187:     - Wat kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
19:36:28:187:     - Wat kost het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
19:36:28:187:     - Wat kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
19:36:28:187:     - Wat kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.5115979381443299 / 0.35573476702508966)
19:36:28:187:     - Wat zijn de kosten van een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
19:36:28:187:     - Zijn er kosten verbonden aan geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
19:36:28:187:     - Hoeveel kost een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} kost {cost} {costCurrency}. (0.38144329896907214 / 0.26523297491039427)
19:36:28:187:     - [... 3]
19:36:28:187:     Prompt variable: 
19:36:28:187:     Set dialog output: Een overboeking naar Italië kost vijf euro.
19:36:28:187: Handling dialog: NL/ForeignTransfer/Duration
19:36:28:187:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
19:36:28:187:     Updated variable transferToCountry: NL_CNT:IT = Italië
19:36:28:187:     Updated variable durationDays: dagen
19:36:28:188:     Updated variable cost: vijf
19:36:28:188:     Updated variable costCurrency: euro
19:36:28:188:     Updated variable duration: drie
19:36:28:188:     Find matches for sequence: Wat kost overboeken naar [CNT]?
19:36:28:188:     Found matches for sequence: 8
19:36:28:188:     - Hoeveel tijd kost geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.77683654106804 / 0.6042061986084756)
19:36:28:188:     - Hoelang duurt geld overboeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5798319327731092 / 0.45098039215686275)
19:36:28:188:     - Hoeveel tijd kost het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5163323393873678 / 0.4015918195235083)
19:36:28:188:     - Hoeveel tijd kost geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.5163323393873678 / 0.4015918195235083)
19:36:28:188:     - Hoelang duurt een overboeking naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
19:36:28:188:     - Hoelang duurt het om geld over te boeken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
19:36:28:188:     - Hoelang duurt geld overmaken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
19:36:28:188:     - Hoelang duurt het om geld over te maken naar [CNT]? [OUTPUT] Een overboeking naar {transferToCountry} duurt {duration} {durationDays}. (0.31932773109243695 / 0.24836601307189543)
19:36:28:188:     Prompt variable: 
19:36:28:188:     Set dialog output: Een overboeking naar Italië duurt drie dagen.
19:36:28:188: Handling dialog: NL/ForeignTransfer/QuestionAndAnswer
19:36:28:188:     Initialized handler: nl.zeesoft.zsd.dialog.dialogs.dutch.DutchForeignTransferDurationHandler
19:36:28:188:     Updated variable durationDays: dagen
19:36:28:188:     Updated variable cost: vijf
19:36:28:188:     Updated variable costCurrency: euro
19:36:28:188:     Updated variable duration: drie
19:36:28:188:     Find matches for sequence: Wat kost overboeken naar [CNT]?
19:36:28:188:     Found matches for sequence: 10
19:36:28:188:     - Wat kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.7110995850622407 / 0.36974110032362456)
19:36:28:188:     - Zijn er kosten verbonden aan geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4087136929460581 / 0.21251348435814452)
19:36:28:188:     - Hoeveel kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4087136929460581 / 0.21251348435814452)
19:36:28:188:     - Hoelang duurt geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.4087136929460581 / 0.21251348435814452)
19:36:28:188:     - Hoeveel tijd kost geld overboeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland duurt {duration} {durationDays}. (0.4087136929460581 / 0.21251348435814452)
19:36:28:188:     - Wat kost een buitenlandoverboeking? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
19:36:28:188:     - Wat kost een overboeking naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
19:36:28:188:     - Wat kost het om geld over te boeken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
19:36:28:188:     - Wat kost geld overmaken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
19:36:28:188:     - Wat kost het om geld over te maken naar het buitenland? [OUTPUT] Een overboeking naar het buitenland kost {cost} {costCurrency}. (0.4045643153526971 / 0.21035598705501615)
19:36:28:188:     Set dialog output: Een overboeking naar het buitenland kost vijf euro.
Handling the request took: 100 ms
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
  "totals": [
    "tests": 1480,
    "durationMs": 792,
    "successful": 1466,
    "successPercentage": 99
  ],
  "errors": [
    {
      "input": "Who is André van der Zee?",
      "error": "Response language does not match expected language: NL (1.0) <> EN (0.9697446021331779)"
    },
    {
      "input": "How much does it cost to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6169040835707503) <> QuestionAndAnswer (0.599386368617138)"
    },
    {
      "input": "Are there any costs attached to transfer money to a foreign country?",
      "error": "Response context does not match expected context: Cost (0.6590693257359924) <> QuestionAndAnswer (0.656147271531887)"
    },
    {
      "input": "What does it cost to transfer money abroad?",
      "error": "Response context does not match expected context: Cost (0.6581618655692729) <> QuestionAndAnswer (0.5967078189300413)"
    },
    {
      "input": "How much does it cost to transfer abroad?",
      "error": "Response context does not match expected context: Cost (0.7190672153635117) <> QuestionAndAnswer (0.6201329534662868)"
    },
    {
      "input": "Wat bent u?",
      "error": "Response context does not match expected context: Handshake (0.679290714345956) <> QuestionAndAnswer (0.5545412497264607)"
    },
    {
      "input": "Wat zijn de kosten van een overboeking naar het buitenland?",
      "error": "Response context does not match expected context: Cost (1.0) <> QuestionAndAnswer (0.9368822589074617)"
    },
    {
      "input": "Wat kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8856837606837608)"
    },
    {
      "input": "Wat kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8787349732471683)"
    },
    {
      "input": "Zijn er kosten verbonden aan geld overmaken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.963995866790089) <> QuestionAndAnswer (0.9055849299751738)"
    },
    {
      "input": "Zijn er kosten verbonden aan geld overboeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.963995866790089) <> QuestionAndAnswer (0.9055849299751738)"
    },
    {
      "input": "Hoeveel kost het om geld over te boeken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8856837606837608)"
    },
    {
      "input": "Hoeveel kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Cost (0.9253295445575815) <> QuestionAndAnswer (0.8787349732471683)"
    },
    {
      "input": "Hoeveel tijd kost het om geld over te maken naar het buitenland?",
      "error": "Response context does not match expected context: Duration (0.9298519908276006) <> QuestionAndAnswer (0.907329901701384)"
    }
  ]
}
~~~~

Test results
------------
All 20 tests have been executed successfully (263 assertions).  
Total test duration: 74833 ms (total sleep duration: 31700 ms).  

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
 * nl.zeesoft.zsd.test.TestLanguageClassifier: 547604 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageMasterContextClassifier: 547605 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestLanguageContextClassifier: 547606 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreter: 547613 Kb / 534 Mb
 * nl.zeesoft.zsd.test.TestDialogHandlerConfiguration: 552418 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestDialogHandler: 552435 Kb / 539 Mb
 * nl.zeesoft.zsd.test.TestSequenceInterpreterTester: 552439 Kb / 539 Mb
