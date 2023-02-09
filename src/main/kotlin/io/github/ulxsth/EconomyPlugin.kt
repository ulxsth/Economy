package io.github.ulxsth

import org.bukkit.plugin.java.JavaPlugin

class EconomyPlugin: JavaPlugin() {
    @Override
    override fun onEnable() {
        this.server.pluginManager.registerEvents(EventListener(), this)
        this.dataFolder.mkdirs()
        saveResource("data.yml", false)
    }
}