package presentation.io

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

class ConsoleIOImplTest {

    private lateinit var mockScanner: Scanner
    private lateinit var consoleIO: ConsoleIOImpl
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        mockScanner = mockk(relaxed = true)

        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        consoleIO = ConsoleIOImpl(mockScanner)
    }

    @Test
    fun `read should return input from scanner`() {
        // Given
        val expectedInput = "test input"
        every { mockScanner.nextLine() } returns expectedInput

        // When
        val result = consoleIO.read()

        // Then
        assertThat(result).isEqualTo(expectedInput)
        verify { mockScanner.nextLine() }
    }


    @Test
    fun `write should output message to console`() {
        // Given
        val message = "test message"

        // When
        consoleIO.write(message)

        // Then
        val expected = "${BLACK}test message${RESET}"
        val output = outputStream.toString().trim()
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `writeln should output message to console`() {
        // Given
        val message = "test message"

        // When
        consoleIO.writeln(message)

        // Then
        val expected = "${BLACK}test message${RESET}"
        val output = outputStream.toString().trim()
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `write should output colored message to console`() {
        // Given
        val message = "test message"

        // When
        consoleIO.write(message, BLUE)

        // Then
        val expected = "${BLUE}test message${RESET}"
        val output = outputStream.toString().trim()
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `writeln should output colored message to console`() {
        // Given
        val message = "test message"

        // When
        consoleIO.writeln(message, BLUE)

        // Then
        val expected = "${BLUE}test message${RESET}"
        val output = outputStream.toString().trim()
        assertThat(output).isEqualTo(expected)
    }


    @Test
    fun `showError should output red colored message`() {
        // Given
        val errorMessage = "error message"

        // When
        consoleIO.showError(errorMessage)

        // Then
        val expected = "${RED}${errorMessage}${RESET}"
        val output = outputStream.toString().trim()
        assertThat(output).isEqualTo(expected)
    }
}