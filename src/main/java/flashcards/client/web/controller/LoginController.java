package flashcards.client.web.controller;

import flashcards.client.config.JwtAuthToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class LoginController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/login")
    public String login() {
        return "/auth/signin";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "auth/access-denied";
    }

    @GetMapping("/auth-info")
    public String info() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Auth info: " + auth.getPrincipal() + "/" + ((JwtAuthToken)auth).getJwtToken());
        return "index";
    }

}
