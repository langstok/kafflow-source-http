package com.langstok.nlp.httpnaf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by sander.puts on 3/22/2017.
 */
@Controller
public class RedirectController {

    @GetMapping("/")
    public RedirectView redirectToSwagger(RedirectAttributes attributes) {
        return new RedirectView("/swagger-ui.html");
    }
}
