package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.AllureJunit4;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.model.User;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends TestHelper {



    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешной регистрации нового пользователя с уникальными данными")
    public void createUniqueUserTest() {
        // Подготовка данных
        String email = generateRandomEmail();
        String password = "password123";
        String name = generateRandomName();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("name", name);

        // Выполнение запроса
        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(REGISTER_PATH)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже существует")
    @Description("Попытка зарегистрировать пользователя с email, который уже используется")
    public void createDuplicateUserTest() {
        // Создаём первого пользователя
        String email = generateRandomEmail();
        String password = "password123";
        String name = generateRandomName();

        User user = new User(email, password, name);
        createUserAndGetToken(user);

        // Пытаемся создать второго пользователя с тем же email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", "differentPassword");
        requestBody.put("name", "DifferentName");

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(REGISTER_PATH)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Попытка регистрации пользователя с незаполненным обязательным полем (email)")
    public void createUserWithMissingFieldTest() {
        // Подготовка данных без поля email
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("password", "password123");
        requestBody.put("name", generateRandomName());

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(REGISTER_PATH)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}

