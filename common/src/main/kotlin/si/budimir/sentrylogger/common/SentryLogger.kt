package si.budimir.sentrylogger.common

import io.sentry.Sentry
import io.sentry.SentryOptions
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.slf4j.Logger
import si.budimir.sentrylogger.common.command.Commands
import si.budimir.sentrylogger.common.command.SentryLoggerCommand
import si.budimir.sentrylogger.common.command.commands.ReloadCommand
import si.budimir.sentrylogger.common.command.commands.TestCommand
import si.budimir.sentrylogger.common.config.MainConfigSchema
import si.budimir.sentrylogger.common.config.base.ConfigManager
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SentryLogger(val platform: SentryLoggerPlatform) {
    val commands = Commands(platform.rootCommandPrefix, platform.commandManager())
    val configManager: ConfigManager = ConfigManager(this)

    val mainConfig: MainConfigSchema
        get() = this.configManager.mainConfig

    val logger: Logger
        get() = this.platform.logger()

    val appender: SentryAppender

    val startTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))

    companion object {
        lateinit var instance: SentryLogger
            private set
    }

    init {
        instance = this

        registerCommands()

        appender = SentryAppender(Level.WARN, this)
        appender.start()

        val ctx = LoggerContext.getContext(false)
        ctx.rootLogger.addAppender(appender)
        this.logger.info("Sentry integration enabled")
    }

    fun setupSentry() {
        Sentry.close()
        if (mainConfig.dsn.isBlank()) {
            this.logger.warn("Sentry DSN is not configured, skipping Sentry setup")
            return
        }

        this.logger.info("Initializing Sentry")
        Sentry.init { options: SentryOptions ->
            options.setDsn(this.mainConfig.dsn)
            options.maxBreadcrumbs = 100
            options.release = "${mainConfig.serverName}-${startTime}"

            // Set a user friendly error title
            options.beforeSend = SentryOptions.BeforeSendCallback { event, hint ->
                event.exceptions?.forEach { ex ->
                    ex.value = "${ex.type}: ${ex.value}"
                    ex.type = event.message!!.message
                }
                return@BeforeSendCallback event
            }
        }
    }

    private fun registerCommands() {
        setOf(
            TestCommand(this, this.commands),
            ReloadCommand(this, this.commands),
        ).forEach(SentryLoggerCommand::register)
    }

    fun dataDirectory(): Path {
        return this.platform.dataDirectory()
    }
}