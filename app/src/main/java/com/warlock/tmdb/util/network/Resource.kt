package com.warlock.tmdb.util.network

/**
 * A generic class that holds a value with its loading status.
 */
data class Resource<ResultType>(
    var status: Status,
    var data: ResultType? = null,
    var error: ApiErrorModel? = null
) {

    companion object {
        /**
         * Creates [Resource] object with `SUCCESS` status and [data].
         */
        fun <ResultType> success(data: ResultType): Resource<ResultType> =
            Resource(
                Status.SUCCESS,
                data
            )

        /**
         * Creates [Resource] object with `LOADING` status to notify
         * the UI to showing loading.
         */
        fun <ResultType> loading(): Resource<ResultType> =
            Resource(Status.RUNNING)

        /**
         * Creates [Resource] object with `ERROR` status and [message].
         */
        fun <ResultType> error(message: ApiErrorModel?): Resource<ResultType> =
            Resource(
                Status.FAILED,
                error = message
            )
    }
}