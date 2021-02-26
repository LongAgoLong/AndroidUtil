package com.leo.commonutil.app

import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.widget.EditText
import com.leo.commonutil.app.EditUtil
import android.text.Spanned
import android.view.KeyEvent
import java.lang.ref.WeakReference
import java.util.regex.Pattern

object EditUtil {
    private val mUIHandler = Handler(Looper.getMainLooper())

    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    fun setNotInputSpace(editText: EditText) {
        editText.filters = arrayOf(spaceFilter)
    }

    /**
     * 空格换行输入过滤器
     *
     * @return
     */
    val spaceFilter: InputFilter
        get() = InputFilter { source, start, end, dest, dstart, dend ->
            if (source == " " || source.toString().contentEquals("\n")) {
                ""
            } else {
                null
            }
        }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    fun setNotInputSpecialChar(editText: EditText) {
        editText.filters = arrayOf(specialCharFilter)
    }// String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * 特殊字符输入过滤器
     *
     * @return
     */
    val specialCharFilter: InputFilter
        get() = InputFilter { source, start, end, dest, dstart, dend -> // String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            val speChat = "[\\p{P}\\s]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())
            if (matcher.find()) {
                ""
            } else if (source == " " || source.toString().contentEquals("\n")) {
                ""
            } else {
                null
            }
        }

    /**
     * 逐个删除EditText的输入元素
     *
     * @param editText
     */
    fun deleteInput(editText: EditText) {
        val keyCode = KeyEvent.KEYCODE_DEL
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, keyCode)
        editText.onKeyDown(keyCode, keyEventDown)
        editText.onKeyUp(keyCode, keyEventUp)
    }

    fun startDeleteInputTask(editText: EditText): DeleteInputTask {
        val deleteInputTask = DeleteInputTask(WeakReference(editText), mUIHandler)
        deleteInputTask.run()
        return deleteInputTask
    }

    class DeleteInputTask constructor(private val editTextWeak: WeakReference<EditText>,
                                      private val mHandler: Handler) : Runnable {
        private var isStop = false
        override fun run() {
            val editText = editTextWeak.get() ?: return
            deleteInput(editText)
            if (isStop) {
                return
            }
            mHandler.postDelayed(this, 300)
        }

        fun stop() {
            isStop = true
            mHandler.removeCallbacks(this)
        }
    }
}