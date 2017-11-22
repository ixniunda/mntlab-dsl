git_url="https://github.com/MNT-Lab/mntlab-dsl.git"
job_folder="Maksim Bialitski/"
job_prefix="MNTLAB-MBialitski"
job_suffix="build-job"
git_brunch="*/mbialitski"

job("${job_folder}${job_prefix}-main-${job_suffix}") {
    description 'This is the main build job'
    scm {
        git {
            remote {
                branch(git_brunch)
                url(git_url)
            }
        }
    }
    parameters {
        activeChoiceParam('BRANCHE_NAME') {
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
                    }
                }
            }

    }
}

(1..4).each {
    job("${job_folder}${job_prefix}-slave${it}-${job_suffix}") {
        description "This is the slave${it} build job"
        scm {
            git {
                remote {
                    branch(git_brunch)
                    url(git_url)
                }
            }
        }
        steps {
            shell(readFileFromWorkspace('script.sh'))
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