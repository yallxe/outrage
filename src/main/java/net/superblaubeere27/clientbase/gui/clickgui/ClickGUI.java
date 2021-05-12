package net.superblaubeere27.clientbase.gui.clickgui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.gui.clickgui.components.Label;
import net.superblaubeere27.clientbase.gui.clickgui.components.*;
import net.superblaubeere27.clientbase.gui.clickgui.layout.GridLayout;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.fontRenderer.GlyphPageFontRenderer;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import net.superblaubeere27.clientbase.valuesystem.Value;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends GuiScreen {
    private final IRenderer renderer;

    private final HashMap<ModuleCategory, List<Module>> moduleCategoryMap = new HashMap<>();
    private final HashMap<ModuleCategory, Window> categoryWindowMap = new HashMap<>();

    private boolean showKeyBinds;

    public ClickGUI() {
        GlyphPageFontRenderer consolasRenderer = GlyphPageFontRenderer.create("BebasNeueRegular", 17, false, false, false);
        renderer = new ClientBaseRendererImpl(consolasRenderer);

        for (Module module : ClientBase.INSTANCE.moduleManager.getModules()) {
            if (!moduleCategoryMap.containsKey(module.getCategory())) {
                moduleCategoryMap.put(module.getCategory(), new ArrayList<>());
            }

            moduleCategoryMap.get(module.getCategory()).add(module);
        }

        initializeCategoryWindows();
        initializeModulesInWindows();
    }

    private void initializeCategoryWindows() {
        int xOffset = 0;
        int windowWidth = 230;

        for (ModuleCategory category : moduleCategoryMap.keySet()) {
            Window window = new Window(category.toString(), 50 + xOffset, 50, windowWidth, 200);
            Pane contentPane = new Pane(renderer, new GridLayout(1, 0, 0));
            window.setContentPane(contentPane);
            categoryWindowMap.put(category, window);
            xOffset += windowWidth + 50;
        }
    }

    private void initializeModulesInWindows() {
        // TODO Split up into several functions
        for (Map.Entry<ModuleCategory, List<Module>> moduleCategoryListEntry : moduleCategoryMap.entrySet()) {
            Window categoryWindow = categoryWindowMap.get(moduleCategoryListEntry.getKey());

            for (Module module : moduleCategoryListEntry.getValue()) {
                Pane settingPane = new Pane(renderer, new GridLayout(1));

                List<Value> values = ClientBase.INSTANCE.valueManager.getAllValuesFrom(module.getName());

                if (values != null) {
                    for (Value value : values) {
                        if (value instanceof BooleanValue) {
                            // settingPane.addComponent(new Label(renderer, value.getName()));

                            CheckBox cb;

                            settingPane.addComponent(cb = new CheckBox(renderer, value.getName()));
                            cb.setSelected(((BooleanValue) value).getObject());
                            cb.setListener(value::setObject);
                            // FIXME Render new value on each render2d event (only if the spoiler is open)
                            // onRenderListeners.add(() -> cb.setSelected(((BooleanValue) value).getObject()));
                        }
                        if (value instanceof ModeValue) {
                            settingPane.addComponent(new Label(renderer, value.getName()));

                            ComboBox cb;

                            settingPane.addComponent(cb = new ComboBox(renderer, ((ModeValue) value).getModes(), ((ModeValue) value).getObject()));
                            cb.setSelectedIndex(((ModeValue) value).getObject());
                            cb.setListener(object -> {
                                value.setObject(object);
                                return true;
                            });

                            // FIXME Render new value on each render2d event (only if the spoiler is open)
                            // onRenderListeners.add(() -> cb.setSelectedIndex(((ModeValue) value).getObject()));
                        }
                        if (value instanceof NumberValue) {
                            settingPane.addComponent(new Label(renderer, value.getName()));

                            Slider cb;

                            Slider.NumberType type = Slider.NumberType.DECIMAL;

                            if (value.getObject() instanceof Integer) {
                                type = Slider.NumberType.INTEGER;
                            } else if (value.getObject() instanceof Long) {
                                type = Slider.NumberType.TIME;
                            } else if (value.getObject() instanceof Float && ((NumberValue) value).getMin().intValue() == 0 && ((NumberValue) value).getMax().intValue() == 100) {
                                type = Slider.NumberType.PERCENT;
                            }

                            settingPane.addComponent(cb = new Slider(renderer, ((Number) value.getObject()).doubleValue(), ((NumberValue) value).getMin().doubleValue(), ((NumberValue) value).getMax().doubleValue(), type));
                            cb.setListener(val -> {
                                if (value.getObject() instanceof Integer) {
                                    value.setObject(val.intValue());
                                }
                                if (value.getObject() instanceof Float) {
                                    value.setObject(val.floatValue());
                                }
                                if (value.getObject() instanceof Long) {
                                    value.setObject(val.longValue());
                                }
                                if (value.getObject() instanceof Double) {
                                    value.setObject(val.doubleValue());
                                }

                                return true;
                            });

                            cb.setValue(((Number) value.getObject()).doubleValue());
                            // FIXME Render new value on each render2d event (only if the spoiler is open)
                            // onRenderListeners.add(() -> cb.setValue(((Number) value.getObject()).doubleValue()));
                        }
                    }
                }

                Spoiler spoiler = new Spoiler(renderer, module.getName(), categoryWindow.getWidth(), settingPane, module);
                categoryWindow.contentPane.addComponent(spoiler);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 80).getRGB());

        Point point = Utils.calculateMouseLocation();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glLineWidth(1.0f);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        for (Window window : categoryWindowMap.values()) {
            window.setHeight(32 + window.contentPane.getHeight());
            window.mouseMoved(point.x * 2, point.y * 2);
            window.render(renderer);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Window window : categoryWindowMap.values()) {
            window.mouseMoved(mouseX * 2, mouseY * 2);
            window.mousePressed(mouseButton, mouseX * 2, mouseY * 2);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Window window : categoryWindowMap.values()) {
            window.mouseMoved(mouseX * 2, mouseY * 2);
            window.mouseReleased(state, mouseX * 2, mouseY * 2);
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (Window window : categoryWindowMap.values()) {
            window.mouseMoved(mouseX * 2, mouseY * 2);
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int eventDWheel = Mouse.getEventDWheel();

        for (Window window : categoryWindowMap.values()) {
            window.mouseWheel(eventDWheel);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_LSHIFT) {
            showKeyBinds = !showKeyBinds;

            // FIXME
            for (Window window : categoryWindowMap.values()) {
                List<AbstractComponent> components = window.contentPane.getComponents();

                for (AbstractComponent component : components) {
                    if (component instanceof Spoiler)
                        ((Spoiler) component).showKeyBinds = showKeyBinds;
                }
            }
        }

        for (Window window : categoryWindowMap.values()) {
            window.keyPressed(keyCode, typedChar);
        }

        super.keyTyped(typedChar, keyCode);
    }
}
