package codepred.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles({"dev", "test"})
@TestPropertySource(properties = {"spring.liquibase.enabled=true"})
@EasyRandomTest
public @interface RepoTest {

}
