package com.example.habittracker.domain.usecases.network

import com.example.habittracker.data.repositories.CloudHabitRepositoryFake
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetHabitListFromCloudUseCaseTest {

    private lateinit var getHabitListFromCloudUseCase: GetHabitListFromCloudUseCase
    private lateinit var cloudHabitRepositoryFake: CloudHabitRepositoryFake

    @BeforeEach
    fun setUp() = runBlocking {
        cloudHabitRepositoryFake = CloudHabitRepositoryFake()
        getHabitListFromCloudUseCase = GetHabitListFromCloudUseCase(cloudHabitRepositoryFake)
    }

    @Test
    fun `return correct list of habits`() = runTest {
        cloudHabitRepositoryFake.initFilling()
        val listFromUseCase = getHabitListFromCloudUseCase.invoke()
        val listFromRepository = cloudHabitRepositoryFake.getHabitList()
        assertThat(
            listFromUseCase is Success && listFromRepository is Success
                    && listFromUseCase.result == listFromRepository.result
        ).isTrue()
    }

    @Test
    fun `return error`() = runTest {
        cloudHabitRepositoryFake.setErrorReturn()
        val list = getHabitListFromCloudUseCase.invoke()
        assertThat(list is Failure).isTrue()
    }
}