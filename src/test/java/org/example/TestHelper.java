package org.example;

import io.restassured.response.Response;
import org.example.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestHelper extends BaseTest {

    private static final Random random = new Random();


     //Генерация уникального email
    public static String generateRandomEmail() {
        return "test_" + System.currentTimeMillis() + "_" + random.nextInt(10000) + "@test.ru";
    }

     //Генерация уникального имени

    public static String generateRandomName() {
        return "User_" + System.currentTimeMillis();
    }


     //Создание пользователя и получение токена

    public String createUserAndGetToken(User user) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", user.getEmail());
        requestBody.put("password", user.getPassword());
        requestBody.put("name", user.getName());

        Response response = given()
                .spec(requestSpec)
                .body(requestBody)
                .when()
                .post(REGISTER_PATH);

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getString("accessToken");
        }
        return null;
    }


     //Получение списка ингредиентов

    public String[] getValidIngredients() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(INGREDIENTS_PATH);

        // Извлекаем список ID ингредиентов
        List<String> ingredientIds = response.jsonPath().getList("data._id", String.class);

        // Возвращаем первые два ингредиента
        return new String[]{ingredientIds.get(0), ingredientIds.get(1)};
    };

}

