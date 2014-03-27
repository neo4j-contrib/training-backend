{
  "message": "Task: Create a node named Keanu Reeves",
  "tasks": [
    {
      "check": "input",
      "test": "create",
      "failMsg": "You should create a node with the CREATE command"
    },
    {
      "check": "input",
      "test": "Keanu",
      "failMsg": "You should create a node with the name \"Keanu\""
    },
    {
      "check": "output",
      "test": "Reeves",
      "failMsg": "You created a node without the last name \"Reeves!\""
    }
  ]
}



database "lesson1"
for simple intro type queries

CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})
CREATE (Keanu:Person {name:'Keanu Reeves', born:1964})
CREATE (Carrie:Person {name:'Carrie-Anne Moss', born:1967})
CREATE (Laurence:Person {name:'Laurence Fishburne', born:1961})
CREATE (Hugo:Person {name:'Hugo Weaving', born:1960})
CREATE (AndyW:Person {name:'Andy Wachowski', born:1967})
CREATE (LanaW:Person {name:'Lana Wachowski', born:1965})
CREATE (JoelS:Person {name:'Joel Silver', born:1952})
CREATE (Emil:Person {name:'Emil Eifrem', born:1974})
CREATE
  (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrix),
  (Carrie)-[:ACTED_IN {roles:['Trinity']}]->(TheMatrix),
  (Laurence)-[:ACTED_IN {roles:['Morpheus']}]->(TheMatrix),
  (Hugo)-[:ACTED_IN {roles:['Agent Smith']}]->(TheMatrix),
  (Emil)-[:ACTED_IN {roles:['Emil']}]->(TheMatrix),
  (AndyW)-[:DIRECTED]->(TheMatrix),
  (LanaW)-[:DIRECTED]->(TheMatrix),
  (JoelS)-[:PRODUCED]->(TheMatrix)

CREATE (CloudAtlas:Movie {title:'Cloud Atlas', released:2012, tagline:'Everything is connected'})
CREATE (HalleB:Person {name:'Halle Berry', born:1966})
CREATE (JimB:Person {name:'Jim Broadbent', born:1949})
CREATE (TomT:Person {name:'Tom Tykwer', born:1965})
CREATE (TomH:Person {name:'Tom Hanks', born:1956})
CREATE
  (TomH)-[:ACTED_IN {roles:['Zachry', 'Dr. Henry Goose', 'Isaac Sachs', 'Dermot Hoggins']}]->(CloudAtlas),
  (Hugo)-[:ACTED_IN {roles:['Bill Smoke', 'Haskell Moore', 'Tadeusz Kesselring', 'Nurse Noakes', 'Boardman Mephi', 'Old Georgie']}]->(CloudAtlas),
  (HalleB)-[:ACTED_IN {roles:['Luisa Rey', 'Jocasta Ayrs', 'Ovid', 'Meronym']}]->(CloudAtlas),
  (JimB)-[:ACTED_IN {roles:['Vyvyan Ayrs', 'Captain Molyneux', 'Timothy Cavendish']}]->(CloudAtlas),
  (TomT)-[:DIRECTED]->(CloudAtlas),
  (AndyW)-[:DIRECTED]->(CloudAtlas),
  (LanaW)-[:DIRECTED]->(CloudAtlas)


mystic-river = lesson1 +

CREATE (m:Movie {title: 'Mystic River',tagline:'We bury our sins here, Dave. We wash them clean.',released:2003})

tasks


{
  "message": "Run the simple queries you've just learned",
  "tasks": [
    {
      "check": "input",
      "test": "match.+return",
      "failMsg": "Your query should contain at least a MATCH and RETURN clause"
    }
  ]
}

{
  "message": "Match the paths between actors and directors in different ways",
  "tasks": [
    {
      "check": "input",
      "test": ":ACTED_IN|:DIRECTED",
      "failMsg": "Your paths should contain both, the ACTED_IN and DIRECTED relationships."
    },
    {
      "check": "input",
      "test": "return\\s+([ad]\\.name|m\\.title)",
      "failMsg": "You should return the actors name, directors name and movie title."
    }
  ]
}


{
  "message": "Assing patterns to paths",
  "tasks": [
    {
      "check": "input",
      "test": ":ACTED_IN|:DIRECTED",
      "failMsg": "Your paths should contain both, the ACTED_IN and DIRECTED relationships."
    },
    {
      "check": "input",
      "test": "p.?\\s*=",
      "failMsg": "You should assign the path expression to a path 'p' or 'p1'"
    }
  ]
}


