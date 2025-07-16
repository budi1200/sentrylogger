package si.budimir.sentrylogger.velocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import net.kyori.adventure.audience.Audience
import si.budimir.sentrylogger.common.command.Commander

open class VelocityCommander(val commandSender: CommandSource) : Commander {
    override fun audience(): Audience = commandSender

    class Player(
        player: com.velocitypowered.api.proxy.Player
    ) : VelocityCommander(player), Commander.Player

    class ConsoleCommandSender(
        consoleCommandSender: ConsoleCommandSource
    ) : VelocityCommander(consoleCommandSender), Commander.ConsoleCommandSender

    companion object {
        fun from(sender: CommandSource): VelocityCommander {
            if (sender is com.velocitypowered.api.proxy.Player) return Player(sender)
            if (sender is ConsoleCommandSource) return ConsoleCommandSender(sender)
            return VelocityCommander(sender)
        }
    }
}