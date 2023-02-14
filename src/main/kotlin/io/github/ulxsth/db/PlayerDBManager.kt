package io.github.ulxsth.db

import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer
import io.github.ulxsth.EconomyPlugin
import io.github.ulxsth.model.Money
import io.github.ulxsth.model.Player
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.UUID

class PlayerDBManager {
    private val connection: Connection
    private val plugin: EconomyPlugin = EconomyPlugin.getInstance()

    private val TABLE_NAME = "player"

    init {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.dataFolder + "\\player.db")
        val statement = connection.createStatement()

        // dbの作成
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS $TABLE_NAME(uuid string primary key, amount integer)")
        this.plugin.logger.info("TABLE CREATED")
    }

    // TODO PreparedStatementを使う
    // TODO close処理の挿入
    fun create(player: Player) {
        val playerMoney = player.money
        val playerUUID = player.bukkitPlayer.uniqueId

        val sql = "INSERT INTO $TABLE_NAME(uuid, amount) VALUES(?, ?)"
        val ps = this.connection.prepareStatement(sql)
        ps.setString(1, playerUUID.toString())
        ps.setInt(2, playerMoney.amount)
        ps.executeUpdate()


        val playerName = player.bukkitPlayer.name
        this.plugin.logger.info("Initialized data: $playerName(uuid: $playerUUID)")
    }

    fun read(uuid: UUID): Money {
        val sql = "SELECT amount FROM $TABLE_NAME WHERE uuid = ?"
        val ps = this.connection.prepareStatement(sql)
        ps.setString(1, uuid.toString())
        val rs = ps.executeQuery()
        rs.next()

        val amount = rs.getInt(2)

        // データが存在するか判定する
        if(rs.wasNull()) throw SQLException("データが存在しません")
        val playerMoney = Money(amount)
        return playerMoney
    }

    fun update(player: Player, amount: Int) {
        val playerUUID: UUID = player.bukkitPlayer.uniqueId

        val sql = "UPDATE $TABLE_NAME SET amount = ? WHERE uuid = ?"
        val ps = this.connection.prepareStatement(sql)
        ps.setInt(1, amount)
        ps.setString(2, playerUUID.toString())
        ps.executeUpdate()
    }

    fun delete(uuid: UUID) {
        val sql = "DELETE FROM $TABLE_NAME WHERE uuid = ?"
        val ps = this.connection.prepareStatement(sql)
        ps.setString(1, uuid.toString())
        ps.executeUpdate()
    }

    fun isExist(uuid: UUID): Boolean {
        try {
            read(uuid)
        } catch (err: SQLException) {
            return false
        }
        return true
    }
}