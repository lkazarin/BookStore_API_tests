package tests;

import dto.ValidAddListOfBookRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetBookInfoByIsbnTest extends BaseTest{

    @Test@DisplayName("Get book info by ISBN")
    public void successfulGetBookInfoByIsbnTest() {
        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);

        // iterate through the ISBN list
        for (ValidAddListOfBookRequest.BookItem bookItem : isbnList) {
            String isbnToCheck = bookItem.getIsbn();

            // get information from book store about book by ISBN
            Response getBookByIsbnResponse = getBookByIsbn(isbnToCheck, 200);

            assertEquals(isbnToCheck, getBookByIsbnResponse.jsonPath().getString("isbn"), "Unexpected ISBN value");
            assertAllFieldsHaveValue(getBookByIsbnResponse);
            assertPagesIsInt(getBookByIsbnResponse);
            assertWebsiteStartsWithHttp(getBookByIsbnResponse);
        }
    }
    private void assertAllFieldsHaveValue(Response response) {
        Map<String, Object> bookDetails = response.jsonPath().getMap("$");
        List<String> requiredFields = List.of("title", "subTitle", "author", "publish_date", "publisher", "description", "website");
        requiredFields.forEach(field -> assertNotNull(bookDetails.get(field), field + " is null"));
    }
    private void assertPagesIsInt(Response response) {
        assertInstanceOf(Integer.class, response.jsonPath().get("pages"), "Pages is not an integer");
    }
    private void assertWebsiteStartsWithHttp(Response response) {
        String website = response.jsonPath().getString("website");
        assertNotNull(website, "Website is null");
        assertTrue(website.startsWith("http"), "Website does not start with 'http'");
    }

    @Test@DisplayName("Get book info by invalid ISBN")
    public void GetBookInfoByInvalidIsbnTest() {
        // get information from book store about book by ISBN
        getBookByIsbn(invalidIsbn, 400);
        }

    @Test@DisplayName("Get book info by empty ISBN")
    public void GetBookInfoByEmptyIsbnTest() {
        // get information from book store about book by ISBN
        getBookByIsbn("", 400);
    }
}