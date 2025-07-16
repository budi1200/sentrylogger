package si.budimir.sentrylogger.paper

import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.CommandManager
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager
import org.slf4j.Logger
import si.budimir.sentrylogger.common.SentryLogger
import si.budimir.sentrylogger.common.SentryLoggerPlatform
import si.budimir.sentrylogger.common.command.Commander
import si.budimir.sentrylogger.paper.command.PaperCommander
import java.nio.file.Path

@Suppress("unused")
class SentryLoggerPaper : JavaPlugin(), SentryLoggerPlatform {
    private lateinit var commandManager: LegacyPaperCommandManager<Commander>

    override val version: String
        get() = this.server.version

    override val brand: String
        get() = this.server.name

    override fun onEnable() {
        this.setupCommandManager()

        val sentryLogger = SentryLogger(this)
        sentryLogger.setupSentry()
    }

    private fun setupCommandManager() {
        this.commandManager = LegacyPaperCommandManager(
            this,
            ExecutionCoordinator.simpleCoordinator(),
            SenderMapper.create(
                { commandSender -> PaperCommander.from(commandSender) },
                { commander -> (commander as PaperCommander).commandSender }
            )
        )

        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            commandManager.registerBrigadier()
        } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions()
        }
    }

    override fun dataDirectory(): Path {
        return this.dataFolder.toPath()
    }

    override fun logger(): Logger {
        return this.slF4JLogger
    }

    override fun commandManager(): CommandManager<Commander> {
        return this.commandManager
    }
}
