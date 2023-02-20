package net.bot.RiceBot.model.Enums;

public enum State {
    // NULL STATE
    NULL("NULL", null),
    // REGISTRATION
    ASK_LOGIN("ASK_PASSWORD", "registration"),
    ASK_PASSWORD("NULL", "registration"),
    FAIL_REG("ASK_LOGIN", "registration"),

    // LOGIN

    LOGIN("NULL", "login"),

    // CHANGING DATA

    ASK_OLD_PWD("ASK_NEW_PWD", "changingData"),
    ASK_NEW_PWD("SUCCESS_CHANGES", "changingData"),
    SUCCESS_CHANGES("NULL", "changingData"),
    FAIL_CHANGES("ASK_OLD_PWD", "changingData"),

    // UPLOAD FILE STATE

    ASK_FOR_UPLOAD("NULL", "uploadFile"),

    // UPLOAD PHOTOS

    PHOTO_UPLOAD_MODE("NULL", "photoUploadMode");













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
