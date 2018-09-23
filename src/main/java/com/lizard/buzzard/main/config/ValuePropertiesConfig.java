package com.lizard.buzzard.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * ${...} placeholders support in @value annotations in spring
 * SEE: http://blog.codeleak.pl/2015/09/placeholders-support-in-value.html
 * SEE: https://www.mkyong.com/spring/spring-is-not-working-in-value/
 * SEE: http://websystique.com/spring/spring-propertysource-value-annotations-example/
 */
@Configuration
@ComponentScan(basePackages = "com.lizard.buzzard")
@PropertySource("classpath:lizard.config.properties")
//@ConfigurationProperties(prefix = "lizard")
public class ValuePropertiesConfig {

    @Value("${lizard.verivication.token.expiration}")
    private String test;

    /**
     * PropertySourcesPlaceHolderConfigurer Bean only required for @Value("{}") annotations.
     * Remove this bean if you are not using @Value annotations for injecting properties.
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setLocation(new ClassPathResource("lizard.config.properties"));
        return c;
    }
}
