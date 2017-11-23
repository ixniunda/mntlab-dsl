job('EPBYMINW1766/MNTLAB-amakhnach-main-build-job ') {
  parameters {
  
    
  	activeChoiceParam('Cities') {
        description('Name of the State')
        choiceType('CHECKBOX')
        groovyScript {
          script('def list = ["MNTLAB-amakhnach-child1-build-job","MNTLAB-amakhnach-child2-build-job","MNTLAB-amakhnach-child3-build-job","MNTLAB-amakhnach-child4-build-job"]; list.each { println "${it}"}')
          fallbackScript('"Error in script"')
        }
   	}
    activeChoiceParam('BRANCH_NAME') {
        choiceType('RADIO')
        groovyScript {
          script('["amakhnach", "master"]')
        }      //fallbackScript('"Error in script"')
  	}
  scm {
    git{
    remote{
      url('https://github.com/MNT-Lab/mntlab-dsl.git')
      
    }
    branch ('*/$BRANCH_NAME')
    }
    //git('https://github.com/MNT-Lab/mntlab-dsl.git', '$BRANCH_NAME')
      //branch ('$BRANCH_NAME')
  }
  
  steps {
    //gradle('clean build')
    shell('echo "${Cities}"')
    conditionalSteps{
      condition {	
      	alwaysRun()
      }
      steps {
       	triggerBuilder {
          configs{
    		blockableBuildTriggerConfig {
              projects('$Cities')
              block {
				buildStepFailureThreshold('FAILURE')
				unstableThreshold('UNSTABLE')
				failureThreshold('never')
				}
              configs{
                predefinedBuildParameters{
                  properties('BRANCH_NAME=$BRANCH_NAME')
                  textParamValueOnNewLine(false)
                }
              }
        	}
          }
    	}
      }
    }
  }
  
}
}


for(def i=1; i<5; i++){
  job("EPBYMINW1766/MNTLAB-amakhnach-child${i}-build-job") {
  parameters {
  
    
    
  	activeChoiceParam('BRANCH_NAME') {
      description('Name of the State')
      choiceType('RADIO')
      groovyScript {
        script('''def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git";def command = "git ls-remote -h $gitURL";def proc = command.execute();proc.waitFor();if ( proc.exitValue() != 0 ) {println "Error, ${proc.err.text}"System.exit(-1)};def branches = proc.in.text.readLines().collect {it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '')}; branches.each { println "${it}"}''')
        fallbackScript('"Error in script"')
      }
  }
  }
  //scm {
  //   git("https://github.com/lubouski/java-sample.git")
  //      }
  
  scm {
    git{
    remote{
      url('https://github.com/MNT-Lab/mntlab-dsl.git')
      
    }
    branch ('*/$BRANCH_NAME')
    }
    //git('https://github.com/MNT-Lab/mntlab-dsl.git', '$BRANCH_NAME')
      //branch ('$BRANCH_NAME')
  }
  steps {
    shell('echo $BRANCH_NAME;[ -f *.tar.gz ] && rm -rf *.tar.gz;chmod +x script.sh;./script.sh > output.txt;tar -zcvf "$BRANCH_NAME"_dsl_script.tar.gz *')
    
    } 
}

}
