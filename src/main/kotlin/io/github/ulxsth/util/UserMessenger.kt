package io.github.ulxsth.util

import org.bukkit.entity.Player

class UserMessenger {
    companion object {
        private val TEXT_COLOR = Color.WHITE.prefix
        private val INFO_COLOR = Color.GREEN.prefix
        private val WARN_COLOR = Color.RED.prefix
        private val ERROR_COLOR = Color.DARK_RED.prefix

        private val INFO_PREFIX = INFO_COLOR + ">> "
        private val WARN_PREFIX = WARN_COLOR + "[!]"
        private val ERROR_PREFIX = ERROR_COLOR + "[ x-x]"

        fun info(player: Player, text: String) {
            val message = INFO_PREFIX + TEXT_COLOR + text
            player.sendMessage(message)
        }

        fun warn(player: Player, text: String) {
            val message = WARN_PREFIX + text
            player.sendMessage(message)
        }

        fun error(player: Player, text: String) {
            val message = ERROR_PREFIX + text
            player.sendMessage(message)
        }
    }
}