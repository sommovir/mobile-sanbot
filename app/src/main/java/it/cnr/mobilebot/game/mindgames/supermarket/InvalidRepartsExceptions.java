package it.cnr.mobilebot.game.mindgames.supermarket;

public class InvalidRepartsExceptions extends MindGameExceptions {

    @Override
    public String errorMessage() {
        return "la lista contiene un reparto nullo o invalido";
    }

}