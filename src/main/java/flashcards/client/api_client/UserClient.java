package flashcards.client.api_client;

import flashcards.client.model.UserDto;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Component
public class UserClient {

    private Client client;

    private String apiUrl;

    private WebTarget usersEndpoint;

    @Autowired
    public UserClient(@Value("${server.url}") String apiUrl) {
        this.apiUrl = apiUrl;
        client = ClientBuilder.newClient();
        client.register(new HttpBasicAuthFilter());
        usersEndpoint = client.target(apiUrl + "/users");
    }

//    private void resetTargets() {
//        usersEndpoint = client.target(apiUrl + "/users");
//        singleEndpointTemplate = usersEndpoint.path("/{id}");
//    }

//    public void setCurrentUser(String id) {
//        singleUserEndpoint = singleEndpointTemplate.resolveTemplate("id", id);
//        Response r = singleUserEndpoint.request().get();
//        if (r.getStatus() != 200) throw new RuntimeException("Does the user with id \"" + id + "\" exist? Response: " + r.getStatus() + " " + r.getStatusInfo().getReasonPhrase());
//    }

//    public void authenticateRequests(String username, String password) {
//        Client authenticatedClient = ClientBuilder.newClient();
//        authenticatedClient.register(new HttpBasicAuthFilter());
//        HttpAuthenticationFeature securityFeature = HttpAuthenticationFeature.basic(username, password);
//        authenticatedClient.register(securityFeature);
//        client.close();
//        client = authenticatedClient;
//        resetTargets();
//    }

    public UserDto create(UserDto e) {
        Invocation.Builder invocationBuilder = usersEndpoint.path("/" + e.getUsername()).request(MediaType.APPLICATION_JSON_TYPE);
        Entity<UserDto> entity = Entity.entity(e, MediaType.APPLICATION_JSON_TYPE);
        return invocationBuilder.post(entity, UserDto.class);
    }

    public Collection<UserDto> readAll() {
        Response response = usersEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();

        if (response.getStatus() == 200) {
            return Arrays.asList(response.readEntity(UserDto[].class));
        } else {
            throw new RuntimeException(response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
        }
    }

    public void update(UserDto e) {
        Invocation.Builder invocationBuilder = usersEndpoint.request(MediaType.APPLICATION_JSON_TYPE);
        Entity<UserDto> entity = Entity.entity(e, MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.put(entity);

        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }


    public void delete(String username) {
        Response response = usersEndpoint.path("/" + username).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

}
