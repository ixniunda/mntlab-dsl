def student = 'dbakulin'
def project = 'ixniunda/mntlab-dsl'
def branchApi = new URL("https://api.github.com/repos/${project}/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())
def childJobs = 4


listBranches = []
branches.each {
 listBranches.push(it.name)
 }

listJobs = []
(1..childJobs).each {
  listJobs.push ("MNTLAB-${student}-child${it}-build-job")
}


def branchesQuotes = listBranches.collect {"'" + it + "'"}

def jobsQuotes = listJobs.collect {"'" + it + "'"}

def mainBranches = branchesQuotes.findAll {it.matches ("'dbakulin'") or it.matches ("'master'")}


job ("MNTLAB-${student}-main-build-job") {
 parameters {
  activeChoiceParam ('BRANCH_NAME') {
    description ('select a branch')
    choiceType ('SINGLE_SELECT')
      groovyScript {
          script("return ${mainBranches}")
		  fallbackScript ("return ['fail choice']")
      } 
  }
   activeChoiceReactiveParam('BUILDS_TRIGGER') {
            description('Trigger jobs to run')
			filterable(false)
            choiceType('CHECKBOX')
     groovyScript {
       script ("return ${jobsQuotes}")
     }
   }
 }
    scm {
        git {
            remote {
                url("https://github.com/${project}.git")
            }
			 branch('$BRANCH_NAME')
        }
    }
    steps {
        conditionalSteps {
            condition {
                alwaysRun ()
            }
            runner ('Fail')
        }
        downstreamParameterized {
            trigger ('$BUILDS_TRIGGER') {
                block {
                    buildStepFailure ('FAILURE')
                    failure ('FAILURE')
                    unstable ('FAILURE')
                }
                parameters {
                    predefinedProp ('BRANCH_NAME', '$BRANCH_NAME')
                }
            }
          
        }
    }
}

    (1..childJobs).each {
        job ("MNTLAB-${student}-child${it}-build-job") {
            parameters {
                activeChoiceParam ('BRANCH_NAME') {
                    description ('select branch')
                    choiceType ('SINGLE_SELECT')
                    groovyScript {
                        script ("return ${branchesQuotes}")
                      fallbackScript ("return ['fail choice']")
                    }
                }
            }
            scm {
                git {
                    remote {
                        url ("https://github.com/${project}.git")
                    }
                    branch ('$BRANCH_NAME')
                }
            }
          steps {
            shell (''' 
			 bash script.sh > out.txt 
			 tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz *.txt *.groovy
		  ''')
          }
        
          publishers {
            archiveArtifacts ('${BRANCH_NAME}_dsl_script.tar.gz')
          }
        }
    } 


sectionedView("$student") {
    sections {
        listView {
          name('MNTLAB')
            width('FULL')
            alignment('LEFT')
            jobs {
                regex('MNTLAB-dbakulin.*')
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
            }
        }
    }
}
