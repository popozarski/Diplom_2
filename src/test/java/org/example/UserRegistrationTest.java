package org.example;

import org.example.model.User;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends BaseTest {
    private UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken, spec);
        }
    }

    @Test
    public void createUniqueUserTest() {
        User u = new User(DataGenerator.generateRandomEmail(), "password123", DataGenerator.generateRandomName());
        var response = userClient.createUser(u, spec);
        accessToken = userClient.extractAccessToken(response);
        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(u.getEmail()))
                .body("user.name", equalTo(u.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void createDuplicateUserTest() {
        User u = new User(DataGenerator.generateRandomEmail(), "password123", DataGenerator.generateRandomName());
        var firstResponse = userClient.createUser(u, spec);
        accessToken = userClient.extractAccessToken(firstResponse);

        userClient.createUser(u, spec)
                .then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createUserMissingEmailTest() {
        User u = new User(null, "pass", DataGenerator.generateRandomName());
        userClient.createUser(u, spec)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserMissingPasswordTest() {
        User u = new User(DataGenerator.generateRandomEmail(), null, DataGenerator.generateRandomName());
        userClient.createUser(u, spec)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserMissingNameTest() {
        User u = new User(DataGenerator.generateRandomEmail(), "pass", null);
        userClient.createUser(u, spec)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}