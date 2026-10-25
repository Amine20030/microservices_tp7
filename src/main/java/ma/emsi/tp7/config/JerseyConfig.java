package ma.emsi.tp7.config;

import com.fasterxml.jackson.jakarta.rs.xml.JacksonXMLProvider;
import ma.emsi.tp7.api.CompteRestJaxRSAPI;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(CompteRestJaxRSAPI.class);
        register(JacksonXMLProvider.class);
    }
}
