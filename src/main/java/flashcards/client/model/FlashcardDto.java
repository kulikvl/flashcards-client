package flashcards.client.model;

import java.util.Collection;
import java.util.Set;

public class FlashcardDto {

    private Long id;

    private String front;

    private String back;

    private String authorUsername;

    private Collection<TagDto> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Collection<TagDto> getTags() {
        return tags;
    }

    public void setTags(Collection<TagDto> tags) {
        this.tags = tags;
    }
}

