package it.cnr.mobilebot.game.mindgames.supermarket;

import java.util.List;

public class SuperMarketBlob {

    private String initialMessage; //watson text
    private List<Products> solutionProducts;
    private String request;
    private String vocalDescription;
    private String textualDescription;

    public SuperMarketBlob() {
    }

    public SuperMarketBlob(List<Products> solutionProducts, String request, String vocalDescription, String textualDescription) {
        this.solutionProducts = solutionProducts;
        this.request = request;
        this.vocalDescription = vocalDescription;
        this.textualDescription = textualDescription;
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

    public String getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }

    public void setTextualDescription(String textualDescription) {
        this.textualDescription = textualDescription;
    }

    public List<Products> getSolutionProducts() {
        return solutionProducts;
    }

    public void setSolutionProducts(List<Products> solutionProducts) {
        this.solutionProducts = solutionProducts;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }





}