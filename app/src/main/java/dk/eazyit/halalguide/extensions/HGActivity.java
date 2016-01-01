package dk.eazyit.halalguide.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Privat on 30/12/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HGActivity {

    int layout() default -1;

    boolean swipeEnabled() default false;

    String screenName();
}
