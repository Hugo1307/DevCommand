package me.hgsoft.minecraft.devcommand.factories;

public interface IObjectFactory<T,C> {
    T generate(C command);
}
