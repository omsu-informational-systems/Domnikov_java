package com.Registration.UserRegistration.Controller;

import org.springframework.stereotype.Controller;

@Controller
public class RegistrationController {
    private RegistrationController registrationController;
    public RegistrationController(RegistrationController registrationController) {
        this.registrationController = registrationController;
    }

}
