package net.kunmc.lab.commandlib.util.nms.exception;

import java.util.Arrays;

public class MethodNotFoundException extends RuntimeException {
    public MethodNotFoundException(String methodName, Throwable cause) {
        this(new String[]{methodName}, cause);
    }

    public MethodNotFoundException(String[] methodNames) {
        super(String.format("Could not find method. %s", Arrays.toString(methodNames)));
    }

    public MethodNotFoundException(String[] methodNames, Throwable cause) {
        super(String.format("Could not find method. %s", Arrays.toString(methodNames)), cause);
    }
}
