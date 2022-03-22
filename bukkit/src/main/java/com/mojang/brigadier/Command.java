package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public interface Command<S> {
    int run(CommandContext<S> ctx) throws CommandSyntaxException;
}
