``"type": "arcana:arcane_crafting_shaped"``<br>
Contains basic crafting syntax with extra objects:<br>
``aspects`` list of aspects that recipe uses, comprise:
* ``"aspect"`` aspect ResourceLocation
* ``"amount"`` amount of aspect (int)

``requirements`` list of required research<br>

Example:
```json
{
    "type": "arcana:arcane_crafting_shaped",
    "pattern": [
      "sss",
      "scs",
      "sss"
    ],
    "key": {
      "s": {
        "item": "minecraft:stone"
      },
      "c": {
        "tag": "arcana:crystals"
      }
    },
    "aspects": [
      {
        "aspect": "arcana:air",
        "amount": 4
      }
    ],
    "requirements": [
        "arcana:research_intro"
    ],
    "result": {
      "item": "arcana:arcane_stone",
      "count": 8
    }
  }
```
``"type": "arcana:alchemy"``<br>
Alchemy syntax:<br>
``"in"`` input items<br>
``"out"`` output items<br>
``aspects`` list of aspects that recipe uses, comprise:
* ``"aspect"`` aspect name
* ``"amount"`` amount of aspect (int)

Example:
```json
{
  "type": "arcana:alchemy",
  "in": {
    "tag": "forge:ingots/iron"
  },
  "out": {
    "item": "arcana:arcanium_ingot"
  },
  "aspects": [
    {
      "aspect": "mana",
      "amount": 6
    }
  ]
}
```