package net.kunmc.lab.commandlib;

import com.google.common.collect.Lists;
import net.kunmc.lab.commandlib.util.fucntion.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class CommonCommand<C extends AbstractCommandContext<?, ?>, B extends AbstractArgumentBuilder<C, B>, T extends CommonCommand<C, B, T>> {
    private final String name;
    private String description = "";
    private T parent = null;
    private boolean inheritParentPrerequisite = true;
    private boolean inheritParentPreprocess = true;
    private final List<T> children = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<Arguments<C>> argumentsList = new ArrayList<>();
    private Predicate<C> prerequisite = ctx -> true;
    private Predicate<C> preprocess = ctx -> true;
    private ContextAction<C> contextAction;
    @SuppressWarnings("unchecked")
    private final PlatformAdapter<?, ?, C, B, T> platformAdapter = (PlatformAdapter<?, ?, C, B, T>) PlatformAdapter.get();

    protected CommonCommand(@NotNull String name) {
        this.name = name;
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }

    public final void setDescription(@NotNull String description) {
        this.description = description;
    }

    @SafeVarargs
    public final void addChildren(@NotNull T child, @NotNull T... children) {
        addChildren(Lists.asList(child, children));
    }

    @SuppressWarnings("unchecked")
    public final void addChildren(@NotNull Collection<? extends T> children) {
        this.children.addAll(children);

        for (CommonCommand<C, B, T> child : children) {
            child.parent = ((T) this);
        }
    }

    public final void addAliases(@NotNull String alias, @NotNull String... aliases) {
        addAliases(Lists.asList(alias, aliases));
    }

    public final void addAliases(@NotNull Collection<String> aliases) {
        this.aliases.addAll(aliases);
    }

    public final void setInheritParentPrerequisite(boolean inherit) {
        this.inheritParentPrerequisite = inherit;
    }

    public final void setInheritParentPreprocess(boolean inherit) {
        this.inheritParentPreprocess = inherit;
    }

    public final void argument(@NotNull Consumer<B> buildArguments) {
        B builder = platformAdapter.createArgumentBuilder();
        buildArguments.accept(builder);
        argumentsList.add(new Arguments<>(builder.build()));
    }

    public final <T1> void argument(@NotNull CommonArgument<T1, C> argument, @NotNull BiConsumer<T1, C> execute) {
        argument(builder -> {
            builder.customArgument(argument)
                   .execute(ctx -> {
                       execute.accept(argument.cast(ctx.getParsedArg(argument.name())), ctx);
                   });
        });
    }

    public final <T1, T2> void argument(@NotNull CommonArgument<T1, C> argument1,
                                        @NotNull CommonArgument<T2, C> argument2,
                                        @NotNull TriConsumer<T1, T2, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name())),
                                      argument2.cast(ctx.getParsedArg(argument2.name())),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3> void argument(@NotNull CommonArgument<T1, C> argument1,
                                            @NotNull CommonArgument<T2, C> argument2,
                                            @NotNull CommonArgument<T3, C> argument3,
                                            @NotNull TetraConsumer<T1, T2, T3, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name())),
                                      argument2.cast(ctx.getParsedArg(argument2.name())),
                                      argument3.cast(ctx.getParsedArg(argument3.name())),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                @NotNull CommonArgument<T2, C> argument2,
                                                @NotNull CommonArgument<T3, C> argument3,
                                                @NotNull CommonArgument<T4, C> argument4,
                                                @NotNull QuintConsumer<T1, T2, T3, T4, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name())),
                                      argument2.cast(ctx.getParsedArg(argument2.name())),
                                      argument3.cast(ctx.getParsedArg(argument3.name())),
                                      argument4.cast(ctx.getParsedArg(argument4.name())),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4, T5> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                    @NotNull CommonArgument<T2, C> argument2,
                                                    @NotNull CommonArgument<T3, C> argument3,
                                                    @NotNull CommonArgument<T4, C> argument4,
                                                    @NotNull CommonArgument<T5, C> argument5,
                                                    @NotNull HexaConsumer<T1, T2, T3, T4, T5, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .customArgument(argument5)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name())),
                                      argument2.cast(ctx.getParsedArg(argument2.name())),
                                      argument3.cast(ctx.getParsedArg(argument3.name())),
                                      argument4.cast(ctx.getParsedArg(argument4.name())),
                                      argument5.cast(ctx.getParsedArg(argument5.name())),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4, T5, T6> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                        @NotNull CommonArgument<T2, C> argument2,
                                                        @NotNull CommonArgument<T3, C> argument3,
                                                        @NotNull CommonArgument<T4, C> argument4,
                                                        @NotNull CommonArgument<T5, C> argument5,
                                                        @NotNull CommonArgument<T6, C> argument6,
                                                        @NotNull HeptConsumer<T1, T2, T3, T4, T5, T6, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .customArgument(argument5)
                   .customArgument(argument6)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name())),
                                      argument2.cast(ctx.getParsedArg(argument2.name())),
                                      argument3.cast(ctx.getParsedArg(argument3.name())),
                                      argument4.cast(ctx.getParsedArg(argument4.name())),
                                      argument5.cast(ctx.getParsedArg(argument5.name())),
                                      argument6.cast(ctx.getParsedArg(argument6.name())),
                                      ctx);
                   });
        });
    }

    public final <T1, T2, T3, T4, T5, T6, T7> void argument(@NotNull CommonArgument<T1, C> argument1,
                                                            @NotNull CommonArgument<T2, C> argument2,
                                                            @NotNull CommonArgument<T3, C> argument3,
                                                            @NotNull CommonArgument<T4, C> argument4,
                                                            @NotNull CommonArgument<T5, C> argument5,
                                                            @NotNull CommonArgument<T6, C> argument6,
                                                            @NotNull CommonArgument<T7, C> argument7,
                                                            @NotNull OctoConsumer<T1, T2, T3, T4, T5, T6, T7, C> execute) {
        argument(builder -> {
            builder.customArgument(argument1)
                   .customArgument(argument2)
                   .customArgument(argument3)
                   .customArgument(argument4)
                   .customArgument(argument5)
                   .customArgument(argument6)
                   .customArgument(argument7)
                   .execute(ctx -> {
                       execute.accept(argument1.cast(ctx.getParsedArg(argument1.name())),
                                      argument2.cast(ctx.getParsedArg(argument2.name())),
                                      argument3.cast(ctx.getParsedArg(argument3.name())),
                                      argument4.cast(ctx.getParsedArg(argument4.name())),
                                      argument5.cast(ctx.getParsedArg(argument5.name())),
                                      argument6.cast(ctx.getParsedArg(argument6.name())),
                                      argument7.cast(ctx.getParsedArg(argument7.name())),
                                      ctx);
                   });
        });
    }

    public final void addPrerequisite(Predicate<C> prerequisite) {
        this.prerequisite = this.prerequisite.and(prerequisite);
    }

    public final void addPreprocess(Consumer<C> preprocess) {
        addPreprocess(ctx -> {
            preprocess.accept(ctx);
            return true;
        });
    }

    /**
     * @param preprocess return false if cancel executing command.
     */
    public final void addPreprocess(Predicate<C> preprocess) {
        this.preprocess = this.preprocess.and(preprocess);
    }

    public final void execute(@NotNull ContextAction<C> execute) {
        this.contextAction = execute;
    }

    final T parent() {
        return parent;
    }

    final List<T> children() {
        return Collections.unmodifiableList(children);
    }

    final Predicate<C> prerequisite() {
        if (!inheritParentPrerequisite || parent == null) {
            return prerequisite;
        }

        return parent.prerequisite()
                     .and(prerequisite);
    }

    final Predicate<C> preprocess() {
        if (!inheritParentPreprocess || parent == null) {
            return preprocess;
        }

        return parent.preprocess()
                     .and(preprocess);
    }

    final List<Arguments<C>> argumentsList() {
        return Collections.unmodifiableList(argumentsList);
    }

    final List<String> aliases() {
        return Collections.unmodifiableList(aliases);
    }

    final boolean isContextActionUndefined() {
        return contextAction == null;
    }

    final ContextAction<C> contextAction() {
        return contextAction;
    }
}
