package com.brainplugs.red.squirrel.redsquirrelserver.rest;

import com.brainplugs.red.squirrel.redsquirrelserver.models.User;
import com.brainplugs.red.squirrel.redsquirrelserver.service.UserAdministrationService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserAdministrationService userAdministrationService;

    @Autowired
    public UserController(final UserAdministrationService userAdministrationService) {
        this.userAdministrationService = userAdministrationService;
    }

    @GetMapping("")
    public List<User> getAll() {
        return Lists.newArrayList(this.userAdministrationService.fetchAllUsers());
    }

    @GetMapping("/{userName}")
    public User get(@PathVariable("userName") String userName) {
        return userAdministrationService.fetch(userName);
    }

    @PostMapping("")
    public void add(@RequestParam String userName, @RequestParam String email) {
        this.userAdministrationService.add(userName, email);
    }

    @PutMapping("")
    public void update(@RequestParam String userName, @RequestParam String updatedEmail) {
        this.userAdministrationService.updateUserDetails(userName, updatedEmail);
    }

    @DeleteMapping("/{userName}")
    public void delete(@PathVariable("userName") String userName) {
        userAdministrationService.delete(userName);
    }
}
