package me.cyperion.normalskyblock.NPC;

public enum NpcActions {
    say_hi("say_hi"),
    showing("showing");
    String command;
    NpcActions(String command) {
        this.command = command;
    }

}
