package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllBookInformationTest extends BaseTest {

    @Test
    @DisplayName("Get all book information")
    public void successfulGetAllBookInformation() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();
        Response getAllBookInformation = getAllBookInformation(200);

        // make book list from response
        List<Map<String, Object>> booksFromResponse = getAllBookInformation.jsonPath().getList("books");

        // check that all fields are there and have values
        assertAllFieldsHaveValue(booksFromResponse);
    }

    private void assertAllFieldsHaveValue(List<Map<String, Object>> books) {
        for (Map<String, Object> book : books) {
            assertTrue(book.containsKey("isbn"), "ISBN is missing");
            assertTrue(book.containsKey("title"), "Title is missing");
            assertTrue(book.containsKey("subTitle"), "Subtitle is missing");
            assertTrue(book.containsKey("author"), "Author is missing");
            assertTrue(book.containsKey("publish_date"), "Publish date is missing");
            assertTrue(book.containsKey("publisher"), "Publisher is missing");
            assertTrue(book.containsKey("pages"), "Pages is missing");
            assertTrue(book.containsKey("description"), "Description is missing");
            assertTrue(book.containsKey("website"), "Website is missing");
            assertInstanceOf(Integer.class, book.get("pages"), "Pages is not an integer");
            String website = (String) book.get("website");
            assertNotNull(website, "Website is null");
            assertTrue(website.startsWith("http"), "Website does not start with 'http'");
        }
    }
}