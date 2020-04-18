package main.restservice.domain;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "MATCH")
public class Match {
    @Id
    @GeneratedValue(generator = "increment")
    private Long id;
    @Column
    private String name;
    @Column
    private String lovers;

    public Match(String name, String lovers) {
        this.name = name;
        this.lovers = lovers;
    }

    public Match() {
    }

}
