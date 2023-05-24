package net.kunmc.lab.commandlib.util.nms.exception;

import java.util.Arrays;

public class InvokeMethodException extends RuntimeException {
    public InvokeMethodException(String methodName, Throwable cause) {
        this(new String[]{methodName}, cause);
    }

    public InvokeMethodException(String[] methodNames) {
        super(String.format("Could not invoke method. %s", Arrays.toString(methodNames)));
    }

    public InvokeMethodException(String[] methodNames, Throwable cause) {
        super(String.format("Could not invoke method. %s", Arrays.toString(methodNames)), cause);
    }
}
