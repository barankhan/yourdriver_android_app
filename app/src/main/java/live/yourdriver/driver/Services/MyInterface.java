package live.yourdriver.driver.Services;

import live.yourdriver.driver.Model.User;

public interface MyInterface {

    // for login
    void register();
    void login(User u);
    void logout();
    void registrationVerificationFragment();
    void loginFragment();
    void forgetPassword();


    void driverDataUpdateStep1();
    void driverDataUpdateStep2();
}
