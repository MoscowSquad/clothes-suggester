package presentation.io

import java.util.*


class ConsoleIOImpl(
    private val scanner: Scanner
) : ConsoleIO {
    override fun read(): String {
        return scanner.nextLine()
    }

    override fun writeln(message: String?, color: String) {
        val reset = "\u001B[0m"   // Reset color
        println("${color}$message${reset}")
    }

    override fun write(message: String?) {
        print(message)
    }

    override fun showError(message: String?) {
        writeln(message, RED)
    }
}