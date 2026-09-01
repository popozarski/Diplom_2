package org.example;

import org.example.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserLoginTest extends BaseTest {
    private UserClient userClient = new UserClient();
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void prepareTestData() {
        email = DataGenerator.generateRandomEmail();
        password = "password123";
        User u = new User(email, password, DataGenerator.generateRandomName());
        var response = userClient.createUser(u, spec);
        accessToken = userClient.extractAccessToken(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken, spec);
        }
    }

    @Test
    public void loginWithValidCredentialsTest() {
        User loginUser = new User(email, password);
        userClient.login(loginUser, spec)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email));
    }

    @Test
    public void loginWithInvalidEmailTest() {
        User loginUser = new User("wrong@test.ru", password);
        userClient.login(loginUser, spec)
                .then().statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithInvalidPasswordTest() {
        User loginUser = new User(email, "wrongpass");
        userClient.login(loginUser, spec)
                .then().statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}
