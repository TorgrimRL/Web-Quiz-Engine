package engine.service

import engine.dto.QuizQuestionResponse
import engine.dto.QuizResponse

 object QuizTestData {
    fun expectedSampleQuiz(): QuizQuestionResponse = QuizQuestionResponse(
            "The Java Logo",
            "What is depicted on the Java logo?",
            listOf("Robot", "Tea leaf", "Cup of coffee", "Bug"))

    fun checkAnswerCorrectAnswer(): QuizResponse = QuizResponse(true, "Congratulations, you're right!")
    fun checkAnswerWrongAnswer(): QuizResponse = QuizResponse(false, "Wrong answer! Please, try again.")
}