package si.budimir.sentrylogger.paper.command

import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender
import si.budimir.sentrylogger.common.command.Commander

open class PaperCommander(val commandSender: CommandSender) : Commander {
    override fun audience(): Audience = commandSender

    class Player(
        player: org.bukkit.entity.Player
    ) : PaperCommander(player), Commander.Player

    class ConsoleCommandSender(
        consoleCommandSender: org.bukkit.command.ConsoleCommandSender
    ) : PaperCommander(consoleCommandSender), Commander.ConsoleCommandSender

    companion object {
        fun from(sender: CommandSender): PaperCommander {
            if (sender is org.bukkit.entity.Player) return Player(sender)
            if (sender is org.bukkit.command.ConsoleCommandSender) return ConsoleCommandSender(sender)
            return PaperCommander(sender)
        }
    }
}