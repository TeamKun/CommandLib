package net.kunmc.lab.commandlib.util.nms.argument;

import net.kunmc.lab.commandlib.util.nms.MinecraftClass;
import net.kunmc.lab.commandlib.util.nms.world.NMSItemStack;

public class NMSArgumentPredicateItemStack extends MinecraftClass {
    public NMSArgumentPredicateItemStack(Object handle) {
        super(handle,
              "ArgumentPredicateItemStack",
              "commands.arguments.item.ArgumentPredicateItemStack",
              "commands.arguments.item.ItemInput");
    }

    public NMSItemStack createItemStack(int amount, boolean checkOverStack) {
        return new NMSItemStack(invokeMethod(new String[]{"a", "createItemStack"},
                                             new Class<?>[]{int.class, boolean.class},
                                             amount,
                                             checkOverStack));
    }
}
