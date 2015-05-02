(ns com.nerdboy.custom-clothing.block.loom
  (:require [com.nerdboy.custom-clothing.utils :as utils])
  (:import [net.minecraft.block Block BlockDirectional]
           [net.minecraft.block.material Material]
           [net.minecraft.block.state BlockState IBlockState]
           [net.minecraft.block.properties IProperty]
           [net.minecraft.creativetab CreativeTabs]
           [net.minecraft.util BlockPos EnumFacing]
           [net.minecraft.world World]
           [net.minecraftforge.fml.common.registry GameRegistry]
           [net.minecraft.entity EntityLivingBase]))

(def property-direction BlockDirectional/FACING)
(def loom-name "loom")

(gen-class
  :name com.nerdboy.custom_clothing.block.Loom
  :extends net.minecraft.block.BlockDirectional
  :main false
  :init init
  :constructors {[] [net.minecraft.block.material.Material]}
  :exposes-methods {canPlaceBlockAt parentCanPlaceBlockAt
                    getDefaultState parentGetDefaultState})

(defn -init []
  [[Material/wood] []])

(defn -canPlaceBlockAt [^Block this ^World worldIn ^BlockPos pos]
  (and (> (.getHeight worldIn) (.getY pos))
       (World/doesBlockHaveSolidTopSurface worldIn (.down pos))
       (.parentCanPlaceBlockAt this worldIn pos)))

(defn -createBlockState [^Block this]
  (BlockState. this
               (into-array IProperty [property-direction])))

(defn -getDefaultState [^Block this]
  (-> this
      .parentGetDefaultState
      (.withProperty property-direction EnumFacing/NORTH)))

(defn -getMetaFromState [^Block this ^IBlockState state]
  (cond-> (.getHorizontalIndex ^EnumFacing (.getValue state property-direction))))

(defn -getStateFromMeta [^Block this meta]
  (-> this
      .getDefaultState
      (.withProperty property-direction (EnumFacing/getHorizontal (mod meta 4)))))

(defn -isFullCube [this]
  false)

(defn -isOpaqueCube [this]
  false)

(defn -onBlockPlaced [^Block this world pos facing hitX hitY hitZ meta ^EntityLivingBase placer]
  (-> this
      .getDefaultState
      (.withProperty property-direction (-> placer
                                            .getHorizontalFacing
                                            .getOpposite))))
(defn setup []
  (let [loom (com.nerdboy.custom_clothing.block.Loom.)]
    (GameRegistry/registerBlock loom loom-name)
    (.setUnlocalizedName loom (utils/format-name loom-name))
    (.setCreativeTab loom CreativeTabs/tabBlock)
    loom))