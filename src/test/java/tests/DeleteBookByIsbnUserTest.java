package tests;

import dto.ValidAddListOfBookRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DeleteBookByIsbnUserTest extends BaseTest {

    @Test@DisplayName("Delete book with ISBN from user's collection")
    public void successfulDeleteBookWithIsbnFromUserTest() {
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
        addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //delete book by random isbn from user's collection
        String randomIsbn = getRandomIsbnFromList(isbnList);
        deleteBookByIsbn(randomIsbn, createdUserId, 204);

        //get user information to check that the is deleted from user's collection
        Response getUserInfoResponse = getUserInformation(createdUserId);
        List<Map<String, Object>> userBooks = getUserInfoResponse.jsonPath().getList("books");
        for (Map<String, Object> book : userBooks) {
            assertNotEquals(book.get("isbn"), randomIsbn, "The book with ISBN " + randomIsbn + " is still present in the user's collection");
        }
    }

    @Test@DisplayName("Delete book with invalid ISBN from user's collection")
    public void deleteBookWithInvalidIsbnFromUserTest() {
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
        addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //delete book by invalid isbn from user's collection
        deleteBookByIsbn(invalidIsbn, createdUserId, 400);
    }

    @Test@DisplayName("Delete book with empty ISBN from user's collection")
    public void deleteBookWithEmptyIsbnFromUserTest() {
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
        addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //delete book by empty isbn from user's collection
        deleteBookByIsbn("", createdUserId, 400);
    }

    @Test@DisplayName("Delete book with ISBN and invalid userId from user's collection")
    public void deleteBookWithIsbnAndInvalidUserIdFromUserTest() {
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
        addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //delete book by isbn from user's collection
        deleteBookByIsbn(getRandomIsbnFromList(isbnList), invalidUserId, 401);
    }

    @Test@DisplayName("Delete book with ISBN and invalid token from user's collection")
    public void deleteBookWithIsbnAndInvalidTokenFromUserTest() {
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
        addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //update token with invalid value
        updateHeader(invalidToken);

        //delete book by isbn from user's collection
        deleteBookByIsbn(getRandomIsbnFromList(isbnList), createdUserId, 401);
    }

    @Test@DisplayName("Delete book with ISBN and empty token from user's collection")
    public void deleteBookWithIsbnAndEmptyTokenFromUserTest() {
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
        addListOfBookInUserCollection(isbnList, createdUserId, 201);

        //add empty token in header
        headerWithoutToken();

        //delete book by isbn from user's collection
        deleteBookByIsbn(getRandomIsbnFromList(isbnList), createdUserId, 401);
    }
}