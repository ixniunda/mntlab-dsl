def student = 'ivauchok'
def giturl = 'https://github.com/MNT-Lab/mntlab-dsl.git'

folder(student) {
    description("Folder containing all jobs for ${student}")
}

job("${student}/MNTLAB-${student}-main-build-job") {
}

for(i in 1..4) {
    job("${student}/MNTLAB-${student}-child${i}-build-job") {
        scm {
            git(giturl)
        }
    }
}

