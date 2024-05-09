package tv.bermu.cloverrpg.listeners;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import tv.bermu.cloverrpg.managers.CharacterManager;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //todo remove player from party
        Player player = event.getPlayer();
        player.getUniqueId();
        CharacterManager characterManager = CharacterManager.getInstance();
        characterManager.removePlayerCharacter(player.getUniqueId());
    }
}