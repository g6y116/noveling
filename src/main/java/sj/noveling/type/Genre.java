package sj.noveling.type;

import lombok.Getter;

@Getter
public enum Genre {

    FANTASY("판타지"),
    CHINESE("무협"),
    MODERN("현대"),
    HISTORY("역사");

    private final String value;

    Genre(String value) {
        this.value = value;
    }
}
