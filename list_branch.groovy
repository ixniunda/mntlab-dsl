def remote_url="https://github.com/MNT-Lab/mntlab-dsl.git"
def command = [ "/bin/bash", "-c", "git ls-remote --heads " + remote_url + " | awk '{print \$2}' | sort  -V | sed 's@refs/heads/@@'" ]
def process = command.execute()

def result = process.in.text.tokenize("\n")

def branches = []
for(i in result) {
        branches.add(i)
}
return branches
