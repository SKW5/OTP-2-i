pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/SKW5/OTP-2-i'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install' // sh for linux and ios
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Docker image') {
            steps {
                sh 'docker build -t temperature-converter:${BUILD_NUMBER} .'
                sh 'docker run --rm --entrypoint java temperature-converter:${BUILD_NUMBER} -version'
            }
        }
    }
}