package tv.bermu.cloverrpg.listeners;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tv.bermu.cloverrpg.MessageFormatter;

public class PlayerChatListener implements Listener {

    private final Set<Player> creatingCharacter;
    private MessageFormatter messageFormatter;

    public PlayerChatListener(Set<Player> creatingCharacter, MessageFormatter messageFormatter) {
        this.creatingCharacter = creatingCharacter;
        this.messageFormatter = messageFormatter;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (creatingCharacter.contains(player)) {
            String characterName = event.getMessage();
            // todo check if theirs any weird characters in the name.
            HashMap<String, Object> slugs = new HashMap<>();
            slugs.put("character_name", characterName);
            player.sendMessage(
                    messageFormatter.formatMessage("character_creation_name_selected", player.getLocale(), slugs));
            // Use characterName as the name of the character
            creatingCharacter.remove(player); // Remove the player from the set
            event.setCancelled(true); // Cancel the event so the message isn't sent to chat
        }
    }

}
