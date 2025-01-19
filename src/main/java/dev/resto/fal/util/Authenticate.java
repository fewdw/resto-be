package dev.resto.fal.util;

import dev.resto.fal.exceptions.ForbiddenException;
import dev.resto.fal.exceptions.NotFoundException;
import dev.resto.fal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Authenticate {

    @Autowired
    UserService userService;

    public void isUserAuthenticated(OAuth2User principal) {
        if(principal == null){
            throw new ForbiddenException("User not authenticated");
        }

        if(!userService.userExists(OauthHelper.getEmail(principal))){
            throw new NotFoundException("Your account does not exist");
        }
    }
}
