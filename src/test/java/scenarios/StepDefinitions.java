package scenarios;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import java.util.List;

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

    @Then("^Receive all following categories with ids:$")
    public void receiveAllFollowingCategoriesWithIds(DataTable dataTable) {
        List<List<String>> data = dataTable.raw();
        data.forEach((element) -> {
            String categoryName = element.get(0);
            String categoryId = element.get(1);
            response
                    .then()
                    .body("categories", Matchers.hasItem(
                            Matchers.allOf(
                                    Matchers.allOf(
                                            Matchers.hasEntry("id", categoryId),
                                            Matchers.hasEntry("name", categoryName)
                                    )
                            )
                    ));
        });
    }
}
