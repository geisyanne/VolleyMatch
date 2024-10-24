package co.geisyanne.volleymatch.presentation.common.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import co.geisyanne.volleymatch.R

class LoadingButton : FrameLayout {

    private lateinit var button: Button
    private lateinit var progress: ProgressBar
    private var text: String? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.component_button_loading, this)

        button = findViewById(R.id.button)
        progress = findViewById(R.id.progress)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
        text = typeArray.getString(R.styleable.LoadingButton_text) // texto inicial

        button.text = text
        button.isEnabled = false

        typeArray.recycle()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        button.isEnabled = enabled
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }


    fun setText(text: String) {
        this.text = text
        button.text = text
    }

    fun showProgress(enabled: Boolean) {
        if (enabled) {
            button.text = ""
            button.isEnabled = false
            progress.visibility = View.VISIBLE
        } else {
            button.text = text
            button.isEnabled = true
            progress.visibility = View.GONE
        }
    }
}
