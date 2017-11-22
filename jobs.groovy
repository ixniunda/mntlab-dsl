def git_URL = 'https://github.com/MNT-Lab/mntlab-dsl.git'
job('EPBYMINW2467/Main') {
    parameters {
        activeChoiceParam('Jobs') {
            description('LIST OF JOBS')
            choiceType('CHECKBOX')
            groovyScript {
                script('def list = ["MNTLAB-uhramovich-child1-build-job","MNTLAB-uhramovich-child2-build-job","MNTLAB-uhramovich-child3-build-job","MNTLAB-uhramovich-child4-build-job"];list.each { println "${it}"}')
                fallbackScript('"Error in script"')
            }
        }
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("return [ 'uhramovich' ,  'master' ]")
                }
            }
        }

        scm	{
            git {
                remote {
                    branch('$BRANCH_NAME')
                    url(git_URL)
                }

            }
        }

        steps {
            shell('echo "${Jobs}"')
            conditionalSteps{
                condition {
                    alwaysRun()
                }
                steps {
                    triggerBuilder {
                        configs{
                            blockableBuildTriggerConfig {
                                projects('$Jobs')
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
    steps {
        shell('echo "здравствуй, мир"')
    }
   

        for(def i=1; i<5; i++){
            job("EPBYMINW2467/MNTLAB-uhramovich-child${i}-build-job") {
                parameters {
                    activeChoiceParam('BRANCH_NAME'){
                        choiceType('RADIO')
                        groovyScript {
                            script('''def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"; def command = "git ls-remote -h $gitURL"; def proc = command.execute(); proc.waitFor(); if ( proc.exitValue() != 0 ) {println "Error, ${proc.err.text}"System.exit(-1)};def branches = proc.in.text.readLines().collect {it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '')}; branches.each { println "${it}" }''')
                        }}

                    scm	{
                        git {
                            remote {
                                branch('$BRANCH_NAME')
                                url('https://github.com/MNT-Lab/mntlab-dsl.git')
                            }

                        }
                    }
                  
        steps {
            shell('sh script.sh > output.txt')
          shell('tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz *')
                }




                steps {
                    shell('echo "hello DSL"')
                } 
                  publishers {
                    archiveArtifacts('*')
                  }
                }}}}
