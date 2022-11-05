package com.dobashi.todolist_jetpack.extensions


/**
 * 文字の長さは1文字だった場合は1文字目に0を追加する
 **/
fun String.addFirstZero() = if (this.length == 1) "0$this" else "$this"

/**
 * 文字列を「:」で区切った文字列をInt型のリストで返す
 */
fun String.splitTime(): List<Int> {
    val tmpTime = this.split(":")
    return listOf(tmpTime[0].toInt(), tmpTime[1].toInt())
}


/**
 * 文字列を「/」で区切った文字列をInt型のリストで返す
 */
fun String.splitDate(): List<Int> {
    val tmpTime = this.split("/")
    return listOf(tmpTime[0].toInt(), tmpTime[1].toInt(), tmpTime[2].toInt())
}