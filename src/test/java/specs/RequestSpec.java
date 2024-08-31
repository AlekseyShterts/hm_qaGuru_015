package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static java.util.logging.Level.ALL;

public class RequestSpec {
    public static RequestSpecification defaultRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .contentType(JSON);

}
