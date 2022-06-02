package service

import java.io.File
import java.nio.charset.Charset

class FileService constructor(val fileName: String) {
    private val filePath: String = "./"
    private val file: File

    init {
        if (File("$filePath$/fileName").exists()) {
            File("$filePath$/fileName").delete()
        }
        file = File("$filePath$/fileName")
    }

    fun saveToFile(string: String) {
        file.writeText(string, Charset.defaultCharset())
    }

    fun loadFromFile(): String {
        return file.readLines().joinToString()
    }
}