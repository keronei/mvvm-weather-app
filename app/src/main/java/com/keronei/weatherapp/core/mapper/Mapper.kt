package com.keronei.weatherapp.core.mapper

import kotlinx.coroutines.*

abstract class Mapper<Input, Output> {
    abstract fun map(input: Input): Output

    suspend fun mapList(inputs: List<Input>?, sentCoroutineScope: CoroutineScope): List<Output> {

        val deferredOutputs = mutableListOf<Deferred<Output>>()

        val job = sentCoroutineScope.async {
            inputs?.forEach { input: Input ->
                val pendingResult = async {
                    map(input)
                }
                deferredOutputs.add(pendingResult)
            }

        }

        job.join()

        return deferredOutputs.awaitAll()
    }
}