package flashcards.client.web.controller;

import flashcards.client.api_client.TagClient;
import flashcards.client.model.FlashcardDto;
import flashcards.client.model.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagClient tagClient;

    @Autowired
    public TagController(TagClient tagClient) {
        this.tagClient = tagClient;
    }

    @PostMapping("/add")
    public String create(@ModelAttribute TagDto tagDto) {
        tagClient.create(tagDto);
        return "redirect:/flashcards/edit";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        tagClient.delete(id);
        return "redirect:/flashcards/edit";
    }

    @PostMapping("/apply/{id}")
    public String applyTag(@PathVariable Integer id, HttpSession session) {

        if (Objects.isNull(session.getAttribute("activeTags"))) {
            Set<Integer> tagIds = new HashSet<>();
            tagIds.add(id);
            session.setAttribute("activeTags", tagIds);
        } else {
            Set<Integer> tagIds = (Set<Integer>) session.getAttribute("activeTags");
            tagIds.add(id);
            session.setAttribute("activeTags", tagIds);
        }

        return "redirect:/flashcards/study";
    }

    @PostMapping("/clear")
    public String clearTags(HttpSession session) {
        session.removeAttribute("activeTags");
        return "redirect:/flashcards/study";
    }

}
