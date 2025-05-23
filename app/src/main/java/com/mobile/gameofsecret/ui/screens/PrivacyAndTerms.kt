package com.mobile.gameofsecret.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.PRIVACY_URL
import com.mobile.gameofsecret.data.TERMS_URL
import com.mobile.gameofsecret.ui.components.BackHeader
import com.mobile.gameofsecret.ui.components.WebView
import com.mobile.gameofsecret.ui.theme.background

@Composable
fun PrivacyScreen(navController: NavController) {
    Scaffold( modifier = Modifier
        .fillMaxSize()
        .background(background)
        .padding(WindowInsets.systemBars.asPaddingValues()),topBar = {
        BackHeader(headerText = stringResource(R.string.privacy), onBackClicked = {
            navController.popBackStack()
        })
    }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(it)
            ) {
            WebView(PRIVACY_URL)
        }
    }
}

@Composable
fun TermsScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            BackHeader(headerText = stringResource(R.string.terms), onBackClicked = {
                navController.popBackStack()
            })
        }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(it)) {
            WebView(TERMS_URL)
        }
    }
}