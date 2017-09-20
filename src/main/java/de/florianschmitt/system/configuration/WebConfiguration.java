package de.florianschmitt.system.configuration;

import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo.EmptyRoleSemantic;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.*;

import java.time.Duration;
import java.util.Collections;

@Configuration
class WebConfiguration {

    @Value("${server.port}")
    private int serverPort;

    @Value("${server.openRedirectionPublicPort:true}")
    private boolean openRedirectionPublicPort;

    @Value("${server.usehttp2:true}")
    private boolean serverUseHttp2;

    @Value("${server.publicPort:80}")
    private int serverPublicPort;

    @Value("${application.allowedOrigins}")
    private String[] allowedOrigins;

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "HEAD", "DELETE");
            }
        };
    }

    @Bean
    WebMvcConfigurer angularResourceConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/ui/**")//
                        .addResourceLocations("classpath:/META-INF/resources/webjars/frontend-request/")//
                        .setCachePeriod((int) Duration.ofHours(1L).getSeconds());

                registry.addResourceHandler("/adminui/**")//
                        .addResourceLocations("classpath:/META-INF/resources/webjars/frontend-admin/")//
                        .setCachePeriod((int) Duration.ofHours(1L).getSeconds());
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/ui").setViewName("forward:/ui/index.html");
                registry.addViewController("/adminui").setViewName("forward:/adminui/index.html");
            }
        };
    }

    @Bean
    ErrorViewResolver redirectDeepPathsOfAngularToIndex() {
        return (request, status, model) -> {
            if (status == HttpStatus.NOT_FOUND) {
                String path = (String) model.get("path");
                if (path.startsWith("/ui")) {
                    return new ModelAndView("/ui/index.html", Collections.emptyMap(), HttpStatus.OK);
                } else if (path.startsWith("/adminui")) {
                    return new ModelAndView("/adminui/index.html", Collections.emptyMap(), HttpStatus.OK);
                }
            }
            return null;
        };
    }

    @Bean
    UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();

        factory.addDeploymentInfoCustomizers(servletBuilder -> {
            servletBuilder
                    .addSecurityConstraint(new SecurityConstraint()
                            .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                            .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                            .setEmptyRoleSemantic(EmptyRoleSemantic.PERMIT))
                    .setConfidentialPortManager(exchange -> serverPort);
        });


        if (openRedirectionPublicPort) {
            factory.addBuilderCustomizers(builder -> {
                builder.addHttpListener(serverPublicPort, "0.0.0.0");
            });
        }

        if (serverUseHttp2) {
            factory.addBuilderCustomizers(builder -> {
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            });
        }

        return factory;
    }
}
