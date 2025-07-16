package si.budimir.sentrylogger.common

import org.incendo.cloud.CommandManager
import org.slf4j.Logger
import si.budimir.sentrylogger.common.command.Commander
import java.nio.file.Path

interface SentryLoggerPlatform {
    val version: String
    val brand: String
    val rootCommandPrefix: String get() = ""

    fun dataDirectory(): Path

    fun logger(): Logger

    fun commandManager(): CommandManager<Commander>
}