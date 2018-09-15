package nl.zeesoft.zevt.mod;

import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.lang.Languages;
import nl.zeesoft.zodb.mod.TesterObject;
import nl.zeesoft.zodb.mod.TesterRequest;

public class ZEVTTester extends TesterObject {
	public ZEVTTester(Config config, String url) {
		super(config, url);
	}

	@Override
	protected void initializeRequestsNoLock() {
		addTranslationAndRetranslationRequests(
			"Eat three donuts at 9:00 or count to 110",
			"UN_ABC:Eat EN_NUM:3|UN_ABC:three UN_ABC:donuts UN_ABC:at UN_TIM:09:00:00 UN_ABC:or UN_ABC:count UN_ABC:to UN_NUM:110",
			"Eat three donuts at 09:00:00 or count to 110");
		addTranslationAndRetranslationRequests(
			"Eet drie donuts om 9:00 of tel tot 110",
			"UN_ABC:Eet NL_NUM:3|UN_ABC:drie UN_ABC:donuts UN_ABC:om UN_TIM:09:00:00 EN_PRE:1|UN_ABC:of UN_ABC:tel UN_ABC:tot UN_NUM:110",
			"Eet drie donuts om 09:00:00 of tel tot 110");
		addTranslationAndRetranslationRequests(
			"I finished twohundredandtwentyfourth or 225th",
			"UN_ABC:I UN_ABC:finished EN_ORD:224|UN_ABC:twohundredandtwentyfourth UN_ABC:or EN_OR2:225",
			"I finished twohundredandtwentyfourth or 225th");
		addTranslationAndRetranslationRequests(
			"Ik ben tweehonderdvierentwintigste geworden",
			"UN_ABC:Ik UN_ABC:ben NL_ORD:224|UN_ABC:tweehonderdvierentwintigste UN_ABC:geworden",
			"Ik ben tweehonderdvierentwintigste geworden");
		addTranslationAndRetranslationRequests(
			"februari march october december",
			"NL_MNT:2|UN_ABC:februari EN_MNT:3|UN_ABC:march EN_MNT:10|UN_ABC:october EN_MNT:12|NL_MNT:12|UN_ABC:december",
			"februari march october december");
		addTranslationAndRetranslationRequests(
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten",
			"EN_DUR:33:41 UN_MTH:D NL_DUR:33:41",
			"thirtythree hours and fourtyone minutes / drieendertig uur en eenenveertig minuten");
		addTranslationAndRetranslationRequests(
			"october first twothousandeighteen",
			"EN_DAT:2018-10-01",
			"october first twothousandeighteen");
		addTranslationAndRetranslationRequests(
			"een oktober tweeduizendachttien",
			"NL_DAT:2018-10-01",
			"een oktober tweeduizendachttien");
		addTranslationAndRetranslationRequests(
			"twelve o'clock OR five minutes to nine OR ten past one in the morning",
			"EN_TIM:12:00:00 UN_ABC:OR EN_TIM:08:55:00 UN_ABC:OR EN_TIM:01:10:00",
			"twelve o'clock OR fiftyfive past eight OR ten past one in the morning");
		addTranslationAndRetranslationRequests(Languages.NLD,
			"twaalf uur OF vijf minuten voor negen OF tien over een sochtends",
			"NL_TIM:12:00:00|NL_DUR:12:00 UN_ABC:OF NL_TIM:08:55:00 UN_ABC:OF NL_TIM:01:10:00",
			"twaalf uur OF acht uur vijfenvijftig OF een uur tien sochtends");
		addTranslationAndRetranslationRequests(
			"to Germany or France",
			"UN_ABC:to EN_CNT:DE|UN_ABC:Germany UN_ABC:or EN_CNT:FR|UN_ABC:France",
			"to Germany or France");
		addTranslationAndRetranslationRequests(
			"naar Duitsland of Frankrijk",
			"UN_ABC:naar NL_CNT:DE|UN_ABC:Duitsland EN_PRE:1|UN_ABC:of NL_CNT:FR|UN_ABC:Frankrijk",
			"naar Duitsland of Frankrijk");
		addTranslationAndRetranslationRequests(
			"You asshole",
			"UN_ABC:You EN_PRF:1|UN_ABC:asshole",
			"You asshole");
		addTranslationAndRetranslationRequests(
			"Jij klootzak",
			"UN_ABC:Jij NL_PRF:2|UN_ABC:klootzak",
			"Jij klootzak");
		addTranslationAndRetranslationRequests(
			"Can I book a room for 5 people?",
			"UN_ABC:Can UN_ABC:I UN_ABC:book UN_ABC:a UN_ABC:room UN_ABC:for UN_NUM:5 UN_ABC:people ?",
			"Can I book a room for 5 people?");
		addTranslationAndRetranslationRequests(
			"ten times five",
			"EN_NUM:10|NL_PRE:8|UN_ABC:ten EN_MTH:M|UN_ABC:times EN_NUM:5|UN_ABC:five",
			"ten multiplied by five");
		addTranslationAndRetranslationRequests(
			"tien keer vijf",
			"NL_NUM:10|UN_ABC:tien NL_MTH:M|UN_ABC:keer NL_NUM:5|UN_ABC:vijf",
			"tien vermenigvuldigd met vijf");
		addTranslationAndRetranslationRequests(
			"fifteen british pound",
			"EN_NUM:15|UN_ABC:fifteen EN_CUR:GBP",
			"fifteen british pound sterling");
		addTranslationAndRetranslationRequests(
			"vijftien euro",
			"NL_NUM:15|UN_ABC:vijftien EN_CUR:EUR|NL_CUR:EUR|UN_ABC:euro",
			"vijftien euro");
		addTranslationAndRetranslationRequests(
			":-) ]0: {;",
			"UN_SML:7 UN_FRN:28 UN_SML:42",
			":-) ]0: {;");
	}
	
	@Override
	protected void addLogLineForRequest(TesterRequest request) {
		TranslatorRequestResponse req = new TranslatorRequestResponse();
		req.fromJson(request.request);
		TranslatorRequestResponse res = new TranslatorRequestResponse();
		res.fromJson(request.response);
		if (req.sequence.length()>0) {
			addLogLineNoLock("Translation took: " + request.time + " ms");
			addLogLineNoLock("  <<< '" + req.sequence + "'");
			addLogLineNoLock("  >>> '" + res.translation + "'");
		}
	}
	
	private void addTranslationAndRetranslationRequests(String seq,String trans,String retrans) {
		addTranslationAndRetranslationRequests("",seq,trans,retrans);
	}
	
	private void addTranslationAndRetranslationRequests(String language,String seq,String trans,String retrans) {
		TranslatorRequestResponse req = null;
		TranslatorRequestResponse res = null;
		
		req = new TranslatorRequestResponse();
		if (language.length()>0) {
			req.languages.add(language);
			req.languages.add(Languages.UNI);
		}
		req.sequence.append(seq);
		res = new TranslatorRequestResponse();
		if (language.length()>0) {
			res.languages.add(language);
			res.languages.add(Languages.UNI);
		}
		res.translation.append(trans);
		addRequestNoLock(req,res);
		
		req = new TranslatorRequestResponse();
		req.translation.append(trans);
		res = new TranslatorRequestResponse();
		res.sequence.append(retrans);
		addRequestNoLock(req,res);
	}
}
