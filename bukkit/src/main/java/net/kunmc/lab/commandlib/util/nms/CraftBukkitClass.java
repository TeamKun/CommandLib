package net.kunmc.lab.commandlib.util.nms;

public abstract class CraftBukkitClass extends NMSClass {
    public CraftBukkitClass(Object handle, String className) {
        super(handle, NMSReflection.findCraftBukkitClass(className));
    }
}
