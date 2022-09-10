package me.hgsoft.minecraft.devcommand.integration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
@ToString
@EqualsAndHashCode
public class Integration {

    private final String name;
    private final JavaPlugin plugin;
    private final String basePackage;

    public Integration(String name, String basePackage) {
        this.name = name;
        this.plugin = null;
        this.basePackage = basePackage;
    }

    private Integration(String name, JavaPlugin plugin, String basePackage) {
        this.name = name;
        this.plugin = plugin;
        this.basePackage = basePackage;
    }

    public static Integration createFromPlugin(JavaPlugin plugin) {
        return new Integration(getNameFromPlugin(plugin), plugin, getBasePackageFromPlugin(plugin));
    }

    private static String getNameFromPlugin(JavaPlugin plugin) {
        return plugin.getName();
    }

    private static String getBasePackageFromPlugin(JavaPlugin plugin) {

        String[] mainPackageWithMainClassParsed = plugin.getClass().getCanonicalName().split("\\.");
        String[] mainPackageParsed = Arrays.copyOfRange(mainPackageWithMainClassParsed, 0, mainPackageWithMainClassParsed.length-1);
        StringBuilder mainPackagePathStringBuilder = new StringBuilder();

        Arrays.stream(mainPackageParsed).forEach(s -> {
            mainPackagePathStringBuilder.append(s);
            mainPackagePathStringBuilder.append(".");
        });

        mainPackagePathStringBuilder.replace(mainPackagePathStringBuilder.length()-1, mainPackagePathStringBuilder.length(), "");

        return mainPackagePathStringBuilder.toString();

    }

}
