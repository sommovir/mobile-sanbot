package it.cnr.mobilebot.game.mindgames.supermarket;

import java.util.List;

public class SuperMarketBlob {

    private String initialMessage; //watson text
    private List<Product> solutionProducts;
    private String textualRequest;
    private String vocalRequest;
    private String vocalDescription;
    private String textualDescription;

    public SuperMarketBlob() {
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }

    public List<Product> getSolutionProducts() {
        return solutionProducts;
    }

    public void setSolutionProducts(List<Product> solutionProducts) {
        this.solutionProducts = solutionProducts;
    }

    public String getTextualRequest() {
        return textualRequest;
    }

    public void setTextualRequest(String textualRequest) {
        this.textualRequest = textualRequest;
    }

    public String getVocalRequest() {
        return vocalRequest;
    }

    public void setVocalRequest(String vocalRequest) {
        this.vocalRequest = vocalRequest;
    }


    public String getVocalDescription() {
        return vocalDescription;
    }

    public void setVocalDescription(String vocalDescription) {
        this.vocalDescription = vocalDescription;
    }

    public String getTextualDescription() {
        return textualDescription;
    }

    public void setTextualDescription(String textualDescription) {
        this.textualDescription = textualDescription;
    }

    public SuperMarketBlob(String initialMessage, List<Product> solutionProducts, String textualRequest, String vocalRequest, String vocalDescription, String textualDescription) {
        this.initialMessage = initialMessage;
        this.solutionProducts = solutionProducts;
        this.textualRequest = textualRequest;
        this.vocalRequest = vocalRequest;
        this.vocalDescription = vocalDescription;
        this.textualDescription = textualDescription;
    }





}