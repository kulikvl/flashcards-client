package flashcards.client.api_client;

import flashcards.client.model.FlashcardDto;
import flashcards.client.model.TagDto;
import flashcards.client.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

@Component
public class UserClient {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final WebTarget usersEndpoint;

    private final FlashcardClient flashcardClient;
    private final TagClient tagClient;

    @Autowired
    public UserClient(@Value("${server.url}") String apiUrl, FlashcardClient flashcardClient, TagClient tagClient) {
        this.flashcardClient = flashcardClient;
        this.tagClient = tagClient;
        Client client = ClientBuilder.newClient();
        client.register(new JwtAuthFilter());
        usersEndpoint = client.target(apiUrl + "/users");
    }

    public Collection<UserDto> readAll() {
        Response response = usersEndpoint.request(MediaType.APPLICATION_JSON_TYPE).get();
        return Arrays.asList(response.readEntity(UserDto[].class));
    }

//    public void delete(String username) {
//        Response response = usersEndpoint.path("/" + username).request(MediaType.APPLICATION_JSON_TYPE).delete();
//    }

    // complex operation on the client side
    public void delete(String username) {
        // currently authenticated user can't delete himself
        if (username.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())) {
            logger.info("Currently authenticated user can't delete himself");
            return;
        }

        // delete user's flashcards (result = empty flashcards and tagged_flashcards tables for this user)
        for (FlashcardDto flashcardDto : flashcardClient.readAllFrom(username)) {
            flashcardClient.deleteFrom(flashcardDto.getId(), username);
        }

        // delete user's tags (result = empty tags table for this user)
        for (TagDto tagDto : tagClient.readAllFrom(username)) {
            tagClient.deleteFrom(tagDto.getId(), username);
        }

        // delete user
        usersEndpoint
                .path("/" + username)
                .request()
                .delete();
    }

}
