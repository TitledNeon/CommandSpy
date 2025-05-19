package me.titledneon.commandspyneon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;

public class CommandSpyPlugin extends JavaPlugin implements Listener {

    private final HashSet<UUID> enabledPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("CommandSpyNeon enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CommandSpyNeon disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can toggle CommandSpy.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("commandspy.toggle")) {
            player.sendMessage(ChatColor.RED + "У тебя нет прав.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        if (enabledPlayers.contains(uuid)) {
            enabledPlayers.remove(uuid);
            player.sendMessage(ChatColor.YELLOW + "CommandSpy выключен.");
        } else {
            enabledPlayers.add(uuid);
            player.sendMessage(ChatColor.GREEN + "CommandSpy включён.");
        }

        return true;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("commandspy.see")) continue;
            if (!enabledPlayers.contains(player.getUniqueId())) continue;

            player.sendMessage(ChatColor.GRAY + "[CommandSpy] " + ChatColor.AQUA + sender.getName() + ": " + ChatColor.WHITE + message);
        }
    }
}
