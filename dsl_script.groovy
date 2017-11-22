def branchName = "ilakhtenkov"
def folderName = "Igor Lakhtenkov"
def childJobsNumber = 4

folder(folderName) {
    description('This is folder of'+folderName)
}

job(folderName+'/MNTLAB-'+branchName+'-main-build-job') {
    description 'This is test main job'
    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Allows branch single choice')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''def getbranches = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute()
					return getbranches.text.readLines()
        				.collect { it.split()[1].replaceAll('refs/heads/', '')  }
        				.unique()
        				.findAll { it.matches('ilakhtenkov|master') }''')
                fallbackScript('return ["error"]')
            }
        }
    }
     parameters {
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Allows triggered child jobs multiple choices')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script('''import jenkins.model.*;
						  import hudson.model.*

						  def list =[]
						  Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  							if (it.fullName.matches('Igor Lakhtenkov\\/MNTLAB-ilakhtenkov-child(.+)')) {
    							list << "${it.name}:selected"
  							}
						  }
						  return list''')
                fallbackScript('return ["error"]')
            }
        }
     }
    scm {
        git {
          remote {
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
          }
          branch('$BRANCH_NAME')
        }
    }
    steps {
        conditionalSteps{
            condition{
                alwaysRun()
            }
            steps {
                downstreamParameterized {
                    trigger('$BRANCH_NAME') {
                        triggerWithNoParameters = false
                        //triggerFromChildProjects = false
                        block {
                    		buildStepFailure('FAILURE')
                    		failure('FAILURE')
                    		unstable('UNSTABLE')
                        }
                        parameters {
                    		predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
                    	}
                    }
                }    
            } 
        }
    }
}
for (i = 0; i < childJobsNumber; i++) {
    job(folderName+'/MNTLAB-'+branchName+'-child'+i+'-build-job') {
        description 'This is test main job'
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                description('Allows branch single choice')
                filterable()
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('''def getbranches = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute()
                        return getbranches.text.readLines()
                            .collect { it.split()[1].replaceAll('refs/heads/', '')  }
                            .unique() }''')
                    fallbackScript('return ["error"]')
                }
            }
        }
        scm {
            git {
              remote {
                url('https://github.com/MNT-Lab/mntlab-dsl.git')
              }
              branch('$BRANCH_NAME')
            }
        }
        steps {
            shell('echo Hello World!')
        }
    }   
}

