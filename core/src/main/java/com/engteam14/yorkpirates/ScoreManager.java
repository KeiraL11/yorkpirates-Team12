package com.engteam14.yorkpirates;

public class ScoreManager {

    private int score;

    /**
     * Initialises a ScoreManager with a default score of 0.
     */
    public ScoreManager(){
        this(0);
    }

    /**
     * Initialises a ScoreManager.
     * @param defaultScore  the default score value.
     */
    public ScoreManager(int defaultScore){
        score = defaultScore;
    }

    /**
     *  Adds an integer value to the score.
     * @param amount    the value to be added.
     */
    public void Add(int amount){
        score += amount;
    }

    /**
     *  Gets the score value in integer form.
     * @return  the score.
     */
    public int Get(){
        return score;
    }

    /**
     *  Gets the score value in string form.
     * @return  the score.
     */
    public String GetString() {
        return Integer.toString(score);
    }

}