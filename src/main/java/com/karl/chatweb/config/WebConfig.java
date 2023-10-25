package com.karl.chatweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cache control is NOT set here in this example project, but will likely be useful in a real application

        // It might still be handy to route to a static assets directory on the server (e.g. for images, or scripts or
        // css that is not part of the single page application)
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");

        // In the classpath locations referenced here, "app" is the output directory that is configured for the
        // maven-resources-plugin in the pom - the name is arbitrary - when the client application is built it is copied
        // to this directory

        // Explicitly map a request for the index.html page to the published client application main page (this is the
        // mapping for the "forward:/index.html" view controller below)
        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/app/index.html");

        // The client application build uses a "assets" directory to contain CSS, JS and media files
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/app/assets/");

        // Unfortunately these special-case explicit mappings are required for the client application support files in
        // the root directory (like the manifest json, the service worker script and so on) - this is to make sure we
        // can still map correctly to these files and keep our catch-all request mapping
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/app/images/");
        registry.addResourceHandler("/themes/**").addResourceLocations("classpath:/app/themes/");
        registry.addResourceHandler("/*.ico").addResourceLocations("classpath:/app/");

        // We want the resource handlers to be tried before the controllers (the particular number does not really
        // matter, but it must be lower than the corresponding controller registry order)
        registry.setOrder(-1000);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // We do not have a redirect here from "index.html" to "/" for two reasons:
        //  1. the resource handlers now by design have higher priority than the view controllers, so the "/index.html"
        //     mapping in the resource handlers will override any "/index.html" mapping here
        //  2. it does not matter anyway because we can have the client application router simply redirect "/index.html"
        //     to the application root to keep the address bar clean

        // A catch-all for the web-service API routes to respond with the NOT_FOUND status code
        registry.addStatusController("/api/**", HttpStatus.NOT_FOUND);

        // A catch-all for everything else to forward to the single page web application, client routing will take over
        registry.addViewController("/**").setViewName("forward:/index.html");

        // We want the controllers to be tried after the resource handlers (the particular number does not really
        //  matter, but it must be higher than the corresponding resource registry order)
        registry.setOrder(1000);
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver();
    }
}