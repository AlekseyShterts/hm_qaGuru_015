import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class UsersTests extends UsersBaseTests{

    @Test
    @DisplayName("Проверка ответа с информацией о юзере")
    void checkUserInfoTest() {
        given()
                .when()
                .log().uri()
                .get("users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"));
    }

    @Test
    @DisplayName("Проверка ответа на запрос о несуществующим пользователе")
    void checkNotFoundUserTest() {
        given()
                .when()
                .log().uri()
                .get("users/256")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Проверка успешного входа в систему")
    void checkSuccessfulLoginTest() {
        String logPassBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .body(logPassBody)
                .contentType(JSON)
                .log().uri()
                .log().body()

                .when()
                .post("login")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Проверка входа в систему без пароля")
    void checkUnsuccessfulLoginTest() {
        String logPassBody = "{\"email\": \"eve.holt@reqres.in\"}";
        given()
                .body(logPassBody)
                .contentType(JSON)
                .log().uri()
                .log().body()

                .when()
                .post("login")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Проверка создания нового пользователя")
    void successfulCreateTest() {

        String authDataBody = "{\"name\": \"BabaYaga\", \"job\": \"killer\"}";

        given()
                .body(authDataBody)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("BabaYaga"))
                .body("job", is("killer"));
    }

}
