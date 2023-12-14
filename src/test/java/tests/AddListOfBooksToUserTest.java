package tests;

import dto.ValidAddListOfBookRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddListOfBooksToUserTest extends BaseTest {

    @Test
    @DisplayName("Adding list of books to the user collection")
    public void successfulAddListOfBooksToUserTest() {

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);


        //add token in header
        addAuthorizationHeader(createdUserToken);

        //add books in the user collection by the isbn list
        Response addListOfBooksResponse = addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //check that all isbn from the previous request are added and displayed in the response body
        String responseBody = addListOfBooksResponse.getBody().asString();
        JsonPath tempIsbnList = new JsonPath(responseBody);
        List<String> actualIsbnList = tempIsbnList.getList("books.isbn");
        for (ValidAddListOfBookRequest.BookItem bookItem : isbnList) {
            assertTrue(actualIsbnList.contains(bookItem.getIsbn()),
                    "ISBN not found in the response: " + bookItem.getIsbn());
        }
    }

    @Test
    @DisplayName("Adding list of books with invalid Isbns to the user collection")
    public void AddListOfBooksWithInvalidIsbnsToUserTest() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);

        //make invalid isbn
        isbnList.get(3).setIsbn("invalid_isbn_3");

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //add books in the user collection by the isbn list
        addListOfBookInUserCollection(isbnList, createdUserId, 400);
    }

    @Test
    @DisplayName("Adding list of books to the user collection with invalid userId")
    public void AddListOfBooksToUserTestWithInvalidUserId() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //add books in the user collection by the isbn list
        addListOfBookInUserCollection(isbnList, invalidUserId, 401);
    }

    @Test
    @DisplayName("Adding list of books to the user collection with empty userId")
    public void AddListOfBooksToUserTestWithEmptyUserId() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //add books in the user collection by the isbn list
        addListOfBookInUserCollection(isbnList, "", 401);
    }

    @Test
    @DisplayName("Adding list of books to the user collection with invalid token")
    public void AddListOfBooksToUserTestWithInvalidToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);

        //add invalid token in header
        addAuthorizationHeader(invalidToken);

        //add books in the user collection by the isbn list
        addListOfBookInUserCollection(isbnList, createdUserId, 401);
    }

    @Test
    @DisplayName("Adding list of books to the user collection with empty token")
    public void AddListOfBooksToUserTestWithEmptyToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //get all book information
        Response getAllBookInformation = getAllBookInformation(200);

        //make list with book store isbns
        List<ValidAddListOfBookRequest.BookItem> isbnList = createBookItemListFromResponse(getAllBookInformation);

        //add empty header without token
        headerWithoutToken();

        //add books in the user collection by the isbn list
        addListOfBookInUserCollection(isbnList, createdUserId, 401);
    }
}