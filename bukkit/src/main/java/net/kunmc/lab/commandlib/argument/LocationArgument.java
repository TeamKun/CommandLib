package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentVec3;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Location;

import java.util.function.Consumer;

public class LocationArgument extends Argument<Location> {
    public LocationArgument(String name, Consumer<Option<Location>> options) {
        super(name, ArgumentVec3.a());
        setOptions(options);
    }

    @Override
    public Location parse(CommandContext<CommandListenerWrapper> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        Vec3D vec = ArgumentVec3.a(ctx, name);
        return new Location(ctx.getSource().getBukkitWorld(), vec.x, vec.y, vec.z);
    }
}
