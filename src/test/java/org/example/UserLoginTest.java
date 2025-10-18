package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.AllureJunit4;
import io.qameta.allure.junit4.DisplayName;
import org.example.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserLoginTest extends TestHelper {



    private User validUser;

    @Before
    public void prepareTestData() {
        super.setUp();
        // Создаём пользователя для тестов
        String email = generateRandomEmail();
        String password = "password123";
        String name = generateRandomName();

        validUser = new User(email, password, name);
        createUserAndGetToken(validUser);
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Успешная авторизация с корректными учётными данными")
    public void loginWithValidCredentialsTest() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", validUser.getEmail());
        requestBody.put("password", validUser.getPassword());

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(validUser.getEmail()))
                .body("user.name", equalTo(validUser.getName()));
    }

    @Test
    @DisplayName("Вход с неверным логином и паролем")
    @Description("Попытка авторизации с некорректными учётными данными")
    public void loginWithInvalidCredentialsTest() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "nonexistent@test.ru");
        requestBody.put("password", "wrongpassword");

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}

