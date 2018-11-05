package fr.gwombat.springrewriteurl.interceptors;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by guillaume.
 *
 * @since 28/07/2018
 */
public class LocaleAttributeChangeInterceptor extends HandlerInterceptorAdapter {

    private static final String REQUEST_PARAM_LANG = "lang";

    private static final Logger logger = LoggerFactory.getLogger(LocaleAttributeChangeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String newLocale = request.getParameter(REQUEST_PARAM_LANG);
        if (newLocale != null) {
            Locale locale = resolveLocaleFromRequestParam(newLocale);

            final LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (localeResolver == null)
                throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");

            if (!AcceptHeaderLocaleResolver.class.isAssignableFrom(localeResolver.getClass()))
                return true;

            final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = (AcceptHeaderLocaleResolver) localeResolver;
            locale = checkLocaleAgainstSupportedLocales(locale, acceptHeaderLocaleResolver);

            if (locale == null) {
                logger.info("Locale {} is not available for this site. Falling back to default locale", newLocale);
                redirectToHomeWithDefaultLocale(response, acceptHeaderLocaleResolver.getDefaultLocale());
                return false;
            }

            logger.info("Setting new locale: {}", locale);
            LocaleContextHolder.setLocale(locale);
        }

        return true;
    }

    private static Locale resolveLocaleFromRequestParam(String strLocale) {
        try {
            return LocaleUtils.toLocale(strLocale);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Locale checkLocaleAgainstSupportedLocales(Locale locale, AcceptHeaderLocaleResolver localeResolver) {
        Locale defaultLocale = localeResolver.getDefaultLocale();

        if (locale == null)
            return defaultLocale;

        if (localeResolver.getSupportedLocales().contains(locale))
            return locale;

        return defaultLocale;
    }

    private void redirectToHomeWithDefaultLocale(HttpServletResponse response, Locale defaultLocale) throws IOException {
        final String uri = "/";
        String lang = "";
        if (defaultLocale != null)
            lang = defaultLocale.getLanguage();

        response.sendRedirect(uri + lang);
    }
}
