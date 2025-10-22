package org.example;

import org.junit.Before;
import org.junit.Test;
import org.example.model.User;
import java.util.List;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends TestHelper {
    private String token;
    private List<String> ingredients;

    @Before
    public void prepareTestData() {
        super.setUp();
        token = createUserAndGetToken(new User(
                generateRandomEmail(), "password123", generateRandomName()
        ));
        ingredients = getValidIngredients();
    }

    @Test
    public void createOrderWithAuthTest() {
        createOrder(token, ingredients)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthTest() {
        createOrder(null, ingredients)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithIngredientsTest() {
        createOrder(token, ingredients)
                .then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutIngredientsTest() {
        createOrder(token, List.of())
                .then().statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidHashTest() {
        createOrder(token, List.of("invalid_hash_123456"))
                .then().statusCode(500);
    }
}
