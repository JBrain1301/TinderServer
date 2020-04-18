package main.restservice.service.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.restservice.domain.Profile;
import main.restservice.domain.ProfileData;
import java.util.List;


public interface ProfileService {
    List<Profile> getAllProfiles();

    ProfileData getProfileById(Long id);

    Profile getProfileByName(String name);

    List<ProfileData> getMatchList(String name);

    void saveMatch(String name, String matchName);

    boolean checkMatch(String name, String matchName);

    ProfileData getProfileDataByName(String name);

    void saveProfile(Profile profile);

    ProfileData getRandomProfile();

    List<String> getAllMansProfile();

    List<String> getAllWomansProfile();

    ProfileData profileToProfileData(Profile profile);

    void changeDescription(String name, String description);

    void saveProfileFromNode(ObjectNode json);

    ProfileData getNext(String name);

    void removeUser(String name);
}


