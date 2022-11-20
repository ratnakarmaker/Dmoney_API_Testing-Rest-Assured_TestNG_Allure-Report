package user;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.CreateUser;
import model.LoginUser;
import org.apache.commons.configuration.ConfigurationException;
import setup.Setup;
import utils.Util;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class User extends Setup {

    public User() throws IOException {
        initConfig();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    public void callingLoginAPI(String email, String password) throws ConfigurationException {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        LoginUser loginUser = new LoginUser(email, password);
        Response res =
                given().contentType("application/json")
                .body(loginUser)
                .when().post("/user/login")
                .then().assertThat().statusCode(200).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String token = jsonpath.get("token");
        String message = jsonpath.get("message");
        setMessage(message);
        Util.setEnvVariable("TOKEN",token);
    }
    public void wrongPass(String email, String pass) {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        LoginUser loginUser = new LoginUser(email,pass);
        Response res =
                given().contentType("application/json")
                .body(loginUser)
                .when().post("/user/login")
                .then().assertThat().statusCode(401).extract().response();
        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("message");
        setMessage(message);
    }
    public void wrongEmail(String email, String pass) {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        LoginUser loginUser = new LoginUser(email,pass);
        Response res =
                given().contentType("application/json")
                        .body(loginUser)
                        .when().post("/user/login")
                        .then().assertThat().statusCode(404).extract().response();
        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("message");
        setMessage(message);
    }
    public String callingUserList() {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        Response res =
                given().contentType("application/json")
                        .header("Authorization",prop.getProperty("TOKEN"))
                        .when().get("/user/list")
                        .then().assertThat().statusCode(200).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String id = jsonpath.get("users[0].id").toString();
        return id;
    }
    public void userListWithWrongToken() {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        Response res =
                given().contentType("application/json")
                        .header("Authorization","1234")
                        .when().get("/user/list")
                        .then().assertThat().statusCode(403).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("error.message").toString();
        setMessage(message);
    }
    public void userListWithoutToken() {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        Response res =
                given().contentType("application/json")
                        .header("Authorization"," ")
                        .when().get("/user/list")
                        .then().assertThat().statusCode(401).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("error.message").toString();
        setMessage(message);
    }
    public void createUser(String name,String email, String pass, String mobile, String nid, String role) {
        RestAssured.baseURI = prop.getProperty("BASE_URL");
        CreateUser createUser = new CreateUser(name,email,pass,mobile,nid,role);
        Response res =
                given().contentType("application/json")
                        .body(createUser)
                        .header("Authorization",prop.getProperty("TOKEN"))
                        .header("X-AUTH-SECRET-KEY",prop.getProperty("pwKey"))
                        .when().post("/user/create")
                        .then().assertThat().statusCode(201).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("message").toString();
        System.out.println(message);
        setMessage(message);
    }
}
