
job('111111') {
     
	parameters {
		choiceParam('BRANCH_NAME', ['ilahutka', 'master'], 'select branch name')
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
	steps {
		shell('echo $BRANCH_NAME')
    }
}
