package net.kunmc.lab.commandlib.util.fucntion;

public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
