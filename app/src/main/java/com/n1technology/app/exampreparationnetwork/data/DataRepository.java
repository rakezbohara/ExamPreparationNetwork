package com.n1technology.app.exampreparationnetwork.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAKEZ on 11/29/2017.
 */

public class DataRepository {

    private static DataRepository dataRepository;

    public static DataRepository getInstance(){
        if(dataRepository==null){
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }


    public  List<String> getQuestionId(int currentLevel){
        List<String> questionId = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++){
            questionId.add("ques"+i); //1
        }
        return questionId;
    }

    public List<String> getQuestions(int currentLevel){
        List<String> questions = new ArrayList<>();
        questions.add("If their are 720 buses coming from biratnagar to kathmandu dated on 27th July, 2018. The main highway through which bus are coming is 10 cm wide? Do you think the buses can easily pass through?"); //1
        questions.add("Very Short Question"); //2
        questions.add("Long Question than usaual content of the question. This is example of long question"); //3
        questions.add("Long Question than usaual content of the question. This is example of long question. Question exceeding 5 lines what happens. Long Question than usaual content of the question. This is example of long question"); //4
        questions.add("The numbers whose root cannot be defined are"); //5
        questions.add("Capacity of 2 jars which initially were 2 liters each at 20 degree celcius, at 55 degree celcius, given jars are made of copper "); //6
        questions.add("What is birth data and birth place of Martin Luther King?"); //7
        questions.add("What will be population of nepel after 10 years if the population growth rate remains constant?"); //8
        questions.add("Length of East-West Mahendra highway?"); //9
        questions.add("Is 5 a rational number?"); //10
        questions.add("Capital of Nepal?"); //11
        questions.add("Long Question than usaual content of the question. This is example of long question"); //12
        questions.add("Capital of Nepal?"); //13
        questions.add("Long Question than usaual content of the question. This is example of long question"); //14
        questions.add("The numbers whose root cannot be defined are"); //15
        questions.add("What will be population of nepel after 10 years if the population growth rate remains constant?"); //16
        questions.add("Capital of Nepal?"); //17
        questions.add("Long Question than usaual content of the question. This is example of long question"); //18
        questions.add("Capital of Nepal?"); //19
        questions.add("Last question of the set?"); //20

        return questions;
    }

    public List<List<String>> getAnswers(int currentLevel){
        List<String> answer;
        List<List<String>> answersList = new ArrayList<>();
        for(int i=0 ; i<20 ; i++){
            answer  = new ArrayList<>();
            answer.add("Short option : "+i); //1
            answer.add("Medium size option for the question : "+i); //2
            answer.add("Long option for the question according to the demand. This answer should exceed 2 lines : "+i); //3
            answer.add("No : "+i); //4
            answersList.add(answer);
        }
        return answersList;

    }

    public List<Integer> getcorrectAnswers(int currentLevel){
        List<Integer> correntAnswers = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++){
            correntAnswers.add((i%4)); //1
        }
        return correntAnswers;
    }

    /*public List<Course> getAllCourses(){
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course("Loksewa for Kharidar","0eft"));
        courseList.add(new Course("Computer Operator","0eft"));
        courseList.add(new Course("Civil Engineer Level 7","0eft"));
        courseList.add(new Course("Adhikirt Level 8","0eft"));
        courseList.add(new Course("A. S. P. Police","0eft"));
        courseList.add(new Course("Electrical Engineer | NEA","0eft"));
        courseList.add(new Course("Adhikirt Level 8","0eft"));
        courseList.add(new Course("A. S. P. Police","0eft"));
        courseList.add(new Course("Electrical Engineer | NEA","0eft"));
        return courseList;
    }*/

    public Integer getCurrentLevel(String courseId){
        return 5;
    }

    public Integer getTotalLevel(String courseId){
        return 10;
    }


}
