package net.kunmc.lab.commandlib.util.nms.chat;

import com.mojang.brigadier.Message;
import net.kunmc.lab.commandlib.util.bukkit.MinecraftVersion;
import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.NMSClassRegistry;
import net.kunmc.lab.commandlib.util.nms.chat.v1_16_0.NMSChatMessage_v1_16_0;
import net.kunmc.lab.commandlib.util.nms.chat.v1_17_0.NMSChatMessage_v1_17_0;
import net.kunmc.lab.commandlib.util.nms.chat.v1_18_0.NMSChatMessage_v1_18_0;
import net.kunmc.lab.commandlib.util.reflection.ReflectionUtil;
import org.bukkit.Bukkit;

/**
 * for before 1.18.2
 */
public abstract class NMSChatMessage extends MinecraftClass {
    public static boolean isSupportedVersion() {
        return new MinecraftVersion(Bukkit.getMinecraftVersion()).isLessThan(new MinecraftVersion("1.19.0"));
    }

    public static NMSChatMessage create(Message handle) {
        return ReflectionUtil.getConstructor(NMSClassRegistry.findClass(NMSChatMessage.class), Message.class)
                             .newInstance(handle);
    }

    public NMSChatMessage(Message handle, String className, String... classNames) {
        super(handle, className, classNames);
    }

    public abstract String getKey();

    public abstract Object[] getArgs();

    static {
        NMSClassRegistry.register(NMSChatMessage.class, NMSChatMessage_v1_16_0.class, "1.16.0", "1.16.5");
        NMSClassRegistry.register(NMSChatMessage.class, NMSChatMessage_v1_17_0.class, "1.17.0", "1.17.1");
        NMSClassRegistry.register(NMSChatMessage.class, NMSChatMessage_v1_18_0.class, "1.18.0", "1.18.2");
    }
}
