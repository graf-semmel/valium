package graf.semmel.valium.form

import android.util.Log
import android.view.View

class SubmitButton(val id: Int) {

    var disableOnError: Boolean = true
    private var buttonView: View? = null

    fun enable(isEnabled: Boolean) {
        Log.d("DEBUG", "SubmitButton enable: $isEnabled")
        if (disableOnError) buttonView?.isEnabled = isEnabled
    }

    fun setupView(view: View) {
        Log.d("DEBUG", "SubmitButton resolveView")
        buttonView = view
        view.isEnabled = true
    }
}
