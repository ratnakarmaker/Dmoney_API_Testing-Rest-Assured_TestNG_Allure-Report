package testRunner;

import com.github.javafaker.Faker;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import user.User;

import java.io.IOException;

public class TestRunner {
    User user;
   @Test
   public void doLogin() throws IOException, ConfigurationException {
       User user=new User();
       user.callingLoginAPI("salman@grr.la","1234");
       String messageExpected="Login successfully";
       Assert.assertEquals(user.getMessage(),messageExpected);
   }
   @Test
    public void wrongPass() throws IOException {
       user = new User();
       user.wrongPass("salman@grr.la", "123");
       String expectedMessage = "Password incorrect";
       String actualMessage = user.getMessage();
       Assert.assertTrue(actualMessage.contains(expectedMessage));
   }
    @Test
    public void wrongEmail() throws IOException {
        user = new User();
        user.wrongEmail("salman.la", "1234");
        String expectedMessage = "User not found";
        String actualMessage = user.getMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void getUserList() throws IOException {
        user = new User();
        String id = user.callingUserList();
        System.out.println(id);
        Assert.assertEquals(id,String.valueOf(58));
    }
    @Test
    public void getUserListWithWrongToken() throws IOException {
        user = new User();
        user.userListWithWrongToken();
        String expectedMessage = "Token expired!";
        String actualMessage = user.getMessage();
        Assert.assertTrue(expectedMessage.contains(actualMessage));
    }
    @Test
    public void getUserListWithoutToken() throws IOException {
        user = new User();
        user.userListWithoutToken();
        String expectedMessage = "No Token Found!";
        String actualMessage = user.getMessage();
        Assert.assertTrue(expectedMessage.contains(actualMessage));
    }
    @Test
    public void createNewUser() throws IOException {
        user = new User();
        Faker faker=new Faker();
        String name=faker.name().fullName();
        String phone=faker.phoneNumber().phoneNumber();
        String email=faker.internet().emailAddress();
        String password=faker.internet().password();
        String nid="641"+(int)Math.random()*((9999999-1000000)+1)+9999999;
        user.createUser(name,email,password,phone,nid,"Customer");
        String expectedMessage = "User created successfully";
        String actualMessage = user.getMessage();
        Assert.assertTrue(expectedMessage.contains(actualMessage));
    }
}
