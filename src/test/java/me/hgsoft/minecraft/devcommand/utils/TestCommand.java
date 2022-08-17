package me.hgsoft.minecraft.devcommand.utils;

import me.hgsoft.minecraft.devcommand.executors.CommandExecutor;

public class TestCommand extends CommandExecutor {

    public static boolean called;

    public TestCommand(Object[] args) {
        super(args);
    }

    @Override
    public void execute() {
        System.out.println("Test Command Executed!");
        called = true;
    }

}
