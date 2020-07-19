#### Arcane Crafting:
Type: `"arcana:arcane_crafting_shaped"`  
A shaped crafting recipe, with extra objects:  
`aspects`: A list of json objects representing aspect stacks required by the recipe:
* `"aspect"`: an aspect's name (as a resource location)
* `"amount"`: an amount (int)

`requirements`: An optional (ignored??) list of resource locations representing required research.  

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

#### Alchemy
Type: `"type": "arcana:alchemy"`  
`"in"`: The input ingredient.  
`"out"`: The itemstack result.  
`research`: An optional resource location representing a required research entry.  
`aspects`: A list of json objects representing aspect stacks required by the recipe:
* `"aspect"`: an aspect's name (as a resource location)
* `"amount"`: an amount (int)

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