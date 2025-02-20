package com.example.transport_marketplace;

import com.example.transport_marketplace.routes.Exceptions.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    String page;
    @GetMapping
    public String home(){
        return "hello";
    }
    @GetMapping("/{page}")
    public String homeExc(String page){
        if(page == null || page.isBlank()){
            throw new BadRequestException();
        }

        if(page.equals("routes") || page.equals("bookings")){
            throw new HomeNotFoundExpection();
        }


        return page;
    }
}
