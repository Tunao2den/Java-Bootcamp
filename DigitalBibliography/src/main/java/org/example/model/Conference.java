package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Conference {
    String name;
    String year;
    int issueNumber;
    String location;

    public Conference(String name) {
        this.name = name;
    }
}
