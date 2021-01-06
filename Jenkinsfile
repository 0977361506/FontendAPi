  pipeline {
    agent any
    stages
    {
        stage('kiem tra')
        {
            steps{
            sh 'mvn -version '
            }
        }

        stage('build maven')
        {
        steps{
            sh('mvn clean install -Plinux')
             }
        }


        stage('build docker files ')
                {
                steps{
                    sh('docker build -f Dockerfile -t eln-frontend-api .')
                     }
                }
          stage('remove docker')
                      {
                          steps
                              {
                               sh('docker rm  -f eln-frontend-api')
                              }
                      }
         stage('run in docker')
         {
         steps{
            sh('docker run --name "eln-frontend-api" --volume /media/data/:/tmp/data-data1/media/data/ -p 8082:8082 eln-frontend-api:latest')
         }
         }

    }
    post {
            failure {
                mail to: 'vinaghost98@gmail.com', subject: 'Build failed', body: 'Please fix!'
            }
        }

}
  pipeline {
    agent any
    stages
    {
        stage('kiem tra')
        {
            steps{
            sh 'mvn -version '
            }
        }

        stage('build maven')
        {
        steps{
            sh('mvn clean install -Plinux')
             }
        }


        stage('build docker files ')
                {
                steps{
                    sh('docker build -f Dockerfile -t eln-frontend-api .')
                     }
                }
          stage('remove docker')
                      {
                          steps
                              {
                               sh('docker rm  -f eln-frontend-api')
                              }
                      }
         stage('run in docker')
         {
         steps{
            sh('docker run --name "eln-frontend-api" --volume /media/data/:/tmp/data-data1/media/data/ -p 8082:8082 eln-frontend-api:latest')
         }
         }

    }
    post {
            failure {
                mail to: 'vinaghost98@gmail.com', subject: 'Build failed', body: 'Please fix!'
            }
        }

}
