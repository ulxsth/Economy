package io.github.ulxsth

import io.github.ulxsth.db.PlayerDBManager
import io.github.ulxsth.model.Money
import io.github.ulxsth.model.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class EventListener: Listener {
    private val db = PlayerDBManager()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val bukkitPlayer = event.player
        val uuid = bukkitPlayer.uniqueId

        if(!db.isExist(uuid)) {
            // プレイヤーの所持金初期化
            val player = Player(bukkitPlayer, Money(1000))
            db.create(player)
        }
    }
}