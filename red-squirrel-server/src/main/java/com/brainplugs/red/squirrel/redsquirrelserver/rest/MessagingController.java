package com.brainplugs.red.squirrel.redsquirrelserver.rest;

import com.brainplugs.red.squirrel.redsquirrelserver.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessagingController {

    private MessagingService messagingService;

    @Autowired
    public MessagingController(final MessagingService messagingService) {
        this.messagingService = messagingService;
    }


    @PostMapping("/users/{alias}/topics/{topic}/")
    public void message(@PathVariable("alias") String userId, @PathVariable("topic") String topic, @RequestBody String message) {
        this.messagingService.publish(userId, topic, message);
    }
}
