#!/bin/bash
cd $HOME/Dropbox/code/clojure/shutterstock/
/usr/bin/java -cp $(/usr/local/bin/clojure -Spath):classes shutterstock.main "$@"
