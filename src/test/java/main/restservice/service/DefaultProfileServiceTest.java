package main.restservice.service;

import main.restservice.domain.Profile;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class DefaultProfileServiceTest {
    @Autowired
    DefaultProfileService defaultProfileService;
    Profile profile = new Profile("Козодой","111","сударь","Нужно срочно жениться, любые условия!");

    @Test
    void getProfileByName() {
        Profile profileByName = defaultProfileService.getProfileByName("Козодой");
        assertThat(profileByName.getUserName()).isEqualTo(profile.getUserName());
        assertThat(profileByName.getDescription()).isEqualTo(profile.getDescription());
        assertThat(profileByName.getPassword()).isEqualTo(profile.getPassword());
        assertThat(profileByName.getDescription()).isEqualTo(profile.getDescription());
    }

    @Test
    void getAllMansProfile() {
        assertThat(defaultProfileService.getAllMansProfile()).contains(profile.getUserName());
    }

    @Test
    void getAllWomansProfile() {
        assertThat(defaultProfileService.getAllWomansProfile()).doesNotContain(profile.getUserName());
    }

}