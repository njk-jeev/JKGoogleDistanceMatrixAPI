package com.bitemii.distancecalc.util;

/**
 * @Auther Jeevan
 */
 

import java.beans.PropertyEditorManager;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.bitemii.distancecalc.constants.BitemiiConstants;

public class DurationUtil {
	static final Logger logger = Logger.getLogger("DurationUtil");

    public static enum Unit {
        // All lowercase so they look good displayed in help
        NANOSECONDS(1), MICROSECONDS(1000), MILLISECONDS(1000), SECONDS(1000), MINUTES(60), HOURS(60), DAYS(24), WEEKS(7);

        private int multiplier;

        Unit(int multiplier) {
            this.multiplier = multiplier;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public long convert(DurationUtil duration) {
            return convert(duration.getTime(), duration.getUnit());
        }

        public long convert(long time, Unit unit) {

            if (this.ordinal() > unit.ordinal()) {
                Unit[] units = Unit.values();
                for (int i = unit.ordinal() + 1; i <= this.ordinal(); i++) {
                    time = time / units[i].getMultiplier();
                }

            } else if (this.ordinal() < unit.ordinal()) {
                Unit[] units = Unit.values();
                for (int i = unit.ordinal(); i > this.ordinal(); i--) {
                    time = time * units[i].getMultiplier();
                }
            }

            return time;
        }
    }

    private long time;
    private Unit unit = Unit.MINUTES;

    public DurationUtil() {
    }

    public DurationUtil(long time, Unit unit) {
        if (unit == null) throw new NullPointerException("unit");
        this.time = time;
        this.unit = unit;
    }

    public DurationUtil(String string) {
        parse(string, this);
    }

    public DurationUtil convert(Unit unit) {
        return new DurationUtil(unit.convert(this.time, this.unit), unit);
    }

    public long convert(TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return this.unit.convert(this.time, Unit.NANOSECONDS);
            case MICROSECONDS:
                return this.unit.convert(this.time, Unit.MICROSECONDS);
            case MILLISECONDS:
                return this.unit.convert(this.time, Unit.MILLISECONDS);
            case SECONDS:
                return this.unit.convert(this.time, Unit.SECONDS);
            default:
                throw new IllegalArgumentException("Unknown TimeUnit " + unit);
        }
    }

    public Unit getUnit() {
        return unit;
    }

    public long getTime() {
        return time;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DurationUtil that = (DurationUtil) o;

        Unit base = Unit.values()[Math.min(this.getUnit().ordinal(), that.getUnit().ordinal())];
        long a = base.convert(this.getTime(), this.getUnit());
        long b = base.convert(that.getTime(), that.getUnit());

        return a == b;
    }

    public int hashCode() {
        int result;
        result = (int) (time ^ (time >>> 32));
        result = 29 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }

    public String toString() {
        if (unit == null) {
            return time + "";
        } else {
            return time + " " + unit;
        }
    }

    public static DurationUtil parse(String text) {
        DurationUtil d = new DurationUtil();
        parse(text, d);
        return d;
    }

    private static void parse(String text, DurationUtil d) {
        text = text.trim();

        StringBuilder t = new StringBuilder();
        StringBuilder u = new StringBuilder();

        int i = 0;

        // get the number
        for (; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isDigit(c) || i == 0 && c == '-') {
                t.append(c);
            } else {
                break;
            }
        }

        if (t.length() == 0) {
            invalidFormat(text);
        }

