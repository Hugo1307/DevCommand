package me.hgsoft.minecraft.devcommand.utils;

import me.hgsoft.minecraft.devcommand.executors.BaseCommandExecutor;

public class TestBaseCommand extends BaseCommandExecutor {

    public static boolean called;

    public TestBaseCommand(Object[] args) {
        super(args);
    }

    @Override
    public void execute() {
        System.out.println("Test Command Executed!");
        called = true;
    }

}
