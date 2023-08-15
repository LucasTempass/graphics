plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        url = uri("https://jogamp.org/deployment/maven")
    }
}

dependencies {
    implementation("org.jogamp.gluegen:gluegen-rt-main:2.4.0")
    implementation("org.jogamp.jogl:jogl-all-main:2.4.0")
}