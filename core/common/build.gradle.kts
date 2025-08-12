plugins {
    alias(libs.plugins.my.android.library)
    alias(libs.plugins.my.android.hilt)
}

android {
    namespace = "ir.miare.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}