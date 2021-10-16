package cz.teamnull.georgesenior.data

class UseCaseEvaluationResult(val useCase: UseCase, val score: Float, phraseIndex: Int) {
    val bestPhrase = useCase.phrases[phraseIndex]

    override fun toString() = "USE CASE: $useCase, SCORE: $score, BEST PHRASE: $bestPhrase\n"
}