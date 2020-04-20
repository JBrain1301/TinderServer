package main.restservice.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.restservice.domain.Profile;
import main.restservice.domain.ProfileData;
import main.restservice.service.DefaultProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TinderController {
    private static final Logger log = LoggerFactory.getLogger(DefaultProfileService.class);
    private final DefaultProfileService service;


    @Autowired
    public TinderController(DefaultProfileService service) {
        this.service = service;
    }

    @GetMapping("users/next")
    public ProfileData nextProfile(@RequestParam(value = "name", defaultValue = "anonym") String name) {
        log.debug("Получение следующего профиля");
            return service.getNext(name);
    }

    @PostMapping("/login")
    public ProfileData login(@RequestBody ObjectNode json) {
        log.debug("Логин пользователя");
        return service.login(json);
    }

    @PostMapping("login/register")
    public String register(@RequestBody ObjectNode json) {
        log.debug("Регистрация нового пользователя");
        return service.register(json);
    }

    @PostMapping("login/edit")
    public String changeDescription(@RequestBody ObjectNode json) {
        log.debug("Изменение описания");
        return service.changeDescription(json);
    }

    @PostMapping("login/edit/delete")
    public String delete(@RequestBody ObjectNode json) {
        log.debug("Удаление пользователя");
        return service.removeUser(json);
    }

    @GetMapping("users/match")
    public List<ProfileData> getMatchList(@RequestParam(value = "name", defaultValue = "anonym") String name) {
        log.debug("Получение матчей пользователя");
        return service.getMatchList(name);
    }

    @PostMapping("users/match/add")
    public String addMatch(@RequestBody ObjectNode json) {
        log.debug("Добавление матча");
        return service.saveMatch(json);
    }
}
