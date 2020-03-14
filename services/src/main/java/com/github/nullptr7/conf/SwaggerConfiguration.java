package com.github.nullptr7.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket moviesAPI() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.basePackage("org.tiaa.controller"))
                                                      .build()
                                                      .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {

        Contact contact = new Contact("nullptr7", "https://www.tiaa.org", "Ishan.Shah@in.tiaa.org");

        StringVendorExtension listVendorExtension = new StringVendorExtension("Swagger with Java 11", "Demo");
        return new ApiInfo("Java 11 POC",
                "Employee RestFul service",
                "1.0",
                "",
                contact,
                "Employee RestFul Service - Source Code",
                "https://www.github.com",
                singletonList(listVendorExtension));
    }
}
