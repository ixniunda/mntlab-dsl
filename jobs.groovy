job("EPBYMINW6406/MNTLAB-ilahutka-main-build-job") {
     
	parameters {
		choiceParam('BRANCH_NAME', ['ilahutka', 'master'], 'select branch name')
    			
    }
	parameters {  
        activeChoiceReactiveParam('JOBS') {
			choiceType('CHECKBOX')
				groovyScript {
					script('["MNTLAB-ilahutka-child1-build-job", "MNTLAB-ilahutka-child2-build-job", "MNTLAB-ilahutka-child3-build-job", "MNTLAB-ilahutka-child4-build-job"]')
							}
				}
			}
  
steps {
	conditionalSteps {
		condition {
			shell('echo $JOBS | grep -q "MNTLAB-ilahutka-child1-build-job"')
					}
		runner('Fail')
		steps {
			downstreamParameterized {
				trigger('EPBYMINW6406/MNTLAB-ilahutka-child1-build-job') {
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
	conditionalSteps {
		condition {
			shell('echo $JOBS | grep -q "MNTLAB-ilahutka-child2-build-job"')
					}
		runner('Fail')
		steps {
			downstreamParameterized {
				trigger('EPBYMINW6406/MNTLAB-ilahutka-child2-build-job') {
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
	conditionalSteps {
		condition {
			shell('echo $JOBS | grep -q "MNTLAB-ilahutka-child3-build-job"')
					}
		runner('Fail')
		steps {
			downstreamParameterized {
				trigger('EPBYMINW6406/MNTLAB-ilahutka-child3-build-job') {
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
	conditionalSteps {
		condition {
			shell('echo $JOBS | grep -q "MNTLAB-ilahutka-child4-build-job"')
					}
		runner('Fail')
		steps {
			downstreamParameterized {
				trigger('EPBYMINW6406/MNTLAB-ilahutka-child4-build-job') {
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
          
for(i in 1..4) {
  job("EPBYMINW6406/MNTLAB-ilahutka-child${i}-build-job") {
     
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
			shell ('chmod +x script.sh; ./script.sh > output.txt; tar czvf ${BRANCH_NAME}_dsl_script.tar.gz jobs.groovy script.sh' )
 		  }
  	publishers {
		archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz, output.txt')
    	}
	}
}
