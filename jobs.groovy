def student   = 'adudko'
def my_folder = 'EPBYMINW2472'
def project   = 'MNT-Lab/mntlab-dsl'
def branchApi = new URL("https://api.github.com/repos/${project}/branches")
def branches  = new groovy.json.JsonSlurper().parse(branchApi.newReader())
def jobs      = 4




// Create folder EPBYMINW2472
folder("${my_folder}") {
    displayName("${my_folder}")
    description("Folder containing all jobs for ${my_folder}")
}



// Convert all Branches for groovyScript input
listBranches = []
branches.each {
  listBranches.push(it.name)
}
List convertListBranches = listBranches.collect{ '"' + it + '"'}



// Convert all Jobs for groovyScript input
listJobs = []
(1..jobs).each {
  listJobs.push("MNTLAB-${student}-child${it}-build-job")
}
List convertListJobs = listJobs.collect{ '"' + it + '"'}



// Filter Branches
def mainListBranches = convertListBranches.findAll { it.matches('"adudko"') or it.matches('"master"') }



  job("${my_folder}/MNTLAB-${student}-main-build-job") {
      parameters {
           activeChoiceParam('MAIN_BRANCH') {
              description('Choose from main branches')
              choiceType('SINGLE_SELECT')
              groovyScript {
                script("${mainListBranches}")
                  fallbackScript('"fallback choice"')
              }
           }
  
           activeChoiceParam('JOB_NAME') {
              description('Choose from multiple jobs')
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
    job("${my_folder}/MNTLAB-${student}-child${it}-build-job") {
      parameters {
        activeChoiceParam('BRANCH_NAME') {
              description('Choose from multiple branch')
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
