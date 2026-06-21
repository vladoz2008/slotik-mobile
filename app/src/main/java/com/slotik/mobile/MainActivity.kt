package com.slotik.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.slotik.mobile.presentation.SlotikApp
import com.slotik.mobile.presentation.theme.SlotikTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlotikTheme {
                SlotikApp()
            }
        }
    }
}
