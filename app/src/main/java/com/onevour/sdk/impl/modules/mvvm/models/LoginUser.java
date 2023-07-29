package com.onevour.sdk.impl.modules.mvvm.models;

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
