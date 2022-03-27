package net.kunmc.lab.commandlib;

import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.command.CommandSource;

import java.util.List;

interface ArgumentsParser {
    void parse(List<Object> dst, CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException;
}
