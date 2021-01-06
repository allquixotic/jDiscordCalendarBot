package com.github.allquixotic.jDiscordCalendarBot;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Evt {
    private String name;
    private boolean recurs;
    private String time;

    @Override
    public String toString() {
        return String.format("\"%s\" %sat %s", name, recurs ? "(recurring) " : "", time);
    }
}
