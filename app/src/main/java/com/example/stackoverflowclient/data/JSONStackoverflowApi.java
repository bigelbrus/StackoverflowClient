package com.example.stackoverflowclient.data;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONStackoverflowApi {

    @GET("/2.2/search?order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wwYGT")
    public Call<ListWrapper<Question>> getQuestionsWithTextInTitle(@Query("intitle") String intitle);

    @GET("/2.2/questions/{question_id}/answers?order=desc&sort=activity&site=stackoverflow&filter=!9Z(-wzu0T")
    public Call<ListWrapper<Answer>>getAnswersToQuestion(@Path("question_id") int id);

}
