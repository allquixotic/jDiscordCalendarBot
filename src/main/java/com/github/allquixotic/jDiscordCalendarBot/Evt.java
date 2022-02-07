package com.github.allquixotic.jDiscordCalendarBot;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalTime;

@Data @Builder
public class Evt implements Serializable {
    private String name;
    private boolean recurs;
    private LocalTime time;

    @Override
    public String toString() {
        return String.format("\"%s\" %sat %s ET", name, recurs ? "(recurring) " : "", time.format(EventScraper.hmma));
    }
}
