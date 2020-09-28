# ksql-udf-overlapping-ranges

KSQL / ksqlDB UDF to test whether any numeric ranges, defined in a JSON array, overlap with each other.

More specifically, assume that the input is an array of JSON strings. Each entry i this array is assumed to have attributes
for the start-value and end-value of some numeric range (integral values only, limits are Long.MIN_VALUE -> Long.MAX_VALUE).

## Installation

Clone this repo, pull the code, and build with Maven,

`mvn clean package`

In the `target` folder you will see a fat-jar created (one which contains the code for the UDF itself plus all it's dependencies, as a self-contained package).
Copy this jar e.g. `overlapping-ranges-1.0-jar-with-dependencies.jar` to the defined UDF extensions directory on your KSQL / ksqlDB server. 
Full instructions for how to define a location from which to load extra UDFs into ksqlDB can be found at https://docs.ksqldb.io/en/0.8.1-ksqldb/concepts/functions/#deploying

## Usage
Once installed you have to restart the ksqlDB server and tehn it should be ready to go! You can double-chcek this by typing `show functions;` at the ksql prompt. 
You should see the new `ranges_overlap` function included in the output. If not, check the ksql-server.log file for clues. You can even `describe function ranges_overlap;` 
to get more information about it's usage and parameters.

Sample usage:

`create table overlapping_ranges as select * from my_input where ranges_overlap(ranges, 'startdt', 'enddt') = true;`

In this case, the input arguments tell the function to inspect an array of JSON strings from a column called `ranges` and that, inside of each JSON string, there is 
an attribute called `startdt` which holds the lower bound of each range, and another called `enddt`which holds the upper bound. These names can match any legal JSON attribute name.
Other attributes within the JSON are ignored.

