package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.RealmsMainScreen;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CenteredStringWidget;
import net.minecraft.client.gui.components.FrameWidget;
import net.minecraft.client.gui.components.GridWidget;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PauseScreen extends Screen {
   private static final String URL_FEEDBACK_SNAPSHOT = "https://aka.ms/snapshotfeedback?ref=game";
   private static final String URL_FEEDBACK_RELEASE = "https://aka.ms/javafeedback?ref=game";
   private static final String URL_BUGS = "https://aka.ms/snapshotbugs?ref=game";
   private static final int COLUMNS = 2;
   private static final int MENU_PADDING_TOP = 50;
   private static final int BUTTON_PADDING = 4;
   private static final int BUTTON_WIDTH_FULL = 204;
   private static final int BUTTON_WIDTH_HALF = 98;
   private static final Component RETURN_TO_GAME = Component.translatable("menu.returnToGame");
   private static final Component ADVANCEMENTS = Component.translatable("gui.advancements");
   private static final Component STATS = Component.translatable("gui.stats");
   private static final Component SEND_FEEDBACK = Component.translatable("menu.sendFeedback");
   private static final Component REPORT_BUGS = Component.translatable("menu.reportBugs");
   private static final Component OPTIONS = Component.translatable("menu.options");
   private static final Component SHARE_TO_LAN = Component.translatable("menu.shareToLan");
   private static final Component PLAYER_REPORTING = Component.translatable("menu.playerReporting");
   private static final Component RETURN_TO_MENU = Component.translatable("menu.returnToMenu");
   private static final Component DISCONNECT = Component.translatable("menu.disconnect");
   private static final Component SAVING_LEVEL = Component.translatable("menu.savingLevel");
   private static final Component GAME = Component.translatable("menu.game");
   private static final Component PAUSED = Component.translatable("menu.paused");
   private final boolean showPauseMenu;
   @Nullable
   private Button disconnectButton;

   public PauseScreen(boolean p_96308_) {
      super(p_96308_ ? GAME : PAUSED);
      this.showPauseMenu = p_96308_;
   }

   protected void init() {
      if (this.showPauseMenu) {
         this.createPauseMenu();
      }

      this.addRenderableWidget(new CenteredStringWidget(0, this.showPauseMenu ? 40 : 10, this.width, 9, this.title, this.font));
   }

   private void createPauseMenu() {
      GridWidget gridwidget = new GridWidget();
      gridwidget.defaultCellSetting().padding(4, 4, 4, 0);
      GridWidget.RowHelper gridwidget$rowhelper = gridwidget.createRowHelper(2);
      gridwidget$rowhelper.addChild(Button.builder(RETURN_TO_GAME, (p_96337_) -> {
         this.minecraft.setScreen((Screen)null);
         this.minecraft.mouseHandler.grabMouse();
      }).width(204).build(), 2, gridwidget.newCellSettings().paddingTop(50));
      gridwidget$rowhelper.addChild(this.openScreenButton(ADVANCEMENTS, () -> {
         return new AdvancementsScreen(this.minecraft.player.connection.getAdvancements());
      }));
      gridwidget$rowhelper.addChild(this.openScreenButton(STATS, () -> {
         return new StatsScreen(this, this.minecraft.player.getStats());
      }));
      gridwidget$rowhelper.addChild(this.openLinkButton(SEND_FEEDBACK, SharedConstants.getCurrentVersion().isStable() ? "https://aka.ms/javafeedback?ref=game" : "https://aka.ms/snapshotfeedback?ref=game"));
      (gridwidget$rowhelper.addChild(this.openLinkButton(REPORT_BUGS, "https://aka.ms/snapshotbugs?ref=game"))).active = !SharedConstants.getCurrentVersion().getDataVersion().isSideSeries();
      gridwidget$rowhelper.addChild(this.openScreenButton(OPTIONS, () -> {
         return new OptionsScreen(this, this.minecraft.options);
      }));
      if (this.minecraft.hasSingleplayerServer() && !this.minecraft.getSingleplayerServer().isPublished()) {
         gridwidget$rowhelper.addChild(this.openScreenButton(SHARE_TO_LAN, () -> {
            return new ShareToLanScreen(this);
         }));
      } else {
         gridwidget$rowhelper.addChild(this.openScreenButton(PLAYER_REPORTING, SocialInteractionsScreen::new));
      }

      Component component = this.minecraft.isLocalServer() ? RETURN_TO_MENU : DISCONNECT;
      this.disconnectButton = gridwidget$rowhelper.addChild(Button.builder(component, (p_261363_) -> {
         p_261363_.active = false;
         this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
      }).width(204).build(), 2);
      gridwidget.pack();
      FrameWidget.alignInRectangle(gridwidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
      this.addRenderableWidget(gridwidget);
   }

   private void onDisconnect() {
      boolean flag = this.minecraft.isLocalServer();
      boolean flag1 = this.minecraft.isConnectedToRealms();
      this.minecraft.level.disconnect();
      if (flag) {
         this.minecraft.clearLevel(new GenericDirtMessageScreen(SAVING_LEVEL));
      } else {
         this.minecraft.clearLevel();
      }

      TitleScreen titlescreen = new TitleScreen();
      if (flag) {
         this.minecraft.setScreen(titlescreen);
      } else if (flag1) {
         this.minecraft.setScreen(new RealmsMainScreen(titlescreen));
      } else {
         this.minecraft.setScreen(new JoinMultiplayerScreen(titlescreen));
      }

   }

   public void tick() {
      super.tick();
   }

   public void render(PoseStack p_96310_, int p_96311_, int p_96312_, float p_96313_) {
      if (this.showPauseMenu) {
         this.renderBackground(p_96310_);
      }

      super.render(p_96310_, p_96311_, p_96312_, p_96313_);
      if (this.showPauseMenu && this.minecraft != null && this.minecraft.getReportingContext().hasDraftReport() && this.disconnectButton != null) {
         RenderSystem.setShaderTexture(0, AbstractWidget.WIDGETS_LOCATION);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         this.blit(p_96310_, this.disconnectButton.getX() + this.disconnectButton.getWidth() - 17, this.disconnectButton.getY() + 3, 182, 24, 15, 15);
      }

   }

   private Button openScreenButton(Component p_262567_, Supplier<Screen> p_262581_) {
      return Button.builder(p_262567_, (p_262524_) -> {
         this.minecraft.setScreen(p_262581_.get());
      }).width(98).build();
   }

   private Button openLinkButton(Component p_262593_, String p_262659_) {
      return this.openScreenButton(p_262593_, () -> {
         return new ConfirmLinkScreen((p_169337_) -> {
            if (p_169337_) {
               Util.getPlatform().openUri(p_262659_);
            }

            this.minecraft.setScreen(this);
         }, p_262659_, true);
      });
   }
}