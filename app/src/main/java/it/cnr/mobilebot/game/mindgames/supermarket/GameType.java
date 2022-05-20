package it.cnr.mobilebot.game.mindgames.supermarket;

public enum GameType {
    LISTA_SPESA("CGX001","Lista della spesa","Dopo aver elencato una lista di cose da acquistare al supermercato e\n" +
            "lasciato il tempo di memorizzarla, chiedere di ricordare cosa bisogna comprare in base ad uno specifico\n" +
            "reparto","Dopo aver elencato, una lista di cose da acquistare al supermercato ,e lasciato il tempo di memorizzarla, ti verrà chiesto di ricordareh cosa bisogna acquistare in base ad uno specifico repartoh"),
    DUE("CGX002","","",""),
    TRE("CGX003","","",""),
    UNKNOWN_GAME("","","","");

    private String nomeDelGioco;
    private String descrizioneTestuale;
    private String descrizioneVocale;
    private String code;

    private GameType(String code,String nomeDelGioco, String descrizioneTestuale, String descrizioneVocale) {
        this.nomeDelGioco = nomeDelGioco;
        this.descrizioneTestuale = descrizioneTestuale;
        this.descrizioneVocale = descrizioneVocale;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getNomeDelGioco() {
        return nomeDelGioco;
    }

    public String getDescrizioneTestuale() {
        return descrizioneTestuale;
    }

    public String getDescrizioneVocale() {
        return descrizioneVocale;
    }

    public static GameType of(String code) throws CodeIsInvalidException{
        if (code==null  || code.isEmpty() || !code.matches("[C]{1}[G]{1}[X,Y,Z]{1}[0-9,A-F]{3}") ){
            throw new CodeIsInvalidException();
        }
        switch(code){
            case "CGX001" : return LISTA_SPESA;
            case "CGX002" : return DUE;
            case "CGX003" : return TRE;

            default: return UNKNOWN_GAME;
        }

    }


}