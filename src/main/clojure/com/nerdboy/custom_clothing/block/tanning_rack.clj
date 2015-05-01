(ns com.nerdboy.custom-clothing.block.tanning-rack
  (:require [com.nerdboy.custom-clothing.utils :as utils])
  (:import [net.minecraft.block Block BlockDirectional]
           [net.minecraft.block.material Material]
           [net.minecraft.block.state BlockState IBlockState]
           [net.minecraft.block.properties IProperty PropertyBool]
           [net.minecraft.creativetab CreativeTabs]
           [net.minecraft.util BlockPos EnumFacing]
           [net.minecraft.world World]
           [net.minecraftforge.fml.common.registry GameRegistry]
           [net.minecraft.entity EntityLivingBase]))

(def property-top (PropertyBool/create "top"))
(def property-direction BlockDirectional/FACING)

(gen-class
  :name com.nerdboy.custom_clothing.block.TanningRack
  :extends net.minecraft.block.BlockDirectional
  :main false
  :init init
  :constructors {[] [net.minecraft.block.material.Material]}
  :exposes-methods {canPlaceBlockAt parentCanPlaceBlockAt
                    getDefaultState parentGetDefaultState})

(defn -init []
  [[Material/wood] []])

(defn -canPlaceBlockAt [^Block this ^World worldIn ^BlockPos pos]
  (and (> (.getHeight worldIn) (.getY (.up pos)))
       (World/doesBlockHaveSolidTopSurface worldIn (.down pos))
       (.parentCanPlaceBlockAt this worldIn pos)
       (.parentCanPlaceBlockAt this worldIn (.up pos))))

(defn -createBlockState [^Block this]
  (BlockState. this
               (into-array IProperty [property-top property-direction])))

(defn -getDefaultState [^Block this]
  (-> this
      .parentGetDefaultState
      (.withProperty property-direction EnumFacing/NORTH)
      (.withProperty property-top false)))

(defn -getMetaFromState [^Block this ^IBlockState state]
  (cond-> (.getHorizontalIndex ^EnumFacing (.getValue state property-direction))
          (.getValue state property-top) (+ 5)))

(defn -getStateFromMeta [^Block this meta]
  (-> this
      .getDefaultState
      (.withProperty property-direction (EnumFacing/getHorizontal (mod meta 4)))
      (.withProperty property-top (= 1 (mod meta 2)))))

(defn -onBlockPlaced [^Block this world pos facing hitX hitY hitZ meta ^EntityLivingBase placer]
  (-> this
      .getDefaultState
      (.withProperty property-direction (-> placer
                                            .getHorizontalFacing
                                            .getOpposite))
      (.withProperty property-top false)))

(defn -onBlockAdded [^Block this ^World worldIn ^BlockPos pos ^IBlockState state]
  (if-not (.getValue state property-top)
    (.setBlockState worldIn (.up pos)
                    (.withProperty state property-top true))))


(defn setup []
  (let [tanning-rack-name "tanning-rack"
        tanning-rack (com.nerdboy.custom_clothing.block.TanningRack.)]
    (GameRegistry/registerBlock tanning-rack tanning-rack-name)
    (.setUnlocalizedName tanning-rack (utils/format-name tanning-rack-name))
    (.setCreativeTab tanning-rack CreativeTabs/tabBlock)
    (utils/print-methods tanning-rack)
    tanning-rack))