package net.kunmc.lab.commandlib;

interface PlatformAdapter<S, C extends AbstractCommandContext<S, ?>, A extends AbstractArguments<S, C>, B extends AbstractArgumentBuilder<C, A, B>, U extends CommonCommand<C, A, B, U>> {
    C createCommandContext(com.mojang.brigadier.context.CommandContext<S> ctx);

    boolean hasPermission(U command, S commandSource);
}
