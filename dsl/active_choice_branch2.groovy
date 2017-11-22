def getbranches = ("git ls-remote -t -h https://github.com/MNT-Lab/mntlab-dsl.git").execute()

def list = getbranches.text.readLines()
        .collect { it.split()[1].replaceAll('refs/heads/', '')  }
        .unique()
        .findAll { it.matches('ilakhtenkov|master') }
def listSelected =[]
for(int i = 0; i < list.size(); i++)
{
    if(list.get(i).contains('ilakhtenkov'))
    {
        listSelected.add(list.get(i).replace('ilakhtenkov', 'ilakhtenkov:selected'));
    } else {
        listSelected.add(list.get(i));
    }
}
return listSelected
