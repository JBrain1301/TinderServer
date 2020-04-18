package main.restservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode
@Data
@Entity
public class Profile implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Profile.class);
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @Column(name = "USERNAME")
    @JsonProperty("name")
    private String userName;
    @Column(name = "PASSWORD")
    @JsonProperty("password")
    private String password;
    @Column(name = "GENDER")
    @JsonProperty("gender")
    private String gender;
    @Column(name = "DESCRIPTION")
    @JsonProperty("description")
    private String description;

    public Profile(String userName, String password, String gender, String description) {
        log.debug("Создаём новую анкету");
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.description = description;
    }

    public Profile() {
    }


}
