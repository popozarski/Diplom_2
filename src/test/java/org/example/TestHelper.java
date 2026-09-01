package org.example;

import io.restassured.response.Response;
import org.example.model.User;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class TestHelper extends BaseTest {
    private static final Random rnd = new Random();

    // === Генерация данных ===
    public static String generateRandomEmail() {
        return "test_" + System.currentTimeMillis() + "_" + rnd.nextInt(10000) + "@test.ru";
    }
    public static String generateRandomName() {
        return "User_" + System.currentTimeMillis();
    }

    public static class RegisterDto {
        public String email;
        public String password;
        public String name;
        public RegisterDto(String e, String p, String n) {
            email=e; password=p; name=n;
        }
    }
    public static class LoginDto {
        public String email;
        public String password;
        public LoginDto(String e, String p) {
            email=e; password=p;
        }
    }
    public static class OrderDto {
        public List<String> ingredients;
        public OrderDto(List<String> lst) { ingredients=lst; }
    }

    // === HTTP-утилиты ===
    public String createUserAndGetToken(User u) {
        Response r = given().spec(spec)
                .body(new RegisterDto(u.getEmail(), u.getPassword(), u.getName()))
                .when().post(REGISTER_PATH);
        return r.getStatusCode()==200 ? r.jsonPath().getString("accessToken") : null;
    }

    public Response login(LoginDto dto) {
        return given().spec(spec)
                .body(dto)
                .when().post(LOGIN_PATH);
    }

    public List<String> getValidIngredients() {
        return given().spec(spec)
                .when().get(INGREDIENTS_PATH)
                .jsonPath().getList("data._id", String.class);
    }

    public Response createOrder(String token, List<String> ingredients) {
        OrderDto dto = new OrderDto(ingredients);
        if (token!=null) {
            return given().spec(spec)
                    .header("Authorization", token)
                    .body(dto)
                    .when().post(ORDERS_PATH);
        } else {
            return given().spec(spec)
                    .body(dto)
                    .when().post(ORDERS_PATH);
        }
    }
}
