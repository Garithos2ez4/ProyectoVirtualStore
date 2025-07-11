plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleService)
}

android {
    namespace = "com.example.sistemadefacturacion_pruni"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sistemadefacturacion_pruni"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.lottie)/*Animaciones*/
    implementation(libs.firebaseAuth)/*Autentificacion*/
    implementation(libs.firebaseDatabase)/*Base de datos*/
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.imagePicker)/*Recortar una imagen del celular*/
    implementation(libs.glide)/*Leer Imagenes*/
    implementation(libs.storage)/*subir multimedia*/
    implementation(libs.authGoogle)/*Iniciar sesión con google*/
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation(libs.ccp)/*Seleecionar codigo de telefono por país*/
    implementation("com.itextpdf:itext7-core:7.2.5")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}