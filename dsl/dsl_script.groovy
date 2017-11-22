def branchName = "ilakhtenkov"
def folderName = "Igor Lakhtenkov1"
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
                script(readFileFromWorkspace('dsl/active_choice_branch1.groovy'))
                fallbackScript('return ["error"]')
                filterable(false)
            }
        }
    }
     parameters {
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Allows triggered child jobs multiple choices')
            filterable()
            choiceType('CHECKBOX')
            groovyScript {
                script(readFileFromWorkspace('dsl/active_choice_jobs.groovy'))
                fallbackScript('return ["error"]')
                filterable(false)
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
for (i = 1; i <= childJobsNumber; i++) {
    job(folderName+'/MNTLAB-'+branchName+'-child'+i+'-build-job') {
        description 'This is test main job'
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                description('Allows branch single choice')
                filterable()
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script(readFileFromWorkspace('dsl/active_choice_branch2.groovy'))
                    fallbackScript('return ["error"]')
                    filterable(false)
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
            shell('script.sh > output.txt')
        }
        publishers {
            archiveArtifacts {
                pattern('output.txt')
                onlyIfSuccessful()
            }
        }
    }   
}

