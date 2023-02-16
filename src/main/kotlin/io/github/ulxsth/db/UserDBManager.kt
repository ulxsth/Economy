package io.github.ulxsth.db

import io.github.ulxsth.EconomyPlugin
import io.github.ulxsth.model.Money
import io.github.ulxsth.model.User
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.UUID

class UserDBManager {
    private val plugin: EconomyPlugin = EconomyPlugin.getInstance()

    private val PATH = "jdbc:sqlite:" + plugin.dataFolder + "\\user.db"
    private val TABLE_NAME = "user"

    init {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(this.PATH)
            val statement = connection.createStatement()

            // dbの作成
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS $TABLE_NAME(uuid string primary key, amount integer)")
            this.plugin.logger.info("TABLE CREATED")
        } catch (err: SQLException) {
            err.printStackTrace()
        } finally {
            connection?.close()
        }
    }

    fun create(user: User) {
        var connection: Connection? = null

        try {
            connection = DriverManager.getConnection(this.PATH)
            val userMoney = user.money
            val userUUID = user.player.uniqueId

            val sql = "INSERT INTO $TABLE_NAME(uuid, amount) VALUES(?, ?)"
            val ps = connection.prepareStatement(sql)
            ps.setString(1, userUUID.toString())
            ps.setInt(2, userMoney.amount)
            ps.executeUpdate()

            val userName = user.player.name
            this.plugin.logger.info("Initialized data: $userName(uuid: $userUUID)")
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
            val amount = rs.getInt(1)
            if (rs.wasNull()) throw SQLException("データが存在しません")

            val userMoney = Money(amount)
            return userMoney
        } catch (err: SQLException) {
            err.printStackTrace()
        } finally {
            connection?.close()
        }

        return null
    }

    fun update(user: User) {
        val amount = user.money.amount
        val userUUID: UUID = user.player.uniqueId
        var connection: Connection? = null

        try {
            connection = DriverManager.getConnection(this.PATH)
            val sql = "UPDATE $TABLE_NAME SET amount = ? WHERE uuid = ?"
            val ps = connection.prepareStatement(sql)
            ps.setInt(1, amount)
            ps.setString(2, userUUID.toString())
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