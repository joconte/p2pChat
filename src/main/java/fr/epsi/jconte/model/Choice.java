package fr.epsi.jconte.model;

public enum Choice {

    CONNECT(1),
    WAIT_FOR_CONNECTION(2),
    EXIT(3),
    ;

    private int choice;

    Choice(int choice) {
        this.choice = choice;
    }
}
