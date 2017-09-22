package de.florianschmitt.system.util

import org.slf4j.LoggerFactory

val <T : Any> T.log get() = LoggerFactory.getLogger(this::class.java)!!