package graf.semmel.valium.form

class ShowErrors {

    var onFirstEdit: Boolean = false
    var onSubsequentEdit: Boolean = true
    var onLeaveField: Boolean = true
    var onCondition: (shouldShow: Boolean) -> Boolean = { it }

    fun shouldShow(hasLostFocus: Boolean, firstFocus: Boolean): Boolean {
        val result = when {
            hasLostFocus -> onLeaveField
            firstFocus -> onFirstEdit
            else -> onSubsequentEdit
        }
        return onCondition.invoke(result)
    }
}