package com.cricketcraft.ftbisland;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

public class IslandCreator {
    public static HashMap<String, IslandPos> islandLocations = new HashMap<String, IslandPos>();
    private static final Item chickenStick = GameRegistry.findItem("excompressum", "chickenStick");
    public final String playerName;
    public final IslandPos pos;

    public IslandCreator() {
        playerName = null;
        pos = null;
    }

    public IslandCreator(String playerName, IslandPos pos) {
        this.playerName = playerName;
        this.pos = pos;
    }

    public static boolean spawnIslandAt(World world, int x, int y, int z, String playerName, EntityPlayer player) {
        reloadIslands();
        if (!islandLocations.containsKey(playerName)) {
            if (FTBIslands.islandType.equalsIgnoreCase("tree")) {
                world.setBlock(x, y, z, Blocks.grass);
                for (int c = -3; c < 2; c++) {
                    for (int d = -3; d < 2; d++) {
                        for (int e = 3; e < 5; e++) {
                            world.setBlock(x + (c) + 1, y + e, d + (z) + 1, Blocks.leaves);
                        }
                    }
                }
                for (int c = -2; c < 1; c++) {
                    for (int d = -2; d < 1; d++) {
                        world.setBlock(x + (c) + 1, y + 5, d + (z) + 1, Blocks.leaves);
                    }
                }

                world.setBlock(x, y + 6, z, Blocks.leaves);
                world.setBlock(x + 1, y + 6, z, Blocks.leaves);
                world.setBlock(x, y + 6, z + 1, Blocks.leaves);
                world.setBlock(x - 1, y + 6, z, Blocks.leaves);
                world.setBlock(x, y + 6, z - 1, Blocks.leaves);
                world.setBlockToAir(x + 2, y + 4, z + 2);

                for (int c = 0; c < 5; c++) {
                    world.setBlock(x, y + c + 1, z, Blocks.log);
                }
            } else if (FTBIslands.islandType.equalsIgnoreCase("grass")) {
                world.setBlock(x, y, z, Blocks.grass);
                world.setBlock(x, y + 1, z, Blocks.standing_sign, 6, 3);
                ((TileEntitySign) world.getTileEntity(x, y + 1, z)).signText[0] = "You get it yet?";
            } else if (FTBIslands.islandType.equalsIgnoreCase("GoG")) {
                // This is similar to how Botania itself generates an island in GoG. This is being done to avoid a soft dependency.
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 3; k++) {
                            world.setBlock(x - 1 + i, y - j, z - 1 + k, j == 0 ? Blocks.grass : Blocks.dirt);
                        }
                    }
                }
                world.setBlock(x - 1, y - 1, z, Blocks.flowing_water);
                int[][] roots = new int[][]{
                        {-1, -2, -1},
                        {-1, -4, -2},
                        {-2, -3, -1},
                        {-2, -3, -2},
                        {1, -3, -1},
                        {1, -4, -1},
                        {2, -4, -1},
                        {2, -4, 0},
                        {3, -5, 0},
                        {0, -2, 1},
                        {0, -3, 2},
                        {0, -4, 3},
                        {1, -4, 3},
                        {1, -5, 2},
                        {1, -2, 0},
                };
                if (Loader.isModLoaded("Botania")) {
                    world.setBlock(x + 1, y + 3, z + 1, GameRegistry.findBlock("Botania", "manaFlame"));
                    world.setBlock(x, y - 3, z, Blocks.bedrock);
                    for (int[] pos : roots) {
                        world.setBlock(x + pos[0], y + pos[1], z + pos[2], GameRegistry.findBlock("Botania", "root"));
                    }
                } else {
                    for (int[] pos : roots) {
                        world.setBlock(x + pos[0], y + pos[1], z + pos[2], Blocks.log, 12, 3);
                    }
                }
            } else {
                for (int c = 0; c < 3; c++) {
                    for (int d = 0; d < 3; d++) {
                        world.setBlock(x + c, y, z + d, Blocks.dirt);
                    }
                }

                world.setBlock(x + 2, y + 1, z + 1, Blocks.chest);
                world.getBlock(x + 2, y + 1, z + 1).rotateBlock(world, x + 2, y + 1, z + 1, ForgeDirection.WEST);
                TileEntityChest chest = (TileEntityChest) world.getTileEntity(x + 2, y + 1, z + 1);

                chest.setInventorySlotContents(0, new ItemStack(Blocks.flowing_water, 1));
                chest.setInventorySlotContents(1, new ItemStack(Blocks.flowing_lava, 1));
                chest.setInventorySlotContents(2, new ItemStack(Items.dye, 64, 15));
                chest.setInventorySlotContents(3, new ItemStack(Items.dye, 64, 15));
                chest.setInventorySlotContents(4, new ItemStack(Items.apple, 16));
                chest.setInventorySlotContents(5, new ItemStack(Blocks.sapling, 8, 0));
                chest.setInventorySlotContents(6, new ItemStack(Items.spawn_egg, 2, 90));
                chest.setInventorySlotContents(7, new ItemStack(Items.spawn_egg, 2, 91));
                chest.setInventorySlotContents(8, new ItemStack(Items.spawn_egg, 2, 92));
                chest.setInventorySlotContents(9, new ItemStack(Items.spawn_egg, 2, 93));
                if (chickenStick != null) {
                    chest.setInventorySlotContents(10, new ItemStack(chickenStick, 1));
                }
            }

            if (islandLocations.size() != 0) {
                islandLocations.put(playerName, FTBIslands.islandLoc.get(islandLocations.size() + 1));
            } else {
                islandLocations.put(playerName, FTBIslands.islandLoc.get(1));
            }

            islandLocations.put(playerName, new IslandPos(x, y, z));
            try {
                FTBIslands.saveIslands(islandLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (player != null) {
                player.addChatMessage(new ChatComponentText(String.format("Created island named %s at %d, %d, %d", playerName, x, y, z)));
            } else {
                FTBIslands.logger.info(String.format("Created island named %s at %d %d %d", playerName, x, y, z));
            }
            return true;
        } else {
            return false;
        }
    }

    protected static void reloadIslands() {
        try {
            islandLocations = FTBIslands.getIslands();
        } catch (EOFException e) {
            // silent catch
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void save() {
        try {
            FTBIslands.saveIslands(islandLocations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class IslandPos implements Serializable {
        private int x;
        private int y;
        private int z;

        public IslandPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }
}
