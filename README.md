# Shutterstock
A clojure program for scraping the shutterstock.com website for images matching a query.  I use it for quickly populating the an image field in Anki (a space repetition program). I typically run it from [Textexpander](https://textexpander.com/). 

## Usage (Command line using Clojure)
Arguments are key value pairs using a map data structure. For this to work, be sure to update `main.clj` by commenting line 96 and uncommenting line 98

`$ clj -X:run :query horses+girls :age teenagers`

## Usage (Babashka script)
The Babashka script is located in `scripts/shutterstock/main`. Notice that queries arguements are not hashmaps
`$ bb -m shutterstock.main query girls+horses`

## Arguments
| Key        | Value   | Required | Values                             |
| :--------- | :------ | :------- | :--------------------------------- |
| :query     | String  | Yes      | Use + to combine words             |
| :age       | String  | No       | infants, children, teenagers, 20s  |
|            |         |          | 30s, 40s, 50s, 60s, older          |
| :ethnicity | String  | No       | japanese, caucasian, black,        |
|            |         |          | southeast-asian, hispanic, chinese |
| :gender    | String  | No       | male, female                       |
| :people    | String  | No       | true, false                        |
| :cell      | Integer | No       | postive number specifes what cell  |
|            |         |          | image you want                     |


## Usage (Textexpander script)
Create the following shell script snippit in Textexpander:
```
#!/bin/bash
args=$(pbpaste)
cd path/to/shutterstock
/usr/local/bin/bb -m shutterstock.main $args
```

copy the query arguements into your clipboard and run the shell script snippit.
