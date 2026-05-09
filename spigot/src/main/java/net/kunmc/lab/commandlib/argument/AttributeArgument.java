package net.kunmc.lab.commandlib.argument;

import org.bukkit.attribute.Attribute;

public class AttributeArgument extends EnumArgument<Attribute> {
    public AttributeArgument(String name) {
        super(name, Attribute.class);
    }
}
