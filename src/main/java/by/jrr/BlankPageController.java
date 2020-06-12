package by.jrr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlankPageController {

    @GetMapping("/blankpage")
    public String openPage() {
        return "blankpage";
    }
}
