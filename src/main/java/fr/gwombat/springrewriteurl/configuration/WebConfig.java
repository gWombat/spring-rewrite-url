package fr.gwombat.springrewriteurl.configuration;

import fr.gwombat.springrewriteurl.interceptors.LocaleAttributeChangeInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by guillaume.
 *
 * @since 28/07/2018
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleAttributeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    public LocaleResolver localeResolver(WebMvcProperties webMvcProperties) {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setSupportedLocales(supportedLocales());
        localeResolver.setDefaultLocale(webMvcProperties.getLocale());

        return localeResolver;
    }

    @Bean
    public List<Locale> supportedLocales() {
        return Arrays.asList(
                Locale.FRENCH,
                Locale.ENGLISH
        );
    }
}
