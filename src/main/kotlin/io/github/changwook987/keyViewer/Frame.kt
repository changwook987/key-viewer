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

class Frame(keys: List<List<String>>, bg: Color, fg: Color) : JFrame() {
    val keyMap = HashMap<String, JLabel>()

    var posX = 0
    var posY = 0

    init {
        setSize(keys.maxOf {
            it.map {
                it.length * 20
            }.reduce { acc, i ->
                acc + i + 1
            }
        }, keys.size * 31 - 1)
        layout = null

        val fontStream = Frame::class.java.classLoader.getResourceAsStream("Hack-Regular.ttf")
            ?: kotlin.run {
                println("font not found!")
                exitProcess(-1)
            }

        val font = Font.createFont(Font.TRUETYPE_FONT, fontStream).run {
            deriveFont(30f)
        }

        val border = BorderFactory.createLineBorder(fg)

        var y = 0

        for (line in keys) {

            var x = 0

            for (key in line) {
                keyMap += key to JLabel(key, SwingConstants.CENTER).apply {
                    val len = key.length * 20

                    setBounds(x, y, len, 30)

                    x += len + 1

                    foreground = bg

                    this.border = border
                    this.font = font
                }
            }
            y += 31
        }



        for ((_, key) in keyMap) {
            add(key)
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
