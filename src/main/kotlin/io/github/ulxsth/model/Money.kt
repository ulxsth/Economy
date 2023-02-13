package io.github.ulxsth.model

import java.lang.IllegalArgumentException

class Money(amount: Int) {
    val currency = "yen"
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
        val reducedAmount = this.amount - amount
        return Money(reducedAmount)
    }
}