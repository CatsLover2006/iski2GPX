# iSki 2 GPX
## How do I use this?
Usage: `java -jar jarfile.jar details.json output.gpx`

## How do I get the details.json file?
Load [iski.cc](https://iski.cc), and log in to your account. Go to My iSki -> Tracks and open the track you want to convert. Find your track ID in the URL bar; as of January 29, 2025, this will be at the end of your URL bar. Go to [https://delphi.iski.cc/api/tracks/xxxxxxxxx/details](https://delphi.iski.cc/api/tracks/xxxxxxxxx/details), replacing xxxxxxxxx with the track ID. This is your `details.json`.

## Why does the output contain nothing useful in the file?
Check your `details.json` file. There should be a `track` key. If it looks like `"track":""`, there are no data points for me to convert. If there is no `track` key, there will also be no data for me to convert (and the failure will be a lot more catastrophic from a code perspective)

## Why are there so few data points in the output file?
Because, as of January 29, 2025, there are so few data points in the input file. If you're translating a multi-day ski trip, and the input file is 20kb, it likely does not contain enough data points to create a useful output file.
