# GameBase
This app is for my university coursework. The general idea is that it interfaces with the database api from IGDB.com, which provides lots of information about video games and related topics.

## Features
* News Feed with collation of video game related news articles
* Search function - search for specific game
* Browse top 50 - view the games with the highest review scores
* Detailed info - Allow user to select a game from browse or search and display detailed information about the game on a separate page -- **In Progress**
* Help Docs - Have a WebView page with a link to a device friendly website containing app instructions -- **TODO**
 
## Tech/Methods
* RoomDB api for caching of queries/results to reduce number of network requests
* RecyclerView to display results from database tables
* Standard HttpUrlConnection with POST method for requests to the IGDB api
