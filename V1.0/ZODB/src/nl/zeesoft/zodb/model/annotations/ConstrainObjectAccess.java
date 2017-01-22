package nl.zeesoft.zodb.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.zeesoft.zodb.model.impl.DbUser;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstrainObjectAccess {
	public int userLevelFetch() 	default DbUser.USER_LEVEL_MAX; 
	public int userLevelUpdate() 	default DbUser.USER_LEVEL_MAX; 
	public int userLevelRemove() 	default DbUser.USER_LEVEL_MAX; 
	public int userLevelAdd() 		default DbUser.USER_LEVEL_MAX; 
}
