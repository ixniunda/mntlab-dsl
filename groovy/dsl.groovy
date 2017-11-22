git_url="https://github.com/MNT-Lab/mntlab-dsl.git"
job_folder="Maksim Bialitski/"
job_prefix="MNTLAB-MBialitski"
job_suffix="build-job"
git_brunch="mbialitski"
job("${job_folder}${job_prefix}-main-${job_suffix}") {
    description 'This is the main build job'
    scm	{
        github (git_url,git_brunch)
    }
    parameters {
        activeChoiceParam('BRANCHE_NAME') {
            description('Allows user choose from multiple choices')

            choiceType('SINGLE_SELECT')
            groovyScript {
                script("return ['${git_brunch}', 'master']")
            }
        }
        activeChoiceParam ('Job') {
            description('Select job to build')
            choiceType('CHECKBOX')
            groovyScript{
                script ("list=[];(1..4).each n {list << (\"${job_prefix}-slave$n-${job_suffix}\")}; return list ")
                fallbackScript('')
            }

        }
        postbuildSteps {

        }
    }
}

(1..4).each {
    job("${job_folder}${job_prefix}-slabe${it}-${job_suffix}"){
        description "This is the slave${it} build job"
        scm	{
            github (git_url,git_brunch)
        }
        build {
            Shell($WORKSPACE/script.sh)
        }
    }
}