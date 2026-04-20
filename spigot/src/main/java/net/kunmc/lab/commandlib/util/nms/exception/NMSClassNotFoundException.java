package net.kunmc.lab.commandlib.util.nms.exception;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NMSClassNotFoundException extends RuntimeException {
    public NMSClassNotFoundException(String className, String... classNames) {
        super(String.format("Could not find classes %s",
                            Stream.concat(Stream.of(className), Stream.of(classNames))
                                  .collect(Collectors.toList())));
    }
}
