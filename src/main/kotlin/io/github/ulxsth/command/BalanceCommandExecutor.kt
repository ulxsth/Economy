package io.github.ulxsth.command

import io.github.ulxsth.EconomyPlugin
import io.github.ulxsth.db.PlayerDBManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BalanceCommandExecutor: CommandExecutor {
    val plugin = EconomyPlugin.getInstance()
    val db = PlayerDBManager()

    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if(sender !is org.bukkit.entity.Player) {
            plugin.logger.warning("コンソールからは実行できません。")
            return false
        }
        val uuid = sender.uniqueId
        val money = db.read(uuid)
        if (money == null) {
            sender.sendMessage("§a>> §f所持金の取得に失敗しました。")
            return true
        }
        val amount = money.amount
        sender.sendMessage("§a>> §f所持金: $amount")
        return true
    }
}