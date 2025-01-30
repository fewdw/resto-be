package dev.resto.fal.util;

import dev.resto.fal.entity.User;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.exceptions.UnauthorizedException;
import dev.resto.fal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Authenticate {

    @Autowired
    UserService userService;

    public User isUserAuthenticated(OAuth2User principal) {
        if(principal == null){
            throw new UnauthorizedException("You are not authenticated.");
        }
        return userService.getUserById(OauthHelper.getId(principal));
    }
}
