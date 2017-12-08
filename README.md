OpenNMS Service Configuration Audit
===================================

This tool helps to analyze and validate configuration for the following OpenNMS components:
- Pollerd
- Collectd
- Provisiond
The output is represented as a matrix in a spreadsheet format like XLS or ODS. The configuration is just validated and give you the following information:
- All services configured for service assurance, datacollection and service detection
- Show configuration mismatches in poller-configuration, e.g. missing monitors entry or orphaned monitor definitions.
- Show which services are used for data collection
- Show which services have have a provisiond service detector
- Show which services are manually assigned throug provisioning

Given information help to understand the configuration for possible issues your requisitions, foreign-sources, poller-configuration and collectd-configuration.

Issues:
=========================
The tool is maintained in JIRA of the OpenNMS project which you can find here: http://issues.opennms.org/browse/OSCA

Requirements:
=============
- Java development environment
- Maven for dependency management
- optional, but recommended: Git

Build
=====
1. Clone this repository from github
2. Build a runnable jar with all dependencies with `mvn clean package`
3. You find the runnable jar in the target folder

Usage
=====
start the jar like this:
`java -jar -DConfigFolder=/your/opennms/config/folder -DOutPutFile=/the/result/file/Result.xls configcleaner.jar`

Development
===========
The workflow for committing to the git repository follows the http://nvie.com/posts/a-successful-git-branching-model/ branch model. In short, there are two main branches
- origin/master: the main branch where the source code of HEAD always reflects a production-ready state.
- origin/develop: the main branch where the source code of HEAD always reflects a state with the latest delivered development changes for the next release

We use three different types of branches:
- Feature branches
- Release branches
- Hotfix branches

Feature branches
================
May branch off from: develop
Must merge back into: develop
Branch naming convention: anything except master, develop, release-*, or hotfix-*
If you want to develop a new feature you would do 
`git checkout -b myfeature develop`

Workflow for incorporating a finished feature on develop

    $ git checkout develop
    Switched to branch 'develop'
    $ git merge --no-ff myfeature
    Updating ea1b82a..05e9557
    (Summary of changes)
    $ git branch -d myfeature
    Deleted branch myfeature (was 05e9557).
    $ git push origin develop

**Hint:** Please take care of the `--no-ff` flag for the merge

Release branches
================
- May branch off from: develop
- Must merge back into: develop and master
- Branch naming convention: release-*

Creating a release branch
=========================

    $ git checkout -b release-1.2 develop
    Switched to a new branch "release-1.2"
    $ ./bump-version.sh 1.2 # fictional script increasing version number
    Files modified successfully, version bumped to 1.2.
    $ git commit -a -m "Bumped version number to 1.2"
    [release-1.2 74d9424] Bumped version number to 1.2
    1 files changed, 1 insertions(+), 1 deletions(-)

Finishing a release branch
==========================
When the state of the release branch is ready to become a real release, some actions need to be carried out. First, the release branch is merged into master (since every commit on master is a new release by definition, remember). Next, that commit on master must be tagged for easy future reference to this historical version. Finally, the changes made on the release branch need to be merged back into develop, so that future releases also contain these bug fixes.

    $ git checkout master
    Switched to branch 'master'
    $ git merge --no-ff release-1.2
    Merge made by recursive.
    (Summary of changes)
    $ git tag -a 1.2
    
The release is now done, and tagged for future reference.
Edit: You might as well want to use the `-s` or `-u` <key> flags to sign your tag cryptographically.

To keep the changes made in the release branch, we need to merge those back into develop, though. In Git:

    $ git checkout develop
    Switched to branch 'develop'
    $ git merge --no-ff release-1.2
    Merge made by recursive.
    (Summary of changes)

This step may well lead to a merge conflict (probably even, since we have changed the version number). If so, fix it and commit.

Now we are really done and the release branch may be removed, since we don’t need it anymore:

    $ git branch -d release-1.2
    Deleted branch release-1.2 (was ff452fe).

Creating the hotfix branch
==========================
Hotfix branches are created from the master branch. For example, say version 1.2 is the current production release running live and causing troubles due to a severe bug. But changes on develop are yet unstable. We may then branch off a hotfix branch and start fixing the problem:

    $ git checkout -b hotfix-1.2.1 master
    Switched to a new branch "hotfix-1.2.1"
    $ ./bump-version.sh 1.2.1
    Files modified successfully, version bumped to 1.2.1.
    $ git commit -a -m "Bumped version number to 1.2.1"
    [hotfix-1.2.1 41e61bb] Bumped version number to 1.2.1
    1 files changed, 1 insertions(+), 1 deletions(-)

Don’t forget to bump the version number after branching off!

Then, fix the bug and commit the fix in one or more separate commits.

    $ git commit -m "Fixed severe production problem"
    [hotfix-1.2.1 abbe5d6] Fixed severe production problem
    5 files changed, 32 insertions(+), 17 deletions(-)

Finishing a hotfix branch
=========================

When finished, the bugfix needs to be merged back into master, but also needs to be merged back into develop, in order to safeguard that the bugfix is included in the next release as well. This is completely similar to how release branches are finished.

First, update master and tag the release.

    $ git checkout master
    Switched to branch 'master'
    $ git merge --no-ff hotfix-1.2.1
    Merge made by recursive.
    (Summary of changes)
    $ git tag -a 1.2.1

Edit: You might as well want to use the `-s` or `-u` <key> flags to sign your tag cryptographically.

Next, include the bugfix in develop, too:

    $ git checkout develop
    Switched to branch 'develop'
    $ git merge --no-ff hotfix-1.2.1
    Merge made by recursive.
    (Summary of changes)

The one exception to the rule here is that, when a release branch currently exists, the hotfix changes need to be merged into that release branch, instead of develop. Back-merging the bugfix into the release branch will eventually result in the bugfix being merged into develop too, when the release branch is finished. (If work in develop immediately requires this bugfix and cannot wait for the release branch to be finished, you may safely merge the bugfix into develop now already as well.)

Finally, remove the temporary branch:

    $ git branch -d hotfix-1.2.1
    Deleted branch hotfix-1.2.1 (was abbe5d6).
