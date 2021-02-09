package scenarios;

import cucumber.api.java.en.Given;

import static io.restassured.RestAssured.given;

public class StepDefinitions {
    private String authURL = "https://allegro.pl/auth/oauth/token";
    private String accessToken;
    private String clientID = "YOUR_CLIENT_ID";
    private String clientSecret = "YOUR_CLIENT_SECRET";

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
    }
}
