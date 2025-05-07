package domain.use_cases

import com.google.common.truth.Truth
import domain.models.Location
import domain.util.location_getter.LocationFetcher
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GetLocationUseCaseTest {
    private lateinit var locationFetcher: LocationFetcher
    private lateinit var getLocationUseCase: GetLocationUseCase

    @BeforeEach
    fun setUp() {
        locationFetcher = mockk(relaxed = true)
        getLocationUseCase = GetLocationUseCase(locationFetcher)
    }

    @Test
    fun `should get the location via a LocationFetcher when getting the location`() = runTest {
        // when
        getLocationUseCase.getLocation()

        // Then
        coVerify { locationFetcher.getLocation() }
    }

    fun `should return location when `() = runTest {
        // Given
        val location = Location(29.9791854, 31.1316879, "The Great Pyramid of Giza")
        coEvery { locationFetcher.getLocation() } returns location

        // when
        val result = getLocationUseCase.getLocation()

        // Then
        Truth.assertThat(result).isEqualTo(location)
    }
}