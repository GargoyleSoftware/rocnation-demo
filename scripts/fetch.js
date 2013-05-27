/*
var geniusClient = require("rapgenius-js");

geniusClient.searchArtist("Jay-Z", function(err, artist){
  if(err){
    console.log("Error: " + err);
  }else{
    console.log("Rap artist found [name=%s, link=%s, popular-songs=%d]",
                artist.name, artist.link, artist.popularSongs.length);

  }
});
*/
var rapgeniusClient = require("rapgenius-js");

function checkVarIsAString(v) {
  return v && typeof v === "string";
}

function clean(str) {
  if (checkVarIsAString(str)) {
    str = str.replace(/^\s*\n*\s*/g, "");
    str = str.replace(/\s*\n*\s*$/g, "");
    //str = str.replace(/\n/g,"");
    str = str.replace(/\n\n+/g,"\n");
    
    return str;
  }

  return "";
}

HOOK_OPTIONS = ['[Hook]', '[Refrain]', '[Hook - Rihanna]', 
    '[Hook: Alicia Keys]', '[Hook: Swizz Beatz]', '[Hook: Drake]', 
    '[Hook] - [Kid Cudi]', '[Hook: K. Briscoe] + (Jay-Z)', '[Hook - Pharrell]',
    '[Hook - Young Jeezy]']


HOOK_REPEAT_OPTIONS = ['[Hook]', '[Refrain]']


var contains = function(a, list){
    isAinList = false;
    list.forEach(function(list_item){
        //console.log(a == list_item);
        if (a == list_item){
            isAinList = true;
            return;
        }
    });
    return isAinList;

}

var copyWithoutExpl = function(jsonVerse){
    var newVerse = [];
    jsonVerse.forEach(function(verse){
        //console.log(verse);
        var verseToAdd = {};
        verseToAdd['id'] = verse['id'];
        verseToAdd['content'] = verse['content'];
        verseToAdd['explanation'] = '';
        newVerse.push(verseToAdd);
    });
    //console.log(jsonVerse);
    return newVerse
}


var copyHook = function(lyrics){
    var sections = lyrics.sections;
    var isHookFound = false;
    var hookVerses;
    sections.forEach(function(section){
        if(!isHookFound){
            //captures first Hook's verses
            //console.log(section.name);
            
            if(contains(section.name, HOOK_OPTIONS)){
                isHookFound = true;
                //console.log(section.verses);
                hookVerses = copyWithoutExpl(section.verses);
                //console.log(hookVerses);
            }
        }
        else{
            if(contains(section.name, HOOK_REPEAT_OPTIONS)){
                //once we've copied the hook, we'll put it into any repeats

                section.verses = hookVerses;
            }
        }
    });
    //console.log(lyrics);
    return lyrics;
}

var lyricsSearchCb = function(err, lyricsAndExplanations){
    if(err){
        console.log("Error: " + err);
    }
    else{

        //Printing lyrics with section names
        var lyrics = lyricsAndExplanations.lyrics;
        var explanations = lyricsAndExplanations.explanations;
        //console.log("**** LYRICS *****\n%s", lyrics.getFullLyrics(true));


        //Now we can embed the explanations within the verses
        lyrics.addExplanations(explanations);

        var old_lyrics = lyrics;
        var lyrics = copyHook(old_lyrics);
        //console.log(lyrics);

        jsonSong = {};
        jsonSong["title"] = songName
        jsonLyrics = [];

        lyrics.sections.forEach(function(section){
            //console.log("\n", section.name)
            section.verses.forEach(function(verses){
                if(verses.id!=-1){
                    var verseLyrics = clean(verses.content);
                    var verseExpl = clean(verses.explanation);
                    var verseJson = {};
                    verseJson["text"] = verseLyrics;
                    verseJson["annotation"] = verseExpl;
                    verseJson["timestamp"] = "";

                    jsonLyrics.push(verseJson);

                    //console.log("\n%s \n -- %s", verseLyrics, verseExpl);
                }
            });
        });
        jsonSong["lyrics"] = jsonLyrics;
        //console.log(jsonSong);
        var jsonSongObj = JSON.stringify(jsonSong);
        console.log(jsonSongObj);
        return(jsonSongObj);
    
    }
};

var searchCallback = function(err, songs){
  if(err){
    console.log("Error: " + err);
  }else{
    if(songs.length > 0){
      //We have some songs
      rapgeniusClient.searchLyricsAndExplanations(songs[0].link, lyricsSearchCb);
    }
  }
};

var songName = "jay-z young jeezy real as it gets"
rapgeniusClient.searchSong(songName, searchCallback);
