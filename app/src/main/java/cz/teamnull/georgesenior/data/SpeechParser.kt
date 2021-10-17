package cz.teamnull.georgesenior.data

import cz.teamnull.georgesenior.ui.account.AccountViewModel
import java.lang.Integer.max
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

object SpeechParser {

    private const val USE_CASE_THRESHOLD = 0.3

    private val USE_CASES = arrayOf(
        UseCasePrototype("Hello", null, ::hello, "ahoj",
            "cau", "nazdar", "cus",
            "dobry den",
            "zdravicko",
            "zdar"),

        UseCasePrototype("AccountBalance", null, ::tellAccountBalance, "ahoj kolik mam na uctu",
            "hej kolik mam na kontu",
            "kolik mam na konte",
            "ukaz mi stav konta",
            "kolik mam penez",
            "prosim rekni mi stav uctu",
            "stav uctu",
            "stav konta",
            "konto",
            "ucet",
            "penize",
            "stav meho uctu",
            "zbyva mi na uctu",
            "co mi zbyva na uctu"
        ),

        UseCasePrototype("MainExpense", null, ::tellMainExpense, "ahoj jake jsou me nejvetsi utraty",
            "za co utracim nejvic",
            "prosim kde mužu usetrit",
            "na cem muzu usetrit",
            "jaky je nejvetsí vydaj",
            "nejdrazsi polozka na ucte je"
        ),

        UseCasePrototype("PermanentPayment", null, ::tellPermPayment, "ahoj zadej trvalou platbu",
            "prosim kazdy mesic proved platbu",
            "hej trvale provadej platbu"
        ),

        UseCasePrototype("BlockAccount", null, ::blockAccount, "pomoc ztratila jsem kartu",
            "hej chci zablokovat svoji kreditni kartu",
            "prosim zamezte pristupu k moji karte",
            "ahoj co mam delat kdyz mi ukrali debetni kartu",
            "chtel bych zrusit platnost karty"
        ),

        UseCasePrototype("BankID", null, ::tellAccountId, "hej jake je moje cislo uctu",
            "ahoj jak zjistim cislo uctu",
            "prosim rekni u jake jsem banky",
            "cislo meho bankovniho uctu je"
        ),

        UseCasePrototype("ReceiveMoney", null, ::receiveMoney,
            "chci prijmout penize", "chci penize", "prijmout penize", "prijmout platbu"
        ),

        UseCasePrototype("SendMoney",null, ::sendMoneyAskWho, "ahoj posli penize na ucet",
            "prosim preved penize",
            "chci prevod penez",
            "odesli castku",
            "hej preposli castku",
            "chci poslat penize",
            "chci je poslat",
            "odesli penize",
            "posli penize",
            "poslat penize",
            "odosli penize",
            "dej penize",
            "platit",
            "chci platit",
            "posli korun",
            "odesli korun"
        ),

        UseCasePrototype("SendMoneyName", "SendMoney", ::sendMoneyAskAmount,
            "posli penize",
            "posli koruny",
            "posli korun",
            "odesli korun",
            "odesli koruny",
            "chci poslat penize",
            "chci je poslat",
            "odesli penize",
            "posli penize",
            "poslat penize",
            "odosli penize"
        ),
        UseCasePrototype("SendMoneyNameAmount", "SendMoneyName", ::sendMoneyOk,
            "posli penize",
            "posli koruny",
            "posli korun",
            "odesli korun",
            "odesli koruny",
            "korun"
        ),

        UseCasePrototype("SendMoneyNameCancel", "SendMoney", ::dismiss,
            "ukonci",
            "ukoncit",
            "skonci",
            "skonci",
            "zastav",
            "neposilej",
            "nechci",
            "nepokracuj",
            "nedelej to",
            "nech me byt"
        ),
        UseCasePrototype("SendMoneyAmountCancel", "SendMoneyName", ::dismiss,
            "ukonci",
            "ukoncit",
            "skonci",
            "skonci",
            "zastav",
            "neposilej",
            "nechci",
            "nepokracuj",
            "nedelej to",
            "nech me byt"
        ),

        UseCasePrototype("Wrong", null, ::wrong,
            "kokot", "pica", "jebe", "kurvo",
            "kurva", "hovno", "k****", "p***", "mrdka", "mrdka", "chuj"
        ),

        UseCasePrototype("Time", null, ::tellTime,
            "kolik je hodin",
            "jaky je cas",
            "rekni mi cas"
        ),
        UseCasePrototype("Date", null, ::tellDate,
            "jaky je datum",
            "kolikateho je dnes",
            "rekni mi datum",
            "datum"
        ),

    )

