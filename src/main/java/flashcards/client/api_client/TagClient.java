package flashcards.client.api_client;

import flashcards.client.model.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Component
public class TagClient {

    private final WebTarget baseEndpoint;

    @Autowired
    public TagClient(@Value("${server.url}") String apiUrl) {
        Client client = ClientBuilder.newClient();
        client.register(new JwtAuthFilter());
        baseEndpoint = client.target(apiUrl + "/users/{id}/tags");
    }

    public void create(TagDto e) {
        // if the tag with the same name already exists => use it instead of creating a new one
        Optional<TagDto> foundTag = readAll().stream().filter(tag -> tag.getName().equals(e.getName())).findFirst();
        if (foundTag.isPresent()) {
            return;
        }

        Invocation.Builder invocationBuilder = baseEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).request(MediaType.APPLICATION_JSON_TYPE);
        Entity<TagDto> entity = Entity.entity(e, MediaType.APPLICATION_JSON_TYPE);
        invocationBuilder.post(entity, TagDto.class);
    }

    public Collection<TagDto> readAll() {
        Response response = baseEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).request(MediaType.APPLICATION_JSON_TYPE).get();
        return Arrays.asList(response.readEntity(TagDto[].class));
    }

    public Collection<TagDto> readAllFrom(String username) {
        Response response = baseEndpoint.resolveTemplate("id", username).request(MediaType.APPLICATION_JSON_TYPE).get();
        return Arrays.asList(response.readEntity(TagDto[].class));
    }

    public void delete(Integer id) {
        baseEndpoint
                .resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .path("/" + id)
                .request()
                .delete();
    }

    public void deleteFrom(Integer id, String username) {
        baseEndpoint
                .resolveTemplate("id", username)
                .path("/" + id)
                .request()
                .delete();
    }

}


