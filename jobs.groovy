Name = "ykaratseyeu"
def folderName = "EPBYMINW1374"
def childJobs = 4


job('MNTLAB-'+branchName+'-main-t-build-job') {
    parameters {
        choiceParam('BRANCH_NAME', ['ykaratseyeu', 'master'], 'select branch name')     
          activeChoiceReactiveParam('JOBS') {
            choiceType('CHECKBOX')
            groovyScript {
                script('["MNTLAB-ykaratseyeu-child1-build-job1", "MNTLAB-ykaratseyeu-child2-build-job", "MNTLAB-ykaratseyeu-child3-build-job", "MNTLAB-ykaratseyeu-child4-build-job" ]')
                
            }
            
        }
     
      }
  steps {
        conditionalSteps {
            condition {
                shell('echo $JOBS | grep -q "child1"')
            }
            runner('Fail')
            steps {
                downstreamParameterized {
                    trigger("MNTLAB-ykaratseyeu-child1-build-job1") {
                        block {
                            buildStepFailure('FAILURE')
                            failure('FAILURE')
                            unstable('UNSTABLE')
                        }
                        parameters {
                            predefinedProp('BRANCH_NAME', '${BRANCH_NAME}')
                        }
}
            }
        }
    }
         
}
}
  
  job('MNTLAB-ykaratseyeu-child1-build-job1') {
  
   parameters {
            stringParam('BRANCH_NAME')
}
    
    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''def remote_url="https://github.com/MNT-Lab/mntlab-dsl.git"
def command = [ "/bin/bash", "-c", "git ls-remote --heads " + remote_url + " | awk '{print \\$2}' | sort  -V | sed 's@refs/heads/@@'" ]
def process = command.execute()

def result = process.in.text.tokenize('\\n')

def branches = []
for(i in result) {
        branches.add(i)
}
return branches''')
            }
        }
}
  
   
  }
