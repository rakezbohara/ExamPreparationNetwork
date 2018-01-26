package com.n1technology.app.exampreparationnetwork.SugarModel;

import com.orm.SugarRecord;

/**
 * Created by RAKEZ on 12/12/2017.
 */

public class Questions extends SugarRecord<Questions> {
    private String question_id;
    private String question;
    private String correct;
    private String level;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;

    public Questions() {
    }

    public Questions(String question_id, String question, String correct, String level, String optiona, String optionb, String optionc, String optiond) {
        this.question_id = question_id;
        this.question = question;
        this.correct = correct;
        this.level = level;
        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOptiona() {
        return optiona;
    }

    public void setOptiona(String optiona) {
        this.optiona = optiona;
    }

    public String getOptionb() {
        return optionb;
    }

    public void setOptionb(String optionb) {
        this.optionb = optionb;
    }

    public String getOptionc() {
        return optionc;
    }

    public void setOptionc(String optionc) {
        this.optionc = optionc;
    }

    public String getOptiond() {
        return optiond;
    }

    public void setOptiond(String optiond) {
        this.optiond = optiond;
    }
}
