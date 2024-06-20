package me.lucky.silence.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.lucky.silence.Preferences
import me.lucky.silence.R
import me.lucky.silence.ui.common.Screen


@Composable
fun RegexScreen(prefs: Preferences, onBackPressed: () -> Boolean) {
    val regexErrorHint = stringResource(R.string.regex_pattern_error)
    var regexError by remember { mutableStateOf(false) }
    val regexDescriptionBlock = stringResource(R.string.regex_pattern_helper_text_block)
    var regexSupportingTextBlock by remember { mutableStateOf(regexDescriptionBlock) }
    var regexTextBlock by remember { mutableStateOf(prefs.regexPatternBlock ?: "") }
    val regexDescriptionAllow = stringResource(R.string.regex_pattern_helper_text_allow)
    var regexSupportingTextAllow by remember { mutableStateOf(regexDescriptionAllow) }
    var regexTextAllow by remember { mutableStateOf(prefs.regexPatternAllow ?: "") }

    Screen(title = R.string.regex_main, onBackPressed = onBackPressed, content = {
        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
            OutlinedTextField(
                label = { Text(stringResource(R.string.regex_pattern_hint)) },
                supportingText = { Text(regexSupportingTextBlock) },
                singleLine = false,
                value = regexTextBlock,
                isError = regexError,
                onValueChange = { newValue ->
                    regexError = !isValidRegex(newValue)
                    if (regexError) {
                        regexSupportingTextBlock = regexErrorHint
                    } else {
                        regexSupportingTextBlock = regexDescriptionBlock
                        prefs.regexPatternBlock = newValue
                    }
                    regexTextBlock = newValue
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    autoCorrect = false,
                    capitalization = KeyboardCapitalization.None
                )
            )
        }
        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
            OutlinedTextField(
                label = { Text(stringResource(R.string.regex_pattern_hint)) },
                supportingText = { Text(regexSupportingTextAllow) },
                singleLine = false,
                value = regexTextAllow,
                isError = regexError,
                onValueChange = { newValue ->
                    regexError = !isValidRegex(newValue)
                    if (regexError) {
                        regexSupportingTextAllow = regexErrorHint
                    } else {
                        regexSupportingTextAllow = regexDescriptionAllow
                        prefs.regexPatternAllow = newValue
                    }
                    regexTextAllow = newValue
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    autoCorrect = false,
                    capitalization = KeyboardCapitalization.None
                )
            )
        }
    })
}

private fun isValidRegex(pattern: String): Boolean {
    try {
        pattern.toRegex(RegexOption.MULTILINE)
    } catch (exc: java.util.regex.PatternSyntaxException) {
        return false
    }
    return true
}

@Preview
@Composable
fun RegexScreenPreview() {
    RegexScreen(Preferences(LocalContext.current)) { true }
}