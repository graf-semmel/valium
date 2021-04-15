package graf.semmel.valium.form

import android.util.Log
import android.view.View

class SubmitButton(val id: Int) {

    var buttonView: View? = null

    fun enable(isEnabled: Boolean) {
        Log.d("DEBUG", "SubmitButton enable: $isEnabled")
        buttonView?.isEnabled = isEnabled
    }

    fun bindView(view: View) {
        Log.d("DEBUG", "SubmitButton resolveView")
        buttonView = view
    }
}
