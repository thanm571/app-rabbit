package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.MessageService;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String exchange, @RequestParam String routingKey, @RequestParam String message) {
        try{
            messageService.sendMessage(exchange, routingKey, message);
            return "Message sent successfully!";
        }catch(Exception e){
            return "Error : "+e.getMessage();
        }
    }
}