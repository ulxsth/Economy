package io.github.ulxsth.command

import io.github.ulxsth.db.UserDBManager
import io.github.ulxsth.model.User
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class addCommandExecutor: CommandExecutor {
    private val db = UserDBManager()

    val USAGE = "§aUsage: /add <player> <amount>"

    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        // 権限チェック
        if(!sender.isOp) {
            sender.sendMessage("§c[WARN] このコマンドを実行する権限を持っていません")
            return true
        }

        // 引数チェック
        if(args?.size != 2) {
            sender.sendMessage(this.USAGE)
            return true
        }

        // プレイヤー検索
        val userName = args[0]
        val player: org.bukkit.entity.Player? = Bukkit.getPlayer(userName)
        if (player == null) {
            // TODO オフラインプレイヤーに対応
            sender.sendMessage("§c[WARN] プレイヤーが見つかりません。対象プレイヤーがオンラインであることを確認してください。")
            return true
        }
        val userUUID = player.uniqueId
        val userMoney = db.read(userUUID)
        if(userMoney == null) {
            sender.sendMessage("§4[ERROR] プレイヤーデータが取得できませんでした")
            return true
        }

        // 加算処理
        val addAmount: Int
        try {
            addAmount = Integer.parseInt(args[1])
        } catch (err: NumberFormatException) {
            sender.sendMessage("§c[WARN] amountの値が不正です。amountは整数である必要があります")
            return true
        }
        if(addAmount < 0) {
            sender.sendMessage("§c[WARN] amountの値が不正です。amountは0以上である必要があります")
            return true
        }
        val newUserMoney = userMoney.add(addAmount)
        val newUser = User(player, newUserMoney)
        db.update(newUser)

        val newAmount = newUserMoney.amount
        sender.sendMessage("§a>> §f所持金の更新に成功しました")
        sender.sendMessage("§a>> §f$userName: $newAmount §2(+$addAmount)")

        return true
    }
}