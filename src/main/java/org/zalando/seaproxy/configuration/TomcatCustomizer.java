package org.zalando.seaproxy.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

import org.springframework.stereotype.Component;

@Component
public class TomcatCustomizer implements EmbeddedServletContainerCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatCustomizer.class);

    @Value("${enable_ssl:false}")
    private boolean isSecure;

    @Override
    public void customize(final ConfigurableEmbeddedServletContainer container) {
        if (container instanceof TomcatEmbeddedServletContainerFactory) {
            if (isSecure) {
                final TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(connector -> { connector.setScheme("https"); });
                LOGGER.info("Enabled secure scheme (https).");
            } else {
                LOGGER.info("Using insecure scheme (http).");
            }
        } else {
            LOGGER.warn("Could not change protocol scheme because Tomcat is not used as servlet container.");
        }
    }
}
