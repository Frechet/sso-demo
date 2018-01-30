package ru.intabia.sso.demo.backend.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.OffsetDateTime;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * Configuration for swagger.
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {

    /**
     * Swagger api configuration.
     *
     * @param typeResolver {@link TypeResolver}
     * @return api config
     */
    @Bean
    public Docket clientControllerApi(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.intabia.sso.demo.backend.controller"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(OffsetDateTime.class, String.class)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class))
                );
    }
}
