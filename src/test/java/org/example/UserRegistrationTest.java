package org.example;

import org.example.model.User;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends TestHelper {

    @Test
    public void createUniqueUserTest() {
        User u = new User(generateRandomEmail(), "password123", generateRandomName());
        given().spec(spec)
                .body(new RegisterDto(u.getEmail(), u.getPassword(), u.getName()))
                .when().post(REGISTER_PATH)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(u.getEmail()))
                .body("user.name", equalTo(u.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void createDuplicateUserTest() {
        User u = new User(generateRandomEmail(), "password123", generateRandomName());
        createUserAndGetToken(u);
        given().spec(spec)
                .body(new RegisterDto(u.getEmail(), "otherPass", "OtherName"))
                .when().post(REGISTER_PATH)
                .then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Такой пользователь уже существует"));
    }

    @Test
    public void createUserMissingEmailTest() {
        given().spec(spec)
                .body(new RegisterDto(null, "pass", generateRandomName()))
                .when().post(REGISTER_PATH)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name обязательные поля"));
    }

    @Test
    public void createUserMissingPasswordTest() {
        given().spec(spec)
                .body(new RegisterDto(generateRandomEmail(), null, generateRandomName()))
                .when().post(REGISTER_PATH)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name обязательные поля"));
    }

    @Test
    public void createUserMissingNameTest() {
        given().spec(spec)
                .body(new RegisterDto(generateRandomEmail(), "pass", null))
                .when().post(REGISTER_PATH)
                .then().statusCode(403)
                .body("message", equalTo("Email, password and name обязательные поля"));
    }
}
