package ar.edu.itba.pod.client;

import java.util.List;

import static ar.edu.itba.pod.client.Constants.*;

public class ArgumentParser {
    public static boolean hasArg(final String key) {
        return System.getProperty(key, null) != null;
    }


    public static String getStringArg(final String key) {
        try {
            String arg = System.getProperty(key);
            if (arg == null) throw new NullPointerException();
            return arg;
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("Missing argument: " + key);
        }
    }

    public static int getIntegerArg(final String key, final int min, final int max) {
        try {
            final String arg = getStringArg(key);
            int intArg = Integer.parseInt(arg);
            if (intArg < min || intArg > max) {
                throw new IllegalArgumentException("Argument out of range: " + key + ". Expected between " + min + " and " + max);
            }
            return intArg;
        }
        catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid argument: " + key);
        }
    }

    public static float getFloatArg(final String key, final float min, final float max) {
        try {
            final String arg = getStringArg(key);
            float floatArg = Float.parseFloat(arg);
            if (floatArg < min || floatArg > max) {
                throw new IllegalArgumentException("Argument out of range: " + key + ". Expected between " + min + " and " + max);
            }
            return floatArg;
        }
        catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid argument: " + key);
        }
    }

    public static List<String> getAddressesArg() {
        final String arg = getStringArg(ADDRESSES_ARG);
        return List.of(arg.split(";"));
    }

    public static String getCityArg() {
        final String arg = getStringArg(CITY_ARG);
        if (!arg.equals(NYC_CITY) && !arg.equals(CHI_CITY)) {
            throw new IllegalArgumentException("Invalid city: " + arg + ". Valid values are NYC or CHI.");
        }
        return arg;
    }
}