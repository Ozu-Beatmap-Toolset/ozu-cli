package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class CliArgumentFinder {

    private final List<String> args;

    public CliArgumentFinder(String[] args) {
        this.args = Arrays.asList(args);
    }

    public Optional<String> findArgument(String commandName) {
        final Optional<List<String>> argumentListOpt = findArguments(commandName, 1);
        AtomicReference<Optional<String>> argumentOptRef = new AtomicReference<>(Optional.empty());

        argumentListOpt.ifPresent(argumentList -> {
            if (argumentList.size() > 0) {
                argumentOptRef.set(Optional.of(argumentList.get(0)));
            }
        });

        return argumentOptRef.get();
    }

    public Optional<List<String>> findArguments(String commandName, int numberOfArguments) {
        final AtomicReference<Optional<List<String>>> arguments = new AtomicReference<>(Optional.empty());

        args.stream()
                .filter(s -> s.equals(commandName))
                .findFirst()
                .ifPresent(s -> extractProgramArguments(commandName, numberOfArguments, arguments, s));

        return arguments.get();
    }

    private void extractProgramArguments(String commandName, int numberOfArguments, AtomicReference<Optional<List<String>>> arguments, String command) {
        final int commandIndex = args.indexOf(command);
        if(numberOfArguments + commandIndex >= args.size()) throw new IllegalArgumentException(commandName);
        arguments.set(Optional.of(new ArrayList<>()));
        IntStream.range(1, numberOfArguments + 1).forEach(i -> {
            final String argument = args.get(commandIndex + i);
            arguments.get().get().add(argument);
        });
    }
}
