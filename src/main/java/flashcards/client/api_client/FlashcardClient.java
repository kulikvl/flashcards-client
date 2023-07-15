package flashcards.client.api_client;

import flashcards.client.model.FlashcardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;

@Component
public class FlashcardClient {

    private Client client;

    private String apiUrl;

    private WebTarget flashcardsEndpoint;

    @Autowired
    public FlashcardClient(@Value("${server.url}") String apiUrl) {
        this.apiUrl = apiUrl;
        client = ClientBuilder.newClient();
        client.register(new HttpBasicAuthFilter());
        flashcardsEndpoint = client.target(apiUrl + "/users/{id}/flashcards");
    }

    public FlashcardDto create(FlashcardDto e) {
        Invocation.Builder invocationBuilder = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).request(MediaType.APPLICATION_JSON_TYPE);
        Entity<FlashcardDto> entity = Entity.entity(e, MediaType.APPLICATION_JSON_TYPE);
        return invocationBuilder.post(entity, FlashcardDto.class);
    }

    public Collection<FlashcardDto> readAll() {
        Response response = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).request(MediaType.APPLICATION_JSON_TYPE).get();

        if (response.getStatus() == 200) {
            return Arrays.asList(response.readEntity(FlashcardDto[].class));
        } else {
            throw new RuntimeException(response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
        }
    }

    public Collection<FlashcardDto> readAllWithTags(Collection<Integer> tagIds) {

        WebTarget target = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        for (Integer tagId : tagIds) {
            target = target.queryParam("tags", tagId);
        }

        System.out.println("DEBUG: " + target.getUri());

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

        if (response.getStatus() == 200) {
            return Arrays.asList(response.readEntity(FlashcardDto[].class));
        } else {
            throw new RuntimeException(response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
        }
    }

    public void update(FlashcardDto e) {
        Invocation.Builder invocationBuilder = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).request(MediaType.APPLICATION_JSON_TYPE);
        Entity<FlashcardDto> entity = Entity.entity(e, MediaType.APPLICATION_JSON_TYPE);

        Response response = invocationBuilder.put(entity);

        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }


    public void delete(Long id) {
        Response response = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).path("/" + id).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    public void addTagToFlashcard(Long flashcardId, Integer tagId) {
        Response response = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .path("/" + flashcardId + "/tags/" + tagId).request(MediaType.APPLICATION_JSON_TYPE).post(null);

        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }

    public void removeTagFromFlashcard(Long flashcardId, Integer tagId) {
        Response response = flashcardsEndpoint.resolveTemplate("id", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .path("/" + flashcardId + "/tags/" + tagId).request(MediaType.APPLICATION_JSON_TYPE).delete();

        if (response.getStatus() > 200)
            throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
    }

}

