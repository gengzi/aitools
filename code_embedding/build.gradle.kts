plugins {
    id("java")
}

group = "com.gengzi"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.github.javaparser:javaparser-core:3.25.2")
    implementation("com.github.jelmerk:faiss4j:0.5.0")
}

tasks.test {
    useJUnitPlatform()
}