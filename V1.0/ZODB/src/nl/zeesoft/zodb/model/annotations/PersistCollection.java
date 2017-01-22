package nl.zeesoft.zodb.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistCollection {
	public boolean entity() default true;
	public String module();
	public String nameSingle();
	public String nameMulti();
}
