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

public class OrderCreationTest extends TestHelper {



    private String accessToken;
    private String[] validIngredients;

    @Before
    public void prepareTestData() {
        super.setUp();

        // Создаём пользователя и получаем токен
        String email = generateRandomEmail();
        String password = "password123";
        String name = generateRandomName();

        User user = new User(email, password, name);
        accessToken = createUserAndGetToken(user);

        // Получаем валидные ингредиенты
        validIngredients = getValidIngredients();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Успешное создание заказа авторизованным пользователем")
    public void createOrderWithAuthTest() {
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", validIngredients);

        given()
                .spec(requestSpec)
                .header("Authorization", accessToken)
                .body(requestBody)
                .when()
                .post(ORDERS_PATH)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Попытка создания заказа без токена авторизации")
    public void createOrderWithoutAuthTest() {
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", validIngredients);

        given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(ORDERS_PATH)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка создания заказа с корректными ингредиентами")
    public void createOrderWithIngredientsTest() {
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", validIngredients);

        given()
                .spec(requestSpec)
                .header("Authorization", accessToken)
                .body(requestBody)
                .when()
                .post(ORDERS_PATH)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Попытка создать заказ с пустым списком ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", new String[]{});

        given()
                .spec(requestSpec)
                .header("Authorization", accessToken)
                .body(requestBody)
                .when()
                .post(ORDERS_PATH)
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Попытка создать заказ с невалидным ID ингредиента")
    public void createOrderWithInvalidHashTest() {
        Map<String, String[]> requestBody = new HashMap<>();
        requestBody.put("ingredients", new String[]{"invalid_hash_123456"});

        given()
                .spec(requestSpec)
                .header("Authorization", accessToken)
                .body(requestBody)
                .when()
                .post(ORDERS_PATH)
                .then()
                .statusCode(500);
    }
}

