package si.budimir.sentrylogger.common

import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.protocol.Message
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.Property
import org.apache.logging.log4j.core.filter.AbstractFilter
import org.apache.logging.log4j.spi.StandardLevel

class SentryAppender(private val logLevel: Level, private val plugin: SentryLogger) :
    AbstractAppender("SentryAdapter", SentryFilter(), null, false, Property.EMPTY_ARRAY) {

    private class SentryFilter : AbstractFilter()

    override fun append(logEvent: LogEvent) {
        if (
            logEvent.level.isMoreSpecificThan(logLevel)
            && logEvent.thrown != null
            && plugin.mainConfig.ignoreList.none { i -> logEvent.thrown.message?.contains(i) ?: false }
        ) {
            try {
                logException(logEvent)
            } catch (e: Exception) {
                plugin.logger.warn("Failed to log event with sentry", e)
            }
            return
        }

        try {
            logBreadcrumb(logEvent)
        } catch (e: Exception) {
            plugin.logger.warn("Failed to log breadcrumb with sentry", e)
        }
    }

    private fun logException(e: LogEvent) {
        val event = SentryEvent(e.thrown)

        val sentryMessage = Message()
        sentryMessage.message = stripANSI(e.message.formattedMessage)

        event.message = sentryMessage
        event.throwable = e.thrown
        event.level = getLevel(e.level)
        event.logger = e.loggerName
        event.transaction = e.loggerName
        event.environment = plugin.mainConfig.serverName
        event.serverName = null

        event.setTag("server.name", plugin.mainConfig.serverName)
        event.setTag("server.brand", plugin.platform.brand)
        event.setTag("server.version", plugin.platform.version)

        Sentry.captureEvent(event)
    }

    private fun logBreadcrumb(e: LogEvent) {
        val breadcrumb = Breadcrumb()

        breadcrumb.level = getLevel(e.level)
        breadcrumb.category = e.loggerName
        breadcrumb.type = e.loggerName
        breadcrumb.message = stripANSI(e.message.formattedMessage)

        Sentry.addBreadcrumb(breadcrumb)
    }

    private fun getLevel(level: Level): SentryLevel {
        return when (level.standardLevel) {
            StandardLevel.TRACE, StandardLevel.DEBUG -> SentryLevel.DEBUG
            StandardLevel.WARN -> SentryLevel.WARNING
            StandardLevel.ERROR -> SentryLevel.ERROR
            StandardLevel.FATAL -> SentryLevel.FATAL
            else -> SentryLevel.INFO
        }
    }

    private fun stripANSI(message: String) = message.replace(Regex("\u001B\\[[;\\d]*m"), "")
}