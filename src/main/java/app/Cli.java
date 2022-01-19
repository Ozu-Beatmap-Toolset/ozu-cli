package app;

import commands.CommandHandler;

public class Cli {

    public static void main(String[] args) {
        CommandHandler.executeWithProgramArguments(args);
    }
}
