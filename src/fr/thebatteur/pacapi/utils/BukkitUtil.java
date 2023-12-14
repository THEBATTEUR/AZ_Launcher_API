/*
 * Class issue du repo https://github.com/PactifyLauncherExamples/PactifyPlugin
 */
package fr.thebatteur.pacapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BukkitUtil {
    private static final Method PLAYER_ADDCHANNEL_METHOD;

    private BukkitUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Pattern SERVER_VERSION = Pattern.compile("\\(MC: (?<major>[0-9]{1,3})\\.(?<minor>[0-9]{1,3})(?:\\.(?<patch>[0-9]{1,3}))?\\)");

    static {
        String ocbPackage = Bukkit.getServer().getClass().getName().replaceAll("\\.[^.]+$", "");
        try {
            Class<?> playerClass = Class.forName(String.valueOf(String.valueOf(ocbPackage)) + ".entity.CraftPlayer");
            Method addChannelMethod = playerClass.getDeclaredMethod("addChannel", new Class[] { String.class });
            addChannelMethod.setAccessible(true);
            PLAYER_ADDCHANNEL_METHOD = addChannelMethod;
        } catch (ClassNotFoundException|NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static int findServerVersion() {
        Matcher m = SERVER_VERSION.matcher(Bukkit.getVersion());
        if (m.find()) {
            int major = Integer.parseInt(m.group("major"));
            int minor = Integer.parseInt(m.group("minor"));
            int patch = (m.group("patch") != null) ? Integer.parseInt(m.group("patch")) : 0;
            return major * 1000000 + minor * 1000 + patch;
        }
        throw new RuntimeException("Unable to detect server version! Bukkit.getVersion()=" + Bukkit.getVersion());
    }

    public static void addChannel(Player player, String channel) {
        try {
            PLAYER_ADDCHANNEL_METHOD.invoke(player, channel);
        } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
