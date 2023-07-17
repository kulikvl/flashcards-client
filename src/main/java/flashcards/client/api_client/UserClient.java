package flashcards.client.api_client;

import flashcards.client.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;

@Component
public class UserClient {

    private final WebTarget usersEndpoint;

    @Autowired
    public UserClient(@Value("${server.url}") String apiUrl) {
        Client client = ClientBuilder.newClient();
        client.register(new JwtAuthFilter());
        usersEndpoint = client.target(apiUrl + "/users");
    }

    public Collection<UserDto> readAll() {
        Response response = usersEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();
        return Arrays.asList(response.readEntity(UserDto[].class));

//        if (response.getStatus() == 200) {
//            return Arrays.asList(response.readEntity(UserDto[].class));
//        } else {
//            throw new RuntimeException(response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
//        }
    }

    public void delete(String username) {
        Response response = usersEndpoint.path("/" + username).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

}
