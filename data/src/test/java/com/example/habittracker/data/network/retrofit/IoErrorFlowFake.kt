package com.example.habittracker.data.network.retrofit

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.IoErrorFlow
import com.example.habittracker.domain.errors.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IoErrorFlowFake : IoErrorFlow {

    private var cloudError: Flow<Either<IoError, Unit>> =
        flow { emit(Unit.success()) }

    override fun getError(): Flow<Either<IoError, Unit>> = cloudError

    override fun setError(error: Either<IoError, Unit>) {
        cloudError = flow { emit(error) }
    }


}