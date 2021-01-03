package nl.zeesoft.zdbd.api.javascript;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class IndexJs extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		append(r,(new MainJs()).render());
		append(r,(new ModalJs()).render());
		append(r,(new StateJs()).render());
		append(r,(new BindingsJs()).render());
		append(r,(new MenuJs()).render());
		append(r,(new ThemeJs()).render());
		append(r,(new SequenceJs()).render());
		append(r,(new SequencerJs()).render());
		append(r,(new NetworkJs()).render());
		append(r,(new GeneratorsJs()).render());
		return r;
	}

}
