def formattedDate = ""
@Library("shared") _
pipeline {
    agent { label "vinod" }
    stages { 
        stage ("Initialization...") {
            steps {
              formattedDate = new Date().format("yyyy-MM-dd_HH-mm-ss")
            }
        }
        
        stage("Code Checkout") {
           steps {
               script {
                   clone("https://github.com/sidhanshumahajan/spring-boot-with-docker", "main")
               }
           }
        }
        post {
            success{
               echo "====++++Code Checkout successful++++===="
            }
            failure{
               echo "====++++Code Checkout failed++++===="
            }
        }
        
        stage("Build Code") {
           steps {
               echo "Building the code...."
               sh 'mvn -B -DskipTests clean package' 
               script {
                 docker_build("80281", "my-spring-app", formattedDate)
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
                   docker_push("80281", "my-spring-app", formattedDate)  
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
               script {
                   docker_run("80281", "my-spring-app", formattedDate)
               }
           }
        }
    }
}
