package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/updateCredential")
    public String updateCredential(@ModelAttribute("credential") Credential credential,
                                   Authentication authentication,
                                   EncryptionService encryptionService,
                                   Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        credential.setUserId(userId);
        credential.setKey(userService.getUser(authentication.getName()).getSalt());
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), userService.getUser(authentication.getName()).getSalt()));
        boolean rs = false;
        if (userId != null && credential.getCredentialId() == null) {
            rs = credentialService.addCredential(credential) > 0;
        } else {
            rs = credentialService.updateCredential(credential) > 0;
        }
        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        model.addAttribute("rs", rs);
        return "result";
    }

    @GetMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId, Authentication authentication, Model model) {
        String userName = authentication.getName();
        Integer userId = userService.getUserId(userName);
        boolean rs = userId != null && credentialService.deleteCredential(credentialId) > 0;
        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        model.addAttribute("rs", rs);
        return "result";
    }
}
