(ns com.nerdboy.custom-clothing.cc-mod
  (:require [com.nerdboy.custom-clothing.block.loom :as loom]
            [com.nerdboy.custom-clothing.utils :as utils])
  (:import [net.minecraft.client Minecraft]
           [net.minecraft.client.resources.model ModelResourceLocation]
           [net.minecraft.item Item]
           [net.minecraftforge.fml.common Mod$EventHandler Mod]
           [net.minecraftforge.fml.relauncher Side]
           [net.minecraftforge.fml.common.registry GameRegistry]))

(def block-atom (atom{}))

(gen-class :name ^{Mod {modid   "@modId@"
                        name    "@modName@"
                        version "@version@"}}
            com.nerdboy.custom_clothing.CCMod
           :main false
           :methods [[^{Mod$EventHandler {}} initialize
                      [net.minecraftforge.fml.common.event.FMLInitializationEvent]
                      void]])


(defn -initialize [this event]
  (swap! block-atom assoc :loom (loom/setup))
  (let [loom-block (:loom @block-atom)]
    (if (.isClient (.getSide event))
      (-> (Minecraft/getMinecraft)
          .getRenderItem
          .getItemModelMesher
          (.register (Item/getItemFromBlock loom-block)
                     0
                     (ModelResourceLocation. "@modId@:loom" "inventory"))))))