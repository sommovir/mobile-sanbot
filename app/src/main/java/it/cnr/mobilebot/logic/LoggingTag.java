package it.cnr.mobilebot.logic;

public enum LoggingTag {
    ELAPSED_TIME("ELAPSED TIME"),
    SYSTEM_TURNS("SYSTEM TURNS"),
    USER_TURNS("USER TURNS"),
    TOTAL_TURNS("TOTAL TURNS"),
    TIMEOUT("TIMEOUT"),
    REJECTS("REJECTS"),
    REPROMPT("REPROMPT"),
    NOANSWER("NOANSWER"),
    NO_USER_ANSWER("NO USER ANSWER"),
    CANCEL("CANCEL"),
    BARGEINS("BARGEINS");


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
