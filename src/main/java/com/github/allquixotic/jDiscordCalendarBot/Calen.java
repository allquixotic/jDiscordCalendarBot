package com.github.allquixotic.jDiscordCalendarBot;

import lombok.Builder;
import lombok.Data;
import lombok.val;
import org.apache.commons.collections4.list.SetUniqueList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data @Builder
public class Calen implements Serializable {
    private LocalDate date;
    private String messageId;
    private SetUniqueList<Evt> events;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE MM/dd/YYYY");

    @Override
    public String toString() {
        val sb = new StringBuilder();
        sb.append("Here is ");
        if(date.equals(LocalDate.now())) {
            sb.append("\uD83D\uDCCC **TODAY**, ");
        }
        else if(date.equals(LocalDate.now().plusDays(1L))) {
            sb.append("\u27A1\uFE0FTOMORROW, ");
        }
        else if(date.equals(LocalDate.now().minusDays(1L))) {
            sb.append("\u2B05\uFE0FYESTERDAY, ");
        }

        sb.append(date.format(fmt));

        sb.append("'s calendar.\n");

        for(Evt e : events) {
            sb.append(e.toString()).append("\n");
        }

        sb.append("--------------------------");

        return sb.toString();
    }
}
