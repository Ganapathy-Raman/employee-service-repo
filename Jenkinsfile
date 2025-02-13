pipeline {

    agent any
 
    stages {

        stage('Checkout') {

            steps {

                git branch: 'master', url: 'https://github.com/Ganapathy-Raman/employee-service-repo.git'

            }

        }
 
        stage('Build') {

            steps {

                bat 'mvn clean install'

            }

        }
 
        stage('Test') {

            steps {

                bat 'mvn test'

            }

        }

        stage('Cleanup') {
            steps {
                bat 'docker stop admin_service'
                bat 'docker rm admin_service'
                bat 'docker rmi tapz_admin_img'
            }
        }
 
        stage('Docker Build') {

            steps {

                script {

                    docker.build('tapz_employee_img', '-f Dockerfile .')

                }

            }

        }
 
        stage('Run Docker') {

            steps {

                bat 'docker run -d -p 5372:5372 --name employee_service tapz_employee_img'

            }

        }



    }

}
 
