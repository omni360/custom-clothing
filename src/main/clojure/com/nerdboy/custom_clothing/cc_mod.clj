(ns com.nerdboy.custom-clothing.cc-mod
  (:require [com.nerdboy.custom-clothing.block.tanning-rack :as tr])
  (:import [net.minecraftforge.fml.common Mod$EventHandler Mod]))

(gen-class :name ^{Mod {modid   "@modId@"
                        name    "@modName@"
                        version "@version@"}}
com.nerdboy.custom_clothing.CCMod
           :main false
           :methods [[^{Mod$EventHandler {}}
initialize
                      [net.minecraftforge.fml.common.event.FMLInitializationEvent]
                      void]])

(defn -initialize [this event]
  (tr/setup))


