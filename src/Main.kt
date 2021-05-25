import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Main {
    val accountList = ArrayList<Account>()

    fun createAccount() {
        print("Telefon raqami: ")
        val scanner = Scanner(System.`in`)
        val phoneNumber = scanner.nextLine()
        print("Parol yarating: ")
        val password = scanner.nextLine()
        if (phoneNumber.isEmpty() || password.isEmpty()) {
            println("Telefon raqami yoki parol bo'sh bo'lmasligi kerak!")
        } else {
            val manager = Manager()
            manager.password = password
            accountList.add(Account(phoneNumber, password, manager))
            println()
            println("Muvaffaqiyatli yaratildi!")
        }
    }

    fun showAllAccounts() {
        if (accountList.isNotEmpty()) {
            for (i in 0 until accountList.size) {
                println()
                println("${i + 1}. ${accountList[i].phoneNumber}")
                println()
                println("---------------------------------------------")
            }
        } else {
            println()
            println("Hali akkaunt mavjud emas!")
        }
    }

    fun payme(manager: Manager) {
        val scanner = Scanner(System.`in`)

        while (true) {
            println()

            println("1 -> Add card")
            println("2 -> Show all cards")
            println("3 -> Delete card")
            println("4 -> Transfer money")
            println("5 -> Transfer between cards ")
            println("6 -> Expences list ")
            println("7 -> Incomes list ")
            println("8 -> Receive money ")
            println("9 -> Refund money ")
            println("10 -> Sign out ")

            println()
            print("Input number: ")
            val son = scanner.nextInt()
            when (son) {
                1 -> manager.addCard()
                2 -> manager.showCards()
                3 -> manager.deleteCard()
                4 -> manager.transferAnotherCard()
                5 -> manager.transferBetweenCards()
                6 -> manager.showExpencesHistory()
                7 -> manager.showIncomesHistory()
                8 -> manager.receiveMoney()
                9 -> manager.refund()
                10 -> break
            }
        }
    }

    fun checkPassword(choosedAccountSerialNumber: Int, password: String): Boolean {
        var check = false
        if (accountList[choosedAccountSerialNumber].password.equals(password)) {
            check = true
        }
        return check
    }

}

fun main() {

    println()
    println("Payme ilovasiga xush kelibsiz!")
    println()

    val scanner2 = Scanner(System.`in`)
    val main = Main()

    while (true) {

        println()
        println("1 -> Akkauntga yaratish")
        println("2 -> Akkauntga kirish")

        println()
        print("Input number: ")

        val s = scanner2.nextInt()
        when (s) {
            1 -> main.createAccount()
            2 -> {
                main.showAllAccounts()

                println()
                print("Akkauntni tanlang: ")

                val scanner = Scanner(System.`in`)
                val choosedAccountSerialNumber = scanner.nextInt()

                println()
                print("Parolni kiriting: ")
                val scanner3 = Scanner(System.`in`)
                val password = scanner3.nextLine()
                if (main.checkPassword(choosedAccountSerialNumber - 1, password)) {
                    main.payme(main.accountList[choosedAccountSerialNumber - 1].manager)
                } else {
                    println()
                    println("Parol noto'g'ri!")
                }
            }
        }
    }

}

class Manager {

    var cardList = ArrayList<Card>()
    var expenceHistoryList = ArrayList<ExpenceHistory>()
    var incomeHistoryList = ArrayList<IncomeHistory>()
    var password = "parol"

    fun addCard() {
        val scanner = Scanner(System.`in`)

        print("Karta egasining ismi: ")
        val name = scanner.nextLine()

        print("Karta egasining familiyasi: ")
        val surname = scanner.nextLine()

        print("Karta egasining sharifi: ")
        val patronomic = scanner.nextLine()

        val cardNumber = inputCardNumber()

        print("Karta amal qilish muddati: ")
        val cardDate = scanner.nextLine()

        val balance = inputBalance()

        print("Karta tipi(Humo/UzCard): ")
        val scanner2 = Scanner(System.`in`)
        val type = scanner2.nextLine()

        print("Bank: ")
        val bank = scanner2.nextLine()

        cardList.add(Card(name, surname, patronomic, cardNumber, cardDate, balance, type, bank))

        showCards()
    }

