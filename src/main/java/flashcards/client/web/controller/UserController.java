package flashcards.client.web.controller;

import flashcards.client.api_client.UserClient;
import flashcards.client.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping
    public String getAll(Model model) {
        Collection<UserDto> users = userClient.readAll();
        model.addAttribute("users", users);
        return "users/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userClient.delete(id);
        return "redirect:/users";
    }

}
