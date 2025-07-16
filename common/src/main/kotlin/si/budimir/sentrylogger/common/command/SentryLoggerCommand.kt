package si.budimir.sentrylogger.common.command

import org.incendo.cloud.CommandManager
import si.budimir.sentrylogger.common.SentryLogger

abstract class SentryLoggerCommand(protected val plugin: SentryLogger, protected val commands: Commands) {
    protected val commandManager: CommandManager<Commander> = commands.commandManager

    abstract fun register()
}