package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class GreetingController {
    @GetMapping("/123")
    public String greeting(){
        return "greeting1";
    }

}
