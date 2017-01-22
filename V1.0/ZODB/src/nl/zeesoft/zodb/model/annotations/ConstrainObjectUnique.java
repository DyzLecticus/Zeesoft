package nl.zeesoft.zodb.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstrainObjectUnique {
	/**
	 * Default value implements unique name
	 * 
	 * @return Array of colon (;) separated property name combinations
	 */
	public String[] properties() default {"name"}; 
}
