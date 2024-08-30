package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.CreateUserSpec.*;
import static specs.LoginSpec.*;
import static specs.UsersSpec.*;
import static utils.TestData.*;

@Tag("regression")
public class UsersTests extends UsersBaseTests {

    @Test
    @DisplayName("Проверка ответа с информацией о юзере")
    @Tag("smoke")
    void checkUserInfoTest() {

        UserDataResponseModel response = step("Запрос информации о пользователе", () ->
                given(singleUserRequestSpec)
                        .when()
                        .get("/2")
                        .then()
                        .spec(singleUserResponseSpec)
                        .extract().as(UserDataResponseModel.class));

        step("Проверка данных пользователя", () -> {
            assertEquals(response.getData().getId(), 2);
            assertEquals(response.getData().getEmail(), "janet.weaver@reqres.in");
            assertEquals(response.getData().getFirstName(), "Janet");
            assertEquals(response.getData().getLastName(), "Weaver");
            assertEquals(response.getData().getAvatar(), "https://reqres.in/img/faces/2-image.jpg");
        });

        step("Проверка информации о поддержке", () -> {
            assertEquals(response.getSupport().getUrl(), "https://reqres.in/#support-heading");
            assertEquals(response.getSupport().getText(),
                    "To keep ReqRes free, contributions towards server costs are appreciated!");
        });

    }

    @Test
    @DisplayName("Проверка ответа на запрос о несуществующим пользователе")
    @Tag("smoke")
    void checkUserNotFoundWithBigIdTest() {
        step("Запрос информации о пользователе с несуществующим Id", () ->
                given(singleUserRequestSpec)
                        .when()
                        .get("/256")

                        .then()
                        .spec(user404ResponseSpec)
                        .body(is(EMPTYBODY))
        );
    }

    @Test
    @DisplayName("Проверка успешного входа в систему")
    @Tag("smoke")
    void checkSuccessfulLoginTest() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = step("Ввод валидного логина и пароля", () ->
                given(loginRequestSpec)
                        .body(authData)

                        .when()
                        .post()

                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Проверяем, что значение токена не пустое", () ->
                assertThat(response.getToken()).isNotNull());
    }

    @Test
    @DisplayName("Проверка входа в систему без пароля")
    @Tag("integration")
    void checkLoginWitOutPasswordTest() {
        LoginBodyWithOutPasswordModel authData = new LoginBodyWithOutPasswordModel();
        authData.setEmail("eve.holt@reqres.in");

        LoginWithOutPasswordResponseModel response = step("Ввод email без пароля", () ->
                given(loginRequestSpec)
                        .body(authData)

                        .when()
                        .post()

                        .then()
                        .spec(loginWithOutPasswordSpec)
                        .extract().as(LoginWithOutPasswordResponseModel.class));

        step("Проверяем получение сообщения об ошибке - отсутствие пароля", () ->
                assertEquals("Missing password", response.getError()));
    }

    @Test
    @DisplayName("Проверка создания нового пользователя")
    @Tag("integration")
    void successfulCreateTest() {
        CreateBodyModel authData = new CreateBodyModel();
        authData.setName("BabaYaga");
        authData.setJob("killer");

        CreateUserResponseModel response = step("Ввод имени и работы пользователя", () ->
                given(createUserRequestSpec)
                        .body(authData)
                        .when()
                        .post()
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class));

        step("Проверка имени созданного пользователя", () ->
                assertThat(response.getName()).isEqualTo("BabaYaga"));

        step("Проверка работы созданного пользователя", () ->
                assertThat(response.getJob()).isEqualTo("killer"));

        step("Проверяем, что значение параметра id не пустое", () ->
                assertThat(response.getId()).isNotNull());

        step("Проверяем, что значение параметра CreatedAt не пустое", () ->
                assertThat(response.getCreatedAt()).isNotNull());
    }

}
