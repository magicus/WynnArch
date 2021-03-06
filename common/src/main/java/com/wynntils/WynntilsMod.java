package com.wynntils;

import net.minecraft.client.gui.screens.ConnectScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class WynntilsMod {
    public static final String MOD_ID = "wynntils";
    // We can use this if we don't want to use DeferredRegister
    public static final LazyLoadedValue<Registries> REGISTRIES = new LazyLoadedValue<>(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeModeTab EXAMPLE_TAB = CreativeTabs.create(new ResourceLocation(MOD_ID, "example_tab"), () ->
            new ItemStack(WynntilsMod.EXAMPLE_ITEM.get()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () ->
            new Item(new Item.Properties().tab(WynntilsMod.EXAMPLE_TAB)));

    public static void init() {
        ITEMS.register();

        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    private static class WynncraftButton extends Button {
        public static final ResourceLocation WYNNCRAFT_SERVER_ICON = new ResourceLocation("textures/misc/unknown_server.png");
        private final Screen backScreen;
        private final ServerData serverData;

        WynncraftButton(Screen backScreen, ServerData serverData,  int x, int y) {
            super(x, y, 20, 20, new TranslatableComponent(""), WynncraftButton::onPress);
            this.backScreen = backScreen;
            this.serverData = serverData;
        }

        @Override
        public void renderButton(PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
            super.renderButton(matrices, mouseX, mouseY, partialTicks);

            Minecraft.getInstance().getTextureManager().bind(WYNNCRAFT_SERVER_ICON);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            this.blit(matrices, this.x, this.y, 0, 0, this.width, this.height);
        }

        public static void onPress(Button button) {
            if (button instanceof WynncraftButton wynncraftButton) {
                Minecraft.getInstance().setScreen(new ConnectScreen(wynncraftButton.backScreen, Minecraft.getInstance(), wynncraftButton.serverData));
            }
        }
    }


    public static void postTitleScreenInit(TitleScreen screen, List<AbstractWidget> buttons, Consumer<AbstractWidget> addButton) {
        ServerData wynncraftServer = new ServerData("Wynncraft", "play.wynncraft.com", false);
        WynncraftButton wynncraftButton = new WynncraftButton(screen, wynncraftServer,
                screen.width / 2 + 104, screen.height / 4 + 48 + 24);
        addButton.accept(wynncraftButton);
    }

    public static void postGameMenuScreenInit(PauseScreen screen, List<AbstractWidget> buttons) {
    }
}
