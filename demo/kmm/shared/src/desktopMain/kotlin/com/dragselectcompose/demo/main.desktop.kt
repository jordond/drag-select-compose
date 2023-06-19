package com.dragselectcompose.demo

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.dragselectcompose.demo.App

@Composable
fun MainView() = App()

@Preview
@Composable
fun AppPreview() {
    App()
}