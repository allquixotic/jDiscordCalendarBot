package com.github.allquixotic.jDiscordCalendarBot;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ClientRect {
    private double left;
    private double right;
    private double top;
    private double bottom;
    private double x;
    private double y;
    private double width;
    private double height;
}