    private fun receiveMoney(userWords: Set<String>) : Boolean{
        AccountViewModel.action("receive")
        return true
    }

    private fun tellTime(userWords: Set<String>): Boolean {
        val sdf = SimpleDateFormat("hh:mm")
        val currentDate = sdf.format(Date())
        output("Je $currentDate")
        return true
    }

    private fun tellDate(userWords: Set<String>): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        output("Dnes je $currentDate")
        return true
    }

    private fun wrong(userWords: Set<String>): Boolean {
        output("Mínus 10 korun")
        return true
    }

    private fun hello(userWords: Set<String>): Boolean {
        output("Ahoj")
        return true
    }

    private fun tellAccountId(userWords: Set<String>): Boolean {
        output("2 1 2 8 3 7 2 0 0 4 / 0 8 0 0")
        return true
    }

    private fun blockAccount(userWords: Set<String>): Boolean {
        output("Platební karta byla zablokována, budeme Vás kontaktovat")
        return true
    }

    private fun sendMoneyAskWho(userWords: Set<String>): Boolean {
        val name = userWords.find { it.matches("[A-Z].*".toRegex()) }
        var amount = userWords.find { it.matches("[0-9].*".toRegex()) }
        if (name != null && amount != null) {
            output("$name bylo odesláno $amount")
            if (amount.endsWith("Kč") || amount.endsWith("kč")) amount = amount.dropLast(2)
            AccountViewModel.balance -= amount.toFloat()
            return true
        }
        output("Komu poslat peníze?")
        return false
    }

    private fun tellPermPayment(userWords: Set<String>): Boolean {
        output("Zadal jsem tvalou platbu na účet: 19123457/0710")
        return true
    }

    private fun tellMainExpense(userWords: Set<String>): Boolean {
        output("Největší výdaje jsou u McDonald's - 95%")
        return true
    }

    private fun tellAccountBalance(userWords: Set<String>): Boolean {
        output("42069.34 Kč")
        return true
    }

    private fun sendMoneyAskAmount(userWords: Set<String>): Boolean {
        output("Kolik poslat peněz?")
        return false
    }

    private fun sendMoneyOk(userWords: Set<String>): Boolean {
        if (history.size == 3) {
            var amount = history[2].second.find { it.matches("[0-9].*".toRegex()) } ?: "0"
            var s = amount
            s += " bylo odesláno "
            s += history[1].second.find { it.matches("[A-Z].*".toRegex()) } ?: ""
            output(s)
            if (amount.endsWith("Kč")) amount = amount.dropLast(2)
            AccountViewModel.balance -= amount.toFloat()
        } else {
            output("Platba vykonána")
        }
        return true
    }

    private fun dismiss(userWords: Set<String>): Boolean {
        output("Okej tak nic")
        history.clear()
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
        val userWords = text.split(" ").map {
            if (it.matches("([A-Z].*)|([0-9].*)".toRegex())) it else removeAccents(it)
        }.toSet()
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
        history.add(Pair(bestResult, difference))
        val end = bestResult.useCase.handler.invoke(userWords)
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
                //words.forEach { if (userWords.contains(it)) count++ }
                //val score = count / words.size.toFloat()
                val score = words.intersect(userWords).size / max(userWords.size, words.size).toFloat()
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