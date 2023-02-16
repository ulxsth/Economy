package io.github.ulxsth.model

import org.bukkit.entity.Player

class User(player: Player, money: Money) {
    val player: Player
    val money: Money

    init {
        this.player = player
        this.money = money
    }

    fun pay(toUser: User, amount: Int): Pair<User, User> {
        val fromUser = this
        val fromUserMoney = fromUser.money
        val toUserMoney = toUser.money

        val newFromUserMoney = fromUserMoney.dec(amount)
        val newToUserMoney = toUserMoney.add(amount)

        val newFromUser = fromUser.setMoney(newFromUserMoney)
        val newToUser = toUser.setMoney(newToUserMoney)

        return Pair(newFromUser, newToUser)
    }

    private fun setMoney(money: Money): User {
        return User(this.player, money)
    }
}