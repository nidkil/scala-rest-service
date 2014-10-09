#! /bin/bash

function update_release {
    FILENAME=$1
    RELEASE=$2
    mv $FILENAME $FILENAME.tmp
    sed -e "s/val release = .*/val release = \"$RELEASE\"/g" $FILENAME.tmp > $FILENAME
    rm $FILENAME.tmp
}

if [ "$1" == "" ]; then
    echo "Error: the version number must be passed as argument"
    exit 2
fi

update_release "./src/main/scala/RestClient.scala" $1
update_release "./src/main/scala/TestRestService.scala" $1
