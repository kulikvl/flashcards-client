package flashcards.client.ui;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserConsole {

    @ShellMethod("test method: todo")
    public void test(String testString) {
        System.out.println("test(): " + testString);
    }


}