Database: _lab_paths

CREATE (Unforgiven:Movie {title:'Unforgiven', released:1992, tagline:'It is a hell of a thing, killing a man'})
CREATE (RichardH:Person {name:'Richard Harris', born:1930})
CREATE (ClintE:Person {name:'Clint Eastwood', born:1930})
CREATE (Gene:Person {name:'Gene Hackman', born:1930})
CREATE
  (RichardH)-[:ACTED_IN {roles:['English Bob']}]->(Unforgiven),
  (ClintE)-[:ACTED_IN {roles:['Bill Munny']}]->(Unforgiven),
  (Gene)-[:ACTED_IN {roles:['Little Bill Daggett']}]->(Unforgiven),
  (ClintE)-[:DIRECTED]->(Unforgiven)
CREATE (TheReplacements:Movie {title:'The Replacements', released:2000, tagline:'Pain heals, Chicks dig scars... Glory lasts forever'})
CREATE (Brooke:Person {name:'Brooke Langton', born:1970})
CREATE (Keanu:Person {name:'Keanu Reeves', born:1964})
CREATE (Orlando:Person {name:'Orlando Jones', born:1968})
CREATE (Howard:Person {name:'Howard Deutch', born:1950})
CREATE
  (Keanu)-[:ACTED_IN {roles:['Shane Falco']}]->(TheReplacements),
  (Brooke)-[:ACTED_IN {roles:['Annabelle Farrell']}]->(TheReplacements),
  (Gene)-[:ACTED_IN {roles:['Jimmy McGinty']}]->(TheReplacements),
  (Orlando)-[:ACTED_IN {roles:['Clifford Franklin']}]->(TheReplacements),
  (Howard)-[:DIRECTED]->(TheReplacements)
CREATE (TheBirdcage:Movie {title:'The Birdcage', released:1996, tagline:'Come as you are'})
CREATE (MikeN:Person {name:'Mike Nichols', born:1931})
CREATE (Nathan:Person {name:'Nathan Lane', born:1956})
CREATE (Robin:Person {name:'Robin Williams', born:1951})
CREATE
  (Robin)-[:ACTED_IN {roles:['Armand Goldman']}]->(TheBirdcage),
  (Nathan)-[:ACTED_IN {roles:['Albert Goldman']}]->(TheBirdcage),
  (Gene)-[:ACTED_IN {roles:['Sen. Kevin Keeley']}]->(TheBirdcage),
  (MikeN)-[:DIRECTED]->(TheBirdcage)


{
  "message": "Lab: Find out which directors also acted in their movies, use what you've learned so far",
  "tasks": [
    {
      "check": "input",
      "test": ":ACTED_IN|:DIRECTED",
      "failMsg": "Your paths should contain both, the ACTED_IN and DIRECTED relationships."
    },
    {
      "check": "output",
      "test": "Clint Eastwood",
      "failMsg": "We expected someone else."
    }
  ]
}


Database: full (actually it is only half)

Lab-Session 5 busiest actors
{
  "message": "Lab: Find the 5 busiest actors in this dataset, use what you've learned",
  "tasks": [
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship"
    },
    {
      "check": "input",
      "test": "count",
      "failMsg": "You probably want to count the ocurrences"
    },
    {
      "check": "input",
      "test": "order by",
      "failMsg": "Ordering the results makes a lot of sense for top n"
    },
    {
      "check": "input",
      "test": "desc",
      "failMsg": "Remember to use the right sort order."
    },
    {
      "check": "input",
      "test": "limit 5",
      "failMsg": "You're still interested in the top 5, remember how to limit the output?"
    },
    {
      "check": "output",
      "results": "Gene Hackman",
      "failMsg": "We expected someone else."
    }
  ]
}


{
  "message": "Lab: All Movies Tom Hanks acted in.",
  "tasks": [
    {
      "check": "input",
      "test": ":Person",
      "failMsg": "You'll want to limit your nodes to ones labeled Person"
    },
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship"
    },
    {
      "check": "input",
      "test": "\\.name|name:",
      "failMsg": "You probably want to check the name property"
    },
    {
      "check": "input",
      "test": "Tom Hanks",
      "failMsg": "You wanted to look for Tom Hank's movies"
    },
    {
      "check": "output",
      "results": "Cloud Atlas",
      "failMsg": "We expected some other movie."
    }
  ]
}

