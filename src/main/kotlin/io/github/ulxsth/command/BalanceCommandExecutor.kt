package io.github.ulxsth.command

import io.github.ulxsth.EconomyPlugin
import io.github.ulxsth.db.UserDBManager
import io.github.ulxsth.util.UserMessenger
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BalanceCommandExecutor: CommandExecutor {
    val plugin = EconomyPlugin.getInstance()
    val db = UserDBManager()

    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if(sender !is Player) {
            plugin.logger.warning("コンソールからは実行できません。")
            return false
        }

        val uuid = sender.uniqueId
        val money = db.read(uuid)
        if (money == null) {
            UserMessenger.error(sender, "所持金の取得に失敗しました。")
            return true
        }
        val amount = money.amount
        UserMessenger.info(sender, "所持金: $amount")
        return true
    }
}