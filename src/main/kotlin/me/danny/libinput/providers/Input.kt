package me.danny.libinput.providers

sealed interface Input

data class SingleLine(val line: String) : Input {
    fun toMultiple(): MultipleLines = MultipleLines(listOf(line))
}

data class MultipleLines(val lines: List<String>) : Input {
    fun toSingle(joiner: (List<String>) -> String): SingleLine = SingleLine(joiner(lines))
}