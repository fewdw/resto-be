package dev.resto.fal.util;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class OauthUsername {

    public static String getId(OAuth2User principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Logged In");
        }

        Map<String, Object> attributes = principal.getAttributes();
        String id = (String) attributes.get("sub");

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username not found in request");
        }

        return id;
    }
}
