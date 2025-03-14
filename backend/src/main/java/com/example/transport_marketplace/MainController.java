package com.example.transport_marketplace;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping
    public RedirectView responseToNonPage() {
        return new RedirectView("/routes");
    }
}
