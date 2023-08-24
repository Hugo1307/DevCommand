package dev.hugog.minecraft.dev_command.factories;

public interface IObjectFactory<T,C> {
    T generate(C command);
}
