package com.codingwitharul.bookmyslot.di

import androidx.credentials.CredentialManager
import com.codingwitharul.bookmyslot.common.DriverFactory
import com.codingwitharul.bookmyslot.common.HardwareInfo
import com.codingwitharul.bookmyslot.common.auth.GoogleAuthProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<HardwareInfo> { HardwareInfo(androidContext()) }
    single<DriverFactory> { DriverFactory(androidContext()) } // Provide Android Context
    factory<CredentialManager> { CredentialManager.create(androidContext()) } bind CredentialManager::class
    factoryOf(::GoogleAuthProvider) bind GoogleAuthProvider::class
}