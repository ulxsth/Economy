package io.github.ulxsth

import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class EventListener: Listener {
    fun onFirstJoin(event: PlayerJoinEvent) {
        val player = event.player
        if(!player.hasPlayedBefore()) {
            // プレイヤーの所持金初期化

        }
    }
}