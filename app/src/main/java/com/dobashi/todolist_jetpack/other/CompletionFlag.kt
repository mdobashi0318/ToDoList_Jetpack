package com.dobashi.todolist_jetpack.other

enum class CompletionFlag(val value: String) {
    /** 未完 */
    Unfinished("0"),

    /** 完了 */
    Completion("1");


    companion object {
        fun getCompletionFlag(boolean: Boolean): CompletionFlag =
            if (boolean) Completion else Unfinished

        fun getCompletionFlag(str: String): Boolean =
            str == Completion.value
    }
}
