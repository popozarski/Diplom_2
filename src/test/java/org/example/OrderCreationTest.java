package org.example;

import org.example.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends BaseTest {
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();
    private String token;
    private List<String> ingredients;

    @Before
    public void prepareTestData() {
        User u = new User(DataGenerator.generateRandomEmail(), "password123", DataGenerator.generateRandomName());
        var response = userClient.createUser(u, spec);
        token = userClient.extractAccessToken(response);
        ingredients = orderClient.getValidIngredients(spec);
    }

    @After
    public void tearDown() {
        if (token != null) {
            userClient.deleteUser(token, spec);
        }
    }

    @Test
    public void createOrderWithAuthTest() {
        orderClient.createOrder(token, ingredients, spec)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthTest() {
        orderClient.createOrder(null, ingredients, spec)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithIngredientsTest() {
        orderClient.createOrder(token, ingredients, spec)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutIngredientsTest() {
        orderClient.createOrder(token, List.of(), spec)
                .then().statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidHashTest() {
        orderClient.createOrder(token, List.of("invalid_hash_123456"), spec)
                .then().statusCode(500);
    }
}
