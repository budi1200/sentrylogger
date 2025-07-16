package si.budimir.sentrylogger.velocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.PluginDescription
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.incendo.cloud.CommandManager
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.velocity.VelocityCommandManager
import org.slf4j.Logger
import si.budimir.sentrylogger.common.SentryLogger
import si.budimir.sentrylogger.common.SentryLoggerPlatform
import si.budimir.sentrylogger.common.command.Commander
import si.budimir.sentrylogger.velocity.command.VelocityCommander
import java.nio.file.Path
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Suppress("unused")
class SentryLoggerVelocity : PluginContainer, SentryLoggerPlatform {
    val executor: ExecutorService = Executors.newFixedThreadPool(10)
    private lateinit var commandManager: VelocityCommandManager<Commander>

    private lateinit var dataDirectory: Path
    private lateinit var logger: Logger
    private lateinit var server: ProxyServer

    override val rootCommandPrefix: String = "v"
    override val brand: String
        get() = server.version.name

    override val version: String
        get() = server.version.version

    @Suppress("unused")
    @Inject
    fun onEnable(server: ProxyServer, logger: Logger, @DataDirectory dataDirectory: Path) {
        this.server = server
        this.logger = logger
        this.dataDirectory = dataDirectory
        this.setupCommandManager()

        val sentryLogger = SentryLogger(this)
        sentryLogger.setupSentry()
    }

    private fun setupCommandManager() {
        this.commandManager = VelocityCommandManager(
            this,
            this.server,
            ExecutionCoordinator.simpleCoordinator(),
            SenderMapper.create(
                { commandSender -> VelocityCommander.from(commandSender) },
                { commander -> (commander as VelocityCommander).commandSender }
            )
        )
    }

    override fun dataDirectory(): Path {
        return this.dataDirectory
    }

    override fun logger(): Logger {
        return this.logger
    }

    override fun commandManager(): CommandManager<Commander> {
        return this.commandManager
    }

    override fun getExecutorService(): ExecutorService {
        return this.executor
    }

    override fun getDescription(): PluginDescription? {
        TODO("Not yet implemented")
    }
}