package com.slotik.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.slotik.mobile.presentation.SlotikApp
import com.slotik.mobile.presentation.SlotikViewModel
import com.slotik.mobile.presentation.theme.SlotikTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlotikTheme {
                val context = LocalContext.current.applicationContext
                val viewModel: SlotikViewModel = viewModel(
                    factory = SlotikViewModel.factory(context),
                )
                val state by viewModel.state.collectAsStateWithLifecycle()

                SlotikApp(
                    state = state,
                    viewModel = viewModel,
                )
            }
        }
    }
}
