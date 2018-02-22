package com.valkryst.VTerminal.revamp.component.component;

import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.revamp.component.Screen;
import com.valkryst.VTerminal.revamp.component.builder.ButtonBuilder;
import com.valkryst.VTerminal.revamp.component.palette.ColorPalette;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.event.MouseEvent;

@ToString
public class Button extends Component {
    /** Whether or not the button is in the normal state. */
    private boolean isInNormalState = true;
    /** whether or not the button is in the hovered state. */
    private boolean isInHoveredState = false;
    /** Whether or not the button is in the pressed state. */
    private boolean isInPressedState = false;

    /** The color palette. */
    @Getter @Setter @NonNull private ColorPalette colorPalette;

    /** The function to run when the button is clicked. */
    @Getter @Setter @NonNull private Runnable onClickFunction;

    /**
     * Constructs a new AsciiButton.
     *
     * @param builder
     *         The builder to use.
     *
     * @throws NullPointerException
     *         If the builder is null.
     */
    public Button(final @NonNull ButtonBuilder builder) {
        super(builder.getDimensions(), builder.getPosition());

        colorPalette = builder.getColorPalette();

        this.onClickFunction = builder.getOnClickFunction();

        // Set the button's text:
        final char[] text = builder.getText().toCharArray();
        final AsciiCharacter[] tiles = super.tiles.getRow(0);

        for (int x = 0; x < tiles.length; x++) {
            tiles[x].setCharacter(text[x]);
            tiles[x].setBackgroundColor(colorPalette.getButton_defaultBackground());
            tiles[x].setForegroundColor(colorPalette.getButton_defaultForeground());
        }
    }

    @Override
    public void createEventListeners(final @NonNull Screen parentScreen) {
        if (super.getEventListeners().size() > 0) {
            return;
        }

        final MouseInputListener mouseListener = new MouseInputListener() {
            @Override
            public void mouseDragged(final MouseEvent e) {}

            @Override
            public void mouseMoved(final MouseEvent e) {
                if (intersects(parentScreen.getMousePosition())) {
                    setStateHovered();
                } else {
                    setStateNormal();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (intersects(parentScreen.getMousePosition())) {
                        setStatePressed();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (isInPressedState) {
                        onClickFunction.run();
                    }

                    if (intersects(parentScreen.getMousePosition())) {
                        setStateHovered();
                    } else {
                        setStateNormal();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };

        super.eventListeners.add(mouseListener);
    }

    /**
     * Sets the button state to normal if the current state allows for the normal
     * state to be set.
     */
    protected void setStateNormal() {
        boolean canSetState = isInNormalState == false;
        canSetState &= isInHoveredState || isInPressedState;

        if (canSetState) {
            isInNormalState = true;
            isInHoveredState = false;
            isInPressedState = false;

            setColors(colorPalette.getButton_defaultBackground(), colorPalette.getButton_defaultForeground());
            redrawFunction.run();
        }
    }

    /**
     * Sets the button state to hovered if the current state allows for the normal
     * state to be set.
     */
    protected void setStateHovered() {
        boolean canSetState = isInNormalState || isInPressedState;
        canSetState &= isInHoveredState == false;

        if (canSetState) {
            isInNormalState = false;
            isInHoveredState = true;
            isInPressedState = false;

            setColors(colorPalette.getButton_hoverBackground(), colorPalette.getButton_hoverForeground());
            redrawFunction.run();
        }
    }

    /**
     * Sets the button state to pressed if the current state allows for the normal
     * state to be set.
     */
    protected void setStatePressed() {
        boolean canSetState = isInNormalState || isInHoveredState;
        canSetState &= isInPressedState == false;

        if (canSetState) {
            isInNormalState = false;
            isInHoveredState = false;
            isInPressedState = true;

            setColors(colorPalette.getButton_pressedBackground(), colorPalette.getButton_pressedForeground());
            redrawFunction.run();
        }
    }

    /**
     * Sets the back/foreground colors of all characters to the specified colors.
     *
     * @param backgroundColor
     *         The new background color.
     *
     * @param foregroundColor
     *         The new foreground color.
     *
     * @throws NullPointerException
     *         If the background or foreground color is null.
     */
    private void setColors(final @NonNull Color backgroundColor, final @NonNull Color foregroundColor) {
        for (final AsciiCharacter tile : super.tiles.getRow(0)) {
            tile.setBackgroundColor(backgroundColor);
            tile.setForegroundColor(foregroundColor);
        }
    }
}