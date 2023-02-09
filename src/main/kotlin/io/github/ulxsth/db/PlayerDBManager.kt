package io.github.ulxsth.db

import io.github.ulxsth.model.Player
import java.util.UUID

class PlayerDBManager {
    companion object {
        fun create(player: Player) {}
        fun read(uuid: UUID) {}
        fun update(uuid: UUID, player: Player) {}
        fun delete(uuid: UUID) {}
    }
}