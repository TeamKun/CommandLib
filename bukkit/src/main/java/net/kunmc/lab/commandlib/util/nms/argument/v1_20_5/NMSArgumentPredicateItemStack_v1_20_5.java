package net.kunmc.lab.commandlib.util.nms.argument.v1_20_5;

import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPredicateItemStack;
import net.kunmc.lab.commandlib.util.nms.world.NMSItemStack;

public class NMSArgumentPredicateItemStack_v1_20_5 extends NMSArgumentPredicateItemStack {
    public NMSArgumentPredicateItemStack_v1_20_5(Object handle) {
        super(handle, "commands.arguments.item.ItemInput");
    }

    @Override
    public NMSItemStack createItemStack(int amount, boolean checkOverStack) {
        return NMSItemStack.create(invokeMethod(new String[]{"createItemStack"},
                                                new Class<?>[]{int.class, boolean.class},
                                                amount,
                                                checkOverStack));
    }
}
