
job('111111') {
     
	parameters {
		choiceParam('BRANCH_NAME', ['ilahutka', 'master'], 'select branch name')
    			}
	parameters {  
        activeChoiceReactiveParam('JOBS') {
			choiceType('CHECKBOX')
				groovyScript {
					script('["job1", "job2"]')
							}
			}
		}
  
steps {
	shell('echo $BRANCH_NAME; echo $JOBS')
	conditionalSteps {
		condition {
			shell('echo $JOBS | grep -q "job1"')
					}
		runner('Fail')
		steps {
			downstreamParameterized {
				trigger('job1') {
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
          
job('job1') {
     
       parameters {
			stringParam('BRANCH_NAME')
       }

        parameters {
			activeChoiceParam('BRANCH_NAME') {
                description('Allows user choose from multiple choices')
                choiceType('SINGLE_SELECT')
           				groovyScript {
                         	script('''
def gitURL = "https://github.com/MNT-Lab/mntlab-dsl"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()

def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '')
}

branches.each {println it}''')    
							}
				}
        }

  scm {
            git {
                remote {
                    url('https://github.com/MNT-Lab/mntlab-dsl.git')
                }
                branch('*/$BRANCH_NAME')
            }
        }
        steps {
            shell ('echo $BRANCH_NAME' )
        }
    }
