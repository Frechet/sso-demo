package ru.intabia.sso.demo.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.intabia.sso.demo.backend.dto.User;
import ru.intabia.sso.demo.backend.service.UserStorageService;

/**
 * User controller.
 */
@Slf4j
@Api(tags = "User controller")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserStorageService userStorageService;

    /**
     * Constructor.
     *
     * @param userStorageService ClientStorageService bean.
     */
    @Autowired
    public UserController(UserStorageService userStorageService) {
        this.userStorageService = userStorageService;
    }

    /**
     * Save user.
     *
     * @return saved user
     */
    @ApiOperation(
            value = "Save user",
            notes = "Save user"
    )
    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<User> saveClient(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            log.error("Cannot save without email and password hash user {}", user);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("save user with login={}", user.getEmail());
        User result = userStorageService.saveUser(user);
        log.debug("saved user={}", result);
        ResponseEntity<User> response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    /**
     * Get client.
     *
     * @return client
     */
    @ApiOperation(
            value = "Get client by id",
            notes = "Get client by id"
    )
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> getClientById(@RequestParam long userId) {
        log.debug("get client by id={}", userId);
        User result = userStorageService.getUser(userId);
        log.debug("found client={}", result);
        ResponseEntity<User> response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }
}
