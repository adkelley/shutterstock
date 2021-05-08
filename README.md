# shutterstock.clj
Scrape the shutterstock.com website for images matching a query.  I uses it for
quickly populating the image field in Anki (a space repetition program). I typically run it from Alfred (an application launcher for macOS). The Alfred script runs a precompiled script to shave off a few seconds of running time. *see Usage (Command line using Java)*. 

## Usage (Command line using Java)
### Pre-compiling the script
Create a `classes` folder in the parent directory.  This is where Clojure compiles source files to `.class` files

`$ mkdir classes`

Then run:

`$ clojure -M -e "(compile 'shutterstock.main)"`

to compile the main namespace (and transitively everything it requires)

### Verify that it works
Run the following from the JVM.  Notice that the arguments are key value pairs, with no map data structure. 

`$ java -cp $(clojure -Spath):classes shutterstock.main query girls+horses age teenagers`


There is a `scrape.sh` shell script to make this easier:

`$ ./scrape.sh query girls+horses age teenagers`

## Usage (Command line using Clojure)
Arguments are key value pairs using a map data structure. For this to work, be sure to update `main.clj` by commenting line 96 and uncommenting line 98

`$ clj -X:run :query horses+girls :age teenagers`

## Arguments
| Key       | Value   | Required | Values                             |
| :-------- | :------ | :------- | :--------------------------------- |
| query     | String  | Yes      | Use + to combine words             |
| age       | String  | No       | infants, children, teenagers, 20s  |
|           |         |          | 30s, 40s, 50s, 60s, older          |
| ethnicity | String  | No       | japanese, caucasian, black,        |
|           |         |          | southeast-asian, hispanic, chinese |
| gender    | String  | No       | male, female                       |
| people    | String  | No       | true, false                        |
| cell      | Integer | No       | postive number specifes what cell  |
|           |         |          | image you want                     |


## Alfred Script
```
args="$@"
cd path/to/shutterstock/directory
/usr/bin/java -cp $(/usr/local/bin/clojure -Spath):classes shutterstock.main $args
```
