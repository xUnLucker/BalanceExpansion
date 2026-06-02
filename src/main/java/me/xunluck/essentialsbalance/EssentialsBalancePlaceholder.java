package me.xunluck.essentialsbalance;

import com.earth2me.essentials.Essentials;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EssentialsBalancePlaceholder extends JavaPlugin {

    private static final String PLACEHOLDER_API = "PlaceholderAPI";
    private static final String ESSENTIALS = "Essentials";

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin(PLACEHOLDER_API) == null) {
            disableMissingDependency("PlaceholderAPI");
            return;
        }

        Plugin essentialsPlugin = getServer().getPluginManager().getPlugin(ESSENTIALS);
        if (!(essentialsPlugin instanceof Essentials)) {
            disableMissingDependency("EssentialsX");
            return;
        }

        new BalanceExpansion(this, (Essentials) essentialsPlugin).register();

        getLogger().info("Зарегестрированы PlaceholderAPI плейсхолдеры.");
        getLogger().info("%essbalance%, %essbalance_formatted%, %essbalance_formatted_decimals%, %essbalance_short%");
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialsBalancePlaceholder отключен.");
    }

    private void disableMissingDependency(String dependency) {
        getLogger().severe(dependency + " не был найден, плагин отключен.");
        getServer().getPluginManager().disablePlugin(this);
    }
}
