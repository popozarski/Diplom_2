package org.example;

import org.junit.Before;
import org.junit.Test;
import org.example.model.User;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends TestHelper {
    private String email;
    private String password;

    @Before
    public void prepareTestData() {
        super.setUp();
        email = generateRandomEmail();
        password = "password123";
        createUserAndGetToken(new User(email, password, generateRandomName()));
    }

    @Test
    public void loginWithValidCredentialsTest() {
        login(new LoginDto(email, password))
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(email));
    }

    @Test
    public void loginWithInvalidEmailTest() {
        login(new LoginDto("wrong@test.ru", password))
                .then().statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithInvalidPasswordTest() {
        login(new LoginDto(email, "wrongpass"))
                .then().statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}
