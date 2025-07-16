package si.budimir.sentrylogger.common.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import si.budimir.sentrylogger.common.SentryLogger

val miniMessage = MiniMessage.miniMessage()

fun miniMessage(message: String, hidePrefix: Boolean = false, placeholders: Map<String, String> = mapOf()): Component {
    val resolver = TagResolver.resolver(placeholders.map { Placeholder.parsed(it.key, it.value) })
    val prefix = if (hidePrefix) "" else SentryLogger.instance.configManager.mainConfig.prefix
    return miniMessage.deserialize(prefix + message, resolver)
}