package codesquad.formatter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeResponseFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
        "EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);

    private DateTimeResponseFormatter() {
    }

    public static String formatZonedDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(FORMATTER);
    }
}
