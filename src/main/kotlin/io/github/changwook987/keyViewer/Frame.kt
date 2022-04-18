package io.github.changwook987.keyViewer

import java.awt.Color
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.FileInputStream
import java.io.FileNotFoundException
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
        //configs
        title = "KeyViewer"
        isUndecorated = true

        contentPane.background = bg

        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
        isAlwaysOnTop = true
        layout = null

        setSize(
            keys.maxOf {
                it.sumOf { s ->
                    s.length * 20
                }
            },
            keys.size * 30
        )


        val fontStream = try {
            FileInputStream("font.ttf")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
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

                    x += len

                    foreground = bg
                    background = bg

                    this.border = border
                    this.font = font
                }
            }
            y += 30
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

        isVisible = true

    }
}
