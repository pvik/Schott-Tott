
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.21"))
    }
}
plugins {
    kotlin("jvm") version "1.3.21"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:1.6.24")
    implementation("org.slf4j:slf4j-log4j12:1.7.5")
}
