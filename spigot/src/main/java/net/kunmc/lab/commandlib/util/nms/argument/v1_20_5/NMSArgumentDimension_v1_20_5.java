package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentDimension;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;

public class NMSArgumentDimension_v1_20_5 extends NMSArgumentDimension {
    public NMSArgumentDimension_v1_20_5() {
        super(null, "commands.arguments.DimensionArgument");
    }

    @Override
    public ArgumentType<?> argument() {
        return (ArgumentType<?>) invokeStaticMethod("dimension");
    }

    @Override
    protected World parseImpl(CommandContext<?> ctx, String name) {
        return toBukkitWorld(invokeStaticMethod("getDimension", ctx, name));
    }

    private World toBukkitWorld(Object level) {
        try {
            return (World) ReflectionUtil.getMethodIncludingSuperclasses(level.getClass(), "getWorld")
                                         .invoke(level);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
