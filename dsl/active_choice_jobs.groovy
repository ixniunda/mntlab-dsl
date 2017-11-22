import jenkins.model.*;
import hudson.model.*
def list =[]
Jenkins.instance.getAllItems(AbstractProject.class).each {it ->
  //if (it.fullName.matches('EPBYMINW2033\\/MNTLAB-ilakhtenkov-child(.+)')) {
  if (it.fullName.matches("${folderName}\\/MNTLAB-${branchName}-child(.+)")) {
	list << "${it.name}:selected"
  }
}
return list
