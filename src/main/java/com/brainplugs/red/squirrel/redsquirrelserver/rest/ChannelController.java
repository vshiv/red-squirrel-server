package com.brainplugs.red.squirrel.redsquirrelserver.rest;

import com.brainplugs.red.squirrel.redsquirrelserver.models.Channel;
import com.brainplugs.red.squirrel.redsquirrelserver.service.ChannelAdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private ChannelAdministrationService administrationService;

    @Autowired
    public ChannelController(final ChannelAdministrationService service) {
        this.administrationService = service;
    }

    @GetMapping("/topics/{topic}")
    public Channel get(@PathVariable("topic") String topic) {
        return administrationService.fetchByTopic(topic);
    }

    @PostMapping("/topics/{topic}")
    public String add(@PathVariable String topic) {
        return administrationService.add(topic);
    }

    @PutMapping("/topics/{topic}/topics/{newTopic}")
    public ResponseEntity changeTopic(@PathVariable("topic") String topic, @RequestParam("newTopic") String newTopic) {
        if(!administrationService.changeTopic(topic, newTopic))
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/topics/{topic}")
    public void delete(@PathVariable("topic") String topic) {
        administrationService.delete(topic);
    }

    @PostMapping("/topics/{topic}/users/{userId}/")
    public ResponseEntity addUser(@PathVariable("topic") String topic, @PathVariable("userId") String userId, @RequestParam(value = "pushEnable", required = false, defaultValue = "false") boolean enablePushNotification ) {
        if(!administrationService.addUser(topic, userId, enablePushNotification))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/topics/{topic}/users/{userId}/")
    public void deleteUser(@PathVariable("topic") String topic, @PathVariable("userId") String userId ) {
        administrationService.deleteUser(topic, userId);
    }


}
