package it.cnr.mobilebot.game.mindgames.supermarket;

public class CodeIsInvalidException extends MindGameExceptions{
    private String details;



    public CodeIsInvalidException(String details) {
        this.details = details;
    }

    public CodeIsInvalidException() {

    }



    @Override
    public String errorMessage() {
        return details;

    }

}