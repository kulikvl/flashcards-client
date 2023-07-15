package flashcards.client.config;

import flashcards.client.api_client.UserClient;
import flashcards.client.model.UserDto;
import flashcards.client.validation.RegistrationUser;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private String apiUrl;

    public CustomAuthenticationProvider(@Value("${server.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDto userDto = new UserDto(username, password);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

        ResponseEntity<String[]> responseEntity = null;

        try {
            responseEntity = restTemplate.postForEntity(apiUrl + "/users/authenticate", requestEntity, String[].class);
            logger.info("Authenticated successfully with credentials: " + username + "/" + password);
        } catch (HttpClientErrorException e) {
            logger.warning("Failed authentication of credentials: " + username + "/" + password);
            return null;
        }

        String[] roles = responseEntity.getBody();
//        Arrays.stream(roles).forEach(System.out::println);

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        Arrays.stream(roles).forEach(r -> grantedAuths.add(new SimpleGrantedAuthority(r)));
        Authentication auth = new AuthTokenImpl(username, password, grantedAuths);
        ((AuthTokenImpl) auth ).password = password;

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class); // Why can't be AuthTokenImpl? - still open question
    }
}
