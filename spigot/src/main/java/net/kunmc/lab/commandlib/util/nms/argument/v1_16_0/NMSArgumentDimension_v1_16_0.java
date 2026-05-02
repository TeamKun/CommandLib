package net.kunmc.lab.commandlib.util.nms.argument.v1_16_0;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentDimension;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;

public class NMSArgumentDimension_v1_16_0 extends NMSArgumentDimension {
    public NMSArgumentDimension_v1_16_0() {
        super(null, "ArgumentDimension");
    }

    protected NMSArgumentDimension_v1_16_0(String className) {
        super(null, className);
    }

    @Override
    public ArgumentType<?> argument() {
        return (ArgumentType<?>) invokeStaticMethod("a");
    }

    @Override
    protected World parseImpl(CommandContext<?> ctx, String name) {
        return toBukkitWorld(invokeStaticMethod("a", ctx, name));
    }

    protected final World toBukkitWorld(Object level) {
        try {
            return (World) ReflectionUtil.getMethodIncludingSuperclasses(level.getClass(), "getWorld")
                                         .invoke(level);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
