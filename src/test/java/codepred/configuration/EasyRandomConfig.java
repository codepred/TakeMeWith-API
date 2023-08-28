package codepred.configuration;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EasyRandomConfig {

//    @Bean
//    public EasyRandom easyRandom() {
//        final var parameters = new EasyRandomParameters()
//            .collectionSizeRange(1, 3)
//            .randomize(XMLGregorianCalendar.class, new AbstractRandomizer<>() {
//                @Override
//                public XMLGregorianCalendar getRandomValue() {
//                    try {
//                        return DatatypeFactory.newInstance()
//                            .newXMLGregorianCalendar(
//                                (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC)));
//                    } catch (DatatypeConfigurationException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            })
//            .randomize(BaseEntity.class, () -> null)
//            .randomize(GeschaedigtesObjekt.class, () -> null)
//            .randomize(LocalTime.class, LocalTime::now);
//        return new EasyRandom(parameters);
//    }
}
