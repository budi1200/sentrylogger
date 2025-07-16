package si.budimir.sentrylogger.common.config.base

import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.objectmapping.ObjectMapper
import org.spongepowered.configurate.objectmapping.meta.NodeResolver
import java.nio.file.Path
import kotlin.io.path.createDirectories

open class ConfigLoader<T : Any>(private val dataDirectory: Path, fileName: String, private val clazz: Class<T>) {
    private val configPath = dataDirectory.resolve(fileName)
    lateinit var config: T
        private set

    // Required to avoid kotlin-reflect error when relocating
    private val mapperFactory = ObjectMapper.factoryBuilder()
        .addNodeResolver { name, _ ->
            // We don't want to attempt serializing delegated properties, and they can't be @Transient
            if (name.endsWith("\$delegate")) {
                NodeResolver.SKIP_FIELD
            } else {
                null
            }
        }
        .build()

    private val loader = HoconConfigurationLoader.builder()
        .path(configPath)
        .prettyPrinting(true)
        .defaultOptions { options ->
            options.shouldCopyDefaults(true)
            options.serializers { builder ->
                builder.registerAnnotatedObjects(mapperFactory)
            }
        }
        .build()

    fun load() {
        dataDirectory.createDirectories()

        try {
            val configRoot = loader.load()
            config = configRoot.get(clazz) ?: error("Failed to deserialize MainConfig")
        } catch (e: Exception) {
            throw IllegalArgumentException("An error occurred while loading configuration: ${e.message}", e)
        }
    }

    fun save() {
        try {
            val configRoot = loader.createNode()
            configRoot.set(clazz, config)
            loader.save(configRoot)
        } catch (e: Exception) {
            throw IllegalArgumentException("An error occurred while saving configuration: ${e.message}", e)
        }
    }

    fun reload() {
        load()
    }

    init {
        load()
        save() // Save any new defaults
    }
}