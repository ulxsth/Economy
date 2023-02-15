package io.github.ulxsth.command

import io.github.ulxsth.db.PlayerDBManager
import io.github.ulxsth.model.Player
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class decCommandExecutor: CommandExecutor {
    val db = PlayerDBManager()

    val USAGE = "§aUsage: /dec <player> <amount>"

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
        val playerName = args[0]
        val bukkitPlayer: org.bukkit.entity.Player? = Bukkit.getPlayer(playerName)
        if (bukkitPlayer == null) {
            sender.sendMessage("§c[WARN] プレイヤーが見つかりません。対象プレイヤーがオンラインであることを確認してください。")
            return true
        }
        val playerUUID = bukkitPlayer.uniqueId
        val playerMoney = db.read(playerUUID)
        if(playerMoney == null) {
            sender.sendMessage("§c[ERROR] プレイヤーデータが取得できませんでした")
            return true
        }


        // 加算処理
        val decAmount: Int
        try {
            decAmount = Integer.parseInt(args[1])
        } catch (err: NumberFormatException) {
            sender.sendMessage("§c[WARN] amountの値が不正です。amountは整数である必要があります")
            return true
        }
        if(decAmount < 0) {
            sender.sendMessage("§c[WARN] amountの値が不正です。amountは0以上である必要があります")
            return true
        }
        val playerAmount = playerMoney.amount
        val newPlayerMoney = playerMoney.dec(decAmount)
        val newPlayer = Player(bukkitPlayer, newPlayerMoney)
        db.update(newPlayer)

        val newAmount = newPlayerMoney.amount
        sender.sendMessage("§a>> §f所持金の更新に成功しました")
        sender.sendMessage("§a>> §f$playerName: $newAmount §2(+$decAmount)")

        return true
    }
}