package si.budimir.sentrylogger.common.config.base

import si.budimir.sentrylogger.common.SentryLogger
import si.budimir.sentrylogger.common.config.MainConfigSchema

class ConfigManager(plugin: SentryLogger) {

    private val mainConfigLoader = ConfigLoader(plugin.dataDirectory(), "config.conf", MainConfigSchema::class.java)

    val mainConfig: MainConfigSchema
        get() = mainConfigLoader.config

    fun reload() {
        mainConfigLoader.reload()
    }
}