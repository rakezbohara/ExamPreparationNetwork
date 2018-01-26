package com.n1technology.app.exampreparationnetwork.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by RAKEZ on 12/1/2017.
 */

public class Question {
    private String question_id;
    private String question;
    private String correct;
    private String level;
    private List<String> answers = new ArrayList<>();

    public Question() {
    }

    public Question(String question_id, String question, String correct, String level, Map<String, String> answers) {
        this.question_id = question_id;
        this.question = question;
        this.correct = correct;
        this.level = level;
        this.answers.add(answers.get("A"));
        this.answers.add(answers.get("B"));
        this.answers.add(answers.get("C"));
        this.answers.add(answers.get("D"));
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

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers.add(answers.get("A"));
        this.answers.add(answers.get("B"));
        this.answers.add(answers.get("C"));
        this.answers.add(answers.get("D"));
    }
}
