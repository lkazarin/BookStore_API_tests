package tests;

import com.github.javafaker.Faker;
import dto.ValidAddListOfBookRequest;
import dto.ValidBookRequest;
import dto.ValidUserRequest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.passay.CharacterData;
//import org.passay.CharacterRule;
//import org.passay.EnglishCharacterData;
//import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

public class BaseTest {
    final static String BASE_URI = "https://demoqa.com/";
    static RequestSpecification specification;
    Faker faker = new Faker();
    String randomUsername = faker.name().username();
    String passwordValue = "mp0p7wCcYJfnSQG_3!";
//  String passwordValue = generatePassword();
    String invalidUsername = faker.regexify("[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{45}");
    String invalidPassword = faker.internet().password();
    String invalidUserId = faker.internet().uuid();
    String invalidToken = faker.regexify("[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{190}");
    String invalidIsbn = faker.number().digits(13);

    protected String userEndpoint = "Account/v1/User/";
    protected String tokenEndpoint = "Account/v1/GenerateToken";
    protected String authorizedEndpoint = "Account/v1/Authorized";
    protected String booksEndpoint = "BookStore/v1/Books";
    protected String bookEndpoint = "BookStore/v1/Book";
    protected String createdUserName;
    protected String createdUserId;
    protected String newUserId;
    protected String userIdToDelete;
    protected String createdUserToken;
    private boolean isUserDeleted = false;

    @BeforeEach
    public void setUp() {
        specification = new RequestSpecBuilder()
                .setUrlEncodingEnabled(false)
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
    }

    @AfterEach
    public void deleteUserAfterTest() {
        if (userIdToDelete != null && !userIdToDelete.isEmpty() && !isUserDeleted) {
            deleteUserAfterEachTest(userIdToDelete);
            userIdToDelete = null;
        }
        isUserDeleted = false;
    }

