package com.baran.driver.Services;

import com.baran.driver.Model.User;

public interface MyInterface {

    // for login
    void register();
    void login(User u);
    void logout();
    void registrationVerificationFragment();
    void loginFragment();
    void forgetPassword();


    void driverDataUpdate();
}
