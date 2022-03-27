package com.mojang.brigadier.context;

import com.mojang.brigadier.Command;

import java.util.List;

public class CommandContext<S> {
    public String getInput() {
        return "";
    }

    public S getSource() {
        return null;
    }

    public Command<S> getCommand() {
        return null;
    }

    public List<ParsedCommandNode<S>> getNodes() {
        return null;
    }

    public <T> T getArgument(String name, Class<T> clazz) {
        return null;
    }
}
