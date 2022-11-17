pipeline {
    agent{
        dockerfile true
    }
    
    stages{
        stage('Build Jar'){
            steps{
            	sh 'cd src/main/native && make linux'
                sh 'mvn compile package'
                
                archiveArtifacts artifacts: 'target/JMD-*.jar'
            }
        }

        stage('Build App'){
            steps{
                sh './makeapp.sh'
                archiveArtifacts artifacts: 'JMD.zip'
            }
        }
    }
}
