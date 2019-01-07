package com.brainplugs.red.squirrel.redsquirrelserver.rest;

import com.brainplugs.red.squirrel.redsquirrelserver.models.ChatMessage;
import com.brainplugs.red.squirrel.redsquirrelserver.models.RedSquirrelMessage;
import com.brainplugs.red.squirrel.redsquirrelserver.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MessagingController {

    private MessagingService messagingService;

    @Autowired
    public MessagingController(final MessagingService messagingService) {
        this.messagingService = messagingService;
    }


    @MessageMapping("/messages")
    public void message(ChatMessage message) {
        this.messagingService.publish(message.getUserId(), message.getTopic(), message.getMessage());
    }
}
