freeStyleJob('EPBYMINW2468/MNTLAB-abandarovich-main-build-job') {
    keepDependencies(false)
    properties {
    }
  parameters {
        activeChoiceReactiveParam('BRANCH_NAME') {
            description('Select a git branch')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''def gitURL = "https://github.com/MNT-Lab/mntlab-dsl"
def command = "git ls-remote -h $gitURL"

def proc = command.execute()
proc.waitFor()              

if ( proc.exitValue() != 0 ) {
   println "Error, ${proc.err.text}"
   System.exit(-1)
}

def branches = proc.in.text.readLines().collect { 
    it.replaceAll(/[a-z0-9]*\trefs\\/heads\\//, '') 
}
return branches
''')
            }

        }
            activeChoiceReactiveParam('BUILDS_TRIGGER') {
            description('Trigger jobs to run')
			filterable(false)
            choiceType('CHECKBOX')
            groovyScript {
                script('''def l = []
l << "MNTLAB-abandarovich-child1-build-job"
l << "MNTLAB-abandarovich-child2-build-job"
l << "MNTLAB-abandarovich-child3-build-job"
l << "MNTLAB-abandarovich-child4-build-job"
return l
''')
            }

        }
    }

    label()
    disabled(false)
    triggers {
 
    }
    concurrentBuild(false)
    steps {
     downstreamParameterized {
            trigger('${BUILDS_TRIGGER}') {
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
      publishers {
    }
}

for (i = 1; i <= 4; i++) {
freeStyleJob('EPBYMINW2468/MNTLAB-abandarovich-child'+i+'-build-job') {
    keepDependencies(false)
    properties {
    }
    scm {
        git {
            remote {
                url('https://github.com/MNT-Lab/mntlab-dsl.git')
            }
            branches('*/${BRANCH_NAME}')
            extensions {
            }
            configure {
                'doGenerateSubmoduleConfigurations'('false')
            }

        }
    }
    label()
    disabled(false)
    triggers {
    }
    concurrentBuild(false)
    steps {
        shell('''\
source ./script.sh > output.txt
tar -cvzf ${BRANCH_NAME}_dsl_script.tar.gz.tar.gz *.groovy
''')
    }
    publishers {
        archiveArtifacts {
            pattern('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz.tar.gz')
            allowEmpty(false)
            onlyIfSuccessful(false)
            fingerprint(false)
            defaultExcludes(true)
        }
    }
}
}
