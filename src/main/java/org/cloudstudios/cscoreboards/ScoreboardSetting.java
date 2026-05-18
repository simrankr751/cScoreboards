package org.cloudstudios.cscoreboards;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ScoreboardSetting {

    private final String type;
    private final String worldName;
    private final String regionName;
    private final String action;
    private final String scoreboardName;
    private final boolean notifyEnabled;
    private final String notifyMessage;
    private final boolean resetOnLeaveEnabled;
    private final String resetScoreboardName;
    public ScoreboardSetting(String type, String worldName, String regionName, String action, String scoreboardName,
                             boolean notifyEnabled, String notifyMessage, boolean resetOnLeaveEnabled, String resetScoreboardName) {
        this.type = type;
        this.worldName = worldName;
        this.regionName = regionName;
        this.action = action;
        this.scoreboardName = scoreboardName;
        this.notifyEnabled = notifyEnabled;
        this.notifyMessage = notifyMessage;
        this.resetOnLeaveEnabled = resetOnLeaveEnabled;
        this.resetScoreboardName = resetScoreboardName;

    }

    public String getType() {
        return type;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getAction() {
        return action;
    }

    public String getScoreboardName() {
        return scoreboardName;
    }

    public boolean isNotifyEnabled() {
        return notifyEnabled;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public boolean isResetOnLeaveEnabled() {
        return resetOnLeaveEnabled;
    }

    public String getResetScoreboardName() {
        return resetScoreboardName;
    }

    public static ScoreboardSetting fromYamlFile(File file) {
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
        String type = yamlConfig.getString("type");
        String worldName = yamlConfig.getString("world-name");
        String regionName = yamlConfig.getString("region-name");
        String action = yamlConfig.getString("action");
        String scoreboardName = yamlConfig.getString("scoreboard-name");
        boolean notifyEnabled = yamlConfig.getBoolean("notify.enabled");
        String notifyMessage = yamlConfig.getString("notify.message");
        boolean resetOnLeaveEnabled = yamlConfig.getBoolean("reset_on_leave.enabled", false); // Default to false
        String resetScoreboardName = yamlConfig.getString("reset_on_leave.resetScoreboard-name", "default"); // Default to 'default'

        return new ScoreboardSetting(type, worldName, regionName, action, scoreboardName, notifyEnabled, notifyMessage, resetOnLeaveEnabled, resetScoreboardName);
    }
}
