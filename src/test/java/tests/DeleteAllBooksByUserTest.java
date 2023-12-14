package tests;

import dto.ValidAddListOfBookRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteAllBooksByUserTest extends BaseTest {

    @Test
    @DisplayName("Delete all book by user collection")
    public void successfulDeleteAllBooksByUserTest() {
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

        //delete all books from user's collection
        deleteRequestWithoutBody(booksEndpoint + "?UserId=" + createdUserId, 204);

        //get user information again to check that the user no longer has the books
        Response getUserInfoResponse = getUserInformation(createdUserId);
        List<?> books = getUserInfoResponse.jsonPath().getList("books");
        assertTrue(books.isEmpty(), "The user's book collection is not empty");
    }

    @Test
    @DisplayName("Delete all book by user collection with invalid userId")
    public void DeleteAllBooksByUserTestWithInvalidUserId() {
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

        //delete all books from user's collection
        deleteRequestWithoutBody(booksEndpoint + "?UserId=" + invalidUserId, 401);
    }

    @Test
    @DisplayName("Delete all book by user collection with empty userId")
    public void DeleteAllBooksByUserTestWithEmptyUserId() {
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

        //delete all books from user's collection
        deleteRequestWithoutBody(booksEndpoint + "?UserId=" + "", 401);
    }

    @Test
    @DisplayName("Delete all book by user collection with invalid token")
    public void DeleteAllBooksByUserTestWithInvalidToken() {
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

        //update invalid token
        updateHeader(invalidToken);

        //delete all books from user's collection
        deleteRequestWithoutBody(booksEndpoint + "?UserId=" + createdUserId, 401);
    }

    @Test
    @DisplayName("Delete all book by user collection with empty token")
    public void DeleteAllBooksByUserTestWithEmptyToken() {
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

        //update empty token in header
        headerWithoutToken();

        //delete all books from user's collection
        deleteRequestWithoutBody(booksEndpoint + "?UserId=" + createdUserId, 401);
    }
}