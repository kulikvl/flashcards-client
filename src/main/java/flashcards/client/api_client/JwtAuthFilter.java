package flashcards.client.api_client;

import flashcards.client.config.JwtAuthToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class JwtAuthFilter implements ClientRequestFilter {

    public JwtAuthFilter() {

    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return;
        }

        requestContext.getHeaders().add("Authorization", "Bearer " + ((JwtAuthToken)authentication).getJwtToken());
    }
}
