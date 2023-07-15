package flashcards.client.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.stereotype.Component;

import java.util.Collection;

public class AuthTokenImpl extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public AuthTokenImpl(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthTokenImpl(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    // TODO: Do Better way with tokens
    public String password;
}
