package io.github.changwook987.keyViewer

import java.util.*

fun String.toKeyString(): String {
    return split(" ").joinToString("") {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }.let {
        when (it) {
            "Meta" -> "Win"
            "Comma" -> ","
            "Period" -> "."
            "Semicolon" -> ";"
            "OpenBracket" -> "["
            "CloseBracket" -> "]"
            "Minus" -> "-"
            "Equals" -> "="
            "Escape" -> "ESC"
            "Quote" -> "'"
            "BackQuote" -> "`"
            "Shift", "ShiftL" -> "ShL"
            "UnknownKeyCode:0xe36", "ShiftR" -> "ShR"
            "Left" -> "⬅"
            "Right" -> "➡"
            "Up" -> "⬆"
            "Down" -> "⬇"
            "Slash" -> "/"
            "BackSlash" -> "\\"
            "Enter" -> "↩"
            "Space" -> "␣"
            "Delete" -> "Del"
            "Insert" -> "Ins"
            "PageUp" -> "PgUp"
            "PageDown" -> "PgDn"
            "Control" -> "Ctrl"
            "CapsLock" -> "CLk"
            "Backspace" -> "BS"
            else -> it
        }
    }
}