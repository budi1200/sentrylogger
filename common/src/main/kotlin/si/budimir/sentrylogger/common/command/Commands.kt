package si.budimir.sentrylogger.common.command

import org.incendo.cloud.Command
import org.incendo.cloud.CommandManager
import java.util.function.Function

class Commands(val rootPrefix: String?, val commandManager: CommandManager<Commander>) {
    fun registerSubCommand(modifier: Function<Command.Builder<Commander>, Command.Builder<out Commander>>) {
        this.commandManager.command(modifier.apply(rootBuilder()))
    }

    fun rootBuilder(): Command.Builder<Commander> {
        return this.commandManager.commandBuilder("${rootPrefix}sentrylogger", "${rootPrefix}sl")
    }

    fun register(builder: Command.Builder<Commander>) {
        this.commandManager.command(builder)
    }
}