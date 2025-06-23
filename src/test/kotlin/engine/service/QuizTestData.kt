package engine.service

import engine.dto.PostQuizRequest
import engine.dto.QuizQuestionResponse
import engine.dto.QuizResponse
import engine.entity.Quiz
import engine.entity.User

object QuizTestData {
    fun expectedSampleQuiz(): QuizQuestionResponse = QuizQuestionResponse(
            "The Java Logo",
            "What is depicted on the Java logo?",
            listOf("Robot", "Tea leaf", "Cup of coffee", "Bug"))

    fun checkAnswerCorrectAnswer(): QuizResponse = QuizResponse(true, "Congratulations, you're right!")
    fun checkAnswerWrongAnswer(): QuizResponse = QuizResponse(false, "Wrong answer! Please, try again.")
    fun postQuizRequest(): PostQuizRequest = PostQuizRequest(
            title = "The Java Logo",
            text = "What is depicted on the Java logo?",
            options = listOf("Robot", "Tea leaf", "Cup of coffee", "Bug"),
            answer = listOf(2))



    fun samleUser(): User = User(
            null,
            "123@hotmail.com",
            "123",
            )
}