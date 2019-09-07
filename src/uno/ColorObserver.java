package uno;

import uno.model.Color;

public interface ColorObserver {
    void update_color(Color color);
    void update_color_chooser();
}
