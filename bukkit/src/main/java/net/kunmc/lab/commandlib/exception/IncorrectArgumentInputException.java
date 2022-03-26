package net.kunmc.lab.commandlib.exception;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class IncorrectArgumentInputException extends Exception {
    private final String input;
    private final String incorrectString;

    public IncorrectArgumentInputException(String input, String incorrectString) {
        this.input = input;
        this.incorrectString = incorrectString;
    }

    public void sendMessage(CommandSender sender) {

        String s = input.split(incorrectString)[0].replaceFirst("^/", "");
        if (s.length() > 9) {
            s = "..." + s.substring(s.length() - 9);
        }
       
        sender.sendMessage(ChatColor.RED + "Incorrect argument for command");
        sender.sendMessage(ChatColor.GRAY + s + ChatColor.RED + ChatColor.UNDERLINE + incorrectString + ChatColor.RESET + ChatColor.RED + "<--" + ChatColor.ITALIC + "[HERE]");
    }
}