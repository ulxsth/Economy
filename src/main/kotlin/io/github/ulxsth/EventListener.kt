package io.github.ulxsth

import io.github.ulxsth.db.UserDBManager
import io.github.ulxsth.model.Money
import io.github.ulxsth.model.User
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class EventListener: Listener {
    private val db = UserDBManager()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if(!db.isExist(uuid)) {
            // プレイヤーの所持金初期化
            val user = User(player, Money(1000))
            db.create(user)
        }
    }
}