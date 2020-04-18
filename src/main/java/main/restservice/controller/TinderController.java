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
    private final Set<String> loggedUsers = new HashSet<>();

    @Autowired
    public TinderController(DefaultProfileService service) {
        this.service = service;
    }

    @GetMapping("users/next")
    public ProfileData nextProfile(@RequestParam(value = "name", defaultValue = "anonym") String name) {
        log.debug("Получение следующего профиля");
        if (loggedUsers.contains(name)) {
            return service.getNext(name);
        }
        return service.getRandomProfile();
    }

    @PostMapping("/login")
    public ProfileData login(@RequestBody ObjectNode json) {
        log.debug("Логин пользователя");
        Profile profile = service.getProfileByName(json.get("name").asText());
        if (profile != null) {
            if (profile.getPassword().equals(json.get("password").asText())) {
                log.debug("Пользователь существует: " + profile.getUserName());
                loggedUsers.add(profile.getUserName());
                return service.profileToProfileData(profile);
            } else {
                log.debug("Неправильный пароль: " + json.get("password").asText());
                return null;
            }
        } else {
            log.debug("Пользователя не существует");
            return null;
        }
    }

    @PostMapping("login/register")
    public String register(@RequestBody ObjectNode json) {
        log.debug("Регистрация нового пользователя");
        Profile profile = service.getProfileByName(json.get("name").asText());
        if (profile == null) {
            if (json.get("gender").asText().equalsIgnoreCase("сударь") || json.get("gender").asText().equalsIgnoreCase("сударыня")) {
                loggedUsers.add(json.get("name").asText());
                service.saveProfileFromNode(json);
                return "Successful";
            } else {
                return "Не правильный пол";
            }
        }
        return "Пользователь с таким именем уже зарегестрирован.";
    }

    @PostMapping("login/edit")
    public String changeDescription(@RequestBody ObjectNode json) {
        log.debug("Изменение описания");
        if (loggedUsers.contains(json.get("name").asText())) {
            service.changeDescription(json.get("name").asText(), json.get("description").asText());
            return "Successful";
        }
        return "Зарегестрируйтесь или войдите в профиль";
    }

    @PostMapping("login/edit/delete")
    public String delete(@RequestBody ObjectNode json) {
        log.debug("Удаление пользователя");
        if (loggedUsers.contains(json.get("name").asText())) {
            service.removeUser(json.get("name").asText());
            exit(json.get("name").asText());
            return "Successful";
        }
        return "Зарегестрируйтесь или войдите в профиль";
    }

    @GetMapping("users/match")
    public List<ProfileData> getMatchList(@RequestParam(value = "name", defaultValue = "anonym") String name) {
        log.debug("Получение матчей пользователя");
        if (name.equals("anonym")) {
            return null;
        }
        return service.getMatchList(name);
    }

    @PostMapping("users/match/add")
    public String addMatch(@RequestBody ObjectNode json) {
        log.debug("Добавление матча");
        if (loggedUsers.contains(json.get("name").asText())) {
            service.saveMatch(json.get("name").asText(), json.get("matchName").asText());
            if (service.checkMatch(json.get("name").asText(), json.get("matchName").asText())) {
                return "Вы любимы";
            }
            return "Любимец добавлен";
        }
        return "Зарегестрируйтесь или войдите в профиль";
    }

    @GetMapping("/exit")
    public void exit(@RequestParam(value = "name", defaultValue = "anonym") String name) {
        log.debug("Выход пользователя");
        loggedUsers.remove(name);
    }
}
