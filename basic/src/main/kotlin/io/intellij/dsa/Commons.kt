package io.intellij.dsa

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

fun beautify(str: String, width: Int = 5): String {
  return str.take(width).padEnd(width) + " "
}
