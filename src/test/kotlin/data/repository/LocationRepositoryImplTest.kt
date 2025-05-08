package data.repository

import com.google.common.truth.Truth
import domain.models.Location
import domain.util.location_getter.LocationFetcher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocationRepositoryImplTest {
    private lateinit var locationFetcher: LocationFetcher
    private lateinit var locationRepository: LocationRepositoryImpl

    @BeforeEach
    fun setUp() {
        locationFetcher = mockk(relaxed = true)
        locationRepository = LocationRepositoryImpl()
    }

    @Test
    fun `getLocation() should return the location from location fetcher when getting the location`() = runTest {
        // Given
        val location = Location(29.9791854, 31.1316879, "The Great Pyramid of Giza")
        coEvery { locationFetcher.getLocation() } returns location

        // When
        val result = locationRepository.getLocation(locationFetcher)

        // Then
        Truth.assertThat(result).isEqualTo(location)
    }

    @Test
    fun `getLocation() should get location using LocationFetcher when getting the location`() = runTest {
        // When
        locationRepository.getLocation(locationFetcher)

        // Then
        coVerify { locationFetcher.getLocation() }
    }
}