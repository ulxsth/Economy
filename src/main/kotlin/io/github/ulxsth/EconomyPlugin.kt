package io.github.ulxsth

import io.github.ulxsth.command.BalanceCommandExecutor
import io.github.ulxsth.command.addCommandExecutor
import org.bukkit.plugin.java.JavaPlugin

class EconomyPlugin: JavaPlugin() {
    companion object {
        private var instance: EconomyPlugin? = null

        fun getInstance(): EconomyPlugin {
            return instance?: throw IllegalStateException("メインクラスが初期化されていません")
        }
    }

    @Override
    override fun onEnable() {
        if(instance != null) throw IllegalStateException("メインクラスは既に初期化されています")
        instance = this
        this.dataFolder.mkdirs()
        this.server.pluginManager.registerEvents(EventListener(), this)

        // コマンドの登録
        getCommand("bal")?.setExecutor(BalanceCommandExecutor())
        getCommand("add")?.setExecutor(addCommandExecutor())
    }
}