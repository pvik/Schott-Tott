buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.21"))
        implementation(kotlin("stdlib-jdk8"))
    }
}
plugins {
    kotlin("jvm")
}

sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir("src")
}