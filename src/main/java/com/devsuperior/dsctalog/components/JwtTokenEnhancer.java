package com.devsuperior.dsctalog.components;

import com.devsuperior.dsctalog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
@RequiredArgsConstructor
public class JwtTokenEnhancer implements TokenEnhancer {
    private final UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, @NonNull OAuth2Authentication authentication) {
        var user = userRepository.findByEmail(authentication.getName());
        var map = new HashMap<String, Object>();

        map.put("userFirstName", user.getFirstName());
        map.put("userId", user.getId());

        var token = (DefaultOAuth2AccessToken) accessToken;
        token.setAdditionalInformation(map);

        return accessToken;
    }
}
