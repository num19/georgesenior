package cz.teamnull.georgesenior.data

import java.text.Normalizer

object SpeechParser {

    private const val USE_CASE_THRESHOLD = 0.2

    private val USE_CASES = arrayOf(
        UseCasePrototype(null, "Hello", ::hello, "ahoj"),

        UseCasePrototype(
            null, "AccountBalance", ::tellAccountBalance, "ahoj kolik mam na uctu",
            "hej kolik mam na kontu",
            "kolik mam na konte",
            "ukaz mi stav konta",
            "kolik mam penez",
            "prosim rekni mi stav uctu"
        ),

        UseCasePrototype(
            null, "MainExpense", ::tellMainExpense, "ahoj jake jsou me nejvetsi utraty",
            "za co utracim nejvic",
            "prosim kde mužu usetrit",
            "na cem muzu usetrit",
            "jaky je nejvetsí vydaj"
        ),

        UseCasePrototype(
            null, "PermanentPayment", ::tellPermPayment, "ahoj zadej trvalou platbu",
            "prosim kazdy mesic proved platbu",
            "hej trvale provadej platbu"
        ),

        UseCasePrototype(
            null, "BlockAccount", ::blockAccount, "pomoc ztratila jsem kartu",
            "hej chci zablokovat svoji kreditní kartu",
            "prosim zamezte pristupu k moji karte",
            "ahoj co mam delat kdyz mi ukrali debetni kartu"
        ),

        UseCasePrototype(
            null, "BankID", ::tellMyBankID, "hej jake je moje cislo uctu",
            "ahoj jak zjistim cislo uctu",
            "prosim rekni u jake jsem banky",
            "cislo meho bankovniho uctu je"
        ),

        UseCasePrototype(
            null, "SendMoney", ::sendMoneyAskWho, "ahoj posli penize na ucet",
            "prosim preved penize",
            "chci prevod penez",
            "odesli castku",
            "hej preposli castku"
        ),

        UseCasePrototype(
            "SendMoney", "SendMoneyName", ::sendMoneyAskAmount,
            "posli penize",
            "posli koruny",
            "posli korun",
            "odesli korun",
            "odesli koruny",
            "chci poslat penize",
            "chci je poslat",
            "odesli penize",
            "posli penize"
        ),
        UseCasePrototype(
            "SendMoneyName", "SendMoneyNameAmount", ::sendMoneyOk,
            "posli penize",
            "posli koruny",
            "posli korun",
            "odesli korun",
            "odesli koruny",
            "korun"
        )

    )


    private fun hello(u: UseCase): Boolean {
        output("Ahoj")
        return false
    }

    private fun tellMyBankID(u: UseCase): Boolean {
        output("Blokuji kreditní kartu patřící Vašemu účtu")
        return false
    }

    private fun blockAccount(u: UseCase): Boolean {
        output("Blokuji kreditní kartu patřící Vašemu účtu")
        return false
    }

    private fun sendMoneyAskWho(u: UseCase): Boolean {
        output("Komu poslat peníze?")
        return false
    }

    private fun tellPermPayment(u: UseCase): Boolean {
        output("Zadal jsem tvalou platbu na účet: 19123457/0710")
        return false
    }

    private fun tellMainExpense(u: UseCase): Boolean {
        output("Největší výdaje jsou u Mc Donalds - 95%")
        return false
    }

    private fun tellAccountBalance(u: UseCase): Boolean {
        output("9999999 Kč")
        return false
    }

    private fun sendMoneyAskAmount(u: UseCase): Boolean {
        output("Kolik poslat peněz?")
        return false
    }

    private fun sendMoneyOk(u: UseCase): Boolean {
        output("Platba vykonána")
        return true
    }

    private fun error() = output("Zopakujte to znovu")

    private val history = mutableListOf<Pair<UseCaseEvaluationResult, Set<String>>>()
    private val preparedUseCases = arrayListOf<UseCase>()
    lateinit var output: ((String) -> Unit)

    fun init() {
        USE_CASES.forEach { uc ->
            val phrases = mutableListOf<Set<String>>()
            uc.phrases.forEach { p -> phrases.add(p.split(" ").toSet()) }
            preparedUseCases.add(UseCase(uc.parent, uc.name, uc.forward, phrases))
        }
    }

    fun parse(text: String): Boolean {
        println("parse: $text")
        val userWords = removeAccents(text).split(" ").toSet()
        var results = evaluate(userWords)

        results = if (history.isNotEmpty()) {
            val last = history.last()
            results.filter { it.useCase.parent == last.first.useCase.name }
        } else {
            results.filter { it.useCase.parent == null }
        }
        if (results.isEmpty()) {
            println("filtered results are empty")
            error()
            return false
        }
        val bestResult = results.first()
        println("Score: ${bestResult.score}")
        println("Phrase: ${bestResult.bestPhrase}")
        if (bestResult.score < USE_CASE_THRESHOLD && bestResult.score != 0f) {
            println("low threshold")
            error()
            return false
        }
        // ok
        val bestPhrase = bestResult.bestPhrase
        val difference = userWords.minus(bestPhrase)
        print(difference)
        val end = bestResult.useCase.handler.invoke(bestResult.useCase)
        history.add(Pair(bestResult, difference))
        if (end) history.clear()
        return end
    }

    private fun evaluate(userWords: Set<String>): List<UseCaseEvaluationResult> {
        val results = arrayListOf<UseCaseEvaluationResult>()
        preparedUseCases.forEach { uc ->
            var bestScore = 0f
            var bestIndex = 0
            uc.phrases.forEachIndexed { index, words ->
                var count = 0
                //userWords.forEach { if (words.contains(it)) count++ }
                words.forEach { if (userWords.contains(it)) count++ }
                val score = count / words.size.toFloat()
                if (score > bestScore) {
                    bestScore = score
                    bestIndex = index
                }
            }
            results.add(UseCaseEvaluationResult(uc, bestScore, bestIndex))
        }
        results.sortByDescending { it.score }
        results.sortedWith(compareBy({ it.score }, { it.bestPhrase.size }))
        //println(results)
        return results
    }

    private fun removeAccents(s: String) = Normalizer.normalize(s, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")

}