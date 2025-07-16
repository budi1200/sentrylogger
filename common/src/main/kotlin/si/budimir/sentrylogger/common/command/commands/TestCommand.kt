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

class TestCommand(plugin: SentryLogger, commands: Commands) : SentryLoggerCommand(plugin, commands) {
    override fun register() {
        this.commands.registerSubCommand { b ->
            b.literal("test")
                .permission(Constants.PERMISSION_TEST)
                .commandDescription(richDescription(Component.text("Test sentry integration")))
                .handler(this::handler)
        }
    }

    private fun handler(ctx: CommandContext<Commander>) {
        ctx.sender().sendMessage(miniMessage("Throwing an exception to test sentry"))
        plugin.logger.error("Throwing an exception to test sentry", RuntimeException("Test Exception"))
    }
}