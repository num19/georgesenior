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
            null, "TransferMoney", ::sendMoney, "ahoj posli penize na ucet",
            "prosim preved penize",
            "chci prevod penez",
            "odesli castku",
            "hej preposli castku"
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

        UseCasePrototype("TransferMoney", "TransferMoneyName", ::aaaa,
            "posli penize")

        )


    private fun hello(u: UseCasePrototype) {
        output("Ahoj")
    }

    private fun tellMyBankID(u: UseCasePrototype) {
        output("Blokuji kreditní kartu patřící Vašemu účtu")
    }

    private fun blockAccount(u: UseCasePrototype) {
        output("Blokuji kreditní kartu patřící Vašemu účtu")
    }

    private fun sendMoney(u: UseCasePrototype) {
        output("Převádím Marušce 10000 Kč do stripbaru")
    }

    private fun tellPermPayment(u: UseCasePrototype) {
        output("Zadal jsem tvalou platbu na účet: 19123457/0710")
    }

    private fun tellMainExpense(u: UseCasePrototype) {
        output("Největší výdaje jsou u Mc Donalds - 95%")
    }

    private fun tellAccountBalance(u: UseCasePrototype) {
        output("9999999 Kč")
    }

    private fun aaaa(u: UseCasePrototype) {
        output("9999999 Kč")
    }

    private var lastResult: UseCaseEvaluationResult? = null
    private val preparedUseCases = arrayListOf<UseCase>()
    lateinit var output: ((String) -> Unit)

    fun init() {
        USE_CASES.forEach { uc ->
            val phrases = mutableListOf<Set<String>>()
            uc.phrases.forEach { p ->
                phrases.add(p.split(" ").toSet())
            }
            preparedUseCases.add(UseCase(uc.parent, uc.name, uc.forward, phrases))
        }
    }

    fun parse(text: String) {
        val userWords = removeAccents(text).split(" ").toSet()
        val results = evaluate(userWords)
        val bestResult = results.first()
        println("Score: ${bestResult.score}")
        println("Phrase: ${bestResult.bestPhrase}")
        if (bestResult.score < USE_CASE_THRESHOLD) {
            output("Zopakujte...")
            lastResult = null
        }
        // ok
        val bestPhrase = bestResult.bestPhrase
        val difference = userWords.minus(bestPhrase)
        print(difference)
        lastResult = bestResult
    }

    private fun evaluate(userWords: Set<String>): ArrayList<UseCaseEvaluationResult> {
        val results = arrayListOf<UseCaseEvaluationResult>()
        preparedUseCases.forEach { uc ->
            var best = 0
            var bestIndex = 0
            uc.phrases.forEachIndexed  { index, words ->
                var count = 0
                userWords.forEach { if (words.contains(it)) count++ }
                if (count > best) {
                    best = count
                    bestIndex = index
                }
            }
            results.add(UseCaseEvaluationResult(uc, best / userWords.size.toFloat(), bestIndex))
        }
        results.sortByDescending { it.score }
        results.sortedWith(compareBy({ it.score }, {it.bestPhrase.size}))
        //println(results)
        return results
    }

    private fun removeAccents(s: String) = Normalizer.normalize(s, Normalizer.Form.NFD)
        .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")

}