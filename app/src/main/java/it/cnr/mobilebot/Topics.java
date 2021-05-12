package it.cnr.mobilebot;

public enum Topics {

    ATTEMPT_LOGIN("attempt_login"),
    ACK_LOGIN("ack_login"),
    CHAT("chat"),
    LOG("log"),
    ATTEMPT_LOGIN_RESPONSE("attempt_login_response"),
    USER_CONNECTED("UserConnected"),
    USER_DISCONNECTED("UserDisconnected"),
    EMERGENCY("emergency"),
    RESPONSES("responses"),
    COMMAND("commands"),
    INFO("info_channel");

    private Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }




    private String topic;
}
