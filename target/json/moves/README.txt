the stuff in this folder represent moves, both the player's and the enemy's
the way we'll represent these textually aren't set yet, but they should have stuff like a target, and an intent
the intent is the hard bit to represent, i'm thinking have a separate folder of "moveIntents" which can store these intents
the intents should probably be class files, with constructors taking arguments based on the move being used

here's an example of this architecture:
Minotaur selects its move, attack.
a new ActionContainer is made to hold whatever intent is specified in the attack file, as well as the messages to be displayed to the user when it is executed.
the container is passed the string for the minotaur's attack description for its message.
the attack file is consulted.
    the attack file says the target is "player"
    the attack file says its intent is "dealDamage"
    the attack file says the amount of damage to be passed to dealDamage is the minotaur's current attack stat
the battle supervisor sets up a dealDamage action targeting the player.
    the dealDamage constructor is passed the minotaur's current attack stat, and the player as its target
    the dealDamage constructor determines however much damage will be passed through to the player
        this is influenced by things such as the player's defense from their shield, if they have a chance to dodge from some special item, if they've used an ability to negate all damage, etc.
    this new value is set as the final damage to be passed into the player's HP
    the container's message is passed a string like "Oof, that hurt! You've taken 23 damage!" or "Thanks to the Boots of Hermes, you nimbly evade the attack! You take no damage!" or "The attack bounces harmlessly off of your stalwart shield! You take no damage!", depending on what happened
the battle supervisor waits until it is time for the minotaur's action to occur (ie. if the player goes first, the player's ActionContainer is executed before the minotaur's)
when it is time for the action to occur, the ActionContainer's .execute() function is called