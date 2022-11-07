pipeline {
    agent{
        dockerfile true
    }
    
    stages{
        stage('Build'){
            steps{
            	sh 'cd src/main/native && make linux'
                sh 'mvn compile package'
                
                archiveArtifacts artifacts: 'target/JMD-*.jar'
            }
        }
    }
}
