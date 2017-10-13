package de.florianschmitt.system.generators

import org.springframework.stereotype.Component
import java.util.*

@Component
class DefaultIdentifierGenerator : IdentifierGenerator {
    override fun generate(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val sb = StringBuilder()
        val random = Random()
        val length = 10

        for (i in 0 until length - 1) {
            val c = alphabet[random.nextInt(26)]
            sb.append(c)
        }
        return sb.toString()
    }
}