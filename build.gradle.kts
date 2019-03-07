
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
    implementation(kotlin("stdlib"))
}
