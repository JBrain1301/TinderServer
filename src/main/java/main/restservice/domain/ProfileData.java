package main.restservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class ProfileData {
    private Long id;
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;


    public ProfileData(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}