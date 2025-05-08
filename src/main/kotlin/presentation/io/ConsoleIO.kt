package presentation.io

const val BLACK = "\u001B[30m"
const val RESET = "\u001B[0m"
const val RED = "\u001B[31m"    // Red for failure
const val BLUE = "\u001B[34m"

interface ConsoleIO {
    fun read(): String
    fun writeln(message: String?, color: String = BLACK)
    fun write(message: String?, color: String = BLACK)
    fun showError(message: String?)
}

