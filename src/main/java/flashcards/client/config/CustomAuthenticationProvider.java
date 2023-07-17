package flashcards.client.config;

import flashcards.client.model.UserDetailsDto;
import flashcards.client.model.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.util.*;
import java.util.logging.Logger;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final String apiUrl;

    public CustomAuthenticationProvider(@Value("${server.url}") String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        logger.info("Authentication process started");

        String inputUsername = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();

        String jwtToken = null;

        try {
            UserDto userDto = new UserDto(inputUsername, inputPassword);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserDto> requestEntity = new HttpEntity<>(userDto, headers);

            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            apiUrl + "/auth/login",
                            HttpMethod.POST,
                            requestEntity,
                            new ParameterizedTypeReference<Map<String, Object>>() {});

            Map<String, Object> jwtTokenPair = response.getBody();

            if (jwtTokenPair == null) {
                throw new NullPointerException("jwtTokenPair is null");
            }

            jwtToken = jwtTokenPair.get("jwt-token").toString();

            logger.info("JWT token: " + jwtToken);

        } catch (HttpClientErrorException e) {
            logger.warning("Failed authentication of credentials: " + inputUsername + "/" + inputPassword);
            return null;
        } catch (RuntimeException e) {
            logger.warning("Failed authentication of credentials: " + inputUsername + "/" + inputPassword + " because: " + e.getMessage());
            return null;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(jwtToken);
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

            ResponseEntity<UserDetailsDto> response = restTemplate.exchange(apiUrl + "/auth/info", HttpMethod.GET, requestEntity, UserDetailsDto.class);

            UserDetailsDto userDetailsDto = response.getBody();

            if (userDetailsDto == null) {
                throw new NullPointerException("userDetailsDto is null");
            }

            logger.info("UserDetailsDto: " + userDetailsDto);

            Collection<String> roles = userDetailsDto.getRoles();

            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            roles.forEach(r -> grantedAuths.add(new SimpleGrantedAuthority(r)));

            JwtAuthToken token = new JwtAuthToken(inputUsername, inputPassword, grantedAuths);
            token.setJwtToken(jwtToken);

            logger.info("Authenticated successfully with credentials: " + inputUsername + "/" + inputPassword);

            return token;

        } catch (HttpClientErrorException e) {
            logger.warning("Failed authentication of credentials: " + inputUsername + "/" + inputPassword);
            return null;
        } catch (RuntimeException e) {
            logger.warning("Failed authentication of credentials: " + inputUsername + "/" + inputPassword + " because: " + e.getMessage());
            return null;
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
