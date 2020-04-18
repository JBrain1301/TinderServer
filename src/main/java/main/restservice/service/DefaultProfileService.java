package main.restservice.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.restservice.domain.Match;
import main.restservice.domain.Profile;
import main.restservice.domain.ProfileData;
import main.restservice.dao.api.ProfileDao;
import main.restservice.service.api.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DefaultProfileService implements ProfileService {
    private final ProfileDao profileDao;
    private static final Logger log = LoggerFactory.getLogger(DefaultProfileService.class);
    private static final Map<String, List<String>> womansProfile = new HashMap<>();
    private static final Map<String, List<String>> mansProfile = new HashMap<>();

    public DefaultProfileService(@Qualifier("dbProfileDao") ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    public List<Profile> getAllProfiles() {
        log.info("Получение всех профилей");
        return profileDao.getAllProfile();
    }

    public ProfileData getProfileById(Long id) {
        log.debug("Получение профиля по id");
        Profile profileById = profileDao.getProfileById(id);
        return new ProfileData(profileById.getId(), profileById.getUserName(), profileById.getDescription());
    }

    public Profile getProfileByName(String name) {
        log.debug("Получение профиля по имени");
        return profileDao.getProfileByName(name);
    }

    public List<ProfileData> getMatchList(String name) {
        List<Profile> matchList = profileDao.getMatchList(name);
        List<ProfileData> data = new ArrayList<>();
        matchList.forEach(match -> data.add(profileToProfileData(match)));
        return data;
    }

    public void saveMatch(String name, String matchName) {
        profileDao.saveMatch(new Match(name, matchName));
    }

    public boolean checkMatch(String name, String matchName) {
        Match match = profileDao.getMatch(matchName, name);
        return match != null;
    }

    public ProfileData getProfileDataByName(String name) {
        log.debug("Получение DTO по имени");
        Profile profileByName = profileDao.getProfileByName(name);
        return new ProfileData(profileByName.getId(), profileByName.getUserName(), profileByName.getDescription());
    }

    public void saveProfile(Profile profile) {
        log.debug("Добавление пользователя");
        if (profile.getGender().equalsIgnoreCase("сударь")) {
            log.info("Добавление мужчины");
            profileDao.save(profile);
            mansProfile.put(profile.getUserName(), getAllWomansProfile());
            womansProfile.forEach((key, value) -> value.add(profile.getUserName()));
        } else if (profile.getGender().equalsIgnoreCase("сударыня")) {
            log.info("Добавление женщины");
            profileDao.save(profile);
            womansProfile.put(profile.getUserName(), getAllMansProfile());
            mansProfile.forEach((key, value) -> value.add(profile.getUserName()));
        } else {
            log.debug("Не правильный пол");
        }
    }

    public ProfileData getRandomProfile() {
        log.info("Получение случайного профиля");
        Profile profile = profileDao.getRandomProfile();
        if (profile == null) {
            return null;
        }
        return new ProfileData(profile.getId(), profile.getUserName(), profile.getDescription());
    }

     public List<String> getAllMansProfile() {
        return profileDao.getMansProfiles().stream()
                .map(Profile::getUserName)
                .collect(Collectors.toList());
    }

    public List<String> getAllWomansProfile() {
        return profileDao.getWomansProfiles().stream()
                .map(Profile::getUserName)
                .collect(Collectors.toList());
    }


    public static Map<String, List<String>> getWomansPrfofile() {
        return womansProfile;
    }

    public static Map<String, List<String>> getMansProfile() {
        return mansProfile;
    }

    public ProfileData profileToProfileData(Profile profile) {
        log.debug("Конвертируем Profile в DTO");
        return new ProfileData(profile.getId(), profile.getUserName(), profile.getDescription());
    }


    public void changeDescription(String name, String description) {
        log.debug("Изменение описания");
        profileDao.changeDescription(name, description);
    }

    public void saveProfileFromNode(ObjectNode json) {
        log.debug("Сохранение профиля из JSON объекта");
        Profile profile = new Profile(json.get("name").asText(), json.get("password").asText(), json.get("gender").asText().toLowerCase(), json.get("description").asText());
        saveProfile(profile);
    }

    public ProfileData getNext(String name) {
        if (womansProfile.containsKey(name)) {
            if (womansProfile.get(name).size() > 0) {
                return profileToProfileData(getProfileByName(womansProfile.get(name).remove(0)));
            }
            return null;
        } else if (mansProfile.containsKey(name)) {
            if (mansProfile.get(name).size() > 0) {
                return profileToProfileData(getProfileByName(mansProfile.get(name).remove(0)));
            }
        }
        return null;
    }

    public void removeUser(String name) {
        if (womansProfile.containsKey(name)) {
            mansProfile.forEach((key,value) -> value.remove(name));
        }else if (mansProfile.containsKey(name)) {
            womansProfile.forEach((key,value) -> value.remove(name));
        }
        profileDao.deleteProfile(name);
    }
}
