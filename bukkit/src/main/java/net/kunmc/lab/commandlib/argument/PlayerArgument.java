package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PlayerArgument extends Argument<Player> {
    public PlayerArgument(String name) {
        this(name, option -> {
        });
    }
   
    public PlayerArgument(String name, Consumer<Option<Player>> options) {
        super(name, ArgumentEntity.c());
        setOptions(options);
    }

    @Override
    protected Player cast(Object parsedArgument) {
        return ((Player) parsedArgument);
    }

    @Override
    public Player parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        return ((Player) ArgumentEntity.e(ctx, name).getBukkitEntity());
    }
}
