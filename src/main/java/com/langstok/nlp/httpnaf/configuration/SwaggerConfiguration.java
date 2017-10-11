package com.langstok.nlp.httpnaf.configuration;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("LANGSTOK NLP SOURCE-HTTP-NAF")
                .description("NLP naf source for langstok NLP pipeline (newsreader adaptation)")
                //.termsOfServiceUrl("http://springfox.io")
                .license("Copyright colourful.news")
                //.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("1.0")
                .build();
    }
}
