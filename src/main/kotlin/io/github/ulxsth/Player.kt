package io.github.ulxsth

import java.util.UUID

class Player(uuid: UUID, money: Money) {
    private val uuid: UUID
    private val money: Money

    init {
        this.uuid = uuid
        this.money = money
    }

    fun setMoney(amount: Int): Player {
        val newMoney = Money(amount)
        return Player(this.uuid, newMoney)
    }

    fun getMoney(): Money {
        return this.money
    }
}