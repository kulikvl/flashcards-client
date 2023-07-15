package flashcards.client.api_client;

import flashcards.client.config.AuthTokenImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filter: add the authorization header to requests to the REST API Server, IF the user is authenticated
 */
@Provider
public class HttpBasicAuthFilter implements ClientRequestFilter {

    public HttpBasicAuthFilter() {

    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return;
        }

        String token = authentication.getPrincipal().toString() + ":" + ((AuthTokenImpl) authentication).password;
        String base64Token = DatatypeConverter.printBase64Binary(token.getBytes(StandardCharsets.UTF_8));
        requestContext.getHeaders().add("Authorization", "Basic " + base64Token);
    }
}
