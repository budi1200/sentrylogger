package si.budimir.sentrylogger.common.command.commands

import net.kyori.adventure.text.Component
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.minecraft.extras.RichDescription.richDescription
import si.budimir.sentrylogger.common.SentryLogger
import si.budimir.sentrylogger.common.command.Commander
import si.budimir.sentrylogger.common.command.Commands
import si.budimir.sentrylogger.common.command.SentryLoggerCommand
import si.budimir.sentrylogger.common.util.Constants
import si.budimir.sentrylogger.common.util.miniMessage

class ReloadCommand(plugin: SentryLogger, commands: Commands) : SentryLoggerCommand(plugin, commands) {
    override fun register() {
        this.commands.registerSubCommand { b ->
            b.literal("reload")
                .permission(Constants.PERMISSION_RELOAD)
                .commandDescription(richDescription(Component.text("Reload the configuration file")))
                .handler(this::handler)
        }
    }

    fun handler(ctx: CommandContext<Commander>) {
        this.plugin.configManager.reload()
        this.plugin.setupSentry()
        ctx.sender().sendMessage {
            miniMessage("<green>Successfully reloaded configuration file")
        }
    }
}