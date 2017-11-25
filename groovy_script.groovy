def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()
proc.waitFor()

if ( proc.exitValue() != 0 ) {
    println "Error, ${proc.err.text}"
    System.exit(-1)
}

def branches = proc.in.text.readLines().collect {
    it.replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
}




job("EPBYMINW6405/MNTLAB-ataran-main-build-job") {
    parameters {
         choiceParam('BRANCH',['master','ataran']) 
   	}
  parameters{
    activeChoiceParam('BUILD_JOBS'){
      choiceType('CHECKBOX')
      groovyScript {
        script('''import jenkins.model.*;
import hudson.model.*;
def list=[];
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->if(it.name.matches('MNTLAB-ataran-child.+')){list<<"${it.name}:selected"};
}
return list ; 
   		      ''')
        fallbackScript('"Bad choice"');
               }
    }
  }
  steps {
        conditionalSteps {
          condition {
            alwaysRun();
          }
           steps {
              downstreamParameterized{
              	trigger('$BUILD_JOBS') {
                  block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
                  parameters{
                    predefinedProp('BRANCH_NAME', '${BRANCH}')
                  }               
                }
            }
        }
    }
  }
  
}







job("EPBYMINW6405/MNTLAB-ataran-child1-build-job") {
  parameters {
        stringParam('BRANCH_NAME')
    }
  parameters {
         choiceParam('BRANCH_NAME',branches)
        
   	}
  scm {
        git{
          remote{
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
            branch('$BRANCH_NAME')
          }
        }
    	
    }
    steps {
            shell('bash script.sh > output.txt')
          shell('tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz *')
                }
          steps { 
                  publishers {
            archiveArtifacts {
                pattern('*_dsl_script.tar.gz')
                onlyIfSuccessful()
            }
        }
}
}
job("EPBYMINW6405/MNTLAB-ataran-child2-build-job") {
  parameters {
        stringParam('BRANCH_NAME')
    }
  parameters {
         choiceParam('BRANCH_NAME',branches)
        
   	}
  scm {
        git{
          remote{
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
            branch('$BRANCH_NAME')
          }
        }
    	
    }
    steps {
            shell('bash script.sh > output.txt')
          shell('tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz *')
                }
          steps { 
                  publishers {
            archiveArtifacts {
                pattern('*_dsl_script.tar.gz')
                onlyIfSuccessful()
            }
        }
}
}
job("EPBYMINW6405/MNTLAB-ataran-child3-build-job") {
  parameters {
        stringParam('BRANCH_NAME')
    }
  parameters {
         choiceParam('BRANCH_NAME',branches)
        
   	}
  scm {
        git{
          remote{
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
            branch('$BRANCH_NAME')
          }
        }
    	
    }
    steps {
            shell('bash script.sh > output.txt')
          shell('tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz *')
                }
          steps { 
                  publishers {
            archiveArtifacts {
                pattern('*_dsl_script.tar.gz')
                onlyIfSuccessful()
            }
        }
}
}
job("EPBYMINW6405/MNTLAB-ataran-child4-build-job") {
  parameters {
        stringParam('BRANCH_NAME')
    }
  parameters {
         choiceParam('BRANCH_NAME',branches)
        
   	}
  scm {
        git{
          remote{
            url('https://github.com/MNT-Lab/mntlab-dsl.git')
            branch('$BRANCH_NAME')
          }
        }
    	
    }
    steps {
            shell('bash script.sh > output.txt')
          shell('tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz *')
                }
          steps { 
                  publishers {
            archiveArtifacts {
                pattern('*_dsl_script.tar.gz')
                onlyIfSuccessful()
            }
        }
}
}
