package yellowbirb.hypixelchattabs.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yellowbirb.hypixelchattabs.HypixelChatTabsClient;
import yellowbirb.hypixelchattabs.HypixelChatTabsClient.Tab;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ChatHud hud = client.inGameHud.getChatHud();
        int x = 5;
        for (Tab chatTab : Tab.values()) {
            String label = switch (chatTab) {
                case ALL -> "All";
                case PARTY -> "Party";
                case GUILD -> "Guild";
                case PRIVATE -> "DM";
                case COOP -> "CO";
            };
            int btnWidth = client.textRenderer.getWidth(label) + 10;
            ButtonWidget tabButton = ButtonWidget.builder(Text.literal(label), (btn) -> {
                HypixelChatTabsClient.tab = chatTab;
                hud.reset();
                client.send(() -> setFocused(chatField));
            }).dimensions(x, this.height - ((ChatHudAccessor) hud).invokeGetHeight() - 40 - 20 - 5, btnWidth, 20).build();
            x += btnWidth + 2;

            addDrawableChild(tabButton);
        }
    }

    @Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
    private void onSendMessage(String message, boolean addToHistory, CallbackInfo ci) {
        if (message.startsWith("/")) return;
        String prefix = switch (HypixelChatTabsClient.tab) {
            case ALL -> null;
            case PARTY -> "/pc ";
            case GUILD -> "/gc ";
            case PRIVATE -> null;
            case COOP -> "/cc ";
        };
        if (prefix != null) {
            ci.cancel();
            ((ChatScreen) (Object) this).sendMessage(prefix + message, addToHistory);
        }
    }

    @Inject(at = @At("HEAD"), method = "keyPressed")
    private void onKeyPressed(KeyInput keyInput, CallbackInfoReturnable<Boolean> cir) {
        setFocused(chatField);
    }
}
