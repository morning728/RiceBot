package net.bot.RiceBot.model.Enums;

public enum State {
    // REGISTRATION
    ASK_LOGIN("ASK_PASSWORD", "registration"),
    ASK_PASSWORD("SUCCESS_REG", "registration"),
    SUCCESS_REG(null, "registration"),
    FAIL_REG("ASK_LOGIN", "registration"),

    // CHANGING DATA

    ASK_OLD_PWD("ASK_NEW_PWD", "changingData"),
    ASK_NEW_PWD("SUCCESS_CHANGES", "changingData"),
    SUCCESS_CHANGES(null, "changingData"),
    FAIL_CHANGES("ASK_OLD_PWD", "changingData"),

    // UPLOAD FILE STATE

    ASK_FOR_UPLOAD(null, "uploadFile");













    private final String nextState;
    private final String code;

    State(String nextState, String code) {
        this.nextState = nextState;
        this.code = code;
    }

    public State next(){
        return State.valueOf(this.nextState);
    }
    public String getCode(){
        return code;
    }
}
