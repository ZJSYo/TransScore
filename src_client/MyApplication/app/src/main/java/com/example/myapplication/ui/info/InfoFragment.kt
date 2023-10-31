package com.example.myapplication.ui.info

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.StaticApplication
import com.example.myapplication.databinding.FragmentInfoBinding


class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val infoViewModel =
            ViewModelProvider(this).get(InfoViewModel::class.java)

        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.infoText
        infoViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val text_feedback: TextView = binding.feedbackText
        val text = "如有问题或更好意见，点此反馈"
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // 在这里处理超链接被点击后的操作，比如打开网页
                val uri = Uri.parse("https://github.com/ZJSYo/TransScore/issues")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // 设置超链接文本的样式，比如颜色
                ds.isUnderlineText = true // 显示下划线
                ds.color =
                    ContextCompat.getColor(StaticApplication.context,R.color.holo_blue_dark) // 设置文本颜色
            }
        }

// 将clickableSpan应用到文本的一部分

// 将clickableSpan应用到文本的一部分
        spannableString.setSpan(clickableSpan, 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

// 设置TextView的文本和点击事件

// 设置TextView的文本和点击事件
        text_feedback.text = spannableString
        text_feedback.movementMethod = LinkMovementMethod.getInstance()
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}