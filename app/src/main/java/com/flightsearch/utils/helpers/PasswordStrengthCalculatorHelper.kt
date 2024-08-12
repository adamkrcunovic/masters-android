package com.flightsearch.utils.helpers

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.flightsearch.constants.ApplicationConstants.*;
import java.util.regex.Matcher
import java.util.regex.Pattern

class PasswordStrengthCalculatorHelper: TextWatcher {

    val default_value = PASSWORD_STRENGTH_NEUTRAL;

    var numberCase: MutableLiveData<Int> = MutableLiveData(default_value)
    var lowerCase: MutableLiveData<Int> = MutableLiveData(default_value)
    var upperCase: MutableLiveData<Int> = MutableLiveData(default_value)
    var digit: MutableLiveData<Int> = MutableLiveData(default_value)
    var specialChar: MutableLiveData<Int> = MutableLiveData(default_value)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}
    override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
        if(char != null){
            numberCase.value = if (char.calculateNumberOfChar()) { PASSWORD_STRENGTH_CORRECT } else { PASSWORD_STRENGTH_INCORRECT }
            lowerCase.value = if (char.hasLowerCase()) { PASSWORD_STRENGTH_CORRECT } else { PASSWORD_STRENGTH_INCORRECT }
            upperCase.value = if (char.hasUpperCase()) { PASSWORD_STRENGTH_CORRECT } else { PASSWORD_STRENGTH_INCORRECT }
            digit.value = if (char.hasDigit()) { PASSWORD_STRENGTH_CORRECT } else { PASSWORD_STRENGTH_INCORRECT }
            specialChar.value = if (char.hasSpecialChar()) { PASSWORD_STRENGTH_CORRECT } else { PASSWORD_STRENGTH_INCORRECT }
        }
    }

    private fun CharSequence.calculateNumberOfChar(): Boolean {
        return this.length in PASSWORD_MIN_REQUIREMENTS..PASSWORD_MAX_REQUIREMENTS;
    }

    private fun CharSequence.hasLowerCase(): Boolean{
        val pattern: Pattern = Pattern.compile("[a-z]")
        val hasLowerCase: Matcher = pattern.matcher(this)
        return hasLowerCase.find()
    }

    private fun CharSequence.hasUpperCase(): Boolean{
        val pattern: Pattern = Pattern.compile("[A-Z]")
        val hasUpperCase: Matcher = pattern.matcher(this)
        return hasUpperCase.find()
    }

    private fun CharSequence.hasDigit(): Boolean{
        val pattern: Pattern = Pattern.compile("[0-9]")
        val hasDigit: Matcher = pattern.matcher(this)
        return hasDigit.find()
    }

    private fun CharSequence.hasSpecialChar(): Boolean{
        val specChars = "`~!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?*"
        for (character in this) {
            if (specChars.contains(character)) return true
        }
        return false
    }

    fun meetsAllRequirements(): Boolean{
        return numberCase.value == PASSWORD_STRENGTH_CORRECT &&
                lowerCase.value == PASSWORD_STRENGTH_CORRECT &&
                upperCase.value == PASSWORD_STRENGTH_CORRECT &&
                digit.value == PASSWORD_STRENGTH_CORRECT &&
                specialChar.value == PASSWORD_STRENGTH_CORRECT
    }
}