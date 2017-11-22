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
        activeChoiceReactiveParam('BRANCHE_NAME') {
            description('Allows user choose from multiple choices')
            filterable()
            choiceType('SINGLE_SELECT')
            groovyScript {
                script("return [${git_brunch},'master']")
            }
        }
        activeChoiceReactiveParam ('Job') {
            description('Select job to build')
            filterable()
            choiceType('CHECKBOX')
            groovyScript{
                script ("list=[];(1..4).each {list.add(\"${job_prefix}-slave${it}-${job_suffix}\")} ")
                fallbackScript('')
            }
        }
    }
}