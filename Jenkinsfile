@Library("shared") _
pipeline {
    agent { label "vinod" }
    stages { 
        stage("Code Checkout") {
           steps {
               script {
                   clone("https://github.com/sidhanshumahajan/spring-boot-with-docker", "main")
               }
           }
        }
        
        stage("Build Code") {
           steps {
               echo "Building the code...."
               sh 'mvn -B -DskipTests clean package' 
               script {
                 docker_build("my-spring-app", "latest")
               }
           } 
           post {
               success{
                   echo "====++++Build successful++++===="
               }
               failure{
                   echo "====++++Build failed++++===="
               }
           }
        }
        
        stage("Docker: Push to DockerHub") {
            steps {
                 echo "====++++Pushing DockerImage To DockerHub++++===="
                 script {
                   docker_push("8281", "my-spring-app", "latest")  
                 } 
            }
        }
        
        stage("Test") {
           steps {
            sh 'mvn test'
            echo "Running the Test cases...."
           }
           post {
               always{
                  junit "target/surefire-reports/*.xml"
               } 
               success{
                   echo "====++++Test Cases Executed successful++++===="
               }
               failure{
                   echo "====++++Test Cases Execution failed++++===="
               }
           }
        }
        stage("Deploy Code") {
           steps {
               echo "Deploying Code to docker..."
               sh "docker run -d -p 8081:8080  my-spring-app:latest"
           }
        }
    }
}
