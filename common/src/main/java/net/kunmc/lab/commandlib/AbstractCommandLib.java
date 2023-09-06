package net.kunmc.lab.commandlib;

import java.util.NoSuchElementException;

abstract class AbstractCommandLib {
    AbstractCommandLib() {
        try {
            PlatformAdapter.get();
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Could not find PlatformAdapter");
        }
    }
}
