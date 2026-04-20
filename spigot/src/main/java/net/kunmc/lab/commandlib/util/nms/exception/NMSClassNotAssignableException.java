package net.kunmc.lab.commandlib.util.nms.exception;

public class NMSClassNotAssignableException extends RuntimeException {
    public NMSClassNotAssignableException(Object handle, Class<?> clazz) {
        super(String.format("clazz(%s) is not assignable from handle's class(%s).", clazz, handle.getClass()));
    }
}
