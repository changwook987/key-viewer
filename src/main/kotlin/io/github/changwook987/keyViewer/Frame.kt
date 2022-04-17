package io.github.changwook987.keyViewer

import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants
import kotlin.system.exitProcess

class Frame(keys: List<String>, bg: Color, fg: Color) : JFrame() {
    val keyMap = HashMap<String, JLabel>()

    var posX = 0
    var posY = 0

    init {
        val fontStream = Frame::class.java.classLoader.getResourceAsStream("Hack-Regular.ttf")
            ?: kotlin.run {
                println("font not found!")
                exitProcess(-1)
            }

        val font = Font.createFont(Font.TRUETYPE_FONT, fontStream).run {
            deriveFont(30f)
        }

        val border = BorderFactory.createLineBorder(fg)

        var x = 0

        for (key in keys) {
            keyMap += key to JLabel(key, SwingConstants.CENTER).apply {
                val len = key.length * 20
                setBounds(x, 0, len, 30)
                x += len
                foreground = bg

                this.border = border
                this.font = font
            }
        }

        setSize(keys.sumOf { it.length * 20 }, 30)
        layout = null

        for (key in keyMap) {
            println(key.value)
            add(key.value)
        }

        //drag
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                posX = e.x
                posY = e.y
            }
        })

        addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                setLocation(e.xOnScreen - posX, e.yOnScreen - posY)
            }
        })

        //configs
        title = "KeyViewer"
        isUndecorated = true

        contentPane.background = bg



        isVisible = true
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
        isAlwaysOnTop = true
    }
}
