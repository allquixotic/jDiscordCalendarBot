package com.github.allquixotic.jDiscordCalendarBot;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalTime;

@Data @Builder
public class Evt implements Serializable, Comparable<Evt> {
    private String name;
    private boolean recurs;
    private LocalTime time;

    @Override
    public String toString() {
        return String.format("\"%s\" %sat %s", name, recurs ? "(recurring) " : "", time.format(EventScraper.hmma));
    }

    @Override
    public int compareTo(@NotNull Evt o) {
        var u = time.compareTo(o.getTime());
        return time == null ? 0
                : (u == 0 ? 1 : u);
    }
}
