package io.github.changwook987.keyViewer

import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import org.json.JSONObject
import java.awt.Color
import java.io.FileInputStream
import java.io.PrintStream
import kotlin.system.exitProcess

class Main(
    keymap: List<List<String>>,
    private val bg: Color,
    private val fg: Color
) : NativeKeyListener {

    private val frame = Frame(keymap, bg, fg)
    private val keys = keymap.reduce { acc, strings -> acc + strings }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val json = try {
                JSONObject(
                    FileInputStream("./config.json").bufferedReader().use {
                        it.readLines().joinToString("").trim()
                    }
                )
            } catch (e: Exception) {
                JSONObject().apply {
                    put("keys", listOf(listOf("A", "B", "C"), listOf("D", "E", "F")))
                    put("foreground", "#FF0000")
                    put("background", "#FFFFFF")

                    PrintStream("config.json").bufferedWriter().use {
                        write(it)
                    }
                }
            }

            val keySet = ArrayList<List<String>>()

            json.getJSONArray("keys").let { arr ->
                for (i in 0 until arr.length()) {
                    keySet += arr.getJSONArray(i).map { it.toString().toKeyString() }
                }
            }

            if (keySet.isEmpty()) {
                println("config is empty")
                exitProcess(0)
            }

            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(
                Main(
                    keySet,
                    Color.decode(json.getString("background")),
                    Color.decode(json.getString("foreground"))
                )
            )
        }
    }

    override fun nativeKeyTyped(event: NativeKeyEvent) {}

    private fun changeKey(keyCode: Int, `on-off`: Boolean) {
        val keyText = NativeKeyEvent.getKeyText(keyCode).toKeyString()

        if (keyText in keys) {
            frame.keyMap[keyText]?.foreground = if (`on-off`) fg else bg
        }
    }

    override fun nativeKeyPressed(event: NativeKeyEvent) {
        changeKey(event.keyCode, true)
    }

    override fun nativeKeyReleased(event: NativeKeyEvent) {
        changeKey(event.keyCode, false)
    }
}
