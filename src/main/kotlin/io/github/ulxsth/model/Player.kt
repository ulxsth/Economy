package io.github.ulxsth.model

import java.util.UUID

class Player(player: org.bukkit.entity.Player, money: Money) {
    val bukkitPlayer: org.bukkit.entity.Player
    val money: Money

    init {
        this.bukkitPlayer = player
        this.money = money
    }

    fun pay(toPlayer: Player, amount: Int): Pair<Player, Player> {
        val fromPlayer = this
        val fromPlayerMoney = fromPlayer.money
        val toPlayerMoney = toPlayer.money

        val newFromPlayerMoney = fromPlayerMoney.dec(amount)
        val newToPlayerMoney = toPlayerMoney.add(amount)

        val newFromPlayer = fromPlayer.setMoney(newFromPlayerMoney)
        val newToPlayer = toPlayer.setMoney(newToPlayerMoney)

        return Pair(newFromPlayer, newToPlayer)
    }

    private fun setMoney(money: Money): Player {
        return Player(this.bukkitPlayer, money)
    }
}