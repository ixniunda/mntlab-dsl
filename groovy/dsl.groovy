git_url="https://github.com/MNT-Lab/mntlab-dsl.git"
job_folder="Maksim Bialitski/"
job_prefix="MNTLAB-MBialitski"
job_suffix="build-job"
git_brunch="*/mbialitski"
job("${job_folder}${job_prefix}-main-${job_suffix}") {
    description 'This is the main build job'
    scm	{
        git {
            remote {
                branch (git_brunch)
                url(git_url)
            }
        }
    }
    parameters {
        activeChoiceReactiveParam('BRANCHE_NAME') {
            description('Allows user choose from multiple choices')

            choiceType('SINGLE_SELECT')
            groovyScript {
                script("return [ '${git_brunch}', 'master' ]")
            }
        }
        activeChoiceReactiveParam ('JOB') {
            description('Select job to build')
            choiceType('CHECKBOX')
            groovyScript{
                script ("list=[];(1..4).each {list << (\"${job_prefix}-slave\${it}-${job_suffix}\")}; return list ")
                fallbackScript('')
            }

        }

    }
    configure { project ->
        project / publishers << 'hudson.tasks.BuildTrigger' {
            childProjects("\$JOB")
        }
    }
}

(1..4).each {
    job("${job_folder}${job_prefix}-slave${it}-${job_suffix}"){
        description "This is the slave${it} build job"
        scm	{
            git {
                remote {
                    branch (git_brunch)
                    url(git_url)
                }
            }
        }
        steps {
            shell(readFileFromWorkspace('script.sh'))
        }
    }
}