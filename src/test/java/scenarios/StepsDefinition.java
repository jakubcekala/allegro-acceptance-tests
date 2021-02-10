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
import static org.hamcrest.Matchers.hasSize;

public class StepsDefinition {
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

    @Given("^User is not authenticated$")
    public void userIsNotAuthenticated() {
        accessToken = null;
        request = RestAssured.given();
        request.headers(
                "Accept",
                "application/vnd.allegro.public.v1+json");
    }

    @When("^User call GET IDs of Allegro categories$")
    public void userCallGETIDsOfAllegroCategories() {
        response = request.get("/sale/categories")
                .then()
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
                    .statusCode(200)
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

    @Then("^Receives unauthorized error$")
    public void receivesUnauthorizedError() {
        response
                .then()
                .statusCode(401);
    }

    @When("^User call GET IDs of Allegro categories with (\\d+) param$")
    public void userCallGETIDsOfAllegroCategoriesWithCorrectParentIDParam(String parentID) {
        response = request.queryParam("parent.id", parentID)
                .get("/sale/categories")
                .then()
                .extract()
                .response();
    }

    @Then("^Receive (\\d+) of categories$")
    public void receiveAmountOfCategories(String amount) {
        response
                .then()
                .statusCode(200)
                .body("categories", hasSize(Integer.parseInt(amount)));
    }

    @Then("^Receive error that category with the given ID does not exist$")
    public void receiveErrorThatCategoryWithTheGivenIDDoesNotExist() {
        response
                .then()
                .statusCode(404);
    }
}
