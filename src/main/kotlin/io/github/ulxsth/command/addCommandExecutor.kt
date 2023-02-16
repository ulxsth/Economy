package io.github.ulxsth.command

import io.github.ulxsth.db.UserDBManager
import io.github.ulxsth.model.User
import io.github.ulxsth.util.UserMessenger
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class addCommandExecutor: CommandExecutor {
    private val db = UserDBManager()

    val USAGE = "§aUsage: /add <player> <amount>"

    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val fromPlayer = sender as Player

        // 権限チェック
        if(!sender.isOp) {
            UserMessenger.warn(fromPlayer, "このコマンドを実行する権限を持っていません")
            return true
        }

        // 引数チェック
        if(args?.size != 2) {
            sender.sendMessage(this.USAGE)
            return true
        }

        // プレイヤー検索
        val userName = args[0]
        val targetPlayer: Player? = Bukkit.getPlayer(userName)
        if (targetPlayer == null) {
            // TODO オフラインプレイヤーに対応
            UserMessenger.warn(fromPlayer, "プレイヤーが見つかりません。対象プレイヤーがオンラインであることを確認してください。")
            return true
        }
        val userUUID = targetPlayer.uniqueId
        val userMoney = db.read(userUUID)
        if(userMoney == null) {
            UserMessenger.warn(fromPlayer, "プレイヤーデータが取得できませんでした")
            return true
        }

        // 加算処理
        val addAmount: Int
        try {
            addAmount = Integer.parseInt(args[1])
        } catch (err: NumberFormatException) {
            UserMessenger.warn(fromPlayer, "amountの値が不正です。amountは整数である必要があります")
            return true
        }
        if(addAmount < 0) {
            UserMessenger.warn(fromPlayer, "amountの値が不正です。amountは0以上である必要があります")
            return true
        }
        val newUserMoney = userMoney.add(addAmount)
        val newUser = User(targetPlayer, newUserMoney)
        db.update(newUser)

        val newAmount = newUserMoney.amount
        UserMessenger.info(fromPlayer, "§f所持金の更新に成功しました")
        UserMessenger.info(fromPlayer, "§f$userName: $newAmount §2(+$addAmount)")

        // TODO 送信先にメッセージを表示（=senderならなし）
        return true
    }
}