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
            // Define container and image names
            def containerName = 'employee_service'
            def imageName = 'tapz_employee_img'

            // Check if the Docker container exists
            def containerExists = bat(script: "docker ps -aq -f name=${containerName}", returnStdout: true).trim()
            
            // Stop and remove container if it exists
            if (containerExists) {
                echo "Stopping and removing container: ${containerName}..."
                bat "docker stop ${containerName}"
                bat "docker rm ${containerName}"
                echo "Successfully stopped and removed container: ${containerName}."
            } else {
                echo "Container '${containerName}' does not exist. Skipping stop and remove."
            }

            // Check if the Docker image exists
            def imageExists = bat(script: "docker images -q ${imageName}", returnStdout: true).trim()

            // Remove image if it exists
            if (imageExists) {
                echo "Removing image: ${imageName}..."
                bat "docker rmi ${imageName}"
                echo "Successfully removed image: ${imageName}."
            } else {
                echo "Image '${imageName}' does not exist. Skipping removal."
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
 
