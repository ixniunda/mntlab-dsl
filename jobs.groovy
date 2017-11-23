def student = 'ivauchok'
def folder_name = 'Ihar Vauchok'

folder(folder_name) {
    description("Folder containing all jobs for ${folder_name}")
}

job("${folder_name}/MNTLAB-${student}-main-build-job") {
    parameters {
        activeChoiceParam('BRANCH_NAME') {
            description('Allows user choose from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('''import jenkins.model.*
import hudson.model.*
def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"
def proc = command.execute()
def branches = proc.in.text.readLines().collect { 
    it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '') 
}
def list = []
for (int i = 0; i < branches.size(); i++) {
  if (branches[i].matches('master') || branches[i].matches('ivauchok')) {
   list.add(branches[i]) 
  }
}
return list''')
            }
        }
    }

    parameters {
        activeChoiceParam('BUILDS_TRIGGER') {
            description('Allows user choose from multiple choices')
            choiceType('CHECKBOX')
            groovyScript {
                script('''import jenkins.model.*
import hudson.model.*
def list = []
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  if(it.fullName.matches('Ihar Vauchok\\\\/MNTLAB-ivauchok-c.+')) {
    list << "${it.name}:selected"
  }
}
return list''')
            }
        }
    }
    steps {
        conditionalSteps {
            condition {
                alwaysRun()
            }
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
        }
    }
}
for(i in 1..4) {
    job("${folder_name}/MNTLAB-${student}-child${i}-build-job") {
        parameters {
            stringParam('BRANCH_NAME')
        }
        parameters {
            activeChoiceParam('BRANCH_NAME') {
                description('Allows user choose from multiple choices')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('''import jenkins.model.*
import hudson.model.*
def command = "git ls-remote -h https://github.com/MNT-Lab/mntlab-dsl.git"
def proc = command.execute()
def branches = proc.in.text.readLines().collect { 
    it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '') 
}
return branches''')
                }
            }
        }
        scm {
            git {
                remote {
                    url('https://github.com/MNT-Lab/mntlab-dsl.git')
                }
                branch('*/$BRANCH_NAME')
            }
        }
        wrappers {
            preBuildCleanup()
        }
        steps {
            shell("bash ./script.sh >> output.txt")
            shell('tar -cvzf ${BRANCH_NAME}_dsl_script.tar.gz ./*')
        }
        publishers {
            archiveArtifacts('**/output.*')
        }
    }
}
