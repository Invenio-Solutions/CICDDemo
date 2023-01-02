

pipeline {
	
  agent any
  options {
  timestamps ()
  }
  
  environment {
  	PATH = "/opt/maven/bin:$PATH"
  	author = sh(returnStdout: true, script: "git log -1 --pretty=format:'%an'").trim()
  	ANYPOINT_CREDS = credentials('ANYPOINT_CREDENTIALS')
	workspace = "/home/ec2-user/.jenkins/workspace/employee-dev-api"
	invenioTeams = "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359"
	
	
  }
  
  stages {
    stage('Build') {
      steps {
            sh 'mvn -B -U -e -V clean -DskipTests package'
      }
    }
    
    stage('Sonarqube Analysis') {
     steps {
    	 withSonarQubeEnv('sonarqube 9.4') { 
         sh "mvn sonar:sonar"
    		}
    	}
    }
    
    stage('Unit Testing') {
      steps {
           script {
               sh 'mvn test'
            }
         }
     }

    stage('Deployment') {
      environment {
      	CLIENT_ID = credentials('DEV_CLIENT_ID')
      	CLIENT_SECRET = credentials('DEV_CLIENT_SECRET')
      }
      steps {
            sh 'mvn -U -V -e -B -DskipTests -Pdev deploy -DmuleDeploy -Danypoint.username="$ANYPOINT_CREDS_USR" -Danypoint.password="$ANYPOINT_CREDS_PSW" -Danypoint.platform.client_id="$CLIENT_ID" -Danypoint.platform.client_secret="$CLIENT_SECRET"'
      }
    }
    
    stage('DAST') {
      steps {
    	 sshagent(['zap-ssh']) {
           sh 'ssh -o StrictHostKeyChecking=no ec2-user@44.210.125.255  "sudo docker run -t owasp/zap2docker-stable zap-baseline.py -t http://3.95.213.97:8081/v1/fetch-employees" || true'
           sh 'ssh -o StrictHostKeyChecking=no ec2-user@44.210.125.255  "sudo docker run -t owasp/zap2docker-stable zap-baseline.py -t http://3.95.213.97:8081/v1/add-employees" || true'
              		
            }
        }
    }
    	
    stage ('Integration-Testing') {
    	steps {
    		script {
    			build 'dev-citrus-test'
    		}
    	}
    }
	
	stage ('Peformance Testing') {
	 steps {
		 dir("/opt/jmeter/bin"){
		 sh 'pwd'
		 sh './jmeter.sh -n -t "/home/ec2-user/.jenkins/workspace/employee-dev-api/TestPlan/employee-dev-api.jmx" -l "/opt/jmeter/bin/dev-result.csv" -R 168.0.54.113'
		 sh 'pwd'
		 }
		 dir("${env.WORKSPACE}"){
		 sh 'pwd'
		 }
	 }
	 post {
	 success {
	   perfReport ( 
	   	sourceDataFiles: '/opt/jmeter/bin/result.csv',
	    compareBuildPrevious: true,
	    excludeResponseTime: true,
	    showTrendGraphs: true
	    )
	  }
	 }
	}
 }
	
  post {
    
    success {
    script {
               sh 'gh pr create --assignee "sarga-invenio" --base sit --head dev --title "Update the code from dev branch for the new commits" --body "To retrieve latest code from dev branch to sit branch"'
            }
        
  }
   failure {
  
    jiraNewIssue (
					   
	issue: ["fields": [
				"project": [
					"id": "10063"
				],
				"summary": "$currentBuild.projectName - Build # $currentBuild.number $currentBuild.result",
				"issuetype": [
					"id": "10002"
				],
				"assignee": [
					"id": "637cc070213a315af346a1c0"
				]]],
	failOnError: true,
	site: "inveniolsi-jira"
  )

    
    }
  always {
  
  		script {
  			def millis = ("${currentBuild.startTimeInMillis}").toLong()
            time = new Date(millis).format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            echo "${time}"
                    
  		}
	emailext attachLog: false, body: '''$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS:

	Branch: ${GIT_BRANCH}
	Commit-ID: ${GIT_REVISION}
	Build log: ${BUILD_LOG}
	Last Changes: ${CHANGES_SINCE_LAST_SUCCESS}

	Check console output at $BUILD_URL to view the results.''', compressLog: true, postsendScript: '${DEFAULT_POSTSEND_SCRIPT}', presendScript: '${DEFAULT_PRESEND_SCRIPT}', recipientProviders: [buildUser(), contributor(), culprits(), previous(), developers(), requestor(), upstreamDevelopers()], replyTo: 'sarga.satheesh@inveniolsi.com', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'sarga.satheesh@inveniolsi.com'

    
    office365ConnectorSend (
    status: "${currentBuild.result} - ${currentBuild.fullDisplayName}",
    webhookUrl: "${invenioTeams}",
    /*webhookUrl: "https://invenio12.webhook.office.com/webhookb2/112429ff-03d1-4ee5-8491-b305a1a36343@e83d39e6-f19d-49ca-8261-07ae0659a01c/IncomingWebhook/986dd2893e3246b9ae566d36ffcb2ee1/65055d5d-72d7-4ac0-88d8-1262dbc14359",*/
    color: '00ff00',
    message: "Deployment ${currentBuild.result}: ${JOB_NAME} - ${BUILD_DISPLAY_NAME}<br>Pipeline duration: ${currentBuild.durationString}",
    factDefinitions: [[name: "Developer", template: "${author}"],
                      [name: "Branch", template: "${GIT_BRANCH}"],
                      [name: "StartTime", template: "${time}"],
                      [name: "View", template: "${currentBuild.absoluteUrl}"]]
                      
    )
    
    dir("${env.WORKSPACE}@tmp") {
      deleteDir()
    }
    dir("${env.WORKSPACE}@script") {
      deleteDir()
    }
    dir("${env.WORKSPACE}@script@tmp") {
      deleteDir()
    }
    
    }

    }
    
}
