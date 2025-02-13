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
        script {
            // Check if the Docker container exists
            def containerExists = bat(script: 'docker ps -aq -f name=employee_service', returnStdout: true).trim()
            
            // Check if the Docker image exists
            def imageExists = bat(script: 'docker images -q tapz_employee_img', returnStdout: true).trim()

            // If the container exists, stop and remove it
            if (containerExists) {
                bat 'docker stop employee_service'
                bat 'docker rm employee_service'
            } else {
                echo "Container 'employee_service' does not exist, skipping stop and remove."
            }

            // If the image exists, remove it
            if (imageExists) {
                bat 'docker rmi tapz_employee_img'
            } else {
                echo "Image 'tapz_employee_img' does not exist, skipping removal."
            }
        }
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
 
