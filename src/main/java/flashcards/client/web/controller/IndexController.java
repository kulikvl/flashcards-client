package flashcards.client.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/ping")
    ResponseEntity<String> ping() {
        return ResponseEntity.ok("Response!!!");
    }

    @GetMapping("/example")
    public String example() {
        return "example/index";
    }

    @GetMapping("/error")
    public String error() {
        System.out.println("ERROR HAPPENED?");
        return "example/index";
    }



//    @GetMapping("/example")
//    public String example(Model model) {
//        model.addAttribute("message", "Hello World");
//
//        class Employee {
//            private String firstName;
//            public String lastName;
//            public String email;
//
//            public Employee(String firstName, String lastName, String email) {
//                this.firstName = firstName;
//                this.lastName = lastName;
//                this.email = email;
//            }
//
//            public String getFirstName() {
//                return firstName;
//            }
//        }
//
//        List<Employee> employees = new ArrayList<>();
//        employees.add(new Employee("Vova", "Kulikov", "vovakulikov02@mail.ru"));
//        employees.add(new Employee("Eliska", "Stasova", "stasova11@seznam.cz"));
//        employees.add(new Employee("Egor", "Dubrovsky", "egordub@seznam.cz"));
//
//        model.addAttribute("employees", employees);
//
//        return "example";
//    }

}
