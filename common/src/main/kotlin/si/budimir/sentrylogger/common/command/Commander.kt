package si.budimir.sentrylogger.common.command

import net.kyori.adventure.audience.ForwardingAudience

interface Commander : ForwardingAudience.Single {
    interface Player : Commander
    interface ConsoleCommandSender : Commander
}