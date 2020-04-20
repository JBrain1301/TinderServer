package main.restservice.service.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.restservice.domain.Profile;
import main.restservice.domain.ProfileData;

import java.util.List;


public interface ProfileService {
    ProfileData getNext(String name);

    ProfileData login(ObjectNode json);

    String register(ObjectNode json);

    String changeDescription(ObjectNode json);

    String removeUser(ObjectNode json);

    List<ProfileData> getMatchList(String name);

    String saveMatch(ObjectNode json);


}


