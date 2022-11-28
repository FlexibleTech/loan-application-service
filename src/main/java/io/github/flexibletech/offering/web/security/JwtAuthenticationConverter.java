package io.github.flexibletech.offering.web.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter;

    public JwtAuthenticationConverter(Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter) {
        this.grantedAuthoritiesConverter = grantedAuthoritiesConverter;
    }

    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
        String username = getUsernameFrom(jwt);
        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    private String getUsernameFrom(Jwt jwt) {
        if (jwt.hasClaim("preferred_username"))
            return jwt.getClaimAsString("preferred_username");
        return jwt.getSubject();
    }

}
