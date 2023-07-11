package flashcards.client.service;

import flashcards.client.api_client.UserClient;
import flashcards.client.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {

    private UserClient userClient;
    private boolean currentUserSet;

    @Autowired
    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public void setCurrentUser(String id) {
        userClient.setCurrentUser(id);
        currentUserSet = true;
    }

    public boolean isCurrentUserSet() {
        return currentUserSet;
    }

    public void login(String username, String password) {
        userClient.authenticateRequests(username, password);
    }

    public UserDto create(UserDto e) {
        return userClient.create(e);
    }

    public Collection<UserDto> readAll() {
        return userClient.readAll();
    }

    public void delete() {
        userClient.delete();
        currentUserSet = false;
    }


}
