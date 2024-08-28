import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class UsersBaseTests {

    @BeforeAll
    static void setTests() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api/";
    }
}
