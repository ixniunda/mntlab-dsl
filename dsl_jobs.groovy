for(def i=1; i<5; i++){
    job("EPBYMINW2470/MNTLAB-kkaliada-child${i}-build-job") {
        parameters {
            publishers {
        archiveArtifacts('*.tar.gz')
    }
            activeChoiceParam('BRANCH_NAME'){
                choiceType('RADIO')
                groovyScript {
                    script('''def gitURL = "https://github.com/MNT-Lab/mntlab-dsl.git"; def command = "git ls-remote -h $gitURL"; def proc = command.execute(); proc.waitFor(); if ( proc.exitValue() != 0 ) {println "Error, ${proc.err.text}"System.exit(-1)};def branches = proc.in.text.readLines().collect {it.replaceAll(/[a-z0-9]*\\trefs\\/heads\\//, '')}; branches.each { println "${it}" }''')
                }}
            scm {
                git {
                    remote {
                        branch('$BRANCH_NAME')
                        url('https://github.com/MNT-Lab/mntlab-dsl.git')
                    }}}
            steps {
                shell('sh script.sh > output.log')
                shell('tar -czvf "$BRANCH_NAME"_dsl_script.tar.gz *')
            }}}}
job('EPBYMINW2470/MNTLAB-kkaliada-main-build-job') {
    parameters {
        activeChoiceReactiveParam('BRANCH_NAME') {
            description('choose branch from multiple choices')
            choiceType('SINGLE_SELECT')
            groovyScript {
                script('return["kkaliada", "master"]')
                fallbackScript('"fallback choice"')
            }}
        activeChoiceParam('Slaves') {
            description('select your Slaves choice')
            choiceType('CHECKBOX')
            groovyScript {
                script('return["MNTLAB-kkaliada-child1-build-job", "MNTLAB-kkaliada-child2-build-job", "MNTLAB-kkaliada-child3-build-job", "MNTLAB-kkaliada-child4-build-job"]')
                fallbackScript('return ["error"]')
            }}}
    steps{
        conditionalSteps{
            condition {
                alwaysRun()
            }
            steps {
                triggerBuilder {
                    configs{
                        blockableBuildTriggerConfig {
                            projects('$Slaves')
                            block {
                                buildStepFailureThreshold('FAILURE')
                                unstableThreshold('UNSTABLE')
                                failureThreshold('never')
                            }
                            configs{
                                predefinedBuildParameters{
                                    properties('BRANCH_NAME=$BRANCH_NAME')
                                    textParamValueOnNewLine(false)
                                }}}}}}}}}
