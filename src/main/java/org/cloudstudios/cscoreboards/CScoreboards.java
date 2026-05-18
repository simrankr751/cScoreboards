package org.cloudstudios.cscoreboards;


import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.scoreboard.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import org.bukkit.plugin.java.JavaPlugin;
import net.raidstone.wgevents.events.RegionEnteredEvent;

import net.raidstone.wgevents.events.RegionLeftEvent;

import org.bukkit.command.Command;

import org.bukkit.command.CommandSender;


import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class CScoreboards extends JavaPlugin implements Listener {
    private String noPerms;
    private String prefix;
    private String reloadMessage;

    @Override
    public void onEnable() {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_GOLD = "\u001B[33;1m";

        getLogger().info(ANSI_GOLD + "==================================" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Conditional Scoreboards" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Plugin enabled successfully." + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Configuration loaded successfully." + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Settings loaded successfully" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Developed by CurryMan" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Made for Charmunk" + ANSI_RESET);
        getLogger().info(ANSI_GOLD + "==================================" + ANSI_RESET);

        saveDefaultConfig();
        loadConfigurations();


        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("cScoreboards plugin initialised successfully");
    }
    @Override
    public void onDisable(){
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_GOLD = "\u001B[33;1m";

        getLogger().info(ANSI_GOLD + "==================================" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Conditional Scoreboards" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Plugin disabled successfully." + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Configuration saved safely." + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Settings saved to memory" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Developed by CurryMan" + ANSI_RESET);
        getLogger().info(ANSI_YELLOW + "Made for Charmunk" + ANSI_RESET);
        getLogger().info(ANSI_GOLD + "==================================" + ANSI_RESET);
    }
    private Map<String, ScoreboardSetting> scoreboardSettings;

    private void loadConfigurations() {
        FileConfiguration config = getConfig();
        scoreboardSettings = new HashMap<>();
        prefix = config.getString("prefix", "&c&lcScoreboard &8» &r");
        noPerms = config.getString("noPerms", "&4Missing Permissions");
        reloadMessage = config.getString("reload-message", "&acScoreboards has been reloaded successfully");


        File settingsDir = new File(getDataFolder(), "Settings");
        if (!settingsDir.exists()) {
            settingsDir.mkdirs();
        }


        File[] files = settingsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                try {

                    ScoreboardSetting setting = ScoreboardSetting.fromYamlFile(file);


                    String settingName = file.getName().substring(0, file.getName().length() - 4);
                    scoreboardSettings.put(settingName, setting);

                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "Could not load settings from file: " + file.getName(), e);
                }
            }
        }
    }
    public void reloadPlugin() {

        reloadConfig();


        loadConfigurations();
    }
    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World playerWorld = player.getWorld();
        String playerWorldName = playerWorld.getName();
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        for (ScoreboardSetting setting : scoreboardSettings.values()) {
            if (setting.getType().equalsIgnoreCase("WORLD") && playerWorldName.equalsIgnoreCase(setting.getWorldName())) {
                if (tabPlayer != null) {

                    if (setting.getAction().equalsIgnoreCase("SHOW")) {
                        Scoreboard scoreboard = TabAPI.getInstance().getScoreboardManager().getRegisteredScoreboards().get(setting.getScoreboardName());
                        if (scoreboard != null) {
                            TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, scoreboard);
                        }
                    } else if (setting.getAction().equalsIgnoreCase("HIDE")) {
                        TabAPI.getInstance().getScoreboardManager().toggleScoreboard(tabPlayer, true);
                    }
                }


                if (setting.isNotifyEnabled()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + setting.getNotifyMessage()));
                }

            }
        }
    }

    @EventHandler
    public void onRegionEnter(RegionEnteredEvent event) {
        Player player = event.getPlayer();
        String region = event.getRegion().getId();
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        for (ScoreboardSetting setting : scoreboardSettings.values()) {
            if (setting.getType().equalsIgnoreCase("REGION") && region.equalsIgnoreCase(setting.getRegionName())) {
                if (tabPlayer != null) {

                    if (setting.getAction().equalsIgnoreCase("SHOW")) {
                        Scoreboard scoreboard = TabAPI.getInstance().getScoreboardManager().getRegisteredScoreboards().get(setting.getScoreboardName());
                        if (scoreboard != null) {
                            TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, scoreboard);
                        }
                    } else if (setting.getAction().equalsIgnoreCase("HIDE")) {
                        TabAPI.getInstance().getScoreboardManager().toggleScoreboard(tabPlayer, true);
                    }
                }


                if (setting.isNotifyEnabled()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + setting.getNotifyMessage()));
                }
                break;
            }
        }
    }

    @EventHandler
    public void onRegionLeave(RegionLeftEvent event) {
        Player player = event.getPlayer();
        String region = event.getRegion().getId();
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        for (ScoreboardSetting setting : scoreboardSettings.values()) {
            if (setting.getType().equalsIgnoreCase("REGION") && region.equalsIgnoreCase(setting.getRegionName()) && setting.isResetOnLeaveEnabled()) {
                if (tabPlayer != null) {
                    Scoreboard resetScoreboard = TabAPI.getInstance().getScoreboardManager().getRegisteredScoreboards().get(setting.getResetScoreboardName());
                    if (resetScoreboard != null) {
                        TabAPI.getInstance().getScoreboardManager().showScoreboard(tabPlayer, resetScoreboard);
                    } else {
                        TabAPI.getInstance().getScoreboardManager().resetScoreboard(tabPlayer);
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("csb.use")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noPerms));
                return true;
            }
        }
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                getLogger().info("This command can only be issued in game");
            }
            if (!sender.hasPermission("csb.use")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noPerms));
                return true;
            }

            sender.sendMessage(ChatColor.GOLD + "-----------------------------");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.GOLD + "cScoreboards");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.YELLOW + "Valid Commands:");
            sender.sendMessage(ChatColor.YELLOW + "/csb reload » Reload the plugin & all settings");
            sender.sendMessage(ChatColor.YELLOW + "/csb info » View information about the plugin");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.GOLD + "-----------------------------");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("csb.use")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noPerms));
                return true;
            }
            if (sender instanceof Player) {
                reloadPlugin();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reloadMessage));
            } else {
                getLogger().info("This command can only be issued in game");
            }
        }
        if (args[0].equalsIgnoreCase("info")) {

            if (!sender.hasPermission("csb.use")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + noPerms));
                return true;
            }
            if (!(sender instanceof Player)) {
                getLogger().info("This command can only be issued in game");
            }
            sender.sendMessage(ChatColor.GOLD + "-----------------------------");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.YELLOW + "cScoreboards - Information");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.YELLOW + "This is a plugin for hiding or showing scoreboards based off of a player's region or world.");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.YELLOW + "VERSIONS: 1.17 and above");
            sender.sendMessage(ChatColor.YELLOW + "DEPENDS: TAB");
            sender.sendMessage(ChatColor.YELLOW + "SOFTDEPEND: WorldGuard, WorldGuardEvents");
            sender.sendMessage(ChatColor.YELLOW + "CONTRIBUTORS: CurryMan, Charmunk");
            sender.sendMessage(ChatColor.YELLOW + " ");
            sender.sendMessage(ChatColor.GOLD + "-----------------------------");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args[0].equalsIgnoreCase("csb")) {
            List<String> options = Arrays.asList("info", "reload");
            return options;
        }
        return Collections.emptyList();
    }

}