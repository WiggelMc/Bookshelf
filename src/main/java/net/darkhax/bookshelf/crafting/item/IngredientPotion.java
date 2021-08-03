package net.darkhax.bookshelf.crafting.item;

import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class IngredientPotion extends Ingredient {

    public static final IngredientPotion.Serializer SERIALIZER = new IngredientPotion.Serializer();
    public static final Ingredient WATER_BOTTLE = new IngredientPotion(Items.POTION, Potions.WATER);
    public static final Ingredient AWKWARD = new IngredientPotion(Items.POTION, Potions.AWKWARD);

    private final Item item;
    private final Potion potion;

    public IngredientPotion (Item item, Potion potion) {

        super(Stream.of(new Ingredient.ItemValue(PotionUtils.setPotion(new ItemStack(item), potion))));

        this.item = item;
        this.potion = potion;
    }

    @Override
    public boolean test (ItemStack input) {

        return input != null && !input.isEmpty() && this.item == input.getItem() && PotionUtils.getPotion(input) == this.potion;
    }

    @Override
    public boolean isSimple () {

        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer () {

        return IngredientPotion.SERIALIZER;
    }

    @Override
    public JsonElement toJson () {

        final JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(IngredientPotion.SERIALIZER).toString());
        json.add("item", Serializers.ITEM.write(this.item));
        json.add("potion", Serializers.POTION.write(this.potion));
        return json;
    }

    static class Serializer implements IIngredientSerializer<IngredientPotion> {

        @Override
        public IngredientPotion parse (FriendlyByteBuf buffer) {

            final Item item = Serializers.ITEM.read(buffer);
            final Potion potion = Serializers.POTION.read(buffer);
            return new IngredientPotion(item, potion);
        }

        @Override
        public IngredientPotion parse (JsonObject json) {

            final Item item = Serializers.ITEM.read(json, "item");
            final Potion potion = Serializers.POTION.read(json, "potion");
            return new IngredientPotion(item, potion);
        }

        @Override
        public void write (FriendlyByteBuf buffer, IngredientPotion ingredient) {

            Serializers.ITEM.write(ingredient.item);
            Serializers.POTION.write(ingredient.potion);
        }
    }
}