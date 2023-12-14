package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteUserTest extends BaseTest {

    @Test
    @DisplayName("Delete user")
    public void successfulDeleteUser() {
        //note that the user is deleted by the test so that @AfterEach is not triggered
        markUserAsDeleted();

        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //delete user
        deleteUserWithoutBody(createdUserId, 200);
    }

    @Test
    @DisplayName("Delete user with invalid userId")
    public void deleteUserWithInvalidUserId() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //delete user
        deleteUserWithoutBody(invalidUserId, 401);
    }

    @Test
    @DisplayName("Delete user with empty userId")
    public void deleteUserWithEmptyUserId() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add token in header
        addAuthorizationHeader(createdUserToken);

        //delete user
        deleteUserWithoutBody("", 401);
    }

    @Test
    @DisplayName("Delete user with invalid token")
    public void deleteUserWithInvalidToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //add invalid token in header
        addAuthorizationHeader(invalidToken);

        //delete user
        deleteUserWithoutBody(createdUserId, 401);
    }

    @Test
    @DisplayName("Delete user with empty token")
    public void deleteUserWithEmptyToken() {
        //create user
        createUser(randomUsername, passwordValue, 201);

        //generate token
        generateToken(createdUserName, passwordValue, 200);

        //delete token from header
        headerWithoutToken();

        //delete user
        deleteUserWithoutBody(createdUserId, 401);
    }
}
