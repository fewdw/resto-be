package dev.resto.fal.util;

import dev.resto.fal.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class OauthHelper {

    public static String getValue(String key, OAuth2User principal){
        if (principal == null) {
            throw new NotFoundException("User not found in request");
        }

        Map<String, Object> attributes = principal.getAttributes();
        String id = (String) attributes.get(key);

        if (id == null) {
            throw new NotFoundException("User not found in request");
        }

        return id;
    }

    public static String getId(OAuth2User principal) {
        return getValue("sub", principal);
    }

    public static String getEmail(OAuth2User principal) {
        return getValue("email", principal);
    }
}
