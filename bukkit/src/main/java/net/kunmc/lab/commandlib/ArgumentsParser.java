package net.kunmc.lab.commandlib;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.List;
import java.util.Map;

interface ArgumentsParser {
    void parse(List<Object> dstList, Map<String, Object> dstMap, CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException;
}
