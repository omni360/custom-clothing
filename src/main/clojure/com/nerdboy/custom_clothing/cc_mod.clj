(ns com.nerdboy.custom-clothing.cc-mod
  (:import [net.minecraft.block Block]
           [net.minecraftforge.fml.common Mod$EventHandler Mod]
           [net.minecraftforge.fml.common.event.FMLInitializationEvent]))


(gen-class :name ^{Mod {modid "@modId@"
                        name "@modName@"
                        version "@version@"}}
  com.nerdboy.custom_clothing.CCMod
  :main false
  :methods [[^{Mod$EventHandler {}}
             initialize
             [net.minecraftforge.fml.common.event.FMLInitializationEvent]
             void]])


(defn can-burn? [^Block block]
  (.getCanBurn (.getMaterial block)))

(defn get-block-game [^Block block]
  (.getLocalizedName block))

(defn -initialize [this event]
  (prn "Hello Minecraft from clojure!")
  (prn (take 10
         (map get-block-game
           (filter can-burn?
             (map #(Block/getBlockById %) (range)))))))



