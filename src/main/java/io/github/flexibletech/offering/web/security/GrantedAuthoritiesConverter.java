package io.github.flexibletech.offering.web.security;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final Converter<Jwt, Collection<GrantedAuthority>> jwtScopeGrantedAuthoritiesConverter
            = new JwtGrantedAuthoritiesConverter();

    private static final GrantedAuthoritiesMapper authoritiesMapper = new SimpleAuthorityMapper();

    @Override
    public Collection<GrantedAuthority> convert(@NotNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(
                authoritiesMapper.mapAuthorities(getRealmRolesFrom(jwt)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())));

        Collection<GrantedAuthority> scopeAuthorities = jwtScopeGrantedAuthoritiesConverter.convert(jwt);
        if (!CollectionUtils.isEmpty(scopeAuthorities)) authorities.addAll(scopeAuthorities);

        return authorities;
    }

    private Set<String> getRealmRolesFrom(Jwt jwt) {
        var realmAccess = jwt.getClaimAsMap("realm_access");
        if (MapUtils.isEmpty(realmAccess)) return Collections.emptySet();

        @SuppressWarnings("unchecked")
        Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
        if (CollectionUtils.isEmpty(realmRoles)) return Collections.emptySet();

        return new HashSet<>(realmRoles);
    }

}
