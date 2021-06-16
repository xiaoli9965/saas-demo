package demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Alex
 */
@Controller
public class IndexController {

    @RequestMapping("/sso-test")
    public String indexHtml(){
        return "index.html";
    }

}
