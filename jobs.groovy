// job parametrs
def student     = "kzalialetdzinau"
def workstation = "EPBYMINW1969"
def project     = "MNT-Lab/mntlab-dsl"
def prefix      = "build-job"


// github link
def branchApi   = new URL("https://api.github.com/repos/${project}/branches")
def branches    = new groovy.json.JsonSlurper().parse(branchApi.newReader())

// counter
def jobs        = 4

// Convert list of branches by groovyscript
listBranches = []
branches.each {
  listBranches.push(it.name)
}
List convertListBranches = listBranches.collect{ '"' + it + '"'}

// Convert list of Jobs by groovy script
listJobs = []
(1..jobs).each {
  listJobs.push("MNTLAB-${student}-child${it}-${prefix}")
}
List convertListJobs = listJobs.collect{ '"' + it + '"'}

// Filter Branches
def mainListBranches = convertListBranches.findAll { it.matches('"kzalialetdzinau"') or it.matches('"master"') }

  job("${workstation}/MNTLAB-${student}-main-${prefix}") {
      parameters {
           activeChoiceParam('MAIN_BRANCH') {
              description('Choose a current branch')
              choiceType('SINGLE_SELECT')
              groovyScript {
                script("${mainListBranches}")
                  fallbackScript('"fallback choice"')
              }
           }
  
           activeChoiceParam('JOB_NAME') {
              description('Choose current jobs from list')
              choiceType('CHECKBOX')
              groovyScript {
                script("${convertListJobs}")
                  fallbackScript('"fallback choice"')
              }
           }
      }
  
    scm {
      git {
        remote {
          url("https://github.com/${project}.git")
        }
        branch('$MAIN_BRANCH')
      }
    }
  
    steps {
      conditionalSteps {
        condition {
          alwaysRun()
        }
  
        runner('Fail')
      }
  
      downstreamParameterized {
        trigger('$JOB_NAME') {
          block {
            buildStepFailure('FAILURE')
            failure('FAILURE')
            unstable('FAILURE')
          }
          parameters {
            predefinedProp('BRANCH_NAME', '$MAIN_BRANCH')
          }
        }
      }
    }
  }
  
  (1..jobs).each {
    job("${workstation}/MNTLAB-${student}-child${it}-${prefix}") {
      parameters {
        activeChoiceParam('BRANCH_NAME') {
              description('Choose a current branch')
              choiceType('SINGLE_SELECT')
              groovyScript {
                script("${convertListBranches}")
                  fallbackScript('"fallback choice"')
              }
        }
      }
  
    scm {
      git {
        remote {
          url("https://github.com/${project}.git")
        }
        branch('${BRANCH_NAME}')
      }
    }
  
      steps {
          shell('''
                   bash script.sh > output.txt
                   tar -czvf ${BRANCH_NAME}_dsl_script.tar.gz output.txt
                ''')
      }
  
      publishers {
        archiveArtifacts('${BRANCH_NAME}_dsl_script.tar.gz')
      }
  }
}