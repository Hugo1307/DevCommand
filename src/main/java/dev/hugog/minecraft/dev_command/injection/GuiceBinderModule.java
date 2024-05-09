package dev.hugog.minecraft.dev_command.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceBinderModule extends AbstractModule {

    public GuiceBinderModule() {}

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {

    }

}
