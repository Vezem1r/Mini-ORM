package org.example.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Level;

public class ColoredFormatter extends Formatter {
    private static final String RESET = "\u001B[0m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_CYAN = "\u001B[96m";

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        String color = "";

        if (record.getMessage().startsWith("===")) {
            builder.append("\n");
        }

        if (record.getLevel() == Level.SEVERE) {
            color = MAGENTA;
        } else if (record.getLevel() == Level.WARNING) {
            color = YELLOW;
        } else if (record.getLevel() == Level.INFO) {
            if (record.getMessage().startsWith("===")) {
                color = BRIGHT_CYAN;
            } else {
                color = WHITE;
            }
        } else if (record.getLevel() == Level.CONFIG) {
            color = CYAN;
        } else if (record.getLevel() == Level.FINE ||
                record.getLevel() == Level.FINER ||
                record.getLevel() == Level.FINEST) {
            color = BLUE;
        }

        builder.append(color);
        builder.append(String.format("%-7s", record.getLevel().getName()));
        builder.append(" ").append(formatMessage(record));
        builder.append(RESET);

        if (record.getThrown() != null) {
            builder.append("\n");
            builder.append(MAGENTA);
            builder.append(record.getThrown().toString());
            for (StackTraceElement element : record.getThrown().getStackTrace()) {
                builder.append("\n    at ").append(element.toString());
            }
            builder.append(RESET);
        }

        builder.append("\n");
        return builder.toString();
    }
}