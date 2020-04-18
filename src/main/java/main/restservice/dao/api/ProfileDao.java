package main.restservice.dao.api;

import main.restservice.domain.Match;
import main.restservice.domain.Profile;
import java.util.List;

public interface ProfileDao {
    Profile getRandomProfile();

    Profile getProfileById(Long id);

    List<Profile> getAllProfile();

    Profile getProfileByName(String username);

    void save(Profile profile);

    List<Profile> getMatchList(String name);

    void deleteProfile(String name);

    void changeDescription(String name, String description);

    void saveMatch(Match match);

    Match getMatch(String name, String matchName);

    List<Profile> getMansProfiles();

    List<Profile> getWomansProfiles();
}
