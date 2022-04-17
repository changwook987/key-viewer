package io.github.changwook987.keyViewer

import org.jnativehook.GlobalScreen
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import java.awt.Color
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.PrintStream
import java.util.*
import kotlin.system.exitProcess

class Main(
    private val keys: List<String>,
    private val bg: Color,
    private val fg: Color
) : NativeKeyListener {

    private val frame = Frame(keys, bg, fg)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val properties = Properties()

            val configStream = try {
                FileInputStream("config.properties")
            } catch (e: FileNotFoundException) {
                PrintStream(File("config.properties")).use {
                    properties.setProperty("key", "A B C")

                    properties.setProperty("bg", Color.WHITE.rgb.toString())
                    properties.setProperty("fg", Color.RED.rgb.toString())

                    properties.store(it, "ConfigFile")
                }

                FileInputStream("config.properties")
            }

            properties.load(configStream)
            configStream.close()

            val keySet = ArrayList<String>()

            properties.getProperty("key", "")
                .split(" ").map { it.toKeyString() }.forEach {
                    if (it !in keySet) keySet += it
                }

            println(properties)

            if (keySet.isEmpty()) {
                println("config is empty")
                exitProcess(0)
            }

            println(keySet.joinToString(" "))

            GlobalScreen.registerNativeHook()
            GlobalScreen.addNativeKeyListener(
                Main(
                    keySet,
                    Color(properties.getProperty("bg").substringAfter("#").toInt(16)),
                    Color(properties.getProperty("fg").substringAfter("#").toInt(16))
                )
            )
        }
    }

    override fun nativeKeyTyped(event: NativeKeyEvent) {}

    private fun changeKey(keyCode: Int, onoff: Boolean) {
        val keyText = NativeKeyEvent.getKeyText(keyCode).toKeyString()

        if (keyText in keys) {
            frame.keyMap[keyText]?.foreground = if (onoff) fg else bg
        }
    }

    override fun nativeKeyPressed(event: NativeKeyEvent) {
        changeKey(event.keyCode, true)
    }

    override fun nativeKeyReleased(event: NativeKeyEvent) {
        changeKey(event.keyCode, false)
    }
}