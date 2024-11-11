# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ---- Configurações Básicas ----
-keepattributes *Annotation*

# Otimização desativada para evitar problemas (opcional em desenvolvimento)
-dontoptimize

# Preserva classes necessárias para anotações do Android
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

-keep class androidx.** { *; }
-dontwarn androidx.**

# ---- Regras específicas para bibliotecas ----

#######################
# OKHTTP
#######################

-dontwarn javax.annotation.**

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*

#######################
# RETROFIT
#######################

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#######################
# KOIN
#######################

# Protege APIs experimentais
-keep @org.koin.core.annotation.KoinExperimentalAPI class * { *; }

# Protege APIs internas, se necessário
-keep @org.koin.core.annotation.KoinInternalApi class * { *; }

# Mantém as classes do Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**

#######################
# COIL (Carregamento de Imagens)
#######################

-keep class coil.** { *; }
-dontwarn coil.**

#######################
# ROOM (Banco de Dados)
#######################

-keep class androidx.room.Entity { *; }
-keep class androidx.room.Dao { *; }
-keep interface androidx.room.* { *; }
-dontwarn androidx.room.**

# ---- Outras Configurações Gerais ----

# Remove logs do APK final
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
    public static *** i(...);
    public static *** v(...);
}

# Evita remoção de classes do projeto principal
-keep class com.picpay.desafio.android.** { *; }
-dontwarn com.picpay.desafio.android.**
