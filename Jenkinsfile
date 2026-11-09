pipeline {
    agent any
    environment { IMAGE = "${DOCKERHUB_USERNAME}/temperature-converter" }
    stages {
        stage('Build and test') {
            steps { sh 'mvn -B clean verify' }
            post { always { junit '**/target/surefire-reports/*.xml'; publishHTML(target: [allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/site/jacoco', reportFiles: 'index.html', reportName: 'JaCoCo']) } }
        }
        stage('Build image') { steps { sh 'docker build -t "$IMAGE:$BUILD_NUMBER" -t "$IMAGE:latest" .' } }
    }
}
