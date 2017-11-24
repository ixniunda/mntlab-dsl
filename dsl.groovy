//Configuration
confGit="https://github.com/MNT-Lab/mntlab-dsl.git"
confBranch='amurzich'
confFolder="EPBYMINW3088/"
confName1="MNTLAB-amurzich"
confName2="build-job"
confChildNumber=4

job("${confFolder}${confName1}-main-${confName2}") {
    scm {
        git {
            remote {
                url(confGit)
                branch{"*/${confBranch}"}
            }
        }
    }
    parameters{
        activeChoiceParam('BRANCH_NAME') {
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("return [ 'master', '${confBranch}' ]")
            }
        }
        activeChoiceParam('BUILDS_TRIGGER') {
            choiceType('CHECKBOX')
            groovyScript {
                script(
                       'import hudson.model.*; ' +
                       'import jenkins.model.*; ' +
                       'result = []; ' +
                       'foundJobs = Jenkins.instance.getAllItems(AbstractProject.class).findAll { job -> job.name =~ /(.*)child(.*)/}; ' +
                       'foundJobs.each { job -> result.add("$job.name"); ' +
                       'return result; '
                )
            }
        }
    }
    steps {
        downstreamParametrized {
            trigger ("\$BUILDS_TRIGGER") {
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                }
                parameters {
                    predefinedProp("BRANCH_NAME", "\$BRANCH_NAME")
                }
            }
        }
    }
}

int i = 1
while (i <= confChildNumber) {
    job("${confFolder}${confName1}-child${i}-${confName2}") {
        scm {
            git {
                remote {
                    url(confGit)
                    branch("*/\$BRANCH_NAME")
                }
            }
        }
        steps {
            shell("./script.sh; tar -czvf artifact-dsl-\$BUILD_NUMBER.tar.gz dsl.groovy")
        }
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("return [ 'master', '${confBranch}' ]")
                }
            }
        }
    }
    i++
}