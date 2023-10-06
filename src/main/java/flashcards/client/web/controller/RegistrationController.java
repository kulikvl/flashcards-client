package flashcards.client.web.controller;

import flashcards.client.model.UserDto;
import flashcards.client.validation.RegistrationUser;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/showRegistrationForm")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationUser", new RegistrationUser());
        return "auth/registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(@Valid @ModelAttribute("registrationUser") RegistrationUser registrationUser, BindingResult bindingResult, Model model) {

        logger.info("Processing registration form for: " + registrationUser);

        if (bindingResult.hasErrors()) {
            logger.info("Registration form contains errors");
            return "auth/registration-form";
        }

        UserDto userDto = new UserDto(registrationUser.getUsername(), registrationUser.getPassword());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/auth/register", requestEntity, String.class);
        } catch (HttpClientErrorException e) {
           // catch HTTP 4xx error server responds (our case is 409)

            model.addAttribute("registrationUser", new RegistrationUser());
            model.addAttribute("registrationError", "Username already exists");

            logger.warning("Username already exists - " + e.getMessage());
            return "auth/registration-form";

        }

        logger.info("Successfully created user: " + registrationUser.getUsername());

        return "auth/registration-confirmation";
    }

}
