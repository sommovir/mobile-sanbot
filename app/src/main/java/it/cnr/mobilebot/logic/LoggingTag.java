package it.cnr.mobilebot.logic;

public enum LoggingTag {
    ELAPSED_TIME("ELAPSED TIME"),
    SYSTEM_TURNS("SYSTEM TURNS"),
    TOTAL_SYSTEM_TURNS("TOTAL SYSTEM TURNS"),
    USER_TURNS("USER TURNS"),
    TOTAL_USER_TURNS("TOTAL USER TURNS"),
    TOTAL_TURNS("TOTAL TURNS"),
    TIMEOUT("TIMEOUT"),
    REJECTS("REJECTS"),
    REPROMPT("REPROMPT"),
    NOANSWER("NOANSWER"),
    NO_USER_ANSWER("NO USER ANSWER"),
    CANCEL("CANCEL"),
    NOTE("NOTE"),
    BARGEINS("BARGEINS"),
    USER_CONNECTED("USER CONNECTED"),
    USER_DISCONNECTED("USER DISCONNECTED"),
    CHANGE_USERNAME("CHANGE_USERNAME"); //when the user change his username


    private LoggingTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    /**
     * Ritorna il valore del tag circondato da minore e maggiore
     * @return il tag con maggiore e minore
     */
    public String getTag(){
        return "<" + tag + ">";
    }
}
