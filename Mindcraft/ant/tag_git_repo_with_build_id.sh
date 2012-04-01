#!/bin/bash
# Update the index
#!/bin/bash
flavor=$(grep flavor= version)
build=$(grep build= version)
git tag -a -f -F version ${flavor:7}-${build:6}
