package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;

public class BaseTest {
    protected static final String BASE_URL = "https://stellarburgers.education-services.ru";
    protected static final String REGISTER_PATH = "/api/auth/register";
    protected static final String LOGIN_PATH = "/api/auth/login";
    protected static final String ORDERS_PATH = "/api/orders";
    protected static final String INGREDIENTS_PATH = "/api/ingredients";

    protected RequestSpecification spec;

    @Before
    public void setUp() {
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
