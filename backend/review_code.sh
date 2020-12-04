#!/usr/bin/env bash

mvn pmd:check -Dpmd.verbose=true > pmd.errors
mvn checkstyle:check -Dcheckstyle.failOnViolation=false > checkstyle.errors
mvn findbugs:check > findbugs.errors

if [ -f ./reviewdog ] ; then
echo "Skip downloading" ;
else
curl -fSL https://github.com/haya14busa/reviewdog/releases/download/0.9.11/reviewdog_darwin_386 -o reviewdog && chmod +x ./reviewdog ;
fi

mvn checkstyle:check -Dcheckstyle.failOnViolation=false > checkstyle.errors

for file in $(echo **/target/checkstyle-result.xml)
do
    cat $file | ./reviewdog -f=checkstyle -diff="git diff integration"
done
