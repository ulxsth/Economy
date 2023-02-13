package io.github.ulxsth.db

import io.github.ulxsth.EconomyPlugin
import io.github.ulxsth.model.Money
import io.github.ulxsth.model.Player
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.UUID

class PlayerDBManager {
    private val connection: Connection
    private val plugin: EconomyPlugin = EconomyPlugin()

    private val TABLE_NAME = "player"

    init {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.dataFolder + "player.db")
        val statement = connection.createStatement()

        // dbの作成
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS player $TABLE_NAME(uuid string, amount integer primary key)")
        statement.executeUpdate("INSERT INTO $TABLE_NAME values('0', 0)")
        this.plugin.logger.info("TABLE CREATED")
    }

    fun create(player: Player) {
        val playerMoney = player.money
        val playerUUID: UUID = player.bukkitPlayer.uniqueId
        val statement = this.connection.createStatement()

        val sql = "INSERT OR REPLACE INTO $TABLE_NAME(uuid, amount) VALUES ($playerUUID, ${playerMoney.amount})"
        statement.executeUpdate(sql)

        val playerName = player.bukkitPlayer.name
        this.plugin.logger.info("Initialized data: $playerName(uuid: $playerUUID)")
    }

    fun read(uuid: UUID): Money {
        val statement = this.connection.createStatement()

        val sql = "SELECT amount FROM $TABLE_NAME WHERE uuid = $uuid"
        val rs = statement.executeQuery(sql)

        if(rs.row == 0) throw SQLException("Data not found")

        val amount = rs.getInt(1)
        val playerMoney = Money(amount)
        return playerMoney
    }

    fun update(player: Player, amount: Int) {
        val playerUUID: UUID = player.bukkitPlayer.uniqueId
        val statement = this.connection.createStatement()

        val sql = "UPDATE $TABLE_NAME SET amount = $amount WHERE uuid = $playerUUID"
        statement.executeUpdate(sql)
    }

    fun delete(uuid: UUID) {
        val statement = this.connection.createStatement()

        val sql = "DELETE FROM $TABLE_NAME WHERE uuid = $uuid"
        statement.executeUpdate(sql)
    }
}