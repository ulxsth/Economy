package io.github.ulxsth

import java.lang.IllegalArgumentException

class Money(amount: Int) {
    private val currency = "yen"
    private val amount: Int

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

    fun reduce(amount: Int): Money {
        if(amount > 0) throw IllegalArgumentException("amountの値が1以上です")
        val reducedAmount = this.amount + amount
        return Money(reducedAmount)
    }
}