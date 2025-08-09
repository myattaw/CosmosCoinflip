# CosmosCoinflip

**CosmosCoinflip** is a fun and competitive Minecraft plugin that lets players wager money in head-to-head coin flips. Players choose a color, place a bet, and see who comes out on top — with configurable messages, GUI layouts, and broadcast alerts.

---

## ✨ Features
- **Custom Win/Loss Messages** — Fully configurable messages for wins, losses, and coin flip creation.
- **Broadcast Big Wins** — Announce coin flips over a set amount to all players.
- **Interactive GUI** — Players can browse available matches in a clean, themed interface.
- **Dynamic Placeholders** — Automatically insert player names, wager amounts, and chosen colors.

---

## ⚙ Configuration

The plugin comes with a `config.yml` file allowing you to customize:
- **Messages**
  - `win-msg`: Message shown when a player wins.
  - `loss-msg`: Message shown when a player loses.
  - `remove-msg`: Message shown when a coin flip is created or removed.
  - `broadcast`: Minimum amount for a public broadcast and the broadcast format.
- **GUIs**
  - `main-gui`: Title, item names, and lore displayed for active coin flips.

Example configuration:
```yaml
settings:
  win-msg: "&c&lFACTIONS &8&l▶ &7You have won &a$%amount%"
  loss-msg: "&c&lFACTIONS &8&l▶ &7You have lost &c$%amount%"
  remove-msg: "&c&lFACTIONS &8&l▶ &7Successfully created coin flip! &c/coinflip remove &7to remove coin flip."
  broadcast:
    minimum: 25000
    message: "&4&lCOINFLIP &8&l▶ &c%winner% &7has just beat &c%loser% &7in a &a$%amount% &7coin flip!"

guis:
  main-gui:
    title: "Coin Flip Matches"
    item-name: "%color%&l%player%"
    item-lore:
      - " "
      - "&7&lWager"
      - "&a$%amount%"
      - " "
      - "&7&lColor Chosen"
      - "&7%color_name%"
      - " "
      - "&7&lWin/Loss Ratio"
      - "&7(%ratio%&7)"
```

---

## 📜 Commands
- `/coinflip` — Open the coin flip GUI.
- `/coinflip create <amount> <color>` — Create a new coin flip.
- `/coinflip remove` — Remove your active coin flip.

---

## 📦 Installation
1. Download the **CosmosCoinflip** jar file.
2. Place it into your server’s `plugins` folder.
3. Restart or reload your server.
4. Edit the `config.yml` to your liking and reload again.

---

## 🖌 Config Placeholders
These placeholders can be used in messages and GUIs:
- `%amount%` — The wagered amount.
- `%winner%` — Name of the winning player.
- `%loser%` — Name of the losing player.
- `%player%` — Player’s name.
- `%color%` — Color code for the chosen color.
- `%color_name%` — Name of the chosen color.
- `%ratio%` — Player’s win/loss ratio.
