(ns com.nerdboy.custom-clothing.cc-mod
  (:require [com.nerdboy.custom-clothing.block.loom :as loom])
  (:import [net.minecraft.client Minecraft]
           [net.minecraft.client.resources.model ModelResourceLocation]
           [net.minecraft.item Item ItemStack]
           [net.minecraftforge.fml.common Mod$EventHandler Mod]
           [net.minecraftforge.fml.common.registry GameRegistry]
           [net.minecraft.util ResourceLocation]))

(def block-atom (atom{}))

(gen-class :name ^{Mod {modid   "@modId@"
                        name    "@modName@"
                        version "@version@"}}
            com.nerdboy.custom_clothing.CCMod
           :main false
           :methods [[^{Mod$EventHandler {}} initialize
                      [net.minecraftforge.fml.common.event.FMLInitializationEvent]
                      void]])
(defn get-item [name]
  (.getObject (Item/itemRegistry)
              (ResourceLocation. name)))

(defn -initialize [this event]
  (let [loom-block (loom/setup)]
    (GameRegistry/addRecipe (ItemStack. loom-block 1)
                            (into-array Object
                                        ["tSt"
                                         "SSS"
                                         "S S"
                                         \t (get-item "string")
                                         \S (get-item "stick")]))
    (if (.isClient (.getSide event))
      (-> (Minecraft/getMinecraft)
          .getRenderItem
          .getItemModelMesher
          (.register (Item/getItemFromBlock loom-block)
                     0
                     (ModelResourceLocation. "@modId@:loom" "inventory"))))))