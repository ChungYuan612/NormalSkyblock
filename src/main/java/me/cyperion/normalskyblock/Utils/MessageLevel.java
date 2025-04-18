package me.cyperion.normalskyblock.Utils;

public enum MessageLevel {
    NORMAL("&7[訊息] "),
    WARN("&e[警告] "),
    ERROR("&c[錯誤] ");
    String prefix;
    MessageLevel(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}