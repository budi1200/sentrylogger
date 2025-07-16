package si.budimir.sentrylogger.common.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class MainConfigSchema(
    val prefix: String = "<bold><gradient:#FFA500:#FF8C00>SentryLogger</gradient> <white><bold>» <reset>",
    
    val serverName: String = "server",

    val dsn: String = "",

    @Comment("List of exception names to ignore.")
    val ignoreList: List<String> = listOf("Connection refused: getsockopt")
)