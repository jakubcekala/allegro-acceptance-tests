package scenarios;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class StepDefinitions {
    private String authURL = "https://allegro.pl/auth/oauth/token";
    private String accessToken;
    private String clientID = System.getenv("ALLEGRO_CLIENT_ID");
    private String clientSecret = System.getenv("ALLEGRO_CLIENT_SECRET");

    private RequestSpecification request;
    private Response response;

    @Given("^User is authenticated$")
    public void userUsAuthenticated() {
        accessToken = given()
                .queryParam("grant_type", "client_credentials")
                .auth()
                .basic(clientID, clientSecret)
                .post(authURL)
                .then()
                .statusCode(200)
                .extract()
                .path("access_token");

        RestAssured.baseURI = "https://api.allegro.pl";
        request = RestAssured.given();
        request.headers(
                "Authorization",
                "Bearer " + accessToken,
                "Accept",
                "application/vnd.allegro.public.v1+json");
    }

    @When("^User call GET IDs of Allegro categories$")
    public void userCallGETIDsOfAllegroCategories() {
        response = request.get("/sale/categories")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
