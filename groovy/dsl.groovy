git_url="https://github.com/MNT-Lab/mntlab-dsl.git"
job_folder="Maksim Bialitski/"
job_prefix="MNTLAB-MBialitski"
job_suffix="build-job"
git_brunch="mbialitski"

job("${job_prefix}-main-${job_suffix}") {
    description 'This is the main build job'
    scm {
        git {
            remote {
                branch("*/${git_brunch}")
                url(git_url)
            }
        }
    }
    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Allows user choose from multiple choices')

            choiceType('SINGLE_SELECT')
            groovyScript {
                script("return [ '${git_brunch}', 'master' ]")
            }
        }
        activeChoiceParam('JOB') {
            description('Select job to build')
            choiceType('CHECKBOX')
            groovyScript {
                script(readFileFromWorkspace('groovy/slaves.groovy'))
            }
        }
    }
    steps {
        conditionalSteps {
            condition {
                alwaysRun()
            }
            runner('Fail')
            steps {
                downstreamParameterized {
                    trigger ("\$JOB") {
                        block {
                            buildStepFailure ('FAILURE')
                            failure ('FAILURE')
                        }
                        parameters {
                            predefinedProp ("BRANCH_NAME", "\$BRANCH_NAME")
                        }
                    }
                }
            }

    }
}

(1..4).each {
    job("${job_prefix}-slave${it}-${job_suffix}") {
        description "This is the slave${it} build job"
        scm {
            git {
                remote {
                    branch("*/\$BRANCH_NAME")
                    url(git_url)
                }
            }
        }
        steps {
            shell("bash script.sh >> output.log ; tar czf \${BRANCH_NAME}_dsl_script.tar.gz groovy/dsl.groovy")
        }
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                description('Select brunch to build')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script(readFileFromWorkspace('groovy/get_git_brunches.groovy'))
                }
            }
        }
    }
}
}