package co.gargoyle.rocnation.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        // addItem(new DummyItem("1", "Item 1"));
        // addItem(new DummyItem("2", "Item 2"));
        // addItem(new DummyItem("3", "Item 3"));
		addItem(new DummyItem("1", "What We Talkin' About"));
		addItem(new DummyItem("2", "Thank You"));
		addItem(new DummyItem("3", "D.O.A."));
		addItem(new DummyItem("4", "Run This Town"));
		addItem(new DummyItem("5", "Empire State of Mind"));
		addItem(new DummyItem("6", "Real as It Gets"));
		addItem(new DummyItem("7", "On to the Next One"));
		addItem(new DummyItem("8", "Off That"));
		addItem(new DummyItem("9", "A Star Is Born"));
		addItem(new DummyItem("10", "Venus vs. Mars"));
		addItem(new DummyItem("10", "Already Home"));
		addItem(new DummyItem("11", "Hate"));
		addItem(new DummyItem("12", "Reminder"));
		addItem(new DummyItem("13", "So Ambitious"));
		addItem(new DummyItem("14", "Young Forever"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
