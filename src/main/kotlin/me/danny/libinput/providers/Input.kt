package me.danny.libinput.providers

sealed interface Input

data class SingleLine(val line: String?) : Input
data class MultipleLines(val lines: List<String?>) : Input