    public Response getRequest(String endPoint, Integer responseCode) {
        Response response = given()
                .spec(specification)
                .when()
                .log().all()
                .get(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public Response postRequest(String endPoint, Integer responseCode, Object body) {
        Response response = given()
                .spec(specification)
                .body(body)
                .when()
                .log().all()
                .post(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public Response deleteRequestWithoutBody(String endPoint, Integer responseCode) {
        Response response = given()
                .spec(specification)
                .when()
                .log().all()
                .delete(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public void deleteRequestWithoutParamAndAssert(String endPoint) {
        given()
                .spec(specification)
                .when()
                .log().all()
                .delete(endPoint)
                .then().log().all()
                .extract().response();
    }

    public Response deleteRequestWithBody(String endPoint, Integer responseCode, Object body) {
        Response response = given()
                .spec(specification)
                .body(body)
                .when()
                .log().all()
                .delete(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    String getCreatedUserIdFromResponse(Response response) {
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getString("userID");
    }

    String getCreatedUserTokenFromResponse(Response response) {
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getString("token");
    }

    public void addAuthorizationHeader(String token) {
        specification.header("Authorization", "Bearer " + token);
    }

    public void updateHeader(String token) {
        specification = new RequestSpecBuilder()
                .setUrlEncodingEnabled(false)
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
        given()
                .spec(specification);
        specification.header("Authorization", "Bearer " + token);
    }

    public void headerWithoutToken() {
        specification = new RequestSpecBuilder()
                .setUrlEncodingEnabled(false)
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
        given()
                .spec(specification);
    }

    protected void markUserAsDeleted() {
        isUserDeleted = true;
    }

    public void deleteUserAfterEachTest(String userIdToDelete) {
        updateHeader(createdUserToken);
        deleteRequestWithoutParamAndAssert(userEndpoint + userIdToDelete);
    }

    public void deleteUserAfterCreatingUserTest() {
        //generate token in order to the user could be deleted after test
        generateToken(createdUserName, passwordValue, 200);
        //add token in header in order to the user could be deleted after test
        addAuthorizationHeader(createdUserToken);
        deleteUserAfterEachTest(createdUserId);
    }

    public Response createUser(String username, String password, int responseCode) {
        createdUserName = username;
        ValidUserRequest createUserRequestBody = ValidUserRequest.builder()
                .userName(createdUserName)
                .password(password)
                .build();
        Response createUserResponse = postRequest(userEndpoint, responseCode, createUserRequestBody);
        createdUserId = getCreatedUserIdFromResponse(createUserResponse);
        newUserId = getCreatedUserIdFromResponse(createUserResponse);
        if (newUserId != null && !newUserId.isEmpty()) {
            userIdToDelete = newUserId;
        }
        return createUserResponse;
    }

    public Response generateToken(String username, String password, int responseCode) {
        createdUserName = username;
        ValidUserRequest generateTokenRequestBody = ValidUserRequest.builder()
                .userName(createdUserName)
                .password(password)
                .build();
        Response generateToken = postRequest(tokenEndpoint, responseCode, generateTokenRequestBody);
        createdUserToken = getCreatedUserTokenFromResponse(generateToken);
        return generateToken;
    }

    public Response checkAuthorization(String username, String password, int responseCode) {
        createdUserName = username;
        ValidUserRequest authorizedUserBody = ValidUserRequest.builder()
                .userName(createdUserName)
                .password(password)
                .build();
        return postRequest(authorizedEndpoint, responseCode, authorizedUserBody);
    }

    public Response checkAuthorization(String username, String password) {
        return checkAuthorization(username, password, 200);
    }

    public Response getUserInformation(String userId, int responseCode) {
        createdUserId = userId;
        return getRequest(userEndpoint + createdUserId, responseCode);
    }

    public Response getUserInformation(String userId) {
        return getUserInformation(userId, 200);
    }

    public void deleteUserWithoutBody(String userId, int responseCode) {
        createdUserId = userId;
        deleteRequestWithoutBody(userEndpoint + createdUserId, responseCode);
    }

    protected List<String> getIsbnListFromResponse(Response response) {
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        return jsonPath.getList("books.isbn");
    }

    public List<ValidAddListOfBookRequest.BookItem> createBookItemListFromResponse(Response response) {
        List<String> isbnList = getIsbnListFromResponse(response);
        List<ValidAddListOfBookRequest.BookItem> bookItemList = new ArrayList<>();
        for (String isbn : isbnList) {
            ValidAddListOfBookRequest.BookItem bookItem = new ValidAddListOfBookRequest.BookItem(isbn);
            bookItemList.add(bookItem);
        }
        return bookItemList;
    }

    protected String getRandomIsbnFromList(List<ValidAddListOfBookRequest.BookItem> bookItemList) {
        int randomIndex = ThreadLocalRandom.current().nextInt(bookItemList.size());
        return bookItemList.get(randomIndex).getIsbn();
    }

    public Response getAllBookInformation(int responseCode) {
        return getRequest(booksEndpoint, responseCode);
    }

    public Response addListOfBookInUserCollection(List<ValidAddListOfBookRequest.BookItem> isbnList, String userId,
                                                  int responseCode) {
        createdUserId = userId;
        ValidAddListOfBookRequest addListOfBookRequest = ValidAddListOfBookRequest.builder()
                .userId(userId)
                .collectionOfIsbns(isbnList)
                .build();
        return postRequest(booksEndpoint, responseCode, addListOfBookRequest);
    }

    public Response getBookByIsbn(String isbn, int responseCode) {
        return getRequest(bookEndpoint + "?ISBN=" + isbn, responseCode);
    }

    public Response deleteBookByIsbn(String isbn, String userId, int responseCode) {
        createdUserId = userId;
        ValidBookRequest deleteBookByUserRequest = ValidBookRequest.builder()
                .isbn(isbn)
                .userId(userId)
                .build();
        return deleteRequestWithBody(bookEndpoint, responseCode, deleteBookByUserRequest);
    }

//    public static String generatePassword() {
//        PasswordGenerator generator = new PasswordGenerator();
//
//        // Rules for password
//        CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);
//        CharacterRule lowerCase = new CharacterRule(EnglishCharacterData.LowerCase);
//        CharacterRule upperCase = new CharacterRule(EnglishCharacterData.UpperCase);
//        CharacterRule specialChars = new CharacterRule(new CharacterData() {
//            public String getErrorCode() {
//                return "SPECIAL_CHAR_ERROR";
//            }
//
//            public String getCharacters() {
//                return "!?.,*()+";
//            }
//        });
//
//
//        // Generate a password with length 12 symbols
//        return generator.generatePassword(12, specialChars, digits, lowerCase, upperCase);
//    }
}