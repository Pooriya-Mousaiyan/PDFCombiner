import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("org.apache.pdfbox:pdfbox:2.0.33")
            implementation("com.formdev:flatlaf:3.2")
            implementation("com.itextpdf:itext7-core:8.0.4")
        }
    }
}

compose.desktop {
    application {
        mainClass = "mousaiyan.pooriya.pdfcombiner.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.AppImage)
            packageName = "PDf Combiner"
            packageVersion = "1.0.0"
            windows {
                iconFile.set(file("src/commonMain/composeResources/drawable/pdf_combine_windows.ico"))
            }
            linux {
                iconFile.set(file("src/commonMain/composeResources/drawable/pdf_combine_linux.png"))
            }
            macOS {
                iconFile.set(file("src/commonMain/composeResources/drawable/pdf_combine_macos.icns"))
            }
        }
    }
}