    fun inputBalance(): Double {
        val scanner = Scanner(System.`in`)

        print("Karta balansi : ")
        val balance = scanner.nextLine().toDoubleOrNull()

        return when (balance) {
            null -> inputBalance()
            else -> balance
        }
    }

    fun inputCardNumber(): String {
        val scanner = Scanner(System.`in`)

        print("Karta raqami(16 raqam) : ")
        val cardNumber = scanner.nextLine()

        if (cardNumber.length != 16) {
            inputCardNumber()
        }
        return cardNumber
    }

    fun showCards() {
        if (cardList.isNotEmpty()) {
            for (i in 0 until cardList.size) {
                println()
                println("${i + 1}.  ${cardList[i].bank} ${cardList[i].type}")
                println("Karta raqami: ${cardList[i].cardNumber}")
                println("Balansi: ${cardList[i].balance}")
                println()
                println("---------------------------------------------")
            }
        } else {
            noCards()
        }
    }

    fun noCards() {
        println()
        println("Kartalar mavjud emas!")
    }

    fun deleteCard() {
        if (cardList.isNotEmpty()) {
            showCards()
            println()
            print("Qaysi kartani o'chirmoqchisiz: ")
            val scanner = Scanner(System.`in`)
            val n = scanner.nextInt()
            cardList.remove(cardList.removeAt(n - 1))
        } else {
            noCards()
        }
    }

    fun transferAnotherCard() {
        if (cardList.isNotEmpty()) {
            showCards()

            print("Qaysi kartadan pul o'tkazmoqchisiz?: ")
            val scanner = Scanner(System.`in`)
            val scanner2 = Scanner(System.`in`)
            val i = scanner.nextInt()
            val cardFortransfer = cardList[i - 1]

            val cardNumber = inputCardNumber()

            print("Summa: ")
            val sum = scanner.nextDouble()
            val sumWithInterest = sum * 1.01

            print("Izoh: ")
            val comment = scanner2.nextLine()

            println()
            println("O'tkazma miqdori: $sum")
            println("Xizmat haqi: ${sum * 0.01}")
            println("Umumiy miqdor: $sumWithInterest")
            println()

            if (cardFortransfer.balance >= sumWithInterest) {
                println()
                println("O'tkazilsinmi?")
                println("1 -> Ha")
                println("2 -> Yo'q")
                val n = scanner.nextInt()

                when (n) {
                    1 -> {
                        cardList[i - 1].balance = cardList[i - 1].balance - sumWithInterest
                        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                        val currentDate = sdf.format(Date())
                        expenceHistoryList.add(
                            ExpenceHistory(
                                cardFortransfer.cardNumber,
                                cardNumber,
                                sumWithInterest,
                                currentDate,
                                comment
                            )
                        )
                        println()
                        println("Muvaffaqqiyatli o'tkazildi!")
                    }
                    2 -> {
                        return
                    }
                }

            } else {
                println()
                println("Kartada yetarli mablag' mavjud emas")
                transferAnotherCard()
            }
        } else {
            noCards()
        }
    }

    fun transferBetweenCards() {
        if (cardList.isNotEmpty()) {
            showCards()

            println()
            print("Qaysi kartadan: ")
            val scanner = Scanner(System.`in`)
            val scanner2 = Scanner(System.`in`)
            val fromCardIndex = scanner.nextInt()

            showCardsFilter(fromCardIndex - 1)
            print("Qaysi kartaga: ")
            val toCardIndex = scanner2.nextInt()

            if (fromCardIndex != toCardIndex
                && fromCardIndex > 0
                && fromCardIndex <= cardList.size
                && toCardIndex > 0
                && toCardIndex <= cardList.size
            ) {
                transferBetweenCards2(fromCardIndex, toCardIndex)
            } else {
                transferBetweenCards()
            }


        } else {
            noCards()
        }
    }

