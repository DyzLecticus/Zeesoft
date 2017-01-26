package nl.zeesoft.zdm.model.transformations;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdm.model.transformations.impl.AddClass;
import nl.zeesoft.zdm.model.transformations.impl.AddPackage;
import nl.zeesoft.zdm.model.transformations.impl.AddProperty;
import nl.zeesoft.zdm.model.transformations.impl.ConvertModel;
import nl.zeesoft.zdm.model.transformations.impl.IncrementVersion;
import nl.zeesoft.zdm.model.transformations.impl.RemoveClass;
import nl.zeesoft.zdm.model.transformations.impl.RemovePackage;
import nl.zeesoft.zdm.model.transformations.impl.RemovePackageAll;
import nl.zeesoft.zdm.model.transformations.impl.RemoveProperty;
import nl.zeesoft.zdm.model.transformations.impl.RevertVersion;
import nl.zeesoft.zdm.model.transformations.impl.RevertVersionCurrent;
import nl.zeesoft.zdm.model.transformations.impl.SetClassAbstract;
import nl.zeesoft.zdm.model.transformations.impl.SetClassExtendsClass;
import nl.zeesoft.zdm.model.transformations.impl.SetClassName;
import nl.zeesoft.zdm.model.transformations.impl.SetPackageName;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyList;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyName;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyType;

/**
 * Transformation utilities. 
 */
public final class Transformations {
	private Transformations() {
		// Abstract
	}
		
	public final static List<TransformationObject> getAllTransformations() {
		List<TransformationObject> r = new ArrayList<TransformationObject>();
		r.add(new AddPackage());
		r.add(new SetPackageName());
		r.add(new RemovePackage());
		r.add(new RemovePackageAll());

		r.add(new AddClass());
		r.add(new SetClassAbstract());
		r.add(new SetClassExtendsClass());
		r.add(new SetClassName());
		r.add(new RemoveClass());

		r.add(new AddProperty());
		r.add(new SetPropertyList());
		r.add(new SetPropertyName());
		r.add(new SetPropertyType());
		r.add(new RemoveProperty());

		r.add(new IncrementVersion());
		r.add(new RevertVersion());
		r.add(new RevertVersionCurrent());

		r.add(new ConvertModel());
		return r;
	}
}
