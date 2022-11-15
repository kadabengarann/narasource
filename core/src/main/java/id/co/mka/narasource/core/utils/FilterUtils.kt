package id.co.mka.narasource.core.utils

object FilterUtils {

    fun getFilteredQuery(filter: ActivityFilterType): Int {
        return when (filter) {
            ActivityFilterType.ACTIVE_ACTIVITY -> {
                0
            }
            ActivityFilterType.SUCCESS_ACTIVITY -> {
                1
            }
            ActivityFilterType.FAILED_ACTIVITY -> {
                2
            }
            else -> {
                -1
            }
        }
    }
}
