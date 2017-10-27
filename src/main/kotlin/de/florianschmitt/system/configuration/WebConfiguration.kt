package de.florianschmitt.system.configuration

import io.undertow.UndertowOptions
import io.undertow.servlet.api.SecurityConstraint
import io.undertow.servlet.api.SecurityInfo.EmptyRoleSemantic
import io.undertow.servlet.api.TransportGuaranteeType
import io.undertow.servlet.api.WebResourceCollection
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.Duration
import javax.servlet.http.HttpServletRequest

@Configuration
internal class WebConfiguration {


    @Value("\${server.port}")
    private var serverPort: Int? = null

    @Value("\${server.openRedirectionPublicPort:true}")
    private var openRedirectionPublicPort: Boolean = true

    @Value("\${server.usehttp2:true}")
    private var serverUseHttp2: Boolean = true

    @Value("\${server.publicPort:80}")
    private var serverPublicPort: Int = 0

    @Value("\${application.allowedOrigins}")
    private lateinit var allowedOrigins: Array<String>

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry?) {
                registry!!.addMapping("/**")
                        .allowedOrigins(*allowedOrigins)
                        .allowedMethods("GET", "POST", "HEAD", "DELETE")
            }
        }
    }

    @Bean
    fun angularResourceConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addResourceHandlers(registry: ResourceHandlerRegistry?) {
                registry!!.addResourceHandler("/ui/**")//
                        .addResourceLocations("classpath:/META-INF/resources/webjars/frontend-request/")//
                        .setCachePeriod(Duration.ofHours(1L).seconds.toInt())

                registry.addResourceHandler("/adminui/**")//
                        .addResourceLocations("classpath:/META-INF/resources/webjars/frontend-admin/")//
                        .setCachePeriod(Duration.ofHours(1L).seconds.toInt())
            }

            override fun addViewControllers(registry: ViewControllerRegistry?) {
                registry!!.addViewController("/ui").setViewName("forward:/ui/index.html")
                registry.addViewController("/adminui").setViewName("forward:/adminui/index.html")
            }
        }
    }

    @Bean
    fun redirectDeepPathsOfAngularToIndex(): ErrorViewResolver {
        return object : ErrorViewResolver {
            override fun resolveErrorView(request: HttpServletRequest?, status: HttpStatus?, model: MutableMap<String, Any>?): ModelAndView? {
                if (status == HttpStatus.NOT_FOUND) {
                    val path = model!!["path"] as String
                    if (path.startsWith("/ui")) {
                        return ModelAndView("/ui/index.html", emptyMap<String, Any>(), HttpStatus.OK)
                    } else if (path.startsWith("/adminui")) {
                        return ModelAndView("/adminui/index.html", emptyMap<String, Any>(), HttpStatus.OK)
                    }
                }
                return null
            }
        }
    }

    @Bean
    fun embeddedServletContainerFactory(): UndertowServletWebServerFactory {
        val factory = UndertowServletWebServerFactory()

        factory.addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer {
            it.addSecurityConstraint(SecurityConstraint()
                    .addWebResourceCollection(WebResourceCollection().addUrlPattern("/*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(EmptyRoleSemantic.PERMIT))
                    .setConfidentialPortManager({ _ -> serverPort!! })
        })


        if (openRedirectionPublicPort) {
            factory.addBuilderCustomizers(UndertowBuilderCustomizer { it.addHttpListener(serverPublicPort, "0.0.0.0") })
        }

        if (serverUseHttp2) {
            factory.addBuilderCustomizers(UndertowBuilderCustomizer { it.setServerOption(UndertowOptions.ENABLE_HTTP2, true) })
        }

        return factory
    }
}
