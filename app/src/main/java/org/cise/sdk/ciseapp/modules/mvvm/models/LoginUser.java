package org.cise.sdk.ciseapp.modules.mvvm.models;

import org.cise.sdk.ciseapp.modules.mvvm.models.UserDTO;

public class LoginUser {

    UserDTO userDTO;

    public LoginUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void doLogin(UserDTO userDTO) {

    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
