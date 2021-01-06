package com.github.allquixotic.jDiscordCalendarBot;

import lombok.Builder;
import lombok.Data;
import lombok.val;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

@Data @Builder
public class Calen {
    private LocalDate date;
    private String messageId;
    private LinkedHashSet<Evt> events;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE MM/dd/YYYY");

    @Override
    public String toString() {
        val sb = new StringBuilder();
        sb.append("Here is");
        if(date.equals(LocalDate.now())) {
            sb.append("TODAY, ");
        }
        else if(date.equals(LocalDate.now().plusDays(1L))) {
            sb.append("TOMORROW, ");
        }
        else if(date.equals(LocalDate.now().minusDays(1L))) {
            sb.append("YESTERDAY, ");
        }

        sb.append(date.format(fmt));

        sb.append("'s calendar.");

        for(Evt e : events) {
            sb.append(e.toString()).append("\n");
        }

        return sb.toString();
    }
}
