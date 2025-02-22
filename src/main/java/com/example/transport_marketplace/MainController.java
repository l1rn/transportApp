package com.example.transport_marketplace;

import com.example.transport_marketplace.entity.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping
    public RedirectView responseToNonPage() {
        // Выполняем перенаправление на /routes
        return new RedirectView("/routes");
    }
//    @GetMapping("*/booking")
//    public RedirectView responseIfNonUser(){
//        User currentUser = new User();
//        if(currentUser.ha)
//    }
}
