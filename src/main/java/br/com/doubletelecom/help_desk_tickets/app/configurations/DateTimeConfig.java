/**
 * Configuration class for setting up date and time formatting in the application.
 * Extends {@link WebMvcConfigurationSupport} to customize the MVC configuration.
 *
 * <p>This class defines a bean for {@link FormattingConversionService} to handle
 * date and time formatting using custom patterns. It registers formatters for
 * both date and date-time formats.</p>
 *
 * <p>The date format used is "dd.MM.yyyy" and the date-time format used is "dd.MM.yyyy HH:mm:ss".</p>
 *
 * <p>Additionally, this class adds a {@link PageableHandlerMethodArgumentResolver} to the list
 * of argument resolvers to support pagination in request handling.</p>
 *
 * @see WebMvcConfigurationSupport
 * @see FormattingConversionService
 * @see DateTimeFormatterRegistrar
 * @see DateFormatterRegistrar
 * @see PageableHandlerMethodArgumentResolver
 */

package br.com.doubletelecom.help_desk_tickets.app.configurations;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class DateTimeConfig extends WebMvcConfigurationSupport {

    @Bean
    @Override
    @NonNull
    public FormattingConversionService mvcConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar dateTimeRegistrar = new DateTimeFormatterRegistrar();
        dateTimeRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        dateTimeRegistrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        dateTimeRegistrar.registerFormatters(conversionService);

        DateFormatterRegistrar dateRegistrar = new DateFormatterRegistrar();
        dateRegistrar.setFormatter(new DateFormatter("dd.MM.yyyy"));
        dateRegistrar.registerFormatters(conversionService);

        return conversionService;
    }

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }

    
}