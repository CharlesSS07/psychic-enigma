plugins {
    id("java")
}

group = "com.psychicenigma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("../../openai-java/api/build/libs/api-0.18.2.jar"))
    implementation(files("../../openai-java/client/build/libs/client-0.18.2.jar"))
    implementation(files("../../openai-java/service/build/libs/service-0.18.2.jar"))
//    implementation("com.theokanning.openai-gpt3-java:service:0.18.2")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}