        // skip whitespace
        for (; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
            } else {
                break;
            }
        }

        // get time unit text part
        for (; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                u.append(c);
            } else {
                invalidFormat(text);
            }
        }

        d.time = Integer.parseInt(t.toString());

        d.unit = parseUnit(u.toString());

    }

    public static DurationUtil parseMultitple(String string) {
        String[] strings = string.split(",| and ");

        DurationUtil duration = new DurationUtil(0, Unit.MINUTES);

        for (String s : strings) {
            DurationUtil part = new DurationUtil(s);
            duration = duration.add(part);
        }

        return duration;
    }

    public DurationUtil add(DurationUtil that) {
        Unit base = Unit.values()[Math.min(this.getUnit().ordinal(), that.getUnit().ordinal())];
        long a = base.convert(this.getTime(), this.getUnit());
        long b = base.convert(that.getTime(), that.getUnit());
        return new DurationUtil(a + b, base);
    }

    public DurationUtil subtract(DurationUtil that) {
        Unit base = Unit.values()[Math.min(this.getUnit().ordinal(), that.getUnit().ordinal())];
        long a = base.convert(this.getTime(), this.getUnit());
        long b = base.convert(that.getTime(), that.getUnit());
        return new DurationUtil(a - b, base);
    }

    private static void invalidFormat(String text) {
        throw new IllegalArgumentException("Illegal duration format: '" + text + "'.  Valid examples are '10s' or '10 seconds'.");
    }

    private static Unit parseUnit(String u) {
        if (u.length() == 0) return Unit.MILLISECONDS;

        if (u.equalsIgnoreCase("NANOSECONDS")) return Unit.NANOSECONDS;
        if (u.equalsIgnoreCase("NANOSECOND")) return Unit.NANOSECONDS;
        if (u.equalsIgnoreCase("NANOS")) return Unit.NANOSECONDS;
        if (u.equalsIgnoreCase("NANO")) return Unit.NANOSECONDS;
        if (u.equalsIgnoreCase("NS")) return Unit.NANOSECONDS;

        if (u.equalsIgnoreCase("MICROSECONDS")) return Unit.MICROSECONDS;
        if (u.equalsIgnoreCase("MICROSECOND")) return Unit.MICROSECONDS;
        if (u.equalsIgnoreCase("MICROS")) return Unit.MICROSECONDS;
        if (u.equalsIgnoreCase("MICRO")) return Unit.MICROSECONDS;

        if (u.equalsIgnoreCase("MILLISECONDS")) return Unit.MILLISECONDS;
        if (u.equalsIgnoreCase("MILLISECOND")) return Unit.MILLISECONDS;
        if (u.equalsIgnoreCase("MILLIS")) return Unit.MILLISECONDS;
        if (u.equalsIgnoreCase("MILLI")) return Unit.MILLISECONDS;
        if (u.equalsIgnoreCase("MS")) return Unit.MILLISECONDS;

        if (u.equalsIgnoreCase("SECONDS")) return Unit.SECONDS;
        if (u.equalsIgnoreCase("SECOND")) return Unit.SECONDS;
        if (u.equalsIgnoreCase("SEC")) return Unit.SECONDS;
        if (u.equalsIgnoreCase("S")) return Unit.SECONDS;

        if (u.equalsIgnoreCase("MINUTES")) return Unit.MINUTES;
        if (u.equalsIgnoreCase("MINUTE")) return Unit.MINUTES;
        if (u.equalsIgnoreCase("MIN")) return Unit.MINUTES;
        if (u.equalsIgnoreCase("M")) return Unit.MINUTES;
        if (u.equalsIgnoreCase("MINS")) return Unit.MINUTES;

        if (u.equalsIgnoreCase("HOURS")) return Unit.HOURS;
        if (u.equalsIgnoreCase("HOUR")) return Unit.HOURS;
        if (u.equalsIgnoreCase("HRS")) return Unit.HOURS;
        if (u.equalsIgnoreCase("HR")) return Unit.HOURS;
        if (u.equalsIgnoreCase("H")) return Unit.HOURS;

        if (u.equalsIgnoreCase("DAYS")) return Unit.DAYS;
        if (u.equalsIgnoreCase("DAY")) return Unit.DAYS;
        if (u.equalsIgnoreCase("D")) return Unit.DAYS;

        if (u.equalsIgnoreCase("WEEKS")) return Unit.WEEKS;
        if (u.equalsIgnoreCase("WEEK")) return Unit.WEEKS;
        if (u.equalsIgnoreCase("W")) return Unit.WEEKS;

        throw new IllegalArgumentException("Unknown time unit '" + u + "'.  Supported units " );
    }

    private static String[] lowercase(Unit[] units) {
        String[] values = new String[units.length];
        for (int i = 0; i < units.length; i++) {
            values[i] = units[i].name().toLowerCase();
        }
        return values;
    }

    public static DurationUtil extractHoursToCalcMins(String duration) {
    	return DurationUtil.parseMultitple(duration.substring(0,duration.indexOf(BitemiiConstants.HOURS_SUBSTRING_S)+1));
   
    }
    
    public static DurationUtil extractMinsFromDuration(String duration) {
   
    	return duration.contains(BitemiiConstants.HOURS_STRING)?
    			DurationUtil.parseMultitple(duration.substring(duration.indexOf(BitemiiConstants.HOURS_SUBSTRING_RS)+2,duration.indexOf(BitemiiConstants.MINUTES_SUBSTRING_NS)+1)):
    			DurationUtil.parseMultitple(duration.substring(0,duration.indexOf(BitemiiConstants.MINUTES_SUBSTRING_NS)+1));
    }

    static {
        PropertyEditorManager.registerEditor(DurationUtil.class, DurationEditor.class);
    }

    public static class DurationEditor extends java.beans.PropertyEditorSupport {
        public void setAsText(String text) {
            DurationUtil d = DurationUtil.parse(text);
            setValue(d);
        }
    }
    
    
}