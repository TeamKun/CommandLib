package net.kunmc.lab.commandlib;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.List;

interface ArgumentsParser {
    void parse(List<Object> dst, CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException;
}
