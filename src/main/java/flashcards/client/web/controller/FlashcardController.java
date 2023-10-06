package flashcards.client.web.controller;

import flashcards.client.api_client.FlashcardClient;
import flashcards.client.api_client.TagClient;
import flashcards.client.model.FlashcardDto;
import flashcards.client.model.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@RequestMapping("/flashcards")
public class FlashcardController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final FlashcardClient flashcardClient;
    private final TagClient tagClient;

    @Autowired
    public FlashcardController(FlashcardClient flashcardClient, TagClient tagClient) {
        this.flashcardClient = flashcardClient;
        this.tagClient = tagClient;
    }

    @GetMapping("/study")
    public String study(Model model, HttpSession session) {
        logger.info("Fetching flashcards with tags: " + session.getAttribute("activeTags"));

        Collection<FlashcardDto> flashcards;

        if (Objects.isNull(session.getAttribute("activeTags")) ) {
            flashcards = flashcardClient.readAll();
        } else {
            flashcards = flashcardClient.readAllWithTags((Set<Integer>)session.getAttribute("activeTags"));
        }

        model.addAttribute("flashcards", flashcards);

        // also include all user's tags (display for filtering)
        Collection<TagDto> tags = tagClient.readAll();
        model.addAttribute("tags", tags);

        return "flashcards/study";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        Collection<FlashcardDto> flashcards = flashcardClient.readAll();

        model.addAttribute("flashcards", flashcards);

        // also include all user's tags (display for deleting tags)
        Collection<TagDto> tags = tagClient.readAll();
        model.addAttribute("tags", tags);

        return "flashcards/edit";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute FlashcardDto flashcardDto) {
        flashcardClient.create(flashcardDto);
        return "redirect:/flashcards/edit";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        flashcardClient.delete(id);
        return "redirect:/flashcards/edit";
    }

    @PostMapping("/{flashcardId}/add-tag/{tagId}")
    public String addTagToFlashcard(@PathVariable Long flashcardId, @PathVariable Integer tagId) {
        flashcardClient.addTagToFlashcard(flashcardId, tagId);
        return "redirect:/flashcards/edit";
    }

    @PostMapping("/{flashcardId}/remove-tag/{tagId}")
    public String removeTagFromFlashcard(@PathVariable Long flashcardId, @PathVariable Integer tagId) {
        flashcardClient.removeTagFromFlashcard(flashcardId, tagId);
        return "redirect:/flashcards/edit";
    }

}

