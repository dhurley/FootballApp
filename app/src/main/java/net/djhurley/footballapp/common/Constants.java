package net.djhurley.footballapp.common;

/**
 * Created by djhurley on 16/01/16.
 */
public class Constants {
    public static final String X_AUTH_TOKEN = "X-Auth-Token";
    public static final String FOOTBALL_API_TOKEN_KEY = "be06c12303c1408189b805502c7281ca";
    public static final String GET_REQUEST = "GET";

    //URLs
    public static final String SEASONS_URL = "http://api.football-data.org/v1/soccerseasons";
    public static final String SEASON_URL = "http://api.football-data.org//v1/soccerseasons/{id}/teams";

    //JSON Keys
    public static final String CAPTION_KEY = "caption";

    //Messages
    public final static String SEASON_MESSAGE = "net.djhurley.footballapp.SEASON_MESSAGE";
}