    fun transferBetweenCards2(fromCardIndex: Int, toCardIndex: Int) {
        println()
        print("Summa: ")
        val scanner = Scanner(System.`in`)
        val scanner2 = Scanner(System.`in`)
        val sum = scanner.nextDouble()
        val sumWithInterest = sum * 1.01

        print("Izoh: ")
        val comment = scanner2.nextLine()

        if (cardList[fromCardIndex - 1].balance > sumWithInterest) {
            cardList[fromCardIndex - 1].balance = cardList[fromCardIndex - 1].balance - sumWithInterest
            cardList[toCardIndex - 1].balance = cardList[toCardIndex - 1].balance + sum

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            expenceHistoryList.add(
                ExpenceHistory(
                    cardList[fromCardIndex - 1].cardNumber,
                    cardList[toCardIndex - 1].cardNumber,
                    sum,
                    currentDate,
                    comment
                )
            )

            incomeHistoryList.add(IncomeHistory(cardList[toCardIndex - 1].cardNumber, sum, currentDate, comment))

            println()
            println("Muvaffaqqiyatli o'tkazildi!")
        } else {
            println()
            println("$fromCardIndex-kartada yetarli mablag' mavjud emas")
            transferBetweenCards2(fromCardIndex, toCardIndex)
        }
    }

    fun wasMySecondCard(cardNumber: String): Int {
        var index = -1

        for (i in 0 until cardList.size) {
            if (cardList[i].cardNumber == cardNumber) {
                index = i
                break
            }
        }
        return index
    }

    fun showCardsFilter(cardSerialNumber: Int) {
        if (cardList.size > 1) {
            for (i in 0 until cardList.size) {
                if (cardSerialNumber != i) {
                    println()
                    println("${i + 1}.  ${cardList[i].bank} ${cardList[i].type}")
                    println("Karta raqami: ${cardList[i].cardNumber}")
                    println("Balansi: ${cardList[i].balance}")
                    println()
                    println("---------------------------------------------")
                }
            }
        } else {
            println()
            println("Avval yana bir karta qo'shing!")
        }
    }

    fun showExpencesHistory() {
        println()
        showCards()

        println()
        print("Kartani tanlang: ")

        val scanner = Scanner(System.`in`)
        val choosedCardSerialNumber = scanner.nextInt()

        if (choosedCardSerialNumber > 0 && choosedCardSerialNumber <= cardList.size) {
            val filteredList = showFilteredExpenceHistory(choosedCardSerialNumber - 1)
            if (filteredList.isNotEmpty()) {
                for (i in 0 until filteredList.size) {
                    println()
                    print("Qaysi kartadan: ")
                    println(filteredList[i].cardNumber)

                    print("Qaysi kartaga: ")
                    println(filteredList[i].toCardNumber)

                    print("O'tkazma miqdori: ")
                    println(filteredList[i].sum)

                    print("Sana: ")
                    println(filteredList[i].date)

                    print("Izoh: ")
                    println(filteredList[i].comment)
                    println()
                    println("---------------------------------------------")
                }
            } else {
                println()
                println("Ushbu kartadan hali tranzaksiya amalga oshirilmagan!")
            }
        } else {
            showExpencesHistory()
        }
    }

    fun showIncomesHistory() {
        println()
        showCards()

        println()
        print("Kartani tanlang: ")

        val scanner = Scanner(System.`in`)
        val choosedCardSerialNumber = scanner.nextInt()

        if (choosedCardSerialNumber > 0 && choosedCardSerialNumber <= cardList.size) {
            val filteredList = showFilteredIncomeHistory(choosedCardSerialNumber - 1)
            if (filteredList.isNotEmpty()) {
                for (i in 0 until filteredList.size) {
                    println()
                    print("Qaysi kartaga: ")
                    println(filteredList[i].cardNumber)

                    print("O'tkazma miqdori: ")
                    println(filteredList[i].sum)

                    print("Sana: ")
                    println(filteredList[i].date)

                    print("Izoh: ")
                    println(filteredList[i].comment)
                    println()
                    println("---------------------------------------------")
                }
            } else {
                println()
                println("Ushbu kartadan hali tranzaksiya amalga oshirilmagan!")
            }
        } else {
            showIncomesHistory()
        }
    }

    fun showFilteredExpenceHistory(index: Int):ArrayList<ExpenceHistory> {
        val filteredList = ArrayList<ExpenceHistory>()

        for (i in 0 until expenceHistoryList.size) {
            if (expenceHistoryList[i].cardNumber.equals(cardList[index].cardNumber)) {
                filteredList.add(expenceHistoryList[i])
            }
        }

        return filteredList
    }

    fun showFilteredIncomeHistory(index: Int):ArrayList<IncomeHistory> {
        val filteredList = ArrayList<IncomeHistory>()

        for (i in 0 until incomeHistoryList.size) {
            if (incomeHistoryList[i].cardNumber.equals(cardList[index].cardNumber)) {
                filteredList.add(incomeHistoryList[i])
            }
        }

        return filteredList
    }

    fun receiveMoney() {
        showCards()

        println()
        print("Kartani tanglang: ")
        val scanner = Scanner(System.`in`)
        val scanner2 = Scanner(System.`in`)
        val choosedCardSerialNumber = scanner.nextInt()
        if (choosedCardSerialNumber > 0 && choosedCardSerialNumber <= cardList.size) {
            val choosedCard = cardList[choosedCardSerialNumber - 1]

            print("Summa: ")
            val sum = scanner.nextDouble()

            print("Izoh: ")
            val comment = scanner2.nextLine()

            cardList[choosedCardSerialNumber - 1].balance = cardList[choosedCardSerialNumber - 1].balance + sum

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            incomeHistoryList.add(IncomeHistory(choosedCard.cardNumber, sum, currentDate, comment))

            println()
            println("Muvaffaqqiyatli o'tkazildi!")

        } else {
            receiveMoney()
        }
    }

    fun refund() {
        if (expenceHistoryList.isNotEmpty()) {
            showCards()
            val scanner = Scanner(System.`in`)
            val scanner2 = Scanner(System.`in`)

            println()
            print("Qaysi karta: ")
            val cardSerialNumber = scanner.nextInt()
            val filteredList = filteredCardList(cardSerialNumber - 1)

            if (filteredList.isNotEmpty()) {

                println("Ushbu kartadagi oxirgi o'tkazma hisoboti")
                println("Summa: ${filteredList.last().sum}")
                println("Vaqti: ${filteredList.last().date}")
                println("Izoh: ${filteredList.last().comment}")

                println()
                println("Ushbu tranzaksiyani bekor qilasizmi?")
                println("1 -> Ha")
                println("1 -> Yo'q")

                val yesNo = scanner2.nextInt()
                when (yesNo) {
                    1 -> {
                        cardList[cardSerialNumber - 1].balance =
                            cardList[cardSerialNumber - 1].balance + filteredList.last().sum

                        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                        val currentDate = sdf.format(Date())
                        incomeHistoryList.add(
                            IncomeHistory(
                                cardList[cardSerialNumber - 1].cardNumber,
                                filteredList.last().sum,
                                currentDate,
                                "Tranzaksiyani bekor qilish"
                            )
                        )

                        if (wasMySecondCard(filteredList.last().toCardNumber) != -1) {
                            val index = wasMySecondCard(filteredList.last().toCardNumber)
                            cardList[index].balance = cardList[index].balance - filteredList.last().sum
                        }

                        expenceHistoryList.remove(filteredList.last())
                    }
                    2 -> return
                }
            } else {
                println()
                println("Ushbu kartadan hali tranzaksiya amalga oshirilmagan!")
            }

        } else {
            println()
            println("Hali tranzaksiyalar mavjud emas!")
        }
    }

    private fun filteredCardList(cardSerialNumber: Int): ArrayList<ExpenceHistory> {
        val filteredList = ArrayList<ExpenceHistory>()

        for (i in 0 until expenceHistoryList.size) {
            if (expenceHistoryList[i].cardNumber.equals(cardList[cardSerialNumber].cardNumber, true)) {
                filteredList.add(expenceHistoryList[i])
            }
        }

        return filteredList
    }
}