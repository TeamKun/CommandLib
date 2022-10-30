package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.argument.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.Location;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.Vec3Argument;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Consumer;

public class LocationArgument extends Argument<Location> {
    public LocationArgument(String name) {
        this(name, option -> {
        });
    }

    public LocationArgument(String name, Consumer<Option<Location>> options) {
        super(name, Vec3Argument.vec3());
        setOptions(options);
    }

    @Override
    public Location cast(Object parsedArgument) {
        return ((Location) parsedArgument);
    }

    @Override
    public Location parse(CommandContext<CommandSource> ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        Vector3d vec = Vec3Argument.getVec3(ctx, name);
        Location loc = new Location(ctx.getSource().getWorld(), vec.x, vec.y, vec.z);

        Entity sender = ctx.getSource().getEntity();
        if (sender != null) {
            loc.setYaw(sender.rotationYaw);
            loc.setPitch(sender.rotationPitch);
        }

        return loc;
    }
}
