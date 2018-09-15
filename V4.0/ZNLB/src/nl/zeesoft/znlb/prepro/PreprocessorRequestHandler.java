package nl.zeesoft.znlb.prepro;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class PreprocessorRequestHandler {
	private Preprocessor	processor	= null;
	
	public PreprocessorRequestHandler(Preprocessor prepro) {
		processor = prepro;
	}
	
	public void handleRequest(PreprocessorRequestResponse request) {
		request.processed = processor.process(new ZStringSymbolParser(request.sequence),request.languages);
	}
}
