package io.github.ulxsth.model

import java.lang.IllegalArgumentException

class Money(amount: Int) {
    val amount: Int

    init {
        if (amount < 0) throw IllegalArgumentException("amountの値が0未満です")
        this.amount = amount
    }

    fun set(amount: Int): Money {
        return Money(amount)
    }

    fun get(): Int {
        return this.amount
    }

    fun add(amount: Int): Money {
        if(amount < 0) throw IllegalArgumentException("amountの値が0未満です")
        val addedAmount = this.amount + amount
        return Money(addedAmount)
    }

    fun dec(amount: Int): Money {
        if(amount < 0) throw IllegalArgumentException("amountの値が0未満です")
        val decreasedAmount = this.amount - amount
        if(decreasedAmount < 0) throw IllegalArgumentException("所持金の値がマイナスです")
        return Money(decreasedAmount)
    }
}