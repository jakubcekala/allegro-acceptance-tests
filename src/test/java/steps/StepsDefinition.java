package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StepsDefinition {
    private final String authURL = "https://allegro.pl/auth/oauth/token";
    private String accessToken;
    private final String clientID = System.getenv("ALLEGRO_CLIENT_ID");
    private final String clientSecret = System.getenv("ALLEGRO_CLIENT_SECRET");

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

    @When("^User calls GET IDs of Allegro categories$")
    public void userCallGETIDsOfAllegroCategories() {
        response = request.get("/sale/categories")
                .then()
                .extract()
                .response();
    }

    @When("^User calls GET IDs of Allegro categories with (\\d+) param$")
    public void userCallGETIDsOfAllegroCategoriesWithCorrectParentIDParam(String parentID) {
        response = request.queryParam("parent.id", parentID)
                .get("/sale/categories")
                .then()
                .extract()
                .response();
    }

    @When("^User calls GET a category by ID with (\\d+)$")
    public void userCallGETACategoryByIDWithIdValue(String idValue) {
        response = request.get("/sale/categories/" + idValue)
                .then()
                .extract()
                .response();
    }

    @When("^User calls GET parameters supported by a category with ID value (\\d+)$")
    public void userCallsGETParametersSupportedByACategoryWithIDValueIdValue(String idValue) {
        response = request.get("/sale/categories/" + idValue + "/parameters")
                .then()
                .extract()
                .response();
    }

    @Then("^Receives all following categories with ids:$")
    public void receiveAllFollowingCategoriesWithIds(DataTable dataTable) {
        List<List<String>> data = dataTable.asLists();
        data.forEach((element) -> {
            String categoryName = element.get(0);
            String categoryId = element.get(1);
            response
                    .then()
                    .statusCode(200)
                    .body(
                            "categories", hasItem(
                                    allOf(
                                            allOf(
                                                    hasEntry("id", categoryId),
                                                    hasEntry("name", categoryName)
                                            )
                                    )
                            )
                    );
        });
    }

    @Then("^Receives unauthorized error$")
    public void receivesUnauthorizedError() {
        response
                .then()
                .statusCode(401);
    }

    @Then("^Receives (\\d+) of categories$")
    public void receiveAmountOfCategories(String amount) {
        response
                .then()
                .statusCode(200)
                .body("categories", hasSize(Integer.parseInt(amount)));
    }

    @Then("^Receives error that category with the given ID does not exist$")
    public void receiveErrorThatCategoryWithTheGivenIDDoesNotExist() {
        response
                .then()
                .statusCode(404);
    }

    @Then("^User receives category ([^\"]*)$")
    public void userReceivesCategoryCategoryName(String categoryName) {
        response
                .then()
                .statusCode(200)
                .body("name", equalTo(categoryName));
    }

    @Then("^Receives (\\d+) parameters$")
    public void receivesParametersAmountParameters(String parametersAmount) {
        response
                .then()
                .statusCode(200)
                .body(
                        "parameters", hasSize(
                                Integer.parseInt(parametersAmount)
                        )
                );
    }

    @And("^There are following categories: ([^\"]*)$")
    public void thereAreFollowingCategoriesCategories(String categoriesString) {
        String[] categories = categoriesString.split("\\s*,\\s*");
        for (String category : categories) {
            response
                    .then()
                    .statusCode(200)
                    .body(
                            "parameters", hasItem(
                                    allOf(
                                            hasEntry("name", category)
                                    )


                            )
                    );
        }
    }
}
