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
    private val plugin: EconomyPlugin = EconomyPlugin.getInstance()

    private val PATH = "jdbc:sqlite:" + plugin.dataFolder + "\\player.db"
    private val TABLE_NAME = "player"

    init {
        val connection = DriverManager.getConnection(this.PATH)
        val statement = connection.createStatement()

        // dbの作成
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS $TABLE_NAME(uuid string primary key, amount integer)")
        this.plugin.logger.info("TABLE CREATED")

        statement.close()
        connection.close()
    }

    fun create(player: Player) {
        var connection: Connection? = null

        try {
            connection = DriverManager.getConnection(this.PATH)
            val playerMoney = player.money
            val playerUUID = player.bukkitPlayer.uniqueId

            val sql = "INSERT INTO $TABLE_NAME(uuid, amount) VALUES(?, ?)"
            val ps = connection.prepareStatement(sql)
            ps.setString(1, playerUUID.toString())
            ps.setInt(2, playerMoney.amount)
            ps.executeUpdate()

            val playerName = player.bukkitPlayer.name
            this.plugin.logger.info("Initialized data: $playerName(uuid: $playerUUID)")
        } catch (err: SQLException) {
            err.printStackTrace()
        } finally {
            try {
                connection?.close()
            } catch (err: SQLException) {
                err.printStackTrace()
            }
        }

    }

    fun read(uuid: UUID): Money? {
        var connection: Connection? = null

        try {
            connection = DriverManager.getConnection(this.PATH)
            val sql = "SELECT amount FROM $TABLE_NAME WHERE uuid = ?"
            val ps = connection.prepareStatement(sql)
            ps.setString(1, uuid.toString())
            val rs = ps.executeQuery()
            rs.next()

            val amount = rs.getInt(2)

            // データが存在するか判定する
            if (rs.wasNull()) throw SQLException("データが存在しません")
            val playerMoney = Money(amount)
            return playerMoney
        } catch (err: SQLException) {
            err.printStackTrace()
        } finally {
            connection?.close()
        }

        return null
    }

    fun update(player: Player, amount: Int) {
        val playerUUID: UUID = player.bukkitPlayer.uniqueId
        var connection: Connection? = null

        try {
            connection = DriverManager.getConnection(this.PATH)
            val sql = "UPDATE $TABLE_NAME SET amount = ? WHERE uuid = ?"
            val ps = connection.prepareStatement(sql)
            ps.setInt(1, amount)
            ps.setString(2, playerUUID.toString())
            ps.executeUpdate()
        } catch (err: SQLException) {
            err.printStackTrace()
        } finally {
            connection?.close()
        }
    }

    fun delete(uuid: UUID) {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(this.PATH)
            val sql = "DELETE FROM $TABLE_NAME WHERE uuid = ?"
            val ps = connection.prepareStatement(sql)
            ps.setString(1, uuid.toString())
            ps.executeUpdate()
        } catch (err: SQLException) {
            err.printStackTrace()
        } finally {
            connection?.close()
        }
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