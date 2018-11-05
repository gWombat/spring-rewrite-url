package fr.gwombat.springrewriteurl.configuration;

import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.ConfigurationRuleBuilder;
import org.ocpsoft.rewrite.servlet.config.Forward;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Redirect;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.ocpsoft.rewrite.transposition.LocaleTransposition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Created by guillaume.
 *
 * @since 27/07/2018
 */
@Configuration
public class RewriteConfiguration {

    @Bean
    public ConfigurationBuilder rewriteRules() {
        ConfigurationBuilder configurationBuilder = ConfigurationRuleBuilder.begin();
        configurationBuilder
                .addRule()
                .when(Path.matches("/"))
                .perform(Redirect.temporary("/" + LocaleContextHolder.getLocale().getLanguage()))

                .addRule()
                .when(Path.matches("/{lang}").withRequestBinding())
                .perform(Forward.to("/"))

                .addRule(Join.path("/{lang}/{path}/{rest}").to("/{path}/{rest}"))
                .where("path").transposedBy(LocaleTransposition.bundle("urls.urls", "lang"));

        return configurationBuilder;
    }
}
