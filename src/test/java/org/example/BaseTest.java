package org.example;

import io.qameta.allure.junit4.AllureJunit4;
import org.junit.Before;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

//@RunWith(AllureJunit4.class)  // Добавляем Allure listener сюда
public class BaseTest {

    protected static final String BASE_URL = "https://stellarburgers.education-services.ru";
    protected static final String REGISTER_PATH = "/api/auth/register";
    protected static final String LOGIN_PATH = "/api/auth/login";
    protected static final String ORDERS_PATH = "/api/orders";
    protected static final String INGREDIENTS_PATH = "/api/ingredients";

    protected RequestSpecification requestSpec;

    @Before
    public void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}

