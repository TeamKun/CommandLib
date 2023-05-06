package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.minecraft.server.v1_16_R3.ArgumentVec3;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Location;

import java.util.function.Consumer;

public class LocationArgument extends Argument<Location> {
    public LocationArgument(String name) {
        this(name, option -> {
        });
    }

    public LocationArgument(String name, Consumer<Option<Location, CommandContext>> options) {
        super(name, ArgumentVec3.a());
        setOptions(options);
    }

    @Override
    public Location cast(Object parsedArgument) {
        return ((Location) parsedArgument);
    }

    @Override
    protected Location parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        Vec3D vec = ArgumentVec3.a(ctx.getHandle(), name);
        return new Location(ctx.getHandle()
                               .getSource()
                               .getBukkitWorld(), vec.x, vec.y, vec.z);
    }
}
