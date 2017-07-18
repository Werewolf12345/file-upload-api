package com.alexboriskin.testapiassignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class MyWebMvcConfig extends WebMvcConfigurerAdapter {
        
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
      configurer.favorPathExtension(true).
      favorParameter(true).
      parameterName("mediaType").
      ignoreAcceptHeader(false).
      useJaf(false).
      defaultContentType(MediaType.APPLICATION_JSON).
      mediaType("xml", MediaType.APPLICATION_XML).
      mediaType("json", MediaType.APPLICATION_JSON);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/static/images/**")
                .addResourceLocations("classpath:/static/images/");
    }

    @Bean
    public WebMvcConfigurerAdapter forwardToFileUpload() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("forward:/files/upload");
            }
        };
    }

}