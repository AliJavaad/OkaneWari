package com.example.okanewari

data class Party(
    var name: String,
    var moneySymbol: String = "$",
    var expenseList: ArrayList<Expense> = arrayListOf()
){
    // Class vars
    private val _key = 0
    var members: ArrayList<String> = arrayListOf("You")

    fun addExpense(name: String = "None", total: Double = 0.0){
        expenseList.add(Expense(name, total))
    }

    fun addMember(toAdd: String){
        members.add(toAdd)
    }

    fun getNumOfMems(): Int {
        return members.size
    }

//    fun setMoneySymbol(toSet: String){
//        moneySymbol = toSet
//    }
//
//    fun setPartyName(toSet: String){
//        name = toSet
//    }
}
