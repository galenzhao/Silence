package me.lucky.silence.fragment

import android.Manifest
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment

import me.lucky.silence.Preferences
import me.lucky.silence.databinding.FragmentExtraBinding

class ExtraFragment : Fragment() {
    private lateinit var binding: FragmentExtraBinding
    private lateinit var ctx: Context
    private lateinit var prefs: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExtraBinding.inflate(inflater, container, false)
        init()
        setup()
        return binding.root
    }

    private fun init() {
        ctx = this.requireContext()
        prefs = Preferences(ctx)
        binding.apply {
            contacts.isChecked = prefs.isContactsChecked
            shortNumbers.isChecked = prefs.isShortNumbersChecked
            unknownNumbers.isChecked = prefs.isUnknownNumbersChecked
            plusNumbers.isChecked = prefs.isBlockPlusNumbers
            stir.isEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
            stir.isChecked = prefs.isStirChecked
            txtLayoutRegex.editText?.setText(prefs.regexPattern)
        }
    }

    private fun setup() = binding.apply {
        contacts.setOnCheckedChangeListener { _, isChecked ->
            prefs.isContactsChecked = isChecked
            if (!isChecked) requestContactsPermissions()
        }
        shortNumbers.setOnCheckedChangeListener { _, isChecked ->
            prefs.isShortNumbersChecked = isChecked
        }
        unknownNumbers.setOnCheckedChangeListener { _, isChecked ->
            prefs.isUnknownNumbersChecked = isChecked
        }
        plusNumbers.setOnCheckedChangeListener { _, isChecked ->
            prefs.isBlockPlusNumbers = isChecked
        }
        stir.setOnCheckedChangeListener { _, isChecked ->
            prefs.isStirChecked = isChecked
        }

        txtLayoutRegex.editText?.doAfterTextChanged {
            val txt = it?.toString()
            if(isBlockRegexValid(txt)) {
                prefs.regexPattern = txt
                txtLayoutRegex.error = ""
            }
            else {
                txtLayoutRegex.error = "Invalid regex!"
            }

            return@doAfterTextChanged
        }
        // on below line adding click listener for button.
        hideBtn.setOnClickListener {

            // on below line getting current view.
            val view: View? = activity?.currentFocus

            // on below line checking if view is not null.
            if (view != null) {
                // on below line we are creating a variable
                // for input manager and initializing it.
                val inputMethodManager =
                    ctx.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                // on below line hiding our keyboard.
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)

                // displaying toast message on below line.
                Toast.makeText(ctx, "Key board hidden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isBlockRegexValid(pattern: String?): Boolean {
        if (pattern.isNullOrBlank())  return true

        try {
            pattern.toRegex()
        }
        catch(ex: java.util.regex.PatternSyntaxException) {
            //Toast.makeText(this.ctx, ex.message, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private val registerForContactsPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private fun requestContactsPermissions() =
        registerForContactsPermissions.launch(Manifest.permission.READ_CONTACTS)


}