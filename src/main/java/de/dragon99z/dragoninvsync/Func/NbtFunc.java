package de.dragon99z.dragoninvsync.Func;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.RegistryWrapper;

import java.io.IOException;

/**
 * Class for NBT related functions.
 */
public class NbtFunc {

    /**
     * Retrieves the NBT element from the provided ItemStack using the specified RegistryWrapper.WrapperLookup.
     *
     * @param stack the ItemStack to retrieve the NBT element from
     * @param registries the RegistryWrapper.WrapperLookup used for encoding the ItemStack
     * @return the NBT element extracted from the ItemStack
     */
    public static NbtElement getNbtFromItemStack(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        return stack.encode(registries);
    }

    /**
     * Parses the given NBT string and returns the corresponding NbtElement.
     *
     * @param nbtString the NBT string to parse
     * @return the NbtElement parsed from the NBT string
     * @throws IOException if an error occurs during parsing
     */
    public static NbtElement stringToNbtElement(String nbtString) throws IOException {
        try {
            return StringNbtReader.parse(nbtString);
        } catch (Exception e) {
            throw new IOException("Failed to parse NBT string: " + nbtString, e);
        }
    }

}
