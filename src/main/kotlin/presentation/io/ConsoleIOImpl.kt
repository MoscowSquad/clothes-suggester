package presentation.io

import java.util.*


class ConsoleIOImpl(
    private val scanner: Scanner
) : ConsoleIO {
    override fun read(): String {
        return scanner.nextLine()
    }

    override fun writeln(message: String?, color: String) {
        println("${color}$message${RESET}")
    }

    override fun write(message: String?, color: String ) {
        print("${color}$message${RESET}")
    }

    override fun showError(message: String?) {
        writeln(message, RED)
    }
}