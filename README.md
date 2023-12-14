# AZ Launcher (aka Pactify Launcher) API

---

Cette API a pour but d'implémenter d'une part, les fonctionnalités offertes par le [PLSP Protocol](https://github.com/PactifyLauncherExamples/maven-repository) permettant de communiquer avec le [AZ Launcher](https://www.az-launcher.nz/). Et d'autre part les autres fonctionnalités liées au Launcher (menus GUIs).

Version de Minecraft testée : `CraftBukkit version git-Spigot-c6871e2-e1d3516 (MC: 1.9.4)`

Ce repository n'est sans doute pas complet et peut être amélioré. Libre à vous d'y contribuer !

## Installation

Téléchargez le .jar dans les releases (ou compillez-le) et ajoutez-le en dépendance de votre projet. Compillez-le avec votre plugin final.

## Utilisation

### PLSP

Pour fonctionner, vous devez nécéssairement instancier le `PactifyManager` au lancement de votre plugin.

```java
private PactifyManager pactifyManager;

@Override
public void onEnable() {
    pactifyManager = new PactifyManager(this); //Instanciation par défaut
    //Instanciation avec des ConfFlags par défauts
    //Ceux-ci seront envoyés lorsqu'un joueur rejoint.
    pactifyManager = new PactifyManager(this, Arrays.asList(PLSPConfFlag.SEE_CHUNKS, PLSPConfFlag.SMOOTH_EXPERIENCE_BAR));
}
```

Exemple de transformation

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    pactifyManager.getPlayer(event.getPlayer()).rescalePlayer(1.5f);
    pactifyManager.getPlayer(event.getPlayer()).editOpacity(0.8f);
    pactifyManager.getPlayer(event.getPlayer()).transformIntoMob(EntityType.ZOMBIE);
    //Ou
    PactifyTransformation trans = new PactifyTransformation(EntityType.ZOMBIE, 1.5f);
    trans.setOpacity(0.8f);
    pactifyManager.getPlayer(event.getPlayer()).setTransformation(trans);
    pactifyManager.getPlayer(event.getPlayer()).applyTransformation();
}
```

### NBT GUIs

Exemple d'utilisation des NBT GUIs.

```java
event.getPlayer().getInventory().setItem(4, new PactifyItem(new ItemStack(Material.QUARTZ_BLOCK))
    .addPacDisplay(new PacDisplay()
        .setSprite(Sprite.EMOJI)
        .setSpriteData("\uEEEE\uDBFF\uDF2D")
         .setChilds(new PactifyItem.PacDisplayChild()
            .setMaterial(Material.GRASS)
            .setCount(1)
            .setPactifyDisplay(new PacDisplay()
                .setSprite(Sprite.EMOJI)
                .setSpriteData("\uEEEE\uDBFF\uDEE6")
                .setScale(1.2f)
                .setZIndex(-1.0f)
            )
        )
    ).addPacMenu(new PacMenu()
        .setBackground(false)
        .setState(PactifyItem.MenuState.DISABLED)
    ).getItemStack());
```

### GUIs Transparants
*À venir ...*

## Remerciements
Merci beaucoup à [@nathan818fr](https://github.com/nathan818fr/) pour 
- son travail sur le AZ Launcher, 
- d'avoir rendu ces informations disponibles,
- ses quelques coups de main
- son autorisation pour ce repo.

---

# AZ Launcher (aka Pactify Launcher) API

This API has been made for 
- Implement the functionalities included in the [PLSP Protocol](https://github.com/PactifyLauncherExamples/maven-repository) which allow to communicate with the [AZ Launcher](https://www.az-launcher.nz/).
- Integrate other functionalities related to the Launcher such as NBT GUIs.

Tested minecraft version : `CraftBukkit version git-Spigot-c6871e2-e1d3516 (MC: 1.9.4)`.

This repository might not be complete and can be improved. Feel free to contribute! 

## Installation

Download the .jar file in the releases (or compile it) and add it as a dependency to your project. Compile it with your final plugin.

## Usage

### PLSP

You need to instantiate the `PactifyManager` class at the start of your plugin.

```java
private PactifyManager pactifyManager;

@Override
public void onEnable() {
    pactifyManager = new PactifyManager(this); //Instanciation par défaut
    //Instanciation avec des ConfFlags par défauts
    //Ceux-ci seront envoyés lorsqu'un joueur rejoint.
    pactifyManager = new PactifyManager(this, Arrays.asList(PLSPConfFlag.SEE_CHUNKS, PLSPConfFlag.SMOOTH_EXPERIENCE_BAR));
}
```

Transformation example

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    pactifyManager.getPlayer(event.getPlayer()).rescalePlayer(1.5f);
    pactifyManager.getPlayer(event.getPlayer()).editOpacity(0.8f);
    pactifyManager.getPlayer(event.getPlayer()).transformIntoMob(EntityType.ZOMBIE);
    //Ou
    PactifyTransformation trans = new PactifyTransformation(EntityType.ZOMBIE, 1.5f);
    trans.setOpacity(0.8f);
    pactifyManager.getPlayer(event.getPlayer()).setTransformation(trans);
    pactifyManager.getPlayer(event.getPlayer()).applyTransformation();
}
```

### NBT GUIs

NBT GUIs usage example.

```java
event.getPlayer().getInventory().setItem(4, new PactifyItem(new ItemStack(Material.QUARTZ_BLOCK))
    .addPacDisplay(new PacDisplay()
        .setSprite(Sprite.EMOJI)
        .setSpriteData("\uEEEE\uDBFF\uDF2D")
         .setChilds(new PactifyItem.PacDisplayChild()
            .setMaterial(Material.GRASS)
            .setCount(1)
            .setPactifyDisplay(new PacDisplay()
                .setSprite(Sprite.EMOJI)
                .setSpriteData("\uEEEE\uDBFF\uDEE6")
                .setScale(1.2f)
                .setZIndex(-1.0f)
            )
        )
    ).addPacMenu(new PacMenu()
        .setBackground(false)
        .setState(PactifyItem.MenuState.DISABLED)
    ).getItemStack());
```

### Clear GUI

*Coming soon...*

## Many thanks
Thanks to [@nathan818fr](https://github.com/nathan818fr/) for
- his work on the AZ Launcher,
- making these informations available,
- his assistance,
- his permission for this repository.

---