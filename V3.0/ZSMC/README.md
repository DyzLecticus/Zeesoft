Zeesoft Symbolic Multithreaded Confabulation
============================================
The Zeesoft Symbolic Multithreaded Confabulation (ZSMC) is an open source library for Java application development.  

It provides support for;  
 * Context sensitive symbolic corrections; word spelling corrections  
 * Sequence context classification; sentence context classification  
 * Context sensitive sequence matching; find matching sentences  
 * Symbolic entity value translation; find variable values  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSMC/releases/zsmc-0.9.0.zip) to download the latest ZSMC release (version 0.9.0).  
All ZSMC releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSMC/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSMC](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/test/ZSMC.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsmc.test.TestSymbolCorrector
----------------------------------------
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
 * [TestSymbolCorrector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/test/TestSymbolCorrector.java)
 * [SymbolCorrector](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/SymbolCorrector.java)

**Test output**  
The output of this test shows the number of generated variations and corrections for certain words.
~~~~
Initializing the SymbolCorrector took: 22 ms
Single variations for 'a'; 80
Single variations for 'an'; 135
Single variations for 'the'; 189
Single variations for 'piza'; 243
Double variations for 'stfu'; 5
Double variations for 'pizza'; 348

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
Average correction time: 75 ms
~~~~

nl.zeesoft.zsmc.test.TestSequenceClassifier
-------------------------------------------
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
 * [TestSequenceClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/test/TestSequenceClassifier.java)
 * [SequenceClassifier](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/SequenceClassifier.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the classifier  
 * The classification results including the time it takes, for a set of input sequences  
~~~~
Initializing the SequenceClassifier took: 2637 ms

Classified sequence: 'Wat kost dat?' -> nlPriveBetalen
Classifying the input sequence took: 1 ms

Classified sequence (case insensitive): 'Wat kost dat?' -> nlPrivatebankingUwvermogen
Classifying the input sequence took: 1 ms

Classified sequence: 'Waar kan ik mijn transacties zien?' -> nlGrootzakelijkProducten
Classifying the input sequence took: 1 ms

Classified sequence: 'Heeft de ABN AMRO Rechtsbijstandverzekering' -> nlPriveVerzekeren
Classifying the input sequence took: 1 ms
~~~~

nl.zeesoft.zsmc.test.TestSequenceMatcher
----------------------------------------
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
 * [TestSequenceMatcher](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/test/TestSequenceMatcher.java)
 * [SequenceMatcher](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/SequenceMatcher.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the classifier  
 * The matches found for a set of input sequences and how long it takes to find them  
~~~~
Initializing the SequenceMatcher took: 5590 ms

Heb -> Heb je je al voor 1 mei ingeschreven voor je opleiding en bedenk je je voor 1 september? [OUTPUT] Dan kun je je nog kosteloos uitschrijven via Studielink. Studentenreisproduct Heb je een studentenreisproduct en lopen je studies in elkaar over , dan hoef je niets te regelen.
Matching the input sequence took: 21 ms

Wat kost dat? -> Zorg thuis : wat kost dat? [OUTPUT] De meeste mensen oriënteren zich pas op deze mogelijkheden als de zorg acuut nodig is. Soms kan men dan niet meer zelf beslissen en moeten anderen dat doen.
Matching the input sequence took: 41 ms

Wat kost dat? (nlPriveBetalen) -> Wat kost een buitenlandse betaling? [OUTPUT] Bekijk het overzicht met de tarieven.
Matching the input sequence took: 3 ms

geld over? -> Alle rekeningen betaald en geld over? [OUTPUT] Sparen ligt voor de hand , maar er is meer mogelijk. Bekijk onze video met de voor- en nadelen van beleggen , hypotheek aflossen , pensioen aanvullen en schenken.
Matching the input sequence took: 37 ms

Waar kan ik mijn transacties zien? -> Waar kan ik mijn transacties inzien? [OUTPUT] Via Mijn ICS Business kunt u online uw transacties , uw limiet , het openstaande saldo en overzichten tot 6 maanden terug bekijken. Ik wil een extra creditcard aanvragen.
Matching the input sequence took: 58 ms

overboeken -> Hoeveel kan ik overboeken vanaf mijn betaalrekening? [OUTPUT] U kunt beide paslezers gebruiken. Dit is het bedrag dat u per dag maximaal kunt overboeken met uw identificatiecode en vingerafdruk.
Matching the input sequence took: 30 ms

Hypotheek berekenen -> null
Matching the input sequence took: 1 ms

Hypotheek berekenen (case insensitive) -> Of bent u gewoon nieuwsgierig naar hoeveel u kunt lenen? [OUTPUT] U kunt ook uw hypotheek berekenen als u geen vast contract heeft of als u zzp'er bent.
Matching the input sequence took: 24 ms

Fraude -> En belangrijker : hoe kunt voorkomen slachtoffer te worden van CEO Fraude? [OUTPUT] Criminelen kunnen veel informatie over bedrijven op internet vinden. Check daarom regelmatig wat voor informatie u over uw bedrijf en de medewerkers online heeft staan. Maak het criminelen zo moeilijk mogelijk om online namen , functies en emailadressen te stelen.
Matching the input sequence took: 12 ms

Fraude (case insensitive) -> Wat is CEO Fraude? [OUTPUT] Bij CEO fraude doen criminelen zich voor als een hooggeplaatste manager of bestuurder ( bijvoorbeeld de CEO of de CFO ) uit uw organisatie , om vervolgens geld te stelen. Bij deze vorm van fraude gaat het vaak om zeer grote bedragen. Bij de meest recente CEO fraude slachtoffers zien we veel overeenkomsten en lijkt het alsof dezelfde daders erachter zitten.
Matching the input sequence took: 14 ms

Heeft de abn amro rechtsbijstandverzekering -> Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.
Matching the input sequence took: 21 ms

Heeft de abn amro rechtsbijstandverzekering (case insensitive) -> Heeft de ABN AMRO Rechtsbijstandverzekering een eigen risico? [OUTPUT] U heeft geen eigen risico bij de ABN AMRO Rechtsbijstandverzekering.
Matching the input sequence took: 401 ms
~~~~

nl.zeesoft.zsmc.test.TestEntityValueTranslator
----------------------------------------------
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

Class references;  
 * [TestEntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/test/TestEntityValueTranslator.java)
 * [EntityValueTranslator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/src/nl/zeesoft/zsmc/EntityValueTranslator.java)

**Test output**  
The output of this test shows;  
 * The time it takes to initialize the translator  
 * The translation results including the time it takes, for a set of input sequences  
~~~~
Initializing the EntityValueTranslator took: 1676 ms

Sequence: Eat three donuts at 9:00 or count to 110
Translation: UNI_ABC:Eat ENG_NUM:3|UNI_ABC:three UNI_ABC:donuts UNI_ABC:at UNI_TIM:09:00:00 UNI_ABC:or UNI_ABC:count UNI_ABC:to UNI_NUM:110
Translating the sequence took: 2 ms
Retranslation: Eat three donuts at 09:00:00 or count to 110
Retranslating the sequence took: 1 ms

Sequence: Eet drie donuts om 9:00 of tel tot 110
Translation: UNI_ABC:Eet NLD_NUM:3|UNI_ABC:drie UNI_ABC:donuts UNI_ABC:om UNI_TIM:09:00:00 UNI_ABC:of UNI_ABC:tel UNI_ABC:tot UNI_NUM:110
Translating the sequence took: 1 ms
Retranslation: Eet drie donuts om 09:00:00 of tel tot 110
Retranslating the sequence took: 0 ms

Sequence: I finished twohundredandtwentyfourth or 225th
Translation: UNI_ABC:I UNI_ABC:finished ENG_ORD:224|UNI_ABC:twohundredandtwentyfourth UNI_ABC:or ENG_OR2:225
Translating the sequence took: 1 ms
Retranslation: I finished twohundredandtwentyfourth or 225th
Retranslating the sequence took: 0 ms

Sequence: Ik ben tweehonderdvierentwintigste geworden
Translation: UNI_ABC:Ik UNI_ABC:ben NLD_ORD:224|UNI_ABC:tweehonderdvierentwintigste UNI_ABC:geworden
Translating the sequence took: 1 ms
Retranslation: Ik ben tweehonderdvierentwintigste geworden
Retranslating the sequence took: 0 ms

Sequence: februari march october december
Translation: NLD_MNT:2|UNI_ABC:februari ENG_MNT:3|UNI_ABC:march ENG_MNT:10|UNI_ABC:october ENG_MNT:12|NLD_MNT:12|UNI_ABC:december
Translating the sequence took: 0 ms
Retranslation: februari march october december
Retranslating the sequence took: 1 ms

Sequence: thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten
Translation: ENG_DUR:33:41 / NLD_DUR:33:41
Translating the sequence took: 0 ms
Retranslation: thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten
Retranslating the sequence took: 0 ms

Sequence: yesterday OR today OR the 1st of october
Translation: ENG_DAT:2018-06-16|UNI_ABC:yesterday UNI_ABC:OR ENG_DAT:2018-06-17|UNI_ABC:today UNI_ABC:OR ENG_DAT:2018-10-01
Translating the sequence took: 1 ms
Retranslation: june sixteenth twothousandeighteen OR june seventeenth twothousandeighteen OR october first twothousandeighteen
Retranslating the sequence took: 0 ms

Sequence: gisteren OF vandaag OF 1 oktober
Translation: NLD_DAT:2018-06-16|UNI_ABC:gisteren UNI_ABC:OF NLD_DAT:2018-06-17|UNI_ABC:vandaag UNI_ABC:OF NLD_DAT:2018-10-01
Translating the sequence took: 1 ms
Retranslation: zestien juni tweeduizendachttien OF zeventien juni tweeduizendachttien OF een oktober tweeduizendachttien
Retranslating the sequence took: 0 ms

Sequence: twelve o'clock OR five minutes to nine OR ten past one in the morning
Translation: ENG_TIM:12:00:00 UNI_ABC:OR ENG_TIM:08:55:00 UNI_ABC:OR ENG_TIM:01:10:00
Translating the sequence took: 0 ms
Retranslation: twelve o'clock OR fiftyfive past eight OR ten past one in the morning
Retranslating the sequence took: 1 ms

Sequence: twaalf uur OF vijf minuten voor negen OF tien over een sochtends
Translation: NLD_TIM:12:00:00|NLD_DUR:12:00 UNI_ABC:OF NLD_TIM:08:55:00 UNI_ABC:OF NLD_TIM:01:10:00
Translating the sequence took: 0 ms
Retranslation: twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends
Retranslating the sequence took: 1 ms
~~~~

Test results
------------
All 4 tests have been executed successfully (65 assertions).  
Total test duration: 11805 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsmc.test.TestSymbolCorrector: 432 Kb / 0 Mb
 * nl.zeesoft.zsmc.test.TestSequenceClassifier: 437 Kb / 0 Mb
 * nl.zeesoft.zsmc.test.TestSequenceMatcher: 447 Kb / 0 Mb
 * nl.zeesoft.zsmc.test.TestEntityValueTranslator: 717 Kb / 0 Mb
