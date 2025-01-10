/**
 * Configuration class for message sources, locale resolution, and locale change interception.
 * This class implements the {@link WebMvcConfigurer} interface to add custom interceptors.
 * 
 * <p>It provides the following beans:</p>
 * <ul>
 *   <li>{@link MessageSource} - Configures the message source for internationalization with support for reloading.</li>
 *   <li>{@link LocalValidatorFactoryBean} - Configures the validator to use the message source for validation messages.</li>
 *   <li>{@link LocaleResolver} - Configures the locale resolver to use session-based locale resolution with a default locale of US.</li>
 *   <li>{@link LocaleChangeInterceptor} - Configures the interceptor to allow changing the current locale via a request parameter.</li>
 * </ul>
 * 
 * <p>Additionally, it overrides the {@code addInterceptors} method to register the {@link LocaleChangeInterceptor}.</p>
 */
package br.com.doubletelecom.help_desk_tickets.app.configurations;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class MessageConfig implements WebMvcConfigurer {
    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/business/business", "classpath:/messages/validation/validations");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
