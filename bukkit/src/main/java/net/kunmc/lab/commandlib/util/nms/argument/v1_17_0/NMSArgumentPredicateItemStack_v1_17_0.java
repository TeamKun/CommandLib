package net.kunmc.lab.commandlib.util.nms.argument.v1_17_0;

import net.kunmc.lab.commandlib.util.nms.argument.NMSArgumentPredicateItemStack;
import net.kunmc.lab.commandlib.util.nms.world.NMSItemStack;

public class NMSArgumentPredicateItemStack_v1_17_0 extends NMSArgumentPredicateItemStack {
    public NMSArgumentPredicateItemStack_v1_17_0(Object handle) {
        super(handle, "commands.arguments.item.ArgumentPredicateItemStack");
    }

    @Override
    public NMSItemStack createItemStack(int amount, boolean checkOverStack) {
        return NMSItemStack.create(invokeMethod(new String[]{"a"},
                                                new Class<?>[]{int.class, boolean.class},
                                                amount,
                                                checkOverStack));
    }
}
