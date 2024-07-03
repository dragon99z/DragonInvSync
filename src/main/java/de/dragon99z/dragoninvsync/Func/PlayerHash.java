package de.dragon99z.dragoninvsync.Func;

import com.google.common.collect.Iterables;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * This class provides static methods to convert player inventories, item stacks, and advancements to and from Base64 strings.
 * It includes methods to serialize and deserialize inventories, item stacks, and advancements using Base64 encoding.
 * The class also provides methods to convert NBT elements to and from strings.
 */
public class PlayerHash {

    private static final String AIR_CONSTANT = "{air}";

    /**
     * Converts a player's inventory to Base64 strings.
     *
     * @param playerInventory the player's inventory to convert
     * @return an array of two Base64 strings representing the player's inventory content and armor
     * @throws IllegalStateException if unable to save item stacks
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {

        String content = toBase64((ServerPlayerEntity) playerInventory.player,playerInventory);
        ItemStack[] itemStacks = new ItemStack[playerInventory.armor.size()];
        for(int i=0;i < playerInventory.armor.size(); i++){
            itemStacks[i] = playerInventory.getArmorStack(i);
        }
        String armor = itemStackArrayToBase64((ServerPlayerEntity) playerInventory.player,itemStacks);

        return new String[] { content, armor };
    }

    /**
     * Converts an array of ItemStacks to a Base64-encoded string.
     *
     * @param p the player entity associated with the ItemStack array
     * @param items the array of ItemStacks to convert
     * @return a Base64-encoded string representing the ItemStack array
     * @throws IllegalStateException if unable to save the ItemStack array
     */
    public static String itemStackArrayToBase64(ServerPlayerEntity p,ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);
            MinecraftServer server = p.server;
            RegistryWrapper.WrapperLookup registries = server.getRegistryManager();

            dataOutput.writeInt(items.length);

            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    var nbt = NbtFunc.getNbtFromItemStack(stack, registries);
                    String nbtString = nbt.toString();
                    dataOutput.writeUTF(nbtString);
                }else{
                    dataOutput.writeUTF("{air}");
                }
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks. Error: " + e.getMessage(), e);
        }
    }

    /**
     * Converts the given player's inventory to a Base64-encoded string.
     *
     * @param p the player entity associated with the inventory
     * @param inventory the inventory to convert to Base64
     * @return a Base64-encoded string representing the player's inventory
     * @throws IllegalStateException if unable to save the item stacks
     */
    public static String toBase64(ServerPlayerEntity p,Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);
            MinecraftServer server = p.server;
            RegistryWrapper.WrapperLookup registries = server.getRegistryManager();

            dataOutput.writeInt(inventory.size());

            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if(!stack.isEmpty()){
                    var nbt = NbtFunc.getNbtFromItemStack(stack,registries);
                    String nbtString = nbt.toString();
                    dataOutput.writeUTF(nbtString);
                }else{
                    dataOutput.writeUTF("{air}");
                }
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Converts the advancements of a given player entity to a Base64-encoded string.
     *
     * @param p the player entity whose advancements are to be converted
     * @return a Base64-encoded string representing the player's advancements
     * @throws IllegalStateException if unable to save the advancements
     */
    public static String advancementToBase64(ServerPlayerEntity p) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            //dataOutput.writeInt(p.server.getAdvancementLoader().getAdvancements().size());

            for (AdvancementEntry advancement : p.server.getAdvancementLoader().getAdvancements()) {
                AdvancementProgress progress = p.getAdvancementTracker().getProgress(advancement);
                dataOutput.writeBoolean(progress.isDone());
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Deserialize a Base64-encoded string to reconstruct a player's inventory.
     *
     * @param p the player entity associated with the inventory
     * @param data the Base64-encoded string representing the player's inventory
     * @return the reconstructed inventory as an Inventory object
     * @throws IOException if an I/O error occurs during deserialization
     */
    public static Inventory fromBase64(ServerPlayerEntity p,String data) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        ObjectInputStream dataInput = new ObjectInputStream (inputStream);
        Inventory inventory = new SimpleInventory(dataInput.readInt());
        MinecraftServer server = p.server;
        RegistryWrapper.WrapperLookup registries = server.getRegistryManager();

        // Read the serialized inventory
        for (int i = 0; i < inventory.size(); i++) {
            String nbtString = dataInput.readUTF();
            if(Objects.equals(nbtString, AIR_CONSTANT)){
                inventory.setStack(i, ItemStack.EMPTY);
            }else{
                NbtElement nbt = NbtFunc.stringToNbtElement(nbtString);
                Optional<ItemStack> opStack = ItemStack.fromNbt(registries,nbt);
                if(opStack.isPresent()){
                    inventory.setStack(i, opStack.get());
                }
            }
        }
        dataInput.close();
        return inventory;
    }

    /**
     * Deserialize a Base64-encoded string to reconstruct a player's advancements.
     *
     * @param p the player entity associated with the advancements
     * @param data the Base64-encoded string representing the player's advancements
     * @throws IOException if an I/O error occurs during deserialization
     */
    public static void advancementFromBase64(ServerPlayerEntity p,String data) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        ObjectInputStream dataInput = new ObjectInputStream (inputStream);

        for (int i = 0; i < p.server.getAdvancementLoader().getAdvancements().size(); i++) {
            boolean done = dataInput.readBoolean();
            AdvancementEntry advancementEntry =  Iterables.get(p.server.getAdvancementLoader().getAdvancements(),i);

            for (String criteriaName : advancementEntry.value().criteria().keySet()) {
                if (done)
                    p.getAdvancementTracker().grantCriterion(advancementEntry, criteriaName);
                else
                    p.getAdvancementTracker().revokeCriterion(advancementEntry, criteriaName);
            }
        }
        p.getAdvancementTracker().sendUpdate(p);
        dataInput.close();
    }

    /**
     * Deserialize a Base64-encoded string to reconstruct an array of ItemStacks.
     *
     * @param p the player entity associated with the ItemStack array
     * @param data the Base64-encoded string representing the ItemStack array
     * @return an array of ItemStacks reconstructed from the Base64-encoded string
     * @throws IOException if an I/O error occurs during deserialization
     */
    public static ItemStack[] itemStackArrayFromBase64(ServerPlayerEntity p,String data) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        ObjectInputStream dataInput = new ObjectInputStream(inputStream);
        ItemStack[] items = new ItemStack[dataInput.readInt()];
        MinecraftServer server = p.server;
        RegistryWrapper.WrapperLookup registries = server.getRegistryManager();

        for (int i = 0; i < items.length; i++) {
            String nbtString = dataInput.readUTF();
            if(Objects.equals(nbtString, "{air}")){
                items[i] = ItemStack.EMPTY;
            }else{
                NbtElement nbt = NbtFunc.stringToNbtElement(nbtString);
                Optional<ItemStack> opStack = ItemStack.fromNbt(registries,nbt);
                if(opStack.isPresent()){
                    items[i] = opStack.get();
                }
            }
        }
        dataInput.close();
        return items;
    }
}
