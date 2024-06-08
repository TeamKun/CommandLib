package net.kunmc.lab.commandlib.argument;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kunmc.lab.commandlib.Argument;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.commandlib.exception.IncorrectArgumentInputException;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentVec3D;
import net.kunmc.lab.commandlib.util.nms.command.NMSCommandListenerWrapper;
import net.kunmc.lab.commandlib.util.nms.world.NMSVec3D;
import org.bukkit.Location;

import java.util.function.Consumer;

public class LocationArgument extends Argument<Location> {
    public LocationArgument(String name) {
        this(name, option -> {
        });
    }

    public LocationArgument(String name, Consumer<Option<Location, CommandContext>> options) {
        super(name,
              NMSArgumentVec3D.create()
                              .argument());
        applyOptions(options);
    }

    @Override
    public Location cast(Object parsedArgument) {
        return ((Location) parsedArgument);
    }

    @Override
    protected Location parseImpl(CommandContext ctx) throws IncorrectArgumentInputException, CommandSyntaxException {
        NMSVec3D vec = NMSArgumentVec3D.create()
                                       .parse(ctx.getHandle(), name());
        return new Location(NMSCommandListenerWrapper.create(ctx.getHandle()
                                                                .getSource())
                                                     .getBukkitWorld(), vec.x(), vec.y(), vec.z());
    }
}
