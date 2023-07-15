package flashcards.client.web.controller;

import flashcards.client.api_client.UserClient;
import flashcards.client.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.logging.Logger;

@Controller
@RequestMapping("/users")
public class UserController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private UserClient userClient;

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

    @PostMapping("/edit")
    public String editUser(@ModelAttribute UserDto data) {
        logger.warning("Nothing happens on editing user, but soon...");
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userClient.delete(id);
        return "redirect:/users";
    }

//    @GetMapping("/add")
//    public String addUser(Model model) {
//        model.addAttribute("userModel", new UserDto());
//        return "users/add";
//    }
//
//    @PostMapping("/add")
//    public String addUserSubmit(@ModelAttribute UserDto data, Model model) {
//        System.out.println("I got: " + data);
//        UserDto testDto = (UserDto) model.getAttribute("userModel");
//        System.out.println("I have to fill: " + testDto);
//
//        try {
//            UserDto userDto = userService.create(data);
//            model.addAttribute("success", true);
//            model.addAttribute("successMessage", "User successfully created with ID: " + userDto.getUsername());
//            model.addAttribute("userModel", new UserDto());
//        }
//        catch (Exception e) {
//            model.addAttribute("success", false);
//            model.addAttribute("errorMessage", e.getMessage());
//            model.addAttribute("userModel", new UserDto());
//        }
//
////        return "users/add";
//        // return "users/index"; - that would be incorrect, because we send our one empty userDto (model) to the view page (users/index), that requires a collection of users as an input model!!!
//
//         /*
//             Correct way: This is a common pattern for a "Post/Redirect/Get" (PRG) flow.
//             1) When a client submits the form, a POST request is sent to the server.
//             2) The server-side code processes this request, then issues a redirect to the client (browser).
//             3) The browser automatically makes a GET request to the new URL (in this case, /employees/list). This results in seeing a list of users.
//         */
//         return "redirect:/users";
//    }

}
