package flashcards.client.ui;

import flashcards.client.model.UserDto;
import flashcards.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.ws.rs.BadRequestException;
import java.util.Collection;

@ShellComponent
public class UserConsole {

    private UserService userService;

    @Autowired
    public UserConsole(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod
    public void setCurrentUser(String id) {
        userService.setCurrentUser(id);
    }

    @ShellMethodAvailability({"delete-user"})
    public Availability availabilityCheck() {
        return userService.isCurrentUserSet()
                ? Availability.available()
                : Availability.unavailable("Current user id is not set");
    }

    @ShellMethod("Authorize requests using credentials")
    public String login(String username, String password) {
        userService.login(username, password);
        return "OK! These credentials will be used to authorize requests: " + username + "/" + password;
    }

    @ShellMethod
    public String createUser(String username, String password) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        UserDto createdUserDto = userService.create(userDto);
        return "Successfully created user: " + createdUserDto.getUsername() + "/" + createdUserDto.getPassword();
    }

    @ShellMethod
    public Collection<UserDto> readAllUsers() {
        return userService.readAll();
    }

    @ShellMethod
    public void deleteUser() {
        userService.delete();
    }

}
