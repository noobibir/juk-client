package wtf.jukclient.command;

import lombok.Getter;

public abstract class Command {
    @Getter
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public abstract void execute(String[] args);
}
