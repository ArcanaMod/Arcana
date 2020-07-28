# How to Use the Research System

## Organisation

The research system consists of the following parts:
 - Books: an individual Thaumonomicon or Crimson Rites.
 - Categories: a research tab, contained within a specific book.
 - Entries: an individual node in the research tree, contained within a category, with
  a number of parent entries.
 - Entry sections: a contiguous part of an entry, such as a continuous section of text, 
  or a recipe, gated by research progress.
 - Requirements: some predicate that must be valid in order to continue a research
  entry, or some resource that must be used up to continue, such as an amount of an
  item, or having done some particular piece of fieldwork.
 - Puzzles: tasks used by requirements with data attached to the player, usually
  completed through the Research Table.

The research system separates the task of representing these parts from displaying them
 to the player. Books, categories, and entries have only one implementation for their
 rendering (`ResearchBookGUI` and `ResearchEntryGUI`), although the textures used can
 be customised. Entry sections and requirements are displayed by `EntrySectionRenderer
 `s and `RequirementRenderer`s, registered on the client-side. Currently, only
 Arcana can register entry sections, but any mod can register custom Requirements.

## Research JSONs

The research system pulls data from .json files. Any .json files found in the
 `assets/<modid>/research` directory will be loaded as research JSONs during the main
 initialisation stage of the mod. A reload can also be forced by running `/arcana-research reload`
 during the game's runtime.

A research JSON can contain up to four root entries: an array named `books`, an array
 named `categories`, an array named `entries`, and an array named `puzzles`. All of
 them are optional, and root entries with other names are ignored.

All `books` arrays are read first, then `categories`, then `entries`, and lastly
 `puzzles`. Books defined in any file can be referenced in a category defined in
 another, and an entry defined in any file can reference any category. You're free to
 use a separate file define books and categories, then use different files for each
 category, for example. You don't need to, though.

#### Books

Every element of the `books` array is loaded as a research book, and should be a JSON
 object with the following keys:
 - `key`: a unique namespaced ID (resource location). The domain of the key should be your
  modid. This is *not* validated, but should be done to enforce uniqueness; and its used
  in GUIs to fetch the correct textures.
 - `prefix`: a string used by rendering code to get the correct textures for your book.
  This will be referenced in the GUIs section of this guide.

#### Categories

Every element of the `categories` array is loaded as a research category, and should be a
 JSON object with the following keys:
 - `key`: a unique namespaced ID (resource location). The domain of the key should be your
  modid.
 - `in`: the key of the research book that this category is in.
 - `icon`: a file to be used as the category's icon. This is a resource location, and
  is automatically part of the `textures/` directory.
 - `name`: a translation key for the category's name, displayed when hovering over its
  icon.
 - `bg`: a file to be used as the category's background. This is a resource location, and
  is automatically part of the `textures/` directory. This image should be about
  512x512, although larger images may become supported (and recommended!) in the future.

#### Entries

Every element of the `entries` array is loaded as a research entry, and should be a JSON
 object with the following keys:
 - `key`: a unique namespaced ID (resource location). The domain of the key should be your
  modid.
 - `name`: a translation key to be used as the entry's title.
 - `desc`: a translation key to be used as the entry's subtitle/description.
 - `icons`: an array of item IDs, with each item used as an icon for the entry. (Images
  may be supported in the future.)
 - `category`: the key of the research category this entry is in.
 - (Optionally) `parents`: an array of IDs referring to this entry's parents.
 - (Optionally) `meta`: an array of strings, with miscellaneous functionality described
  below.
 - `x` and `y`: the location of this entry in the research book UI.
 - `sections`: an array of JSON objects that describe research entry sections.

##### Meta

Meta tags are case sensitive.

 - `"hidden"`: this entry won't be displayed whatsoever unless it has any progress.
 - `"root"`: this entry will be available to do even with no progress if it has no
  parents.
 - `"no_base"`: this entry won't display any base below it.
 - base shapes; `"round_base"`, `"square_base"`, `"hexagon_base"`, `"spiky_base"`:
  changes the texture used for this entry's base. `"square_base"` is default.
 - base colours; `"purple_base"`, `"yellow_base"`: changes the colour used for this
  entry's base. This is white by default, and there isn't a corresponding meta tag for
  white.
 - `"reverse"`: display arrows leading into this entry on the other side.

##### Sections

Every element of a `sections` array is loaded as an entry section, and should be a JSON
 object with the following keys:
 - `type`: the type of section. Currently, valid types are `string`, `guessowork`, and
  `recipe`. Registry of custom section types may become available in the future. 
 - `content`: the content of this section. The format used for this is based on the
  type. String sections use a translation key, and simply display that text. `\n` may
  be used in these translations. Guesswork sections are WIP. Recipe sections use the ID
  of a recipe JSON. (Check addendum 1.)
 - (Optionally) `requirements`: an array of strings that will be loaded as requirements
  added to this section.

#### Requirements
A requirement string is in the following format: `amount*Item ID{arga}` OR `amount*domain::name{args}`.
 The first format refers to item requirements, and the second refers to
 requirements that have been registered by a mod. If `amount` is 1, `amount*` can be
 omitted. If `args` is empty, `{args}` can be omitted. Arcana registers `xp`,`guesswork`,
 and `fieldwork` requirements (although the latter two are subject to change soon).

Here're some examples: `minecraft:sponge`, `3*minecraft:stick`, `88*arcana::xp`,
 `arcana::guesswork{0}`.

NBT tags may be added in a future update to item requirements.

###### Addendum 1

1.12 only supports crafting recipes in JSON files, so cooking
 recipes instead currently use their own format. This will be
 changed with 1.14.

Cooking recipes are in the format `__furnace__:ingredient.result`, where `ingredient` is
 the ID of the ingredient item (with the colon replaced by a dash), and `result` is the
 ID of the result item (with the colon replaced by a dash). It's not possible to
 specify multiple inputs or outputs, and that is not planned to be added, as this is
 a temporary format.