{
  "message": "Lab: All Movies Keanu Reeves acted in.",
  "tasks": [
    {
      "check": "input",
      "test": ":Person",
      "failMsg": "You'll want to limit your nodes to ones labeled Person"
    },
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship"
    },
    {
      "check": "input",
      "test": "\\.name|name:",
      "failMsg": "You probably want to check the name property"
    },
    {
      "check": "input",
      "test": "Keanu Reeves",
      "failMsg": "You wanted to look for Keanu Reeves's movies"
    },
    {
      "check": "output",
      "results": "The Matrix",
      "failMsg": "We expected some otherher movie."
    }
  ]
}



{
  "message": "All the actors who acted with Tom Hanks and are older than him.",
  "tasks": [
    {
      "check": "input",
      "test": ":Person",
      "failMsg": "You'll want to limit your nodes to ones labeled Person"
    },
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship"
    },
    {
      "check": "input",
      "test": "\\.name|name:",
      "failMsg": "You probably want to check the name property"
    },
    {
      "check": "input",
      "test": "Tom Hanks",
      "failMsg": "You wanted to look for Tom Hanks's colleagues"
    },
    {
      "check": "input",
      "test": "\\.born [<>]",
      "failMsg": "Compare the born (year) property"
    },
    {
      "check": "output",
      "results": "Jim Broadbent",
      "failMsg": "We expected someone else."
    }
  ]
}

##Recommend 3 actors that Keanu Reeves should work with (but hasn’t).##

This is kind of a *friends-of-a-friend* query, only that we don't have `FRIEND` relationships here but co-acting in a movie (`ACTS_IN`). So it might be a bit verbose in the first place. There are different approaches for the recommendation. So keep in mind that the top 3 most frequently appearing people in that network seem to be good candidates.

Advanced Lab: Recommendations for Keanu Reeves

MATCH (keanu:Person)-[:ACTED_IN]->()<-[:ACTED_IN]-(colleague), 
      (co_colleague)-[:ACTED_IN]->()<-[:ACTED_IN]-(colleague) 
WHERE keanu.name = "Keanu Reeves" AND not(keanu = co_colleague)  
AND not (co_colleague)-[:ACTS_IN]->()<-[:ACTS_IN]-(keanu) 
RETURN co_colleague, count(*) 
ORDER BY count(*) DESC 
LIMIT 3

{
  "message": "Lab: Recommend 3 actors that Keanu Reeves should work with (but hasn't)",
  "tasks": [
    {
      "check": "input",
      "test": "Keanu Reeves",
      "failMsg": "You should look for Keanu Reeves"
    },
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship several times"
    },
    {
      "check": "input",
      "test": "count",
      "failMsg": "You probably want to count the ocurrences"
    },
    {
      "check": "input",
      "test": "order by",
      "failMsg": "Ordering the results makes a lot of sense for top n"
    },
    {
      "check": "input",
      "test": "desc",
      "failMsg": "Remember to use the right sort order."
    },
    {
      "check": "input",
      "test": "limit 3",
      "failMsg": "You're still interested in the top 3, remember how to limit the output?"
    },
    {
      "check": "input",
      "test": "not",
      "failMsg": "Did you remember to exclude the ones he already worked with?"
    }
  ]
}

,
{
  "check": "results",
  "results": "(Meg Ryan|Val Kilmer)",
  "failMsg": "We expected someone else."
}



{
  "message": "Lab: All Characters in the Matrix",
  "tasks": [
    {
      "check": "input",
      "test": ":Movie",
      "failMsg": "You'll want to limit your nodes to ones labeled Movie"
    },
    {
      "check": "input",
      "test": "\\.title|title:",
      "failMsg": "You probably want to check the title property"
    },
    {
      "check": "input",
      "test": "The Matrix",
      "failMsg": "You wanted to look for the movie titled 'The Matrix'"
    },
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship"
    },
    {
      "check": "input",
      "test": "\\w+:ACTED_IN",
      "failMsg": "You probably wanted to assign an identifier to your relationship"
    },
    {
      "check": "input",
      "test": "\\.roles",
      "failMsg": "You wanted to RETURN the roles property of the relationship"
    },
    {
      "check": "output",
      "results": "Neo",
      "failMsg": "We expected some other characters"
    }
  ]
}

{
  "message": "Aggregation",
  "tasks": [
    {
      "check": "input",
      "test": ":Person",
      "failMsg": "You'll want to start at nodes labeled Person"
    },
    {
      "check": "input",
      "test": ":ACTED_IN",
      "failMsg": "Your paths should use the ACTED_IN relationship"
    },
    {
      "check": "input",
      "test": "(collect|count|avg|min|max)",
      "failMsg": "You certainly wanted to use an aggregation function"
    }
  ]
}

CREATE (TheMatrix:Movie {title:'The Matrix', released:1999, tagline:'Welcome to the Real World'})
CREATE (Keanu:Person {name:'Keanu Reeves', born:1964})
CREATE (Carrie:Person {name:'Carrie-Anne Moss', born:1967})
CREATE (Laurence:Person {name:'Laurence Fishburne', born:1961})
CREATE (Hugo:Person {name:'Hugo Weaving', born:1960})
CREATE (AndyW:Person {name:'Andy Wachowski', born:1967})
CREATE (LanaW:Person {name:'Lana Wachowski', born:1965})
CREATE (JoelS:Person {name:'Joel Silver', born:1952})
CREATE
  (Keanu)-[:ACTED_IN {roles:['Neo']}]->(TheMatrix),
  (Carrie)-[:ACTED_IN {roles:['Trinity']}]->(TheMatrix),
  (Laurence)-[:ACTED_IN {roles:['Morpheus']}]->(TheMatrix),
  (Hugo)-[:ACTED_IN {roles:['Agent Smith']}]->(TheMatrix),
  (AndyW)-[:DIRECTED]->(TheMatrix),
  (LanaW)-[:DIRECTED]->(TheMatrix),
  (JoelS)-[:PRODUCED]->(TheMatrix)


CREATE (TopGun:Movie {title:'Top Gun', released:1986, tagline:'I feel the need, the need for speed.'})
CREATE (TomC:Person {name:'Tom Cruise', born:1962})
CREATE (KellyM:Person {name:'Kelly McGillis', born:1957})
CREATE (ValK:Person {name:'Val Kilmer', born:1959})
CREATE (AnthonyE:Person {name:'Anthony Edwards', born:1962})
CREATE (TomS:Person {name:'Tom Skerritt', born:1933})
CREATE (MegR:Person {name:'Meg Ryan', born:1961})
CREATE (TonyS:Person {name:'Tony Scott', born:1944})
CREATE (JimC:Person {name:'Jim Cash', born:1941})
CREATE
  (TomC)-[:ACTED_IN {roles:['Maverick']}]->(TopGun),
  (KellyM)-[:ACTED_IN {roles:['Charlie']}]->(TopGun),
  (ValK)-[:ACTED_IN {roles:['Iceman']}]->(TopGun),
  (AnthonyE)-[:ACTED_IN {roles:['Goose']}]->(TopGun),
  (TomS)-[:ACTED_IN {roles:['Viper']}]->(TopGun),
  (MegR)-[:ACTED_IN {roles:['Carole']}]->(TopGun),
  (TonyS)-[:DIRECTED]->(TopGun),
  (JimC)-[:WROTE]->(TopGun)

CREATE (JerryMaguire:Movie {title:'Jerry Maguire', released:2000, tagline:'The rest of his life begins now.'})
CREATE (ReneeZ:Person {name:'Renee Zellweger', born:1969})
CREATE (KellyP:Person {name:'Kelly Preston', born:1962})
CREATE (JerryO:Person {name:'Jerry O\\'Connell', born:1974})
CREATE (JayM:Person {name:'Jay Mohr', born:1970})
CREATE (BonnieH:Person {name:'Bonnie Hunt', born:1961})
CREATE (ReginaK:Person {name:'Regina King', born:1971})
CREATE (JonathanL:Person {name:'Jonathan Lipnicki', born:1990})
CREATE (CameronC:Person {name:'Cameron Crowe', born:1957})
CREATE (CubaG:Person {name:'Cuba Gooding Jr.', born:1968})
CREATE
  (TomC)-[:ACTED_IN {roles:['Jerry Maguire']}]->(JerryMaguire),
  (CubaG)-[:ACTED_IN {roles:['Rod Tidwell']}]->(JerryMaguire),
  (ReneeZ)-[:ACTED_IN {roles:['Dorothy Boyd']}]->(JerryMaguire),
  (KellyP)-[:ACTED_IN {roles:['Avery Bishop']}]->(JerryMaguire),
  (JerryO)-[:ACTED_IN {roles:['Frank Cushman']}]->(JerryMaguire),
  (JayM)-[:ACTED_IN {roles:['Bob Sugar']}]->(JerryMaguire),
  (BonnieH)-[:ACTED_IN {roles:['Laurel Boyd']}]->(JerryMaguire),
  (ReginaK)-[:ACTED_IN {roles:['Marcee Tidwell']}]->(JerryMaguire),
  (JonathanL)-[:ACTED_IN {roles:['Ray Boyd']}]->(JerryMaguire),
  (CameronC)-[:DIRECTED]->(JerryMaguire),
  (CameronC)-[:PRODUCED]->(JerryMaguire),
  (CameronC)-[:WROTE]->(JerryMaguire)


CREATE (TheReplacements:Movie {title:'The Replacements', released:2000, tagline:'Pain heals, Chicks dig scars... Glory lasts forever'})
CREATE (Brooke:Person {name:'Brooke Langton', born:1970})
CREATE (Gene:Person {name:'Gene Hackman', born:1930})
CREATE (Orlando:Person {name:'Orlando Jones', born:1968})
CREATE (Howard:Person {name:'Howard Deutch', born:1950})
CREATE
  (Keanu)-[:ACTED_IN {roles:['Shane Falco']}]->(TheReplacements),
  (Brooke)-[:ACTED_IN {roles:['Annabelle Farrell']}]->(TheReplacements),
  (Gene)-[:ACTED_IN {roles:['Jimmy McGinty']}]->(TheReplacements),
  (Orlando)-[:ACTED_IN {roles:['Clifford Franklin']}]->(TheReplacements),
  (Howard)-[:DIRECTED]->(TheReplacements)

CREATE (TheBirdcage:Movie {title:'The Birdcage', released:1996, tagline:'Come as you are'})
CREATE (MikeN:Person {name:'Mike Nichols', born:1931})
CREATE (Nathan:Person {name:'Nathan Lane', born:1956})
CREATE (Robin:Person {name:'Robin Williams', born:1951})
CREATE
  (Robin)-[:ACTED_IN {roles:['Armand Goldman']}]->(TheBirdcage),
  (Nathan)-[:ACTED_IN {roles:['Albert Goldman']}]->(TheBirdcage),
  (Gene)-[:ACTED_IN {roles:['Sen. Kevin Keeley']}]->(TheBirdcage),
  (MikeN)-[:DIRECTED]->(TheBirdcage)

CREATE (Unforgiven:Movie {title:'Unforgiven', released:1992, tagline:'it\\'s a hell of a thing, killing a man'})
CREATE (RichardH:Person {name:'Richard Harris', born:1930})
CREATE (ClintE:Person {name:'Clint Eastwood', born:1930})
CREATE
  (RichardH)-[:ACTED_IN {roles:['English Bob']}]->(Unforgiven),
  (ClintE)-[:ACTED_IN {roles:['Bill Munny']}]->(Unforgiven),
  (Gene)-[:ACTED_IN {roles:['Little Bill Daggett']}]->(Unforgiven),
  (ClintE)-[:DIRECTED]->(Unforgiven)

CREATE (CloudAtlas:Movie {title:'Cloud Atlas', released:2012, tagline:'Everything is connected'})
CREATE (TomH:Person {name:'Tom Hanks', born:1956})
CREATE (HalleB:Person {name:'Halle Berry', born:1966})
CREATE (JimB:Person {name:'Jim Broadbent', born:1949})
CREATE (TomT:Person {name:'Tom Tykwer', born:1965})
CREATE
  (TomH)-[:ACTED_IN {roles:['Zachry', 'Dr. Henry Goose', 'Isaac Sachs', 'Dermot Hoggins']}]->(CloudAtlas),
  (Hugo)-[:ACTED_IN {roles:['Bill Smoke', 'Haskell Moore', 'Tadeusz Kesselring', 'Nurse Noakes', 'Boardman Mephi', 'Old Georgie']}]->(CloudAtlas),
  (HalleB)-[:ACTED_IN {roles:['Luisa Rey', 'Jocasta Ayrs', 'Ovid', 'Meronym']}]->(CloudAtlas),
  (JimB)-[:ACTED_IN {roles:['Vyvyan Ayrs', 'Captain Molyneux', 'Timothy Cavendish']}]->(CloudAtlas),
  (TomT)-[:DIRECTED]->(CloudAtlas),
  (AndyW)-[:DIRECTED]->(CloudAtlas),
  (LanaW)-[:DIRECTED]->(CloudAtlas)

CREATE (AFewGoodMen:Movie {title:'A Few Good Men', released:1992, tagline:'In the heart of the nation\\'s capital, in a courthouse of the U.S. government, one man will stop at nothing to keep his honor, and one will stop at nothing to find the truth.'})
CREATE (JackN:Person {name:'Jack Nicholson', born:1937})
CREATE (DemiM:Person {name:'Demi Moore', born:1962})
CREATE (KevinB:Person {name:'Kevin Bacon', born:1958})
CREATE (KieferS:Person {name:'Kiefer Sutherland', born:1966})
CREATE (NoahW:Person {name:'Noah Wyle', born:1971})
CREATE (KevinP:Person {name:'Kevin Pollak', born:1957})
CREATE (JTW:Person {name:'J.T. Walsh', born:1943})
CREATE (JamesM:Person {name:'James Marshall', born:1967})
CREATE (ChristopherG:Person {name:'Christopher Guest', born:1948})
CREATE (RobR:Person {name:'Rob Reiner', born:1947})
CREATE (AaronS:Person {name:'Aaron Sorkin', born:1961})
CREATE
  (TomC)-[:ACTED_IN {roles:['Lt. Daniel Kaffee']}]->(AFewGoodMen),
  (JackN)-[:ACTED_IN {roles:['Col. Nathan R. Jessup']}]->(AFewGoodMen),
  (DemiM)-[:ACTED_IN {roles:['Lt. Cdr. JoAnne Galloway']}]->(AFewGoodMen),
  (KevinB)-[:ACTED_IN {roles:['Capt. Jack Ross']}]->(AFewGoodMen),
  (KieferS)-[:ACTED_IN {roles:['Lt. Jonathan Kendrick']}]->(AFewGoodMen),
  (NoahW)-[:ACTED_IN {roles:['Cpl. Jeffrey Barnes']}]->(AFewGoodMen),
  (CubaG)-[:ACTED_IN {roles:['Cpl. Carl Hammaker']}]->(AFewGoodMen),
  (KevinP)-[:ACTED_IN {roles:['Lt. Sam Weinberg']}]->(AFewGoodMen),
  (JTW)-[:ACTED_IN {roles:['Lt. Col. Matthew Andrew Markinson']}]->(AFewGoodMen),
  (JamesM)-[:ACTED_IN {roles:['Pfc. Louden Downey']}]->(AFewGoodMen),
  (ChristopherG)-[:ACTED_IN {roles:['Dr. Stone']}]->(AFewGoodMen),
  (AaronS)-[:ACTED_IN {roles:['Man in Bar']}]->(AFewGoodMen),
  (RobR)-[:DIRECTED]->(AFewGoodMen),
  (AaronS)-[:WROTE]->(AFewGoodMen)

CREATE (Apollo13:Movie {title:'Apollo 13', released:1995, tagline:'Houston, we have a problem.'})
CREATE (EdH:Person {name:'Ed Harris', born:1950})
CREATE (BillPax:Person {name:'Bill Paxton', born:1955})
CREATE (RonH:Person {name:'Ron Howard', born:1954})
CREATE (GaryS:Person {name:'Gary Sinise', born:1955})
CREATE
  (TomH)-[:ACTED_IN {roles:['Jim Lovell']}]->(Apollo13),
  (KevinB)-[:ACTED_IN {roles:['Jack Swigert']}]->(Apollo13),
  (EdH)-[:ACTED_IN {roles:['Gene Kranz']}]->(Apollo13),
  (BillPax)-[:ACTED_IN {roles:['Fred Haise']}]->(Apollo13),
  (GaryS)-[:ACTED_IN {roles:['Ken Mattingly']}]->(Apollo13),
  (RonH)-[:DIRECTED]->(Apollo13